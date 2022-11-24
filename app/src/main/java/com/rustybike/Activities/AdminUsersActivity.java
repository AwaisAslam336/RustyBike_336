package com.rustybike.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.adapters.AdminUserListAdapter;
import com.rustybike.response.UserListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsersActivity extends AppCompatActivity implements AdminUserListAdapter.callback1 {

    ImageView tool_img;
    TextView Tooltv;
    String token,id_String;
    int userId,restaurantId;
    ProgressBar progressBar;
    UserListRes userListRes;
    ApiInterface apiInterface;
    RecyclerView recyclerView;
    AdminUserListAdapter adminadapter;
    TextView addDriver;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_admin_users);
        apiInterface = Api.getClient();
        tool_img = findViewById(R.id.tool_img);
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Tooltv = findViewById(R.id.tool_tv);
        Tooltv.setText("Users Management");
        recyclerView = findViewById(R.id.admin_userlist_recycler);
        addDriver = findViewById(R.id.add_driver_adminBtn);
        addDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminUsersActivity.this,AddUserActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", userId);
                startActivityForResult(intent,0);
            }
        });

        progressBar = findViewById(R.id.simpleProgressBar_adminDashboard);
        progressBar.setVisibility(View.VISIBLE);
        getdata();
        getallUserList();
    }

    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("restaurant_id",0);

        }
    }
    public void getallUserList(){
        id_String = Integer.toString(userId);
        final Call<UserListRes> userListResCall = apiInterface.getAllUserList(token,id_String);
        userListResCall.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getResult().getCode()==0){
                userListRes = response.body();
                setDataInRecyclerView();
                }
                else {
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void setDataInRecyclerView() {

        if (userListRes.getData() != null) {
            adminadapter = new AdminUserListAdapter(AdminUsersActivity.this, userListRes,token,id_String);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminUsersActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adminadapter);

        }

    }

    @Override
    public void setvalue() {
        getallUserList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
           getallUserList();
        }
    }
}
