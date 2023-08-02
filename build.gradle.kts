import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization").version("1.8.0")


}

group = "dev.levi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://jitpack.io")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality

    val lifecycle_version = "2.6.1"
    val arch_version = "2.2.0"
    val precompose_version = "1.4.3"
    val ktor_version = "2.3.2"

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.jkcclemens:khttp:0.1.0")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    //Ktor Dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Precompose Dependencies
    implementation("moe.tlaster:precompose:$precompose_version")
    implementation("moe.tlaster:precompose-molecule:$precompose_version") // For Molecule intergration
    implementation("moe.tlaster:precompose-viewmodel:$precompose_version") // For ViewModel intergration


    // Annotation processor

//                implementation("dev.icerock.moko:mvvm-compose:0.16.1") // api mvvm-core, getViewModel for Compose Multiplatfrom
//                implementation("dev.icerock.moko:mvvm-flow-compose:0.16.1") // api mvvm-flow, binding extensions for Compose Multiplatfrom
//                implementation("dev.icerock.moko:mvvm-livedata-compose:0.16.1") // api mvvm-livedata,

    //Dependency Injection
    implementation("org.kodein.di:kodein-di:7.19.0")

    implementation("ch.qos.logback:logback-classic:1.4.8")



}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Pastey"
            packageVersion = "1.0.0"
        }
    }
}
