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
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springframework.cloud:spring-cloud-gateway-server-webflux")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("io.projectreactor:reactor-core")
	implementation("com.playtika.reactivefeign:feign-reactor-core:4.2.1")
	implementation("com.playtika.reactivefeign:feign-reactor-spring-configuration:4.2.1")
	implementation("com.playtika.reactivefeign:feign-reactor-webclient:4.2.1")
	testImplementation("io.projectreactor:reactor-test")
	implementation(project(":shared"))
}
extra["springCloudVersion"] = "2025.0.0"
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		//mavenBom("io.projectreactor:reactor-bom:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
	imageName = "rykktiadloans/blog-gateway"
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
