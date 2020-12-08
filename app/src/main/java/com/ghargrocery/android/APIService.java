package com.ghargrocery.android;

import com.ghargrocery.android.Notifications.MyResponse;
import com.ghargrocery.android.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService  {
    @Headers(
            {
                    "Authorization:key=AAAA0IPSnhA:APA91bF5KKSTDmBYOJUVcntXv44y8v5knMF1wUuRYaUvBI918zgfKqEhPtIYLYxldYTxMehCgAFwvF-M_I_2NY3CcTt9g8cQ-1bCEZMe8JXQaVkbFzWGr-2HbSDBgk45wzc2EHmmKEEM",
                    "Content-Type:application/json"

            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
