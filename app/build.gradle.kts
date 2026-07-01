import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.androidx.room)
    id("com.google.firebase.crashlytics")
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))


//apply(from = "./gradle/properties.gradle")

extensions.configure<ApplicationExtension> {
    namespace = "pk.sufiishq.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "pk.sufiishq.app"
        minSdk = 25
        targetSdk = 36

        versionCode = 29
        versionName = "3.0.0"
        ndkVersion = "28.0.13004108"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        /*ndk {
            abiFilters += setOf(
                "arm64-v8a",
                "armeabi-v7a"
            )
        }*/
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String

            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            enableUnitTestCoverage = true

            buildConfigField(
                "Boolean",
                "ENABLE_CRASHLYTICS",
                "false"
            )
        }

        release {
            signingConfig = signingConfigs.getByName("release")

            buildConfigField(
                "Boolean", "ENABLE_CRASHLYTICS", "true"
            )

            ndk {
                debugSymbolLevel = "FULL"
            }

            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    coreLibraryDesugaring(
        libs.desugar.jdk.libs
    )

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.timber)
    implementation(libs.commons.io)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.ffmpeg.kit)
    implementation(libs.androidx.security.crypto)

    implementation(
        libs.kotlinx.coroutines.play.services
    )

    implementation(libs.materialdatetimepicker)
    implementation(libs.kotlin.visibility)
    implementation(libs.lottie.compose)

    // Navigation Compose
    implementation(
        libs.androidx.navigation.compose
    )

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    //kapt("androidx.room:room-compiler:2.8.4")

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Hilt WorkManager Extensions (Fixes your exact error)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    //implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    // Hilt
    //implementation("com.google.dagger:hilt-android:2.56.2")
    //kapt("com.google.dagger:hilt-compiler:2.56.2")
    //ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    //ksp(libs.hilt.compiler)

    /*implementation(
            "androidx.hilt:hilt-navigation-compose:1.0.0"
    )

    implementation(
            "androidx.hilt:hilt-work:1.0.0"
    )*/

    /*kapt(
            "androidx.hilt:hilt-compiler:1.0.0"
    )*/

    // Firebase BOM
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.analytics)
    //implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    //implementation(libs.firebase.database)
    //implementation(libs.firebase.auth)

    //implementation(libs.firebase.ui.auth)

    /*implementation(
        libs.play.services.auth
    )*/

    implementation(
        libs.firebase.appcheck.debug
    )

    // Date/time
    implementation(libs.joda.time)

    // Play in-app update
    implementation(
        libs.app.update
    )

    implementation(
        libs.app.update.ktx
    )

    // Accompanist
    implementation(
        libs.accompanist.systemuicontroller
    )

    // Splash screen
    implementation(
        libs.androidx.core.splashscreen
    )

    implementation(
        libs.androidx.lifecycle.livedata.ktx
    )

    // Kotlin reflection
    implementation(
        libs.kotlin.reflect
    )

    // Coil
    implementation(
        libs.coil.kt.coil.compose
    )

    // Retrofit
    implementation(
        libs.retrofit
    )

    implementation(
        libs.kotlinx.coroutines.core
    )

    implementation(
        libs.kotlinx.coroutines.android
    )

    implementation(
        libs.gson
    )

    // Exoplayer
    implementation(
        libs.exoplayer.core
    )

    implementation(
        libs.exoplayer.ui
    )

    // Aurora module
    implementation(project(":aurora"))


    // -----------------------------
    // Testing
    // -----------------------------

    testImplementation(
        libs.androidx.core
    )

    testImplementation(
        libs.junit
    )

    testImplementation(
        libs.mockk
    )

    testImplementation(
        libs.robolectric
    )

    testImplementation(
        libs.androidx.core.testing
    )

    testImplementation(
        libs.kotlinx.coroutines.test
    )

    testImplementation(
        libs.androidx.work.testing
    )

    testImplementation(
        libs.androidx.ui.test.junit4
    )

    debugImplementation(
        libs.androidx.ui.tooling
    )

    debugImplementation(
        libs.ui.test.manifest
    )

    testImplementation(
        libs.hilt.android.testing
    )


}