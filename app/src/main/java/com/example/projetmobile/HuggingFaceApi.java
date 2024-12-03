package com.example.projetmobile;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HuggingFaceApi {

    @Headers("Authorization: Bearer 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") // Correct format for Authorization header
    @POST("https://api-inference.huggingface.co/models/Qwen/QwQ-32B-Preview") // No need for the full URL, just the endpoint
    Call<ResponseBody> getResponse(@Body RequestBody body);
}