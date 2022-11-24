package com.rustybike.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.adapters.RestaurantOrderAdapter;
import com.rustybike.response.OrderListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantOrdersActivity extends AppCompatActivity {

    ImageView tool_img,logout_icon;
    int items;
    TextView food_ready,history;
    ApiInterface apiInterface;
    int userId,restaurantId;
    OrderListRes orderListRes;
    String token,id_String,usertype;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private Parcelable recyclerViewState;
    TextView Tooltv;
    Timer timer;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_oderlist2);
        tool_img = findViewById(R.id.tool_img);
        tool_img.setVisibility(View.GONE);
        food_ready = findViewById(R.id.food_ready_btn);
        history = findViewById(R.id.history_btn);
        progressBar = findViewById(R.id.simpleProgressBar_restaurant);
        recyclerView = findViewById(R.id.recyler_orderlist2);
        apiInterface = Api.getClient();
        Tooltv = findViewById(R.id.tool_tv);
        logout_icon = findViewById(R.id.logout_rest_icon);
        Tooltv.setText("Live Restaurant Orders");

        logout_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantOrdersActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getdata();
        getorderlist();

        //refreshing order list after every 10 seconds


        //Simple Buttons OnClick
        food_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantOrdersActivity.this, CreateOrderActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("id",userId);
                intent.putExtra("restaurant_id",restaurantId);
                intent.putExtra("type",usertype);
                startActivityForResult(intent,20);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantOrdersActivity.this, RestaurantHistoryActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("id",userId);
                intent.putExtra("restaurant_id",restaurantId);
                startActivity(intent);
            }
        });
    }


    public void getorderlist(){
        id_String = Integer.toString(userId);
        LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearManager != null){
            items= recyclerView.getAdapter().getItemCount();
        }

        Call<OrderListRes> orderListResCall = apiInterface.getOrderList(token,id_String,restaurantId);
        orderListResCall.enqueue(new Callback<OrderListRes>() {
            @Override
            public void onResponse(Call<OrderListRes> call, Response<OrderListRes> response) {
                if (response.body().getResult().getCode()==0){
                    orderListRes = response.body();
                    setDataInRecyclerView();
                    //setting the last previous state of the recyclerView
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null){
                        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                        if (firstVisiblePosition != 0){
                            if (items == recyclerView.getAdapter().getItemCount()) {
                                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            }
                        }}
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
             //   Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("restaurant_id",0);
            usertype = getIntent().getStringExtra("type");

        }
    }
    private void setDataInRecyclerView() {
        if (orderListRes.getData() != null){
            progressBar.setVisibility(View.GONE);
        RestaurantOrderAdapter RestaurantOrderAdapter = new RestaurantOrderAdapter(RestaurantOrdersActivity.this,orderListRes,token,userId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RestaurantOrdersActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(RestaurantOrderAdapter);
        }
        else {
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            progressBar.setVisibility(View.VISIBLE);
            getorderlist();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                if (recyclerView.getLayoutManager() != null) {
                    recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                    getorderlist();
                }
            }
        };
        timer.scheduleAtFixedRate(t,15000,10000);
    }
}
