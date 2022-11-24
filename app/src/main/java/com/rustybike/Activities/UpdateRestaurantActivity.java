package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rustybike.R;
import com.rustybike.response.UpdateOrderRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRestaurantActivity extends AppCompatActivity {

    TextView Tooltv;
    ImageView tool_img;
    int restaurantId,ID;
    String token,restName,restAddress,restPhone,restFax,restPostCode,restCity;
    EditText restName_et,restCity_et,restAddress_et,restPhone_et,restFax_et,restPostCode_et;
    Button updateBtn;
    ApiInterface apiInterface;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_update_restaurant);
        restName_et = findViewById(R.id.restaurantname_update);
        restCity_et = findViewById(R.id.restaurantCity_update);
        restAddress_et = findViewById(R.id.restaurantAddress_update);
        restPhone_et = findViewById(R.id.restaurantphone_update);
        restFax_et = findViewById(R.id.restaurantfax_update);
        restPostCode_et = findViewById(R.id.restaurantpostcode_update);
        updateBtn = findViewById(R.id.update_restaurant_btn);
        apiInterface = Api.getClient();
        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Update Restaurant");
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validationPart();
        getdata();

        //setting data to edit texts
        restName_et.setText(restName);
        restAddress_et.setText(restAddress);
        restCity_et.setText(restCity);
        restPhone_et.setText(restPhone);
        restFax_et.setText(restFax);
        restPostCode_et.setText(restPostCode);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restName_et.getText().toString().isEmpty()){
                    restName_et.setError("Restaurant name required");
                }
                else if (restCity_et.getText().toString().isEmpty()){
                    restCity_et.setError("CityName required");
                }
                else if (restAddress_et.getText().toString().isEmpty()){
                    restAddress_et.setError("Address required");
                }
                else if (restPhone_et.getText().toString().isEmpty()){
                    restPhone_et.setError("PhoneNumber required");
                }
                else if (restFax_et.getText().toString().isEmpty()){
                    restFax_et.setError("Fax required");
                }
                else if (restPostCode_et.getText().toString().isEmpty()){
                    restPostCode_et.setError("PostCode required");
                }
                else {
                    updateRestaurant();
                }

            }
        });

    }
    public void updateRestaurant(){
        restName = restName_et.getText().toString();
        restPhone = restPhone_et.getText().toString();
        restCity = restCity_et.getText().toString();
        restAddress = restAddress_et.getText().toString();
        restFax = restFax_et.getText().toString();
        restPostCode = restPostCode_et.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(UpdateRestaurantActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<UpdateOrderRes> restCall = apiInterface.updateRestaurant(token,ID,restCity,restName,restAddress,restPhone,restFax,restPostCode,1,restaurantId);
        restCall.enqueue(new Callback<UpdateOrderRes>() {
            @Override
            public void onResponse(Call<UpdateOrderRes> call, Response<UpdateOrderRes> response) {
                progressDialog.dismiss();
                if (response.body().getResult().getCode()==0){
                    setResult(RESULT_OK);
                    Toast.makeText(getApplicationContext(),"Restaurant Updated",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateOrderRes> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            ID = getIntent().getIntExtra("id",0);
            restaurantId = getIntent().getIntExtra("RestaurantId",0);
            restName = getIntent().getStringExtra("RestaurantName");
            restCity = getIntent().getStringExtra("RestaurantCity");
            restAddress = getIntent().getStringExtra("RestaurantAddress");
            restPhone = getIntent().getStringExtra("RestaurantPhone");
            restFax = getIntent().getStringExtra("RestaurantFax");
            restPostCode = getIntent().getStringExtra("RestaurantPostCode");
        }
    }
    public void validationPart(){
        restName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restName_et.getText().toString().length() <= 0) {
                    restName_et.setError("Restaurant name required");
                } else {
                    restName_et.setError(null);
                }
            }
        });
        restCity_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restCity_et.getText().toString().length() <= 0) {
                    restCity_et.setError("CityName required");
                } else {
                    restCity_et.setError(null);
                }
            }
        });
        restPhone_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restPhone_et.getText().toString().length() <= 0) {
                    restPhone_et.setError("PhoneNumber required");
                } else {
                    restPhone_et.setError(null);
                }
            }
        });
        restAddress_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restAddress_et.getText().toString().length() <= 0) {
                    restAddress_et.setError("Address required");
                } else {
                    restAddress_et.setError(null);
                }
            }
        });
        restFax_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restFax_et.getText().toString().length() <= 0) {
                    restFax_et.setError("Fax required");
                } else {
                    restFax_et.setError(null);
                }
            }
        });
        restPostCode_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (restPostCode_et.getText().toString().length() <= 0) {
                    restPostCode_et.setError("PostCode required");
                } else {
                    restPostCode_et.setError(null);
                }
            }
        });
    }
}
