package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.adapters.RestaurantHistoryAdapter;
import com.rustybike.response.OrderListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantHistoryActivity extends AppCompatActivity implements RestaurantHistoryAdapter.callback{

    TextView Tooltv;
    int userId,restaurantId;
    String token,id_String;
    ApiInterface apiInterface;
    OrderListRes orderListRes;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button yesterday_tv,month_tv,week_tv,custom_filter_tv;
    TextView filter_btn;
    Button back_btn_history;
    ImageView left_mini_btn,right_mini_btn,tool_img;
    String date;
    RestaurantHistoryAdapter restaurantHistoryAdapter;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_history);
        Tooltv = findViewById(R.id.tool_tv);
        Tooltv.setText("Restaurant Orders History");
        recyclerView = findViewById(R.id.history_recyclerView);
        filter_btn = findViewById(R.id.filter_btn);
        progressBar = findViewById(R.id.simpleProgressBar);
        apiInterface = Api.getClient();
        left_mini_btn = findViewById(R.id.left_mini_btn_history);
        right_mini_btn = findViewById(R.id.right_mini_btn_history);
        tool_img = findViewById(R.id.tool_img);
        back_btn_history = findViewById(R.id.btnBackHistory);

        back_btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        left_mini_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int visibleItemCount =recyclerView.getLayoutManager().getChildCount();
                if (visibleItemCount>0) {
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
        right_mini_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int visibleItemCount =recyclerView.getLayoutManager().getChildCount();
                if (visibleItemCount>0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    recyclerView.smoothScrollToPosition(lastVisiblePosition + visibleItemCount);
                }
            }
        });

        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                //adding custom layout in dialog box
                View dialoglayout = inflater.inflate(R.layout.custom_dialog_layout, null);
                AlertDialog.Builder alertDialogB =new AlertDialog.Builder(RestaurantHistoryActivity.this);
                alertDialogB.setView(dialoglayout);
                final AlertDialog alertDialog1 = alertDialogB.create();
                alertDialog1.show();
                //initializing dialog box widgets
                yesterday_tv = dialoglayout.findViewById(R.id.yesterday_tv);
                week_tv = dialoglayout.findViewById(R.id.last_week_tv);
                month_tv = dialoglayout.findViewById(R.id.last_month_tv);
                custom_filter_tv = dialoglayout.findViewById(R.id.custom_filter_tv);
                //performing events on dialog box options OnClick
                yesterday_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        date="Y";
                        getorderlistbyDate();
                        filter_btn.setText("Yesterday History");

                    }
                });
                week_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        date="W";
                        getorderlistbyDate();
                        filter_btn.setText("Last Week History");

                    }
                });
                month_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        date="M";
                        getorderlistbyDate();
                        filter_btn.setText("Last Month History");

                    }
                });
                custom_filter_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                        final Calendar calendar = Calendar.getInstance();
                        final int year = calendar.get(Calendar.YEAR);
                        final int month = calendar.get(Calendar.MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePicker = new DatePickerDialog(RestaurantHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(i, i1, i2);
                                date = sdf.format(calendar.getTime());
                                SimpleDateFormat sdff = new SimpleDateFormat("dd MMM yyyy");
                                calendar.set(i, i1, i2);
                                String dateString = sdff.format(calendar.getTime());
                                filter_btn.setText(dateString+" History");
                                getorderlistbyDate();
                            }
                        },year, month, day);
                        datePicker.show();

                        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(final DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });


        getdata();
        getorderlist();


    }

    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("restaurant_id",0);

        }
    }
    public void getorderlist(){
        progressBar.setVisibility(View.VISIBLE);
        id_String = Integer.toString(userId);
        Call<OrderListRes> orderListResCall = apiInterface.getOrderList(token,id_String,restaurantId);

        orderListResCall.enqueue(new Callback<OrderListRes>() {
            @Override
            public void onResponse(Call<OrderListRes> call, Response<OrderListRes> response) {
                orderListRes = response.body();
                setDataInRecyclerView();
            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {

            }
        });
    }
    public void getorderlistbyDate(){
        progressBar.setVisibility(View.VISIBLE);
        id_String = Integer.toString(userId);
        Call<OrderListRes> orderListResCall = apiInterface.getOrderListbyDate(token,id_String,restaurantId,date);

        orderListResCall.enqueue(new Callback<OrderListRes>() {
            @Override
            public void onResponse(Call<OrderListRes> call, Response<OrderListRes> response) {
                if (response.body().getResult().getCode()==0){
                orderListRes = response.body();
                setDataInRecyclerView(); }
                else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.removeAllViewsInLayout();
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed to get orders, try again",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setDataInRecyclerView() {
        if (orderListRes.getData() != null){
            progressBar.setVisibility(View.GONE);
            restaurantHistoryAdapter = new RestaurantHistoryAdapter(RestaurantHistoryActivity.this,orderListRes,token,userId);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RestaurantHistoryActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(restaurantHistoryAdapter);

        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"No DriverOrdersActivity found",Toast.LENGTH_SHORT).show();
            recyclerView.removeAllViewsInLayout();
        }


    }

    @Override
    public void setvalue() {
        getorderlist();
    }


}
