// build.gradle.kts (Project level)
plugins {
    // Use a mesma versão do plugin Kotlin que das libs Kotlin
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.services) apply false
}

// Configuração comum para todos os subprojetos/módulos
subprojects {
    configurations.all {
        // Callback para resolver conflitos de dependências
        resolutionStrategy {
            // Forçar versão única do Kotlin
            force("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-common:1.8.10")
        }
    }
}