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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.adapters.DriverOrderAdapter;
import com.rustybike.response.OrderListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverOrdersActivity extends AppCompatActivity {

    Button pick_order;
    int items;
    ImageView tool_img,logout_icon;
    ImageButton left_imgbtn,right_imgbtn;
    ApiInterface apiInterface;
    int userId,restaurantId;
    OrderListRes orderListRes;
    String token,id_String,usertype;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    DriverOrderAdapter orderAdapter;
    private Parcelable recyclerViewState;
    int visibleItemCount;
    TextView Tooltv;
    Timer timer;
    TimerTask t;
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
        setContentView(R.layout.activity_order);
        tool_img = findViewById(R.id.tool_img);
        tool_img.setVisibility(View.GONE);
        apiInterface = Api.getClient();
        pick_order = findViewById(R.id.pick_order_btn);
        recyclerView = findViewById(R.id.driver_recycler);
        left_imgbtn = findViewById(R.id.left_imagebtn);
        right_imgbtn = findViewById(R.id.right_imagebtn);
        progressBar = findViewById(R.id.simpleProgressBar_driver);
        logout_icon = findViewById(R.id.logout_driver_icon);

        logout_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverOrdersActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Tooltv = findViewById(R.id.tool_tv);
        Tooltv.setText("Live Driver Orders");

        progressBar.setVisibility(View.VISIBLE);
        getdata();
        getorderlist();

        //refreshing order list after every 10 seconds


        //Simple Buttons OnClick
        right_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleItemCount =recyclerView.getLayoutManager().getChildCount();
                if (visibleItemCount>0){
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                recyclerView.smoothScrollToPosition(lastVisiblePosition+visibleItemCount);}
            }
        });
        left_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visibleItemCount =recyclerView.getLayoutManager().getChildCount();
                if (visibleItemCount>0){
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition>visibleItemCount) {
                    recyclerView.smoothScrollToPosition(lastVisiblePosition - visibleItemCount);
                }
                else {
                    recyclerView.smoothScrollToPosition(0);
                }
                }
            }
        });
        pick_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverOrdersActivity.this, CreateOrderActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("id",userId);
                intent.putExtra("restaurant_id",restaurantId);
                intent.putExtra("type",usertype);
                startActivityForResult(intent,20);
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
                    if (linearLayoutManager != null) {
                        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                        if (firstVisiblePosition != 0) {
                            if (items == recyclerView.getAdapter().getItemCount()) {
                                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            }
                        }
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
               // Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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
        if (orderListRes.getData() != null) {
            progressBar.setVisibility(View.GONE);
            orderAdapter = new DriverOrderAdapter(DriverOrdersActivity.this, orderListRes);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DriverOrdersActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(orderAdapter);

        }
        else {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(),"No DriverOrdersActivity found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
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
        t = new TimerTask() {
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
