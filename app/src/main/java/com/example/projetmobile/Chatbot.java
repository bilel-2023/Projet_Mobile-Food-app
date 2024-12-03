package com.example.projetmobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chatbot extends AppCompatActivity {

    private HuggingFaceApi huggingFaceApiService; // Only Hugging Face API service
    private LinearLayout chatContainer;
    private EditText inputMessage;
    private ScrollView chatScrollView;

    private final String huggingFaceApiKey = "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"; // Replace with your Hugging Face API token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbot);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        chatContainer = findViewById(R.id.chatContainer);
        inputMessage = findViewById(R.id.inputMessage);
        chatScrollView = findViewById(R.id.chatScrollView);
        Button sendButton = findViewById(R.id.sendButton);

        // Initialize Retrofit API service
        huggingFaceApiService = RetrofitClient.getHuggingFaceRetrofitInstance().create(HuggingFaceApi.class);

        sendButton.setOnClickListener(v -> {
            String message = inputMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessageToChat("You: " + message);
                fetchChatbotResponse(message);
                inputMessage.setText("");
            }
        });
    }

    private void fetchChatbotResponse(String message) {
        // Example of using Hugging Face API
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("inputs", message);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        huggingFaceApiService.getResponse(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        // Parse the JSON response to extract just the text
                        JsonArray jsonResponse = new Gson().fromJson(responseData, JsonArray.class);
                        StringBuilder botResponseText = new StringBuilder();

                        // Loop through the response array if needed
                        for (JsonElement element : jsonResponse) {
                            JsonObject responseObject = element.getAsJsonObject();
                            // Adjust the key based on actual response structure
                            botResponseText.append(responseObject.get("generated_text").getAsString()).append("\n");
                        }

                        addMessageToChat("Specialist (Hugging Face): " + botResponseText.toString().trim());
                    } catch (Exception e) {
                        Log.e("ChatbotActivity", "Error parsing Hugging Face response", e);
                        addMessageToChat("Bot (Hugging Face): Sorry, an error occurred.");
                    }
                } else {
                    addMessageToChat("Bot (Hugging Face): Sorry, I didn't understand that.");
                }
                scrollToBottom();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                addMessageToChat("Bot (Hugging Face): Something went wrong. Please try again.");
                Log.e("ChatbotActivity", "Hugging Face API call failed", t);

            }
        });
    }

    private void addMessageToChat(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        chatContainer.addView(textView);
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }
}