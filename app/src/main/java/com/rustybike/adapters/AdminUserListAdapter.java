package com.rustybike.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.rustybike.Activities.UpdateUserActivity;
import com.rustybike.R;
import com.rustybike.models.Result;
import com.rustybike.models.Users;
import com.rustybike.response.UserListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserListAdapter extends RecyclerView.Adapter<AdminUserListAdapter.UsersViewHolder> {

    Activity context;
    List<Users> users;
    UserListRes userListRes;
    ApiInterface apiInterface;
    String token,ID;

    public AdminUserListAdapter(Activity context, UserListRes userListRes,String token,String ID) {
        this.userListRes = userListRes;
        this.context = context;
        users = userListRes.getData();
        apiInterface = Api.getClient();
        this.token = token;
        this.ID = ID;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_card, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {

        final String name = users.get(position).getUserName();
        String type = users.get(position).getUserType();
        String userType;
        if(type.equals("D")){
            userType = "Driver";
        }
        else{
            userType = "Restaurant";
        }
        holder.userName.setText(name);
        holder.usertype.setText(userType);

        holder.action_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateUserActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", ID);
                intent.putExtra("UserId",users.get(position).getUserId());
                intent.putExtra("FullName",users.get(position).getFullName());
                intent.putExtra("UserType",users.get(position).getUserType());
                intent.putExtra("Password",users.get(position).getPassword());
                intent.putExtra("UserName",users.get(position).getUserName());
                context.startActivityForResult(intent,0);
            }
        });

        holder.action_btn_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    final int userId = users.get(position).getUserId();
                    AlertDialog.Builder alertDialogB =new AlertDialog.Builder(context);
                    alertDialogB.setTitle("Delete Confirmation");
                    alertDialogB.setIcon(R.drawable.ic_priority_high_black_24dp);
                    alertDialogB.setMessage("Alert: Are you sure, you want to Delete User permanently!");
                    alertDialogB.setCancelable(false);
                    alertDialogB.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Call<Result> resultCall = apiInterface.DeletUser(token,ID,userId);
                            resultCall.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(Call<Result> call, Response<Result> response) {

                                    if (response.body().getCode() == 0){
                                        Toast.makeText(context,users.get(position).getUserName()+" User deleted",Toast.LENGTH_SHORT).show();
                                        ((AdminUserListAdapter.callback1) context).setvalue();
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
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView userName, usertype;
        TextView action_btn_delet,action_btn_update;
        public UsersViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name_tv);
            usertype = (TextView) itemView.findViewById(R.id.user_type_tv);
            action_btn_delet = itemView.findViewById(R.id.dashboard_card_delet_btn);
            action_btn_update = itemView.findViewById(R.id.dashboard_card_update_btn);

        }

    }
    public interface callback1{
        public void setvalue();
    }
}


