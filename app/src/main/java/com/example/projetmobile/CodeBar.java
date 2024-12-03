package com.example.projetmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeBar extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private boolean isProcessingBarcode = false; // Flag to prevent multiple scans

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_bar);

        // Set up the PreviewView and camera executor
        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Set window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check for camera permissions and start the camera
        if (hasCameraPermission()) {
            startCamera();
        } else {
            requestCameraPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission is needed to scan barcodes", Toast.LENGTH_LONG).show();
        }
        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Create Preview use case
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::processImage);

                // Bind the camera to the lifecycle
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                Log.e("CodeBar", "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void processImage(@NonNull ImageProxy imageProxy) {
        if (isProcessingBarcode) {
            imageProxy.close(); // Close the image proxy if already processing
            return;
        }
        isProcessingBarcode = true; // Set flag to indicate processing

        try {
            @SuppressLint("UnsafeOptInUsageError")
            InputImage image = InputImage.fromMediaImage(
                    imageProxy.getImage(),
                    imageProxy.getImageInfo().getRotationDegrees()
            );

            BarcodeScanner scanner = BarcodeScanning.getClient();

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            String value = barcode.getRawValue();
                            if (value != null) {
                                fetchProductInfo(value);
                                break; // Stop after the first successful scan
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("CodeBar", "Barcode scan failed: " + e.getMessage()))
                    .addOnCompleteListener(task -> {
                        imageProxy.close();
                        isProcessingBarcode = false; // Reset flag after processing
                    });
        } catch (Exception e) {
            Log.e("CodeBar", "Error processing image: " + e.getMessage());
            imageProxy.close();
            isProcessingBarcode = false; // Reset flag on error
        }
    }

    private void fetchProductInfo(String barcode) {
        OpenFoodApi apiService = RetrofitClient.getRetrofitInstanceOpenFoodFacts().create(OpenFoodApi.class);

        apiService.getProductInfo(barcode).enqueue(new Callback<ProductInfo>() {
            @Override
            public void onResponse(Call<ProductInfo> call, Response<ProductInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductInfo productInfo = response.body();
                    ProductInfo.Product product = productInfo.getProduct();

                    // Prepare the Intent to start ProductScannedDetails
                    Intent intent = new Intent(CodeBar.this, ProductScannedDetails.class);

                    // Assuming 'product' is an object of a class that has the required methods
                    if (product.getProductName() != null) {
                        intent.putExtra("product_name", product.getProductName());
                    } else {
                        Toast.makeText(CodeBar.this, "Product name not available", Toast.LENGTH_SHORT).show();
                    }

                    if (product.getIngredientsText() != null) {
                        intent.putExtra("ingredients", product.getIngredientsText());
                    } else {
                        Toast.makeText(CodeBar.this, "Ingredients not available", Toast.LENGTH_SHORT).show();
                    }

                    if (product.getNutritionGrade() != null) {
                        intent.putExtra("nutrition_grade", product.getNutritionGrade());
                    } else {
                        Toast.makeText(CodeBar.this, "Nutrition grade not available", Toast.LENGTH_SHORT).show();
                    }

                    if (product.getImageUrl() != null) {
                        intent.putExtra("image_url", product.getImageUrl());
                    } else {
                        Toast.makeText(CodeBar.this, "Image URL not available", Toast.LENGTH_SHORT).show();
                    }

// Start the new activity
                    startActivity(intent);
                    finish(); // Optionally finish this activity to prevent going back
                } else {
                    Toast.makeText(CodeBar.this, "Product information not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductInfo> call, Throwable t) {
                Toast.makeText(CodeBar.this, "Error fetching product info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}