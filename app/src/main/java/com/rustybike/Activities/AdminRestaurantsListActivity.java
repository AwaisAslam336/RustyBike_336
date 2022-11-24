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
import com.rustybike.adapters.AdminRestaurantListAdapter;
import com.rustybike.adapters.AdminUserListAdapter;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRestaurantsListActivity extends AppCompatActivity implements AdminRestaurantListAdapter.callback2 {

    String token;
    int userId,restaurantId;
    ApiInterface apiInterface;
    TextView Tooltv;
    ImageView tool_img;
    ProgressBar progressBar;
    TextView addRestaurantBtn;
    RestaurantsListRes restaurantsListRes;
    RecyclerView recyclerView;
    AdminRestaurantListAdapter adminadapter;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_admin_restaurants_list);
        apiInterface = Api.getClient();
        recyclerView = findViewById(R.id.admin_restlist_recycler);
        addRestaurantBtn = findViewById(R.id.add_restaurant_adminBtn);

        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Restaurants Management");
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getdata();
        getallRestaurantList();
        progressBar = findViewById(R.id.ProgressBar_Admin_restaurant);
        progressBar.setVisibility(View.VISIBLE);

        addRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminRestaurantsListActivity.this,AddRestaurantActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", userId);
                startActivityForResult(intent,0);
            }
        });
    }

    public void getallRestaurantList(){
        Call<RestaurantsListRes> restaurantsListResCall = apiInterface.getAllRestaurants();
        restaurantsListResCall.enqueue(new Callback<RestaurantsListRes>() {
            @Override
            public void onResponse(Call<RestaurantsListRes> call, Response<RestaurantsListRes> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getResult().getCode()==0) {
                    restaurantsListRes = response.body();
                    setDataInRecyclerView();
                }
                else {
                   Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantsListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataInRecyclerView() {
        if (restaurantsListRes.getData() != null) {
            adminadapter = new AdminRestaurantListAdapter(AdminRestaurantsListActivity.this, restaurantsListRes,token,userId);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminRestaurantsListActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adminadapter);
        }
    }
    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("restaurant_id",0);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            getallRestaurantList();
        }
    }
    @Override
    public void setvalue() {
        getallRestaurantList();
    }
}
