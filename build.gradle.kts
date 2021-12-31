import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectVersion: String by ext
val groupPath: String by ext
val springBootStarterVersion: String by ext
val kotlinVersion : String by ext
val kotlinxCoroutinesVersion : String by ext
val jacksonVersion : String by ext

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

plugins {
	id("org.springframework.boot") version "2.6.2" apply false
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	idea
}

group = groupPath
version = projectVersion
java.sourceCompatibility = JavaVersion.VERSION_11

allprojects {
	version = projectVersion
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply {
		plugin("kotlin")
		plugin("kotlin-kapt")
		plugin("io.spring.dependency-management")
		plugin("kotlin-spring")
		plugin("idea")
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootStarterVersion")
		mavenBom("org.springframework.boot:spring-boot-starter-parent:$springBootStarterVersion")
		mavenBom("com.fasterxml.jackson:jackson-bom:$jacksonVersion")
	}
	dependencies {
		dependency("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesVersion")
		dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
		dependency("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
		dependency("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinxCoroutinesVersion")
		dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
		dependency("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
		dependency("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
		dependency("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
		dependency("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
		dependency("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
	}
}
/**/
// Workaround used to make devolpmentOnly works. I think that is a bug
val developmentOnly = configurations.create("developmentOnly")
configurations.runtimeClasspath.get().extendsFrom(developmentOnly)

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
