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
import com.rustybike.models.Restaurants;
import com.rustybike.response.CreateRestaurantRes;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRestaurantActivity extends AppCompatActivity {

    String token;
    int userId;
    TextView Tooltv;
    ImageView tool_img;
    EditText restName_et,restCity_et,restPhone_et,restAddress_et,restFax_et,restPostCode_et;
    String restName,restCity,restPhone,restAddress,restFax,restPostCode;
    Button addRestaurant;
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
        setContentView(R.layout.activity_add_restaurant);
        restName_et = findViewById(R.id.restaurantname_etAdmin);
        restCity_et = findViewById(R.id.restaurantCity_etAdmin);
        restPhone_et = findViewById(R.id.restaurantphone_etAdmin);
        restAddress_et = findViewById(R.id.restaurantAddress_etAdmin);
        restFax_et = findViewById(R.id.restaurantfax_etAdmin);
        restPostCode_et = findViewById(R.id.restaurantpostcode_etAdmin);
        addRestaurant =findViewById(R.id.add_restaurant_btn);
        apiInterface = Api.getClient();
        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Add Restaurant");
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validationPart();
        getdata();

        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean everythingOk = true;
                if (restName_et.getText().toString().isEmpty()){
                    restName_et.setError("Restaurant name required");
                    everythingOk = false;
                }
                if (restCity_et.getText().toString().isEmpty()){
                    restCity_et.setError("City Name Required");
                    everythingOk = false;
                }
                if (restAddress_et.getText().toString().isEmpty()){
                    restAddress_et.setError("Address Required");
                    everythingOk = false;
                }
                if (restPhone_et.getText().toString().isEmpty()){
                    restPhone_et.setError("PhoneNumber Required");
                    everythingOk = false;
                }
                if (restFax_et.getText().toString().isEmpty()){
                    restFax_et.setError("Fax Required");
                    everythingOk = false;
                }
                if (restPostCode_et.getText().toString().isEmpty()){
                    restPostCode_et.setError("PostCode Required");
                    everythingOk = false;
                }
                if (everythingOk){
                    addRestaurant();
                }
            }
        });

    }

    public void addRestaurant(){
        final ProgressDialog progressDialog = new ProgressDialog(AddRestaurantActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        restName = restName_et.getText().toString();
        restCity = restCity_et.getText().toString();
        restPhone = restPhone_et.getText().toString();
        restFax = restFax_et.getText().toString();
        restAddress = restAddress_et.getText().toString();
        restPostCode = restPostCode_et.getText().toString();
        Call<CreateRestaurantRes> createRestaurant = apiInterface.createRestaurant(token,userId,restCity,restName,restAddress,restPhone,restFax,restPostCode);
        createRestaurant.enqueue(new Callback<CreateRestaurantRes>() {
            @Override
            public void onResponse(Call<CreateRestaurantRes> call, Response<CreateRestaurantRes> response) {
                Toast.makeText(getApplicationContext(),"Restaurant "+response.body().getData().getRestaurantName()+" Added",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                onBackPressed();
            }

            @Override
            public void onFailure(Call<CreateRestaurantRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Network error, Please try Later",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);
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
