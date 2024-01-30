import java.lang.Long

plugins {
	id("java")
}

group = "com.hyuk.mapel"
version = "1.0"

repositories {
	mavenCentral()
	maven {
		name = "papermc-repo"
		url = uri("https://repo.papermc.io/repository/maven-public/")
	}
	maven {
		name = "sonatype"
		url = uri("https://oss.sonatype.org/content/groups/public/")
	}
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

val targetJavaVersion = 17
java {
	val javaVersion = JavaVersion.toVersion(targetJavaVersion)
	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
	if (JavaVersion.current() < javaVersion) {
		toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
	}
}

tasks.withType<JavaCompile>().configureEach {
	if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
		options.release.set(targetJavaVersion)
	}
}

tasks {
	processResources {
		val props = linkedMapOf(
			"version" to project.version,
			"group" to project.group,
			"name" to rootProject.name
		)
		inputs.properties(props)
		filteringCharset = "UTF-8"
		var list = mutableListOf("plugin.yml", "paper-plugin.yml")
		filesMatching(list) {
			expand(props)
		}
	}

	register<Copy>("build_and_add_jar") {
		from("build/libs") {
			include("**/*.jar")
		}
		into(project.properties["mapel.build.result.path"] ?: "build/libs")

		group = "build"
		dependsOn("build")
	}
}


