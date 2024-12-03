package com.example.projetmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private ListView profileListView;
    private TextView userNameTextView, userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize the ListView
        profileListView = findViewById(R.id.profile_list_view);

        // Initialize the TextViews
        userNameTextView = findViewById(R.id.user_name);
        userEmailTextView = findViewById(R.id.user_email);

        // Retrieve user details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Guest");
        String userEmail = sharedPreferences.getString("userEmail", "No email available");

        // Set the retrieved data in the TextViews
        userNameTextView.setText(userName);
        userEmailTextView.setText(userEmail);

        // Array of items (text)
        String[] profileOptions = {"Logout", "Preferences", "Share", "Reclamation"};

        // Array of icons
        int[] profileIcons = {
                R.drawable.diet,      // Icon for Logout
                R.drawable.fastfood,  // Icon for Preferences
                R.drawable.eating,    // Icon for Share
                R.drawable.healthyfood    // Icon for Reclamation (add a suitable drawable)
        };

        // Create the custom adapter
        ProfileAdapter adapter = new ProfileAdapter(this, profileOptions, profileIcons);

        // Set the adapter to the ListView
        profileListView.setAdapter(adapter);

        // Set item click listener for the ListView
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Handle Logout
                        Toast.makeText(Profile.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                        logout();
                        break;
                    case 1:
                        // Handle Preferences
                        Toast.makeText(Profile.this, "Preferences clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Profile.this, PreferencesActivity.class));
                        break;
                    case 2:
                        // Handle Share
                        Toast.makeText(Profile.this, "Share clicked", Toast.LENGTH_SHORT).show();
                        shareApp();
                        break;
                    case 3:
                        // Handle Reclamation
                        Toast.makeText(Profile.this, "Reclamation clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Profile.this, ReclammationActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(Profile.this, SignIn.class));
        finish();
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download our app from the Play Store: [Link]");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
