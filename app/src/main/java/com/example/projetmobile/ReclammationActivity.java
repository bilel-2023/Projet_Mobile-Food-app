package com.example.projetmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReclammationActivity extends AppCompatActivity {
    private Button sendReclamationButton;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclammation); // Replace with your layout file if necessary

        // Initialize UI components
        sendReclamationButton = findViewById(R.id.send_reclamation); // Replace with the actual button ID in your layout file
        editTextMessage = findViewById(R.id.edit_text_message); // Replace with your EditText ID for the message

        // Example usage
        String recipient = "adhemnaiji@gmail.com"; // Replace with the recipient's email

        sendReclamationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user information from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userName = sharedPreferences.getString("userName", "Unknown User");
                String userEmail = sharedPreferences.getString("userEmail", "No Email Provided");

                // Get message content from EditText
                String reclamationMessage = editTextMessage.getText().toString();

                // Validate input
                if (reclamationMessage.isEmpty()) {
                    Toast.makeText(ReclammationActivity.this, "Veuillez écrire un message.", Toast.LENGTH_SHORT).show();
                } else {
                    // Combine user info and message
                    String fullMessage = "User Information:\n" +
                            "Name: " + userName + "\n" +
                            "Email: " + userEmail + "\n\n" +
                            "Message:\n" + reclamationMessage;

                    sendReclamationEmail(recipient, fullMessage);
                }
            }
        });
    }

    /**
     * Sends a reclamation email to the specified recipient.
     *
     * @param recipient   The recipient's email address.
     * @param fullMessage The full content of the reclamation email, including user information.
     */
    private void sendReclamationEmail(String recipient, String fullMessage) {
        new Thread(() -> {
            try {
                // Sender email credentials
                final String senderEmail = "bonsoincentre.info@gmail.com"; // Replace with your email
                final String senderAppPassword = "ijfblvadxezpluiz"; // Replace with your app password

                // Configure SMTP properties
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // Initialize the email session with authentication
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderAppPassword);
                    }
                });

                // Create the email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject("Reclamation");
                message.setText("Bonjour,\n\n" + fullMessage + "\n\nMerci.");

                // Send the email
                Transport.send(message);
                startActivity(new Intent(ReclammationActivity.this, HomeActivity.class));


                // Show success message on the UI thread
                runOnUiThread(() -> Toast.makeText(ReclammationActivity.this, "Email de réclamation envoyé avec succès", Toast.LENGTH_SHORT).show());
            } catch (MessagingException e) {
                // Log error and show failure message on the UI thread
                Log.e("EmailError", "Erreur lors de l'envoi de l'email", e);
                runOnUiThread(() -> Toast.makeText(ReclammationActivity.this, "Erreur lors de l'envoi de l'email", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
