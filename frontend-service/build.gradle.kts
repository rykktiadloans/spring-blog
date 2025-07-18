import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	id("java-conventions")
}

group = "org.rl"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation(project(":shared"))
}
extra["springCloudVersion"] = "2025.0.0"
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
	imageName = "rykktiadloans/blog-frontend-service"
}

tasks.register<Copy>("processFrontendResources") {
	// Directory containing the artifacts produced by the frontend project
	val frontendProjectDir = project(":frontend").layout.projectDirectory
	val frontendBuildDir = frontendProjectDir.dir("dist/")
	// Directory where the frontend artifacts must be copied to be packaged alltogether with the backend by the 'war'
	// plugin.
	val frontendResourcesDir = project.layout.buildDirectory.dir("resources/main/static/")

	println("static exists: " + frontendResourcesDir.get().asFile.exists())
	println("dist exists: " + frontendBuildDir.asFile.exists())

	group = "Frontend"
	description = "Process frontend resources"
	dependsOn(":frontend:assembleFrontend")

	from(frontendBuildDir)
	into(frontendResourcesDir)
}

tasks.named<Task>("compileJava") {
	dependsOn("processFrontendResources")
}

tasks.register<Copy>("getDeps") {
	from(sourceSets.main.get().runtimeClasspath)
	into("runtime/")

	doFirst {
		val runtime = File("runtime")
		runtime.deleteRecursively()
		runtime.mkdir()
	}

	doLast {
		File("runtime").deleteRecursively()
	}
}
