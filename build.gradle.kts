import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.backend.wasm.lower.excludeDeclarationsFromCodegen
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

group = "com.flexingstudios"
version = "0.8.14-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.papermc.io/repository/maven-releases/")}
    maven { url = uri("https://repo.papermc.io/repository/maven-snapshots/")}
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
        exclude("org.slf4j", "slf4j-api")
        exclude("junit", "junit")
    }
    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT") {
        exclude("junit", "junit")
    }
    compileOnly("com.mojang:authlib:1.5.25")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.kyori:adventure-api:4.13.1")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-RC")
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.runServer {
    minecraftVersion("1.12.2")
}

tasks.register("apiJar", Jar::class).configure {
    archiveClassifier.set("api")
    from(sourceSets.main.get().output) {
        include("com/flexingstudios/flexingnetwork/api/**")
        include("com/flexingstudios/common/**")
    }

    val attributes = mutableMapOf(
        "Implementation-Version" to version,
        "Main-Class" to "com.flexingstudios.flexingnetwork.api.FlexingNetwork"
    )
    manifest.attributes(attributes)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.flexingstudios"
            artifactId = "flexingnetwork"
            version = version

            artifact("build/libs/FlexingNetwork-$version-api.jar")

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/FlexingWorldDev/FlexingNetwork")
                    credentials {
                        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                    }
                }
            }
        }
    }
}

tasks
    .withType<JavaCompile>()
    .matching { it.name == "compileJava" || it.name == "compileTestJava" }
    .configureEach {
        options.compilerArgs.addAll(listOf("-Xlint:all"))
        options.isDeprecation = true
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("dist")
    dependencies {
        relocate("kotlin", "com.flexingstudios.kotlin")
    }
    exclude("GradleStart**")
    minimize()
}

tasks.named<Copy>("processResources") {
    filteringCharset = Charsets.UTF_8.name()
    val props = mapOf(
        "name" to project.name,
        "version" to version
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

sourceSets.main {
    java.srcDirs("src/main/java", "src/main/kotlin")
}