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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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

    // --- DEPENDENCIAS DE LIFECYCLE (PARA VIEWMODEL) ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2") // Para collectAsState
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2") // ¡IMPORTANTE! Para la función viewModel()
    kapt("androidx.lifecycle:lifecycle-compiler:2.8.2")
    kapt("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2") //<- Se agrego esta linea
    // --- NAVIGATION ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- ROOM (BASE DE DATOS) ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2") // ¡Esta es la línea que falta!

    // --- CAMERAX ---
    val cameraxVersion = "1.3.3"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // --- ESCÁNER QR ---
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // --- DATASTORE ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // --- DESUGARING (PARA COMPATIBILIDAD CON APIS DE JAVA 8+) ---
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    // --- DEPENDENCIAS DE TEST ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // AÑADIR PARA Arreglar "Unresolved reference 'coil'" en PerfilScreen.kt
    implementation("io.coil-kt:coil-compose:2.6.0")

// AÑADIR PARA Arreglar "Unresolved reference 'accompanist'" en TomarFotoScreen.kt
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

// AÑADIR PARA Arreglar "Property delegate must have a 'getValue'..." en RegistroScreen.kt
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

}


// El bloque Kapt se mantiene porque Room lo necesita
kapt {
    correctErrorTypes = true
}
