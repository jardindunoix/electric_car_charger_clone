import java.util.Properties

val properties: Properties = Properties()

properties.load(
   project.rootProject
      .file("local.properties")
      .inputStream()
)

plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
// asdded
   alias(libs.plugins.android.dagger.hilt)
//   alias(libs.plugins.ksp)
   id("kotlin-android")
   id("kotlin-kapt")
}

android {
   namespace = "cl.codechunksdev.electriccarcharger2"
   compileSdk = 34

   defaultConfig {
      applicationId = "cl.codechunksdev.electriccarcharger2"
      minSdk = 28
      targetSdk = 34
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

      vectorDrawables {
         useSupportLibrary = true
      }
   }

   buildTypes {
      debug {
         applicationIdSuffix = ".debug"
         isDebuggable = true
         isMinifyEnabled = false
      }

      release {
         isMinifyEnabled = true
         isShrinkResources = true
         isDebuggable = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }

   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }

   buildFeatures {
      buildConfig = true
      viewBinding = true
//      compose = true
   }

//   composeOptions {
//      kotlinCompilerExtensionVersion = "1.5.14"
//   }

//   update kotlin version
   kotlinOptions {
      jvmTarget = "1.8"
   }

   flavorDimensions += "version"
   productFlavors {

      create("Charger") {
         applicationIdSuffix = ".Charger"
         versionNameSuffix = "1_Charger"

         resValue(
            "string",
            "key_map",
            properties.getProperty("API_MAP_KEY")
         )

         resValue(
            "string",
            "app_name",
            "Charger"
         )

         buildConfigField(
            "String",
            "URL_BASE",
            properties.getProperty("URL_BASE")
         )

         buildConfigField(
            "String",
            "COMPANY_ID",
            properties.getProperty("COMPANY_ID")
         )

      }

   }

}

dependencies {
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)
   implementation(libs.androidx.activity)
   implementation(libs.androidx.constraintlayout)
   // TEST
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)

   /* TESTS ADDED */
   testImplementation(libs.mokk)
   testImplementation(libs.mockitocore)
   testImplementation(libs.mockitoinline)

   /*splashscreen*/
   implementation(libs.androidx.splash)

   /*google map*/
   implementation(libs.play.services.maps)
   implementation(libs.play.services.location)
   implementation(libs.android.maps.utils)

   /*navigation NO CAMBIAR LA VERSION "2.6.0" **************/
   implementation(libs.androidx.navigation.fragment)
   implementation(libs.androidx.navigation.fragmentktx)
   implementation(libs.androidx.legacy)

   /*picasso*/
   implementation(libs.androidx.picasso)
   // constraint layout
   implementation(libs.androidx.constraintlayout)
   //    retrofit
   implementation(libs.androidx.retrofit)
   implementation(libs.androidx.retrofitgson)
// okhttp
   implementation(libs.okhttp)
   implementation(libs.googlegson)
   implementation(libs.okhttp.interceptor)

   //    coroutines
   implementation(libs.androidx.coroutines)
   implementation(libs.androidx.coroutinesandroid)

   //   coroutine lifecycle scopes
   implementation(libs.androidx.activityktx)
   implementation(libs.androidx.lifecyclesextensions)
   implementation(libs.androidx.lifecycle)
   implementation(libs.androidx.lifecycle)
   implementation(libs.androidx.lifecycle.runtime.ktx)

   // KTX - Viewmodel Y Livedata NO ACTUALIZAR: 2.5.1
   implementation(libs.androidx.lifecycle.livedata)
   //    data store
   implementation(libs.androidx.datastore)
   //Room
   implementation(libs.room)
   // kapt genera el codigo para la base de datos
   kapt(libs.roomkaptcompiler)
   implementation(libs.androidx.room.runtime)

   //Hilt
   implementation(libs.dagger.google)
   implementation(libs.androidx.hilt.navigation.compose.v120)
   annotationProcessor(libs.androidx.google.compiler)
   kapt(libs.androidx.google.dagger.kapt.android)
   kapt(libs.androidx.google.compiler)
   kapt(libs.androidx.hilt.compiler.v120)

   // shimmer
   implementation(libs.shimmer)

//   COMPOSE
   implementation(platform(libs.androidx.compose.bom))
   implementation(libs.ui)
   implementation(libs.androidx.material)
   implementation(libs.androidx.runtime)
   implementation(libs.androidx.activity.compose)
   implementation(libs.ui.graphics)
   implementation(libs.ui.tooling.preview)

}

kapt { correctErrorTypes = true }