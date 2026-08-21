import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
}

val vkRewardedSlotId = localProperties
    .getProperty("VK_REWARDED_SLOT_ID")
    ?.takeIf { it.isNotBlank() }
    ?: "0"
val vkBannerSlotId = localProperties
    .getProperty("VK_BANNER_SLOT_ID")
    ?.takeIf { it.isNotBlank() }
    ?: "0"
val yandexRewardedAdUnitId = localProperties
    .getProperty("YANDEX_REWARDED_AD_UNIT_ID")
    ?.takeIf { it.isNotBlank() }
    ?: ""
val yandexBannerAdUnitId = localProperties
    .getProperty("YANDEX_BANNER_AD_UNIT_ID")
    ?.takeIf { it.isNotBlank() }
    ?: ""

fun String.asBuildConfigString(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

android {
    namespace = "com.alonso.dotdash"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.alonso.dotdash"
        minSdk = 26
        targetSdk = 37
        versionCode = 7
        versionName = "1.0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField("int", "VK_REWARDED_SLOT_ID", vkRewardedSlotId)
            buildConfigField("int", "VK_BANNER_SLOT_ID", vkBannerSlotId)
            buildConfigField(
                "String",
                "YANDEX_REWARDED_AD_UNIT_ID",
                yandexRewardedAdUnitId
                    .ifBlank { "demo-rewarded-yandex" }
                    .asBuildConfigString()
            )
            buildConfigField(
                "String",
                "YANDEX_BANNER_AD_UNIT_ID",
                yandexBannerAdUnitId
                    .ifBlank { "demo-banner-yandex" }
                    .asBuildConfigString()
            )
        }
        release {
            buildConfigField("int", "VK_REWARDED_SLOT_ID", vkRewardedSlotId)
            buildConfigField("int", "VK_BANNER_SLOT_ID", vkBannerSlotId)
            buildConfigField(
                "String",
                "YANDEX_REWARDED_AD_UNIT_ID",
                yandexRewardedAdUnitId.asBuildConfigString()
            )
            buildConfigField(
                "String",
                "YANDEX_BANNER_AD_UNIT_ID",
                yandexBannerAdUnitId.asBuildConfigString()
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("androidx.datastore:datastore-preferences-core:1.2.1")
    implementation("com.my.target:mytarget-sdk:5.47.1")
    implementation("com.yandex.android:mobileads:8.3.0")
}
