package com.example.projetmobile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText etName, etEmail, etPassword;
    private Button btnSignUp, btnGoogleSignUp;
    private TextView tvSignInRedirect;
    private ImageView signupImg; // Reference to the ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // Enable edge-to-edge display
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your Firebase Web Client ID
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Find Views by ID
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoogleSignUp = findViewById(R.id.btnGoogleSignUp);
        tvSignInRedirect = findViewById(R.id.tvSignInRedirect);
        signupImg = findViewById(R.id.signupimg); // ImageView reference

        // Set up Email/Password Sign-Up button
        btnSignUp.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                // Trigger earthquake animation on the ImageView
                shakeImageView(signupImg);

                // Show Toast message
                Toast.makeText(SignUp.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Update the user's display name after successful account creation
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name) // Set the username (name) here
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "Welcome, " + name, Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignUp.this, SignIn.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, "Failed to set username: " + profileTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Trigger earthquake animation on the ImageView for error
                            shakeImageView(signupImg);

                            Toast.makeText(SignUp.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set up Google Sign-Up button
        btnGoogleSignUp.setOnClickListener(view -> googleSignUp());

        // Redirect to Sign-In page
        tvSignInRedirect.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });
    }

    // Shake animation for the ImageView (earthquake effect)
    private void shakeImageView(ImageView imageView) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(imageView, "translationX", -25f, 25f, -25f, 25f, 0f);
        shake.setDuration(500); // Duration of the animation
        shake.start();
    }

    // Google Sign-In method
    private void googleSignUp() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    // Google Sign-In Result Launcher
    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
                    if (account != null) {
                        firebaseAuthWithGoogle(account.getIdToken());
                    }
                } else {
                    // Trigger earthquake animation on the ImageView for Google Sign-In failure
                    shakeImageView(signupImg);

                    Toast.makeText(SignUp.this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
                }
            });

    // Firebase authentication with Google credentials
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(SignUp.this, "Signed in with Google: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        // Trigger earthquake animation on the ImageView for Google Sign-In failure
                        shakeImageView(signupImg);

                        Toast.makeText(SignUp.this, "Google Sign-In Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
