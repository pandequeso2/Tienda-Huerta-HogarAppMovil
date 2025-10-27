plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false // Hilt definido aquí
    alias(libs.plugins.kotlin.kapt) apply false   // Kapt definido aquí
    alias(libs.plugins.kotlin.compose) apply false // Compose definido aquí
}
