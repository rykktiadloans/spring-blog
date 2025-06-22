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
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}

tasks.withType<Test> {
	useJUnitPlatform()
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
