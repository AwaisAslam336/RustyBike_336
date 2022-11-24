package com.rustybike.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;

public class AdminDashboardActivity extends AppCompatActivity {

    String token;
    int userId,restaurantId;
    Button manageUsers,manageRest;
    TextView Tooltv,logout;
    ImageView tool_img;
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
        setContentView(R.layout.activity_admin_dashboard);
        manageUsers = findViewById(R.id.mngUsersBtn);
        manageRest = findViewById(R.id.mngRestBtn);
        logout = findViewById(R.id.logout_tv);
        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Admin Panel");
        tool_img.setVisibility(View.GONE);
        getdata();
        manageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this, AdminUsersActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", userId);
                intent.putExtra("restaurant_id", restaurantId);
                startActivity(intent);
            }
        });

        manageRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboardActivity.this,AdminRestaurantsListActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("id", userId);
                intent.putExtra("restaurant_id", restaurantId);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setTextColor(getResources().getColor(R.color.text_color));
                Intent intent = new Intent(AdminDashboardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("restaurant_id",0);

        }
    }


}
