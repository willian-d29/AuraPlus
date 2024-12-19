// Archivo de configuración de nivel superior para todos los módulos del proyecto
plugins {
    alias(libs.plugins.android.application) apply false // Android plugin
    alias(libs.plugins.kotlin.android) apply false // Kotlin para Android
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false


}

repositories {

}
