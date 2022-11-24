package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rustybike.R;
import com.rustybike.adapters.CustomSpinnerAdapter;
import com.rustybike.models.Restaurants;
import com.rustybike.models.Users;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.response.UserListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;
import com.rustybike.response.LoginResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Button Login;
    EditText name_editText,password_editText;
    TextView txt1,retry_tv;
    String name,password;
    Activity mContext;
    CheckBox login_as_ch_bx;
    ApiInterface apiInterface;
    LinearLayout user_ll,restaurant_ll,admin_username_ll,password_ll;
    String Login_type;
    ConstraintLayout constraintLayout;
    Activity context;
    int restaurantID;
    ArrayList<String> restaurantNames;
    List<Restaurants> restaurants;
    List<Users> users;
    TextInputLayout nameInputLayout,passwordInputLayout;
    ProgressBar progressBar;
    boolean isUserSelected,gotRestaurantRes,gotUserRes,isRestaurantSelected = false;
    Spinner restaurantSpinner,userSpinner;


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        mContext = this;
        Login = findViewById(R.id.button1);
        name_editText = findViewById(R.id.name_edit);
        password_editText = findViewById(R.id.password_edit);

        progressBar = findViewById(R.id.login_progressbar);

        user_ll = findViewById(R.id.user_ll);
        retry_tv = (TextView) findViewById(R.id.retry_tv);
        restaurant_ll = findViewById(R.id.restaurant_ll);
        admin_username_ll = findViewById(R.id.admin_username_ll);
        password_ll = findViewById(R.id.password_ll);
        login_as_ch_bx = findViewById(R.id.login_as_ch_bx);

        context = this;
        constraintLayout = findViewById(R.id.loginlayout);
        txt1 = findViewById(R.id.admin_email_txt);
        apiInterface = Api.getClient();
        restaurantSpinner = findViewById(R.id.login_restaurant_spinner);
        userSpinner = findViewById(R.id.login_user_spinner);
        nameInputLayout = findViewById(R.id.name_editt);
        passwordInputLayout = findViewById(R.id.etRespass);

        retryTextView();
        login_as_ch_bx.setOnCheckedChangeListener(this);

        getRestaurants();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        name_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (name_editText.getText().toString().length() <= 0) {
                    name_editText.setError("Username required");
                } else {
                    name_editText.setError(null);
                }
            }
        });
        password_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password_editText.getText().toString().length() <= 0) {
                    password_editText.setError("Password Required");
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                } else {
                    password_editText.setError(null);
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }
        });
    }

    public void login(){
        password = password_editText.getText().toString();

        if (admin_username_ll.getVisibility() != View.VISIBLE) {
            if (!isUserSelected) {
                Toast.makeText(getApplicationContext(), "User Not Selected", Toast.LENGTH_SHORT).show();
            } else if (password.equals("")) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                password_editText.setError("Password Required");
            } else {
                retry_tv.setVisibility(View.GONE);
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                Call<LoginResponse> loginResponseCall = apiInterface.loginUser(name, password);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        retry_tv.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                if (loginResponse.getResult().getCode() == 0) {
                                    Login_type = loginResponse.getData().getLogin_type();
                                    if (Login_type != null) {

                                        Intent intent = null;

                                        if (Login_type.equals("D")) { // Driver Screen
                                            intent = new Intent(mContext, DriverOrdersActivity.class);

                                        } else if (Login_type.equals("A")) {
                                            intent = new Intent(mContext, AdminDashboardActivity.class);

                                        } else {// Restaurant Screen
                                            intent = new Intent(LoginActivity.this, RestaurantOrdersActivity.class);

                                        }

                                        intent.putExtra("token", loginResponse.getData().getAuth_token());
                                        intent.putExtra("id", loginResponse.getData().getUser_id());
                                        intent.putExtra("restaurant_id", loginResponse.getData().getRestaurant_id());
                                        intent.putExtra("type",loginResponse.getData().getLogin_type());
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), response.body().getResult().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().getResult().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        retry_tv.setText("Please Check Internet Connection and Try Again");
                        retry_tv.setVisibility(View.VISIBLE);
                    }
                });

            }
        }
        else if (admin_username_ll.getVisibility() == View.VISIBLE){

            name = name_editText.getText().toString();
            if (name.equals("")){
                name_editText.setError("UserName Required");
            }
            if (password.equals("")) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                password_editText.setError("Password Required");
            } else {
                retry_tv.setVisibility(View.GONE);
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                Call<LoginResponse> loginResponseCall = apiInterface.loginUser(name, password);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        retry_tv.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                LoginResponse loginResponse = response.body();
                                if (loginResponse.getResult().getCode() == 0) {
                                    Login_type = loginResponse.getData().getLogin_type();
                                    if (Login_type != null) {

                                        if (Login_type.equals("D")) { // Driver Screen
                                            Intent intent = new Intent(mContext, DriverOrdersActivity.class);
                                            intent.putExtra("token", loginResponse.getData().getAuth_token());
                                            intent.putExtra("id", loginResponse.getData().getUser_id());
                                            intent.putExtra("restaurant_id", loginResponse.getData().getRestaurant_id());
                                            intent.putExtra("type",loginResponse.getData().getLogin_type());
                                            mContext.startActivity(intent);
                                            finish();

                                        } else if (Login_type.equals("A")) {
                                            Intent intent = new Intent(mContext, AdminDashboardActivity.class);
                                            intent.putExtra("token", loginResponse.getData().getAuth_token());
                                            intent.putExtra("id", loginResponse.getData().getUser_id());
                                            intent.putExtra("restaurant_id", loginResponse.getData().getRestaurant_id());
                                            mContext.startActivity(intent);
                                            finish();
                                        } else {// Restaurant Screen
                                            Intent intent = new Intent(LoginActivity.this, RestaurantOrdersActivity.class);
                                            intent.putExtra("token", loginResponse.getData().getAuth_token());
                                            intent.putExtra("id", loginResponse.getData().getUser_id());
                                            intent.putExtra("restaurant_id", loginResponse.getData().getRestaurant_id());
                                            intent.putExtra("type",loginResponse.getData().getLogin_type());
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), response.body().getResult().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        retry_tv.setText("Please Check Your Internet Connection and Try Again");
                        retry_tv.setVisibility(View.VISIBLE);
                    }
                });

            }

        }
    }

    public void getRestaurants(){
        retry_tv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(
                constraintLayout);
        Call<RestaurantsListRes> restaurantsListResCall = apiInterface.getAllRestaurants();
        restaurantsListResCall.enqueue(new Callback<RestaurantsListRes>() {
            @Override
            public void onResponse(Call<RestaurantsListRes> call, Response<RestaurantsListRes> response) {
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
                gotRestaurantRes = true;
                if (response.body().getResult().getCode()==0){
                    if (response.body().getData() != null) {
                        restaurants = response.body().getData();
                        restaurantNames = new ArrayList<>();
                        restaurantNames.add("Select Restaurant");
                        for (int i=0;i< restaurants.size(); i++ ){
                            restaurantNames.add(restaurants.get(i).getRestaurantName());}

                        ArrayAdapter<String> RestspinnerAdapter = new ArrayAdapter<String>(context,R.layout.usertype_spinner_item,restaurantNames){
                            @Override
                            public boolean isEnabled(int position){
                                if(position == 0)
                                {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        RestspinnerAdapter.setDropDownViewResource(R.layout.usertype_spinner_item);
                        restaurantSpinner.setAdapter(RestspinnerAdapter);
                        restaurantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i>0) {
                                    isRestaurantSelected = true;
                                    restaurantID = restaurants.get(i - 1).getRestaurantId();
                                    getUsersByRestaurantId();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                isRestaurantSelected = false;
                            }
                        });

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantsListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
                gotRestaurantRes = false;
            }
        });
    }

    public void getUsersByRestaurantId() {
        retry_tv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(
                constraintLayout);
        Call<UserListRes> usersListResCall = apiInterface.getUsersByRestaurantId(restaurantID);
        usersListResCall.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                gotUserRes = true;
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
                if (response.body().getResult().getCode() == 0) {

                    user_ll.setVisibility(View.VISIBLE);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    users = response.body().getData();
                    Users user = new Users();
                    user.setUserType("");
                    user.setFullName("Choose user");

                    response.body().getData().add(0,user);

                    CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(),response.body().getData());
                    userSpinner.setAdapter(customSpinnerAdapter);
                    userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i > 0) {
                                        isUserSelected = true;
                                        password_ll.setVisibility(View.VISIBLE);
                                        Login.setVisibility(View.VISIBLE);
                                        TransitionManager.beginDelayedTransition(constraintLayout);
                                        name = users.get(i).getUserName();
                                    }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            isUserSelected = false;
                        }
                    });
                }
                else if (response.body().getResult().getCode() == 102){
                    user_ll.setVisibility(View.GONE);
                    password_ll.setVisibility(View.GONE);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    Toast.makeText(getApplicationContext(), "No user found please choose another restaurant", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                gotUserRes = false;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            restaurant_ll.setVisibility(View.GONE);
            user_ll.setVisibility(View.GONE);

            admin_username_ll.setVisibility(View.VISIBLE);
            password_ll.setVisibility(View.VISIBLE);
            Login.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(
                    constraintLayout);

            restaurant_ll.setVisibility(View.GONE);
            user_ll.setVisibility(View.GONE);
        }else{
            restaurant_ll.setVisibility(View.VISIBLE);
            admin_username_ll.setVisibility(View.GONE);
            password_ll.setVisibility(View.GONE);
            Login.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(
                    constraintLayout);

            if(isRestaurantSelected)
            {
                user_ll.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
            }
            if(isUserSelected)
            {
                password_ll.setVisibility(View.VISIBLE);
                Login.setVisibility(View.VISIBLE);
                TransitionManager.beginDelayedTransition(
                        constraintLayout);
            }
        }
    }

    public void retryTextView(){
        SpannableString ss = new SpannableString("Please Check Internet Connection and Try Again");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (!gotRestaurantRes){
                    getRestaurants();
                }
                else if (!gotUserRes){
                    getUsersByRestaurantId();
                }

            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 37, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        retry_tv.setText(ss);
        retry_tv.setMovementMethod(LinkMovementMethod.getInstance());
        retry_tv.setHighlightColor(Color.TRANSPARENT);

    }

}
