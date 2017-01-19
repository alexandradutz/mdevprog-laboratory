package com.example.dutzi.snowy;

import com.example.dutzi.snowy.model.Resort;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by dutzi on 19.01.2017.
 */

public interface REST {
    @POST("/snowy.json")
    Call<Resort> addResort(@Body Resort res);

    @DELETE("/snowy/{id}.json")
    Call<Resort> removeResort(@Path("id") String id);

    @GET("/snowy.json")
    Call<ResponseBody> getId(@Query("orderBy") String orderBy, @Query("equalTo") String equalTo);

    @PUT("/snowy/{id}.json")
    Call<Resort> updateResort(@Path("id") String id,@Body Resort res);


}
