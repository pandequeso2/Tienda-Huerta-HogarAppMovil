plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("kapt")
}

android {
    namespace = "com.example.tiendahuertohogar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tiendahuertohogar"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    // 📢 BLOQUE DE CONFIGURACIÓN CLAVE PARA ARREGLAR ERROR DE MERGE
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            // 📢 Excluye los archivos de licencia duplicados que causan el error app:mergeDebugAndroidTestJavaResource
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE.txt"

            excludes += "META-INF/LICENSE-notice.md"

        }
    }
}

dependencies {

    // --- DEPENDENCIAS DE ANDROIDX Y COMPOSE ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // --- DEPENDENCIAS DE LIFECYCLE (VIEWMODEL) ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    kapt("androidx.lifecycle:lifecycle-compiler:2.8.2")

    // --- OTROS COMPONENTES DE ANDROID ---
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // --- ROOM (BASE DE DATOS) ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // --- CAMERAX, QR, IMAGEN ---
    val cameraxVersion = "1.3.3"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("io.coil-kt:coil-compose:2.6.0") // Coil para imagen
    implementation("com.google.accompanist:accompanist-permissions:0.34.0") // Accompanist

    // --- RETROFIT Y COROUTINES ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")


    // =========================================================
    // --- DEPENDENCIAS DE TEST (TEST UNITARIO LOCAL) ---
    // =========================================================

    // Kotlin (necesario para que las funciones de test corran bien)
    testImplementation(kotlin("test"))

    // Kotest (El framework de testing)
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // Coroutines Test (Para el runTest y dispatchers)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // MockK (Para simular objetos si es necesario)
    testImplementation("io.mockk:mockk:1.13.10")

    // Test de Arquitectura de Android (Para ViewModels)
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Deshabilita JUnit 4 y usa el motor de JUnit 5 para Kotest
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    // --- DESUGARING (PARA COMPATIBILIDAD CON APIS DE JAVA 8+) ---
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // =========================================================
    // --- ANDROID TEST (TEST DE INSTRUMENTACIÓN) ---
    // =========================================================

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // 📢 MOCKK PARA TESTS DE INSTRUMENTACIÓN (Necesario para ProductoFormScreenTest)
    androidTestImplementation("io.mockk:mockk-android:1.13.10")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// 📢 Bloque crucial para que Gradle use JUnit 5 (Kotest) para tests unitarios
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
}