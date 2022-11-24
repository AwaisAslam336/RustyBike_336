package com.rustybike.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rustybike.Activities.UpdateRestaurantActivity;
import com.rustybike.Activities.UpdateUserActivity;
import com.rustybike.R;
import com.rustybike.models.Restaurants;
import com.rustybike.models.Result;
import com.rustybike.models.Users;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.response.UserListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminRestaurantListAdapter extends RecyclerView.Adapter<AdminRestaurantListAdapter.UsersViewHolder> {

    Activity context;
    List<Restaurants> restaurants;
    RestaurantsListRes restaurantsListRes;
    ApiInterface apiInterface;
    String token;
    int restid;
       int ID;

    public AdminRestaurantListAdapter(Activity context, RestaurantsListRes restaurantsListRes,String token,int ID) {
        this.restaurantsListRes = restaurantsListRes;
        this.context = context;
        restaurants = restaurantsListRes.getData();
        apiInterface = Api.getClient();
        this.token = token;
        this.ID = ID;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_card, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {

        String name = restaurants.get(position).getRestaurantName();
        String phone = restaurants.get(position).getRestaurantPhone();
        holder.restaurantName.setText(name);
        holder.restaurantPhone.setText(phone);
        holder.action_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateRestaurantActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", ID);
                intent.putExtra("RestaurantCity",restaurants.get(position).getRestaurantCity());
                intent.putExtra("RestaurantName",restaurants.get(position).getRestaurantName());
                intent.putExtra("RestaurantAddress",restaurants.get(position).getRestaurantAddress());
                intent.putExtra("RestaurantPhone",restaurants.get(position).getRestaurantPhone());
                intent.putExtra("RestaurantFax",restaurants.get(position).getRestaurantFax());
                intent.putExtra("RestaurantPostCode",restaurants.get(position).getRestaurantPostCode());
                intent.putExtra("RestaurantId",restaurants.get(position).getRestaurantId());
                context.startActivityForResult(intent,0);
            }
        });

        holder.action_btn_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int restaurantId = restaurants.get(position).getRestaurantId();
                AlertDialog.Builder alertDialogB =new AlertDialog.Builder(context);
                alertDialogB.setTitle("Delete Confirmation");
                alertDialogB.setIcon(R.drawable.ic_priority_high_black_24dp);
                alertDialogB.setMessage("Alert: Are you sure, you want to Delete Restaurant permanently!");
                alertDialogB.setCancelable(false);
                alertDialogB.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<Result> resultCall = apiInterface.DeletRestaurant(token,ID,restaurantId);
                        resultCall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {

                                if (response.body().getCode() == 0){
                                    Toast.makeText(context,restaurants.get(position).getRestaurantName()+" Restaurant deleted",Toast.LENGTH_SHORT).show();
                                    ((AdminRestaurantListAdapter.callback2) context).setvalue();
                                }
                                else {
                                    Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alertDialogB.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog1 = alertDialogB.create();
                alertDialog1.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName, restaurantPhone;
        TextView action_btn_delet,action_btn_update;
        public UsersViewHolder(View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name_tv);
            restaurantPhone = (TextView) itemView.findViewById(R.id.restaurant_phone);
            action_btn_update = itemView.findViewById(R.id.restaurant_update_btn);
            action_btn_delet = itemView.findViewById(R.id.restaurant_card_delet_btn);

        }

    }
    public interface callback2{
        void setvalue();
    }
}