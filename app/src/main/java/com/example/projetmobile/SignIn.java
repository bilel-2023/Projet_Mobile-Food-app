package com.example.projetmobile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvSignUpRedirect;
    private ImageView signupImg; // Reference to the ImageView
    private ProgressBar progressBar; // ProgressBar for loading state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Find Views by ID
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUpRedirect = findViewById(R.id.tvSignUpRedirect);
        signupImg = findViewById(R.id.signupimg); // Initialize the ImageView
        progressBar = findViewById(R.id.progressBar); // ProgressBar initialization

        // Set up Sign-In button click listener
        btnSignIn.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                // Trigger earthquake animation on the ImageView
                shakeImageView(signupImg);

                // Show Toast message
                Toast.makeText(SignIn.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show the loading progress bar
            progressBar.setVisibility(ProgressBar.VISIBLE);

            // Sign in with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // Hide the progress bar
                        progressBar.setVisibility(ProgressBar.INVISIBLE);

                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(SignIn.this, "Welcome back!", Toast.LENGTH_SHORT).show();

                            // Get current user details
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userName = user.getDisplayName(); // Get the display name
                                String userEmail = user.getEmail(); // Get the email

                                // Save user data in SharedPreferences (for later use)
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userEmail", userEmail);
                                editor.putString("userName", userName); // Save the display name
                                editor.apply();
                            }

                            // Redirect to MainActivity
                            startActivity(new Intent(SignIn.this, HomeActivity.class));
                            finish();
                        } else {
                            // Login failed
                            // Trigger earthquake animation on the ImageView for error
                            shakeImageView(signupImg);

                            Toast.makeText(SignIn.this, "Sign-In Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Redirect to Sign-Up page
        tvSignUpRedirect.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });
    }

    // Shake animation for the ImageView (earthquake effect)
    private void shakeImageView(ImageView imageView) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(imageView, "translationX", -25f, 25f, -25f, 25f, 0f);
        shake.setDuration(500); // Duration of the animation
        shake.start();
    }
}
