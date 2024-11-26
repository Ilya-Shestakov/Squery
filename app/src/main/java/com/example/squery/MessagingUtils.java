package com.example.squery;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagingUtils {
    private FirebaseMessaging msgInst = FirebaseMessaging.getInstance();


    // GETTING TOKEN MOBILE

    public void logToken() {
        msgInst.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(Task<String> task) {
                if (task.isSuccessful()) {

                    main(task.getResult());
                }
            }
        });
    }



    public static void sendMessageToTelegram(String token, String chatId, String message) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";
        String json = "{\n" +
                "    \"chat_id\": \"" + chatId + "\",\n" +
                "    \"text\": \"" + message + "\"\n" +
                "}";

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                System.out.println(response.body().string());
            }
        });
    }

    public static void main(String message) {
        String token = "7145282253:AAH0Ykv6m8tn9STsdLEqiLuwQwaCUkjd5nQ";
        String chatId = "-1002155842233";

        sendMessageToTelegram(token, chatId, message);
    }



}

