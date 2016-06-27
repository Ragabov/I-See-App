package com.ragab.ahmed.educational.happenings.network;

import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.data.models.FavouriteLocation;
import com.ragab.ahmed.educational.happenings.data.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
                                   @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(ApiHelper.EVENT_ENDPOINT + "/confirm")
    Call<ResponseBody> confirmEvent(@Field("user_id") long userId, @Field("event_id") long eventId);

    @FormUrlEncoded
    @POST(ApiHelper.EVENT_ENDPOINT + "/disconfirm")
    Call<ResponseBody> disconfirmEvent(@Field("user_id") long userId, @Field("event_id") long eventId);

    @FormUrlEncoded
    @POST(ApiHelper.USER_ENDPOINT + "/get_favorite")
    Call<FavouriteLocation[]> getFavouriteLocation(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST(ApiHelper.USER_ENDPOINT + "/add_favorite")
    Call<Integer> addFavouriteLocation(
            @Field("favorite_name") String name, @Field("latitude") double latitude,
            @Field("longitude") double longitude, @Field("user_id") long userId);

    @FormUrlEncoded
    @POST(ApiHelper.USER_ENDPOINT + "/delete_favorite")
    Call<ResponseBody> deleteFavouriteLocation(@Field("favorite_id") long favoriteId);

    @FormUrlEncoded
    @POST(ApiHelper.EVENT_ENDPOINT + "/events_by")
    Call<ArrayList<Event>> getEvents(@Field("start_lattitude") double startLatitude, @Field("start_longitude") double startLongitude,
                                 @Field("end_lattitude") double endLatitude, @Field("end_longitude") double endLongitude,
                                 @Field("start_date") Long startDate, @Field("end_date") Long endDate, @Field("category") Integer categoryId);
    @FormUrlEncoded
    @POST(ApiHelper.EVENT_ENDPOINT + "/events_around")
    Call<ArrayList<Event>> getEventsAround(@Field("user_id") long userId, @Field("latitude") double latitude, @Field("longitude") double longitude);

    @Multipart
    @POST(ApiHelper.USER_ENDPOINT + "/profile_picture")
    Call<String> updateProfilePicture(@Part("id") long userId,@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST(ApiHelper.USER_ENDPOINT + "/history")
    Call<ArrayList<Event>> getUserHistory(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST(ApiHelper.USER_ENDPOINT + "/get_users")
    Call<ArrayList<User>> getUsers(@Field("users_id") ArrayList<Integer> userIds);

}
