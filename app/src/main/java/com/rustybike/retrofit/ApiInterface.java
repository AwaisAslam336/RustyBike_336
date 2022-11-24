package com.rustybike.retrofit;

import com.rustybike.models.Restaurants;
import com.rustybike.models.Result;
import com.rustybike.response.AddUserRes;
import com.rustybike.response.CreateOrderRes;
import com.rustybike.response.CreateRestaurantRes;
import com.rustybike.response.LoginResponse;
import com.rustybike.response.OrderListRes;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.response.UpdateOrderRes;
import com.rustybike.response.UserListRes;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    //@Headers({"Content-Type: application/json"})
    @FormUrlEncoded
    @POST("rustybikeapi/api/users/login")
    Call<LoginResponse> loginUser(@Field("UserName") String username,
                                  @Field("Password") String password);

    @FormUrlEncoded
    @POST("rustybikeapi/api/orders/getorderlist")
    Call<OrderListRes> getOrderList(@Field("Token") String token,
                                          @Field("ID") String id,
                                          @Field("RestaurantId") int restaurantId);
    @FormUrlEncoded
    @POST("rustybikeapi/api/users/allusers")
    Call<UserListRes> getAllUserList(@Field("Token") String token,
                                     @Field("ID") String id);
    @FormUrlEncoded
    @POST("rustybikeapi/api/users/restaurantusers")
    Call<UserListRes> getUsersByRestaurantId(@Field("RestaurantId") int restaurantId);


    @POST("rustybikeapi/api/restaurant/allrestaurants")
    Call<RestaurantsListRes> getAllRestaurants();

    @FormUrlEncoded
    @POST("rustybikeapi/api/orders/getorderlist")
    Call<OrderListRes> getOrderListbyDate(@Field("Token") String token,
                                          @Field("ID") String id,
                                          @Field("RestaurantId") int restaurantId,
                                          @Field("SortDate")String date);

    @FormUrlEncoded
    @POST("rustybikeapi/api/orders/create")
    Call<CreateOrderRes> createOrder(@Field("Token") String token,
                                    @Field("ID") String id,
                                    @Field("RestaurantId") int restaurantId,
                                    @Field("OrderCompanyId") int companyId,
                                    @Field("OrderRefNumber") String OrderRef,
                                    @Field("OrderStatus") String Status);
    @FormUrlEncoded
    @POST("rustybikeapi/api/users/register")
    Call<AddUserRes> createUser(@Field("UserName") String userName,
                                @Field("Password") String password,
                                @Field("UserType") String userType,
                                @Field("FullName") String fullName,
                                @Field("IsActive") int isActive,
                                @Field("RestaurantId") int restaurantId);
    @FormUrlEncoded
    @POST("rustybikeapi/api/restaurant/add")
    Call<CreateRestaurantRes> createRestaurant(@Field("Token") String token,
                                               @Field("ID") int id,
                                               @Field("RestaurantCity") String RestaurantCity,
                                               @Field("RestaurantName") String RestaurantName,
                                               @Field("RestaurantAddress") String RestaurantAddress,
                                               @Field("RestaurantPhone") String RestaurantPhone,
                                               @Field("RestaurantFax") String RestaurantFax,
                                               @Field("RestaurantPostCode") String RestaurantPostCode);

    @FormUrlEncoded
    @POST("rustybikeapi/api/restaurant/update")
    Call<UpdateOrderRes> updateRestaurant(@Field("Token") String token,
                                               @Field("ID") int id,
                                               @Field("RestaurantCity") String RestaurantCity,
                                               @Field("RestaurantName") String RestaurantName,
                                               @Field("RestaurantAddress") String RestaurantAddress,
                                               @Field("RestaurantPhone") String RestaurantPhone,
                                               @Field("RestaurantFax") String RestaurantFax,
                                               @Field("RestaurantPostCode") String RestaurantPostCode,
                                               @Field("RestaurantStatus") int status,
                                               @Field("RestaurantId") int restaurantId);

    @FormUrlEncoded
    @POST("rustybikeapi/api/orders/updateorderstatus")
    Call<UpdateOrderRes> UpdateOrder(@Field("Token") String token,
                                    @Field("ID") int id,
                                    @Field("OrderId") int orderid,
                                    @Field("OrderStatus") String Status,
                                     @Field("OrderDate") String date);
    @FormUrlEncoded
    @POST("rustybikeapi/api/users/update")
    Call<UpdateOrderRes> UpdateUser(@Field("UserName") String userName,
                                    @Field("Password") String password,
                                    @Field("UserType") String UserType,
                                    @Field("FullName") String FullName,
                                    @Field("UserId") int UserId,
                                    @Field("Token") String token,
                                    @Field("ID") String id,
                                    @Field("IsActive") String status);

    @FormUrlEncoded
    @POST("rustybikeapi/api/orders/deleteorder")
    Call<Result> DeletOrder(@Field("Token") String token,
                            @Field("ID") int id,
                            @Field("OrderId") int orderid);

    @FormUrlEncoded
    @POST("rustybikeapi/api/users/delete")
    Call<Result> DeletUser(@Field("Token") String token,
                           @Field("ID") String id,
                           @Field("UserId") int userId);

    @FormUrlEncoded
    @POST("rustybikeapi/api/restaurant/delete")
    Call<Result> DeletRestaurant(@Field("Token") String token,
                                 @Field("ID") int id,
                                 @Field("RestaurantId") int restaurantId);

}
