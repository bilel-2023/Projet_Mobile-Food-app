import com.android.build.api.dsl.*;
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services) // Firebase
}

        android {
            namespace = "com.example.projetmobile"
            compileSdk = 34

            defaultConfig {
                applicationId = "com.example.projetmobile"
                minSdk = 24
                targetSdk = 34
                versionCode = 1
                versionName = "1.0"

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            buildFeatures {
                viewBinding = true // Enable view binding if required for easier layout handling
            }

            // Packaging options to exclude duplicate META-INF files
            packagingOptions {
                exclude("**/META-INF/LICENSE")
                exclude("**/META-INF/LICENSE.txt")
                exclude("**/META-INF/NOTICE")
                exclude("**/META-INF/NOTICE.txt")
                exclude("**/META-INF/DEPENDENCIES")
                exclude("**/META-INF/DEPENDENCIES.txt")
                exclude("**/META-INF/NOTICE.md")
                exclude("**/META-INF/LICENSE.md")
            }
        }

dependencies {
    // Core UI components
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)

    // CameraX and Views
    implementation(libs.camera.camera2)
    implementation(libs.camera.view)
    implementation(libs.camera.lifecycle)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Lifecycle and ViewModel
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.annotation)

    // Image Loading with Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Networking with Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Firebase Integration
    implementation(libs.firebase.auth)
    implementation(libs.firestore)

    // Google Play Services and Maps
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.android.gms:play-services-location:17.0.0")

    // ML Kit for Barcode Scanning
    implementation(libs.mlkit.barcode.scanning)

    // External Map SDK
    implementation("org.osmdroid:osmdroid-android:6.1.20")

    // CardView and RecyclerView
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Image loading with Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    // Email Sending (JavaMail API)
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:21.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
