package com.ragab.ahmed.educational.happenings.network;

import com.ragab.ahmed.educational.happenings.data.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Ragabov on 3/28/2016.
 */
public interface IseeApi {

@FormUrlEncoded
@POST(ApiHelper.USER_ENDPOINT + "/sign_up")
    Call<Void> signUp(@Field("fname") String firstName, @Field("lname") String lastName,
                           @Field("email") String email, @Field("password") String password);

@FormUrlEncoded
@POST(ApiHelper.USER_ENDPOINT + "/sign_in")
    Call<User> signIn(@Field("email") String email, @Field("password") String password);

@Multipart
@POST(ApiHelper.EVENT_ENDPOINT + "/submit_event")
Call<ResponseBody> submitEvent(@Part("event_name") String eventName,
                       @Part("event_description") String description,
                       @Part("longitude") double longitude,
                       @Part("latitude") double latitude,
                       @Part("type_id") int type,
                       @Part("anonymous") boolean isAnonymous,
                       @Part("user_id") long id,
                       @Part MultipartBody.Part  image);
}