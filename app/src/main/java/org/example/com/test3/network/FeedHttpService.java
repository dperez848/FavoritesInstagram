package org.example.com.test3.network;

import org.example.com.test3.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeedHttpService {

    @GET("/v1/media/popular")
    Call<ResponseModel> getAll(@Query("client_id") String client_id);

}
