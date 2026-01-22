import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.spotless)
    idea
    `maven-publish`
}

val mod_version: String by project
val mod_id: String by project
val maven_group: String by project

version = mod_version

group = maven_group

base.archivesName = mod_id

repositories {
    val repositories =
        setOf(
            "https://maven.parchmentmc.org", // Parchment
            "https://maven.terraformersmc.com/", // ModMenu
            "https://api.modrinth.com/maven/", // LazyDFU
        )

    repositories.forEach { maven { url = uri(it) } }
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(
        loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-1.18.2:2022.11.06@zip")
        }
    )

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)

    modLocalRuntime(libs.modmenu) { exclude(group = "net.fabricmc") }
    modLocalRuntime(libs.lazydfu)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        val mod_id: String by project
        register(mod_id) {
            sourceSet("main")
            sourceSet("client")
        }
    }

    runs {
        named("server") { runDir("run/server") }

        named("client") { programArgs(setOf("--username", "Player")) }
    }
}

tasks.withType<ProcessResources>().configureEach {
    val mod_name: String by project
    val mod_id: String by project
    val mod_license: String by project
    val mod_version: String by project
    val mod_description: String by project

    val replaceProperties =
        hashMapOf(
            "loader_version" to libs.versions.fabric.loader.get(),
            "minecraft_version" to libs.versions.minecraft.get(),
            "mod_id" to mod_id,
            "mod_name" to mod_name,
            "mod_license" to mod_license,
            "mod_version" to mod_version,
            "mod_description" to mod_description,
        )

    inputs.properties(replaceProperties)

    filesMatching(setOf("fabric.mod.json")) { expand(replaceProperties) }
}

tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8" }

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.GRAAL_VM
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

kotlin { compilerOptions { jvmTarget = JvmTarget.JVM_17 } }

tasks.named<Jar>("jar") {
    inputs.property("archivesName", project.base.archivesName)

    from("LICENSE") { rename { "${it}_${inputs.properties["archivesName"]}" } }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = mod_id
            version = project.version.toString()

            from(components["java"])
        }
    }

    repositories {}
}

spotless {
    encoding("UTF-8")

    kotlin {
        ktfmt().kotlinlangStyle().configure {
            it.setMaxWidth(100)
            it.setBlockIndent(4)
            it.setContinuationIndent(4)
            it.setRemoveUnusedImports(true)
        }
        endWithNewline()
        toggleOffOn()
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktfmt().kotlinlangStyle()
    }

    java {
        importOrder()
        removeUnusedImports()
        palantirJavaFormat()
    }

    json {
        target("src/*/resources/**/*.json")
        targetExclude("src/generated/resources/**")

        biome("2.3.11")
            .downloadDir(File(rootDir, ".gradle/biome").absolutePath)
            .configPath(File(rootDir, "spotless/biome.json").absolutePath)

        endWithNewline()
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
