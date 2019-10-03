package com.t3h.appdemo.api;

import com.t3h.appdemo.notification.MyResponse;
import com.t3h.appdemo.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers
            (
                    {
                            "Content-Type:application/json",
                            "Authorization:key=AAAA_9Ofl3c:APA91bF9PLGe3MvrAnzUGAyL863v65S74aNEQjBofa-9uxO0VQ4vukOq0OafnoOQZQChR9_PmGB3MofQMvk3bXay4-L4rtDqM5bqQbFld3qnTga1C1GG1yoy5WUth1CRLwkc5UAH2NMz"
                    }
            )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
