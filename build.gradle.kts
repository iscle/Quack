import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "me.iscle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    val log4jVersion = "2.22.1"
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("org.jcommander:jcommander:1.83")
    implementation("com.fifesoft:rsyntaxtextarea:3.3.4")
    implementation("com.squareup.moshi:moshi:1.14.0")

    implementation("io.github.skylot:jadx-core:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-dex-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-java-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-java-convert:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-smali-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-raung-input:1.5.0-SNAPSHOT") {
        isChanging = true
    }
    implementation("io.github.skylot:jadx-plugins-tools:1.5.0-SNAPSHOT") {
        isChanging = true
    }

    implementation("org.benf:cfr:0.152")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "quack"
            packageVersion = "1.0.0"
        }
    }
}
