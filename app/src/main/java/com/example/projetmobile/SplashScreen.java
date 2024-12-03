package com.example.projetmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        ImageView imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);

        // Set onClickListener for the button
        button.setOnClickListener(v -> {
            // Rotate the image for 4 seconds
            RotateAnimation rotateAnimation = new RotateAnimation(
                    0, 360,  // Start and end angle
                    Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot X at center
                    Animation.RELATIVE_TO_SELF, 0.5f   // Pivot Y at center
            );
            rotateAnimation.setDuration(4000);  // Duration in milliseconds
            rotateAnimation.setFillAfter(true); // Keep the rotation state after animation ends

            // Start animation
            imageView.startAnimation(rotateAnimation);

            // Transition to SignupActivity after the animation ends
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, SignUp.class);
                startActivity(intent);
                finish(); // Close SplashScreen to prevent going back to it
            }, 4000); // Delay equal to animation duration
        });
    }
}
