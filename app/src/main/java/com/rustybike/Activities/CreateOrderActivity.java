package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.Utility.Utility;
import com.rustybike.response.CreateOrderRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    FrameLayout frame1,frame2,frame3;
    RelativeLayout relativeLayout2,relativeLayout1,relativeLayout3;
    ImageView deleivro_img,uber_img,justeat_img,tool_backimg;
    LinearLayout card_layout,back_btn_layout;
    LinearLayout LinLay;
    TextView uber_tv,tool_tv,delivro_tv,justeat_tv;
    ConstraintLayout constraintLayout;
    Button back_btn_below,back_btn_beside_submit,submit_btn;
    int userId,restaurantId,company_id;
    String token,status,id_String,usertype;
    ApiInterface apiInterface;
    EditText order_number_ET;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_delivery_company);

        getdata();

        initialization();
//Simple Button  OnClicks
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(CreateOrderActivity.this);
                createOrder();
            }
        });
        back_btn_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goback();
            }
        });
        back_btn_beside_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goback();
            }
        });
        tool_backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goback();
            }
        });

        frame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTablet(getApplicationContext())) {
                    frame2_clicked();
                }
                else {
                    frame2Mini_clicked();
                }
                company_id = 2;
            }
        });
        frame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTablet(getApplicationContext())) {
                    frame1_clicked();

                }
                else {
                    frame1Mini_Clicked();
                }
                company_id = 1;
            }
        });
        frame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTablet(getApplicationContext())) {
                frame3_clicked();}
                else {
                    frame3Mini_Clicked();
                }
                company_id =3;
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

    public void createOrder(){
        if (order_number_ET.getText().toString().equals("")){
            order_number_ET.setError("Please Enter Your DriverOrdersActivity Number!");
        }
        else {
            final String refNumber = order_number_ET.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialog(CreateOrderActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            id_String = Integer.toString(userId);
            Call<CreateOrderRes> createOrderResCall = apiInterface.createOrder(token, id_String, restaurantId, company_id,refNumber, status);
            createOrderResCall.enqueue(new Callback<CreateOrderRes>() {
                @Override
                public void onResponse(Call<CreateOrderRes> call, Response<CreateOrderRes> response) {
                    progressDialog.dismiss();
                    CreateOrderRes createOrderRes = response.body();
                    if (createOrderRes.getResult().getCode() == 0) {
                        setResult(RESULT_OK);
                        goback();
                    }else if(createOrderRes.getResult().getCode() == 904)
                    {
                        Utility.showDialog(CreateOrderActivity.this,createOrderRes.getResult().getMessage()+refNumber);
                    }
                }

                @Override
                public void onFailure(Call<CreateOrderRes> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void initialization(){
        apiInterface = Api.getClient();
        order_number_ET = findViewById(R.id.order_number_ET);
        back_btn_layout = findViewById(R.id.back_btn_layout);
        card_layout = findViewById(R.id.card_lay_id);
        back_btn_below = findViewById(R.id.back_btn_below);
        back_btn_beside_submit = findViewById(R.id.back_btn_beside_submit);
        submit_btn = findViewById(R.id.submit_btn);
        tool_backimg = findViewById(R.id.tool_img);
        uber_img = findViewById(R.id.uber_img);
        tool_tv = findViewById(R.id.tool_tv);
        tool_tv.setText("Delivery Company");
        deleivro_img= findViewById(R.id.delivro_img);
        justeat_img = findViewById(R.id.justeat_img);
        uber_tv = findViewById(R.id.uber_tv);
        delivro_tv = findViewById(R.id.delivro_tv);
        justeat_tv = findViewById(R.id.justeat_tv);
        constraintLayout = findViewById(R.id.main_layout);
        frame1 = findViewById(R.id.cardView);
        frame2 = findViewById(R.id.cardView2);
        frame3 = findViewById(R.id.cardView3);
        LinLay = findViewById(R.id.lin_layout);
        relativeLayout2= findViewById(R.id.relative2);
        relativeLayout1= findViewById(R.id.relative1);
        relativeLayout3= findViewById(R.id.relative3);
        if (usertype.equals("D")){
            status = "W";
        }
        else if (usertype.equals("R")){
            status = "R";
        }

    }

    public void frame2_clicked(){
        frame2.setBackgroundResource(R.drawable.highlited_card);
        frame1.setBackgroundResource(R.drawable.card_background);
        frame3.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.5f));
        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));
        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));

        relativeLayout2.setPadding(80,60,80,60);
        relativeLayout1.setPadding(40,30,40,30);
        relativeLayout3.setPadding(40,30,40,30);
       // uber_img.setPadding(20,20,20,20);


       /* deleivro_img.getLayoutParams().height = 120;
        deleivro_img.getLayoutParams().width = 120;
        deleivro_img.requestLayout();*/

        uber_tv.setTextColor(getResources().getColor(R.color.app_color));
        uber_tv.setTextSize(20);
        delivro_tv.setTextColor(getResources().getColor(R.color.text_color));
        delivro_tv.setTextSize(12);
        justeat_tv.setTextColor(getResources().getColor(R.color.text_color));
        justeat_tv.setTextSize(12);

        /*justeat_img.getLayoutParams().height = 120;
        justeat_img.getLayoutParams().width = 120;
        justeat_img.requestLayout();
*/

        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(20, 40, 20, 30);

        frame2.setLayoutParams(params1);
        frame3.setLayoutParams(params2);
        frame1.setLayoutParams(params2);

       /* if (card_layout.getAlpha() > .6f){
            card_layout.animate().alpha(0.0f).setDuration(750);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    card_layout.animate().alpha(1).setDuration(1500);
                }
            }, 750);

        }
        else {
            card_layout.animate().alpha(1.0f).setDuration(1500);
        }*/

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }

        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame2.setAlpha(1);
        frame1.setAlpha(.3f);
        frame3.setAlpha(.3f);

    }
    public void frame1_clicked(){
        frame1.setBackgroundResource(R.drawable.highlited_card);
        frame2.setBackgroundResource(R.drawable.card_background);
        frame3.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.5f));
        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));
        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));

        relativeLayout1.setPadding(80,60,80,60);
        relativeLayout2.setPadding(40,30,40,30);
        relativeLayout3.setPadding(40,30,40,30);
       // deleivro_img.setPadding(30,20,30,20);


       /* uber_img.getLayoutParams().height = 120;
        uber_img.getLayoutParams().width = 120;
        uber_img.requestLayout();
        justeat_img.getLayoutParams().height = 120;
        justeat_img.getLayoutParams().width = 120;
        justeat_img.requestLayout();*/

        delivro_tv.setTextColor(getResources().getColor(R.color.app_color));
        delivro_tv.setTextSize(16);
        uber_tv.setTextColor(getResources().getColor(R.color.text_color));
        uber_tv.setTextSize(12);
        justeat_tv.setTextColor(getResources().getColor(R.color.text_color));
        justeat_tv.setTextSize(12);

        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(20, 40, 20, 30);

        frame1.setLayoutParams(params1);
        frame3.setLayoutParams(params2);
        frame2.setLayoutParams(params2);

        /*if (card_layout.getAlpha() > .6f){
            card_layout.animate().alpha(0.0f).setDuration(750);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    card_layout.animate().alpha(1).setDuration(1500);
                }
            }, 750);

        }
        else {
            card_layout.animate().alpha(1.0f).setDuration(1500);
        }
*/
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }



        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame1.setAlpha(1);
        frame2.setAlpha(.3f);
        frame3.setAlpha(.3f);

    }
    public void frame3_clicked(){
        frame3.setBackgroundResource(R.drawable.highlited_card);
        frame1.setBackgroundResource(R.drawable.card_background);
        frame2.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.5f));
        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));
        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.75f));

        relativeLayout3.setPadding(80,60,80,60);
        relativeLayout1.setPadding(40,30,40,30);
        relativeLayout2.setPadding(40,30,40,30);
      //  justeat_img.setPadding(30,20,30,20);


       /* deleivro_img.getLayoutParams().height = 120;
        deleivro_img.getLayoutParams().width = 120;
        deleivro_img.requestLayout();*/

        justeat_tv.setTextColor(getResources().getColor(R.color.app_color));
        justeat_tv.setTextSize(16);
        delivro_tv.setTextColor(getResources().getColor(R.color.text_color));
        delivro_tv.setTextSize(12);
        uber_tv.setTextColor(getResources().getColor(R.color.text_color));
        uber_tv.setTextSize(12);

      /*  uber_img.getLayoutParams().height = 120;
        uber_img.getLayoutParams().width = 120;
        uber_img.requestLayout();*/


        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(20, 40, 20, 30);

        frame3.setLayoutParams(params1);
        frame2.setLayoutParams(params2);
        frame1.setLayoutParams(params2);

       /* if (card_layout.getAlpha() > .6f){
            card_layout.animate().alpha(0.0f).setDuration(750);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    card_layout.animate().alpha(1).setDuration(1500);
                }
            }, 750);

        }
        else {
            card_layout.animate().alpha(1.0f).setDuration(1500);
        }*/

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }

        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame3.setAlpha(1);
        frame1.setAlpha(.3f);
        frame2.setAlpha(.3f);


    }

    public void frame2Mini_clicked(){
        frame2.setBackgroundResource(R.drawable.highlited_card);
        frame1.setBackgroundResource(R.drawable.card_background);
        frame3.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.7f));
        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));
        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));

        relativeLayout2.setPadding(60,40,60,40);
        relativeLayout1.setPadding(20,5,20,5);
        relativeLayout3.setPadding(20,5,20,5);


        uber_tv.setTextColor(getResources().getColor(R.color.app_color));
        uber_tv.setTextSize(16);
        delivro_tv.setTextColor(getResources().getColor(R.color.text_color));
        delivro_tv.setTextSize(12);
        justeat_tv.setTextColor(getResources().getColor(R.color.text_color));
        justeat_tv.setTextSize(12);


        uber_img.getLayoutParams().height = 140;
        uber_img.getLayoutParams().width = 140;
        uber_img.requestLayout();

        deleivro_img.getLayoutParams().height = 120;
        deleivro_img.getLayoutParams().width = 120;
        deleivro_img.requestLayout();

        justeat_img.getLayoutParams().height = 120;
        justeat_img.getLayoutParams().width = 120;
        justeat_img.requestLayout();


        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(10, 40, 10, 30);

        frame2.setLayoutParams(params1);
        frame3.setLayoutParams(params2);
        frame1.setLayoutParams(params2);


        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }

        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame2.setAlpha(1);
        frame1.setAlpha(.3f);
        frame3.setAlpha(.3f);
    }
    public void frame1Mini_Clicked(){
        frame1.setBackgroundResource(R.drawable.highlited_card);
        frame2.setBackgroundResource(R.drawable.card_background);
        frame3.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.7f));
        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));
        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));

        relativeLayout1.setPadding(60,40,60,40);
        relativeLayout2.setPadding(20,5,20,5);
        relativeLayout3.setPadding(20,5,20,5);


        uber_tv.setTextColor(getResources().getColor(R.color.text_color));
        uber_tv.setTextSize(12);
        delivro_tv.setTextColor(getResources().getColor(R.color.app_color));
        delivro_tv.setTextSize(16);
        justeat_tv.setTextColor(getResources().getColor(R.color.text_color));
        justeat_tv.setTextSize(12);

        uber_img.getLayoutParams().height = 120;
        uber_img.getLayoutParams().width = 120;
        uber_img.requestLayout();

        deleivro_img.getLayoutParams().height = 140;
        deleivro_img.getLayoutParams().width = 140;
        deleivro_img.requestLayout();

        justeat_img.getLayoutParams().height = 120;
        justeat_img.getLayoutParams().width = 120;
        justeat_img.requestLayout();



        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(10, 40, 10, 30);

        frame1.setLayoutParams(params1);
        frame3.setLayoutParams(params2);
        frame2.setLayoutParams(params2);

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }

        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame1.setAlpha(1);
        frame2.setAlpha(.3f);
        frame3.setAlpha(.3f);
    }
    public void frame3Mini_Clicked(){
        frame3.setBackgroundResource(R.drawable.highlited_card);
        frame1.setBackgroundResource(R.drawable.card_background);
        frame2.setBackgroundResource(R.drawable.card_background);

        TransitionManager.beginDelayedTransition(
                constraintLayout);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        frame3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.7f));
        frame1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));
        frame2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,.4f));

        relativeLayout3.setPadding(60,40,60,40);
        relativeLayout1.setPadding(20,5,20,5);
        relativeLayout2.setPadding(20,5,20,5);

        uber_tv.setTextColor(getResources().getColor(R.color.text_color));
        uber_tv.setTextSize(12);
        delivro_tv.setTextColor(getResources().getColor(R.color.text_color));
        delivro_tv.setTextSize(12);
        justeat_tv.setTextColor(getResources().getColor(R.color.app_color));
        justeat_tv.setTextSize(16);

        uber_img.getLayoutParams().height = 120;
        uber_img.getLayoutParams().width = 120;
        uber_img.requestLayout();

        deleivro_img.getLayoutParams().height = 120;
        deleivro_img.getLayoutParams().width = 120;
        deleivro_img.requestLayout();

        justeat_img.getLayoutParams().height = 140;
        justeat_img.getLayoutParams().width = 140;
        justeat_img.requestLayout();

        params1.setMargins(5, 20, 5, 10);
        params2.setMargins(10, 40, 10, 30);

        frame3.setLayoutParams(params1);
        frame2.setLayoutParams(params2);
        frame1.setLayoutParams(params2);


        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.visibilty_slide_up);
        if (card_layout.getVisibility() == View.GONE) {
            card_layout.setVisibility(View.VISIBLE);
            card_layout.startAnimation(slideUp);
        }

        back_btn_layout.animate().translationY(frame2.getHeight()).alpha(0.0f).setDuration(1500);
        frame3.setAlpha(1);
        frame1.setAlpha(.3f);
        frame2.setAlpha(.3f);
    }
    public void goback(){
       onBackPressed();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
