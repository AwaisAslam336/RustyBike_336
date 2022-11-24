package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rustybike.R;
import com.rustybike.models.Restaurants;
import com.rustybike.response.AddUserRes;
import com.rustybike.response.RestaurantsListRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserActivity extends AppCompatActivity {

    String usertype,fullname;
    EditText userName_et,password_et,confirmPassword_et,fullname_et;
    Button addUserBtn;
    TextView Tooltv,retry_tv;
    Activity context;
    ImageView tool_img;
    ProgressBar progressBar;
    ApiInterface apiInterface;
    Spinner userTypeSpinner,restaurantSpinner;
    String token;
    int userId,restaurantID;
    TextInputLayout passwordInputLayout,confirmPasswordInputLayout;
    ArrayList<String> restaurantNames;
    List<Restaurants> restaurants;
    boolean isTypeSelected,isRestaurantSelected = false;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_add_user);
        retry_tv = (TextView) findViewById(R.id.retry_tvUser);
        progressBar = findViewById(R.id.user_progressbar);
        userName_et = findViewById(R.id.username_etAdmin);
        password_et = findViewById(R.id.password_etAdmin);
        fullname_et = findViewById(R.id.fullname_etAdmin);
        passwordInputLayout = findViewById(R.id.etRestpassuser);
        confirmPasswordInputLayout = findViewById(R.id.etRestpassconfirm);
        context = this;
        confirmPassword_et = findViewById(R.id.confirmPassword_etAdmin);
        addUserBtn = findViewById(R.id.add_user_btn);
        userTypeSpinner = findViewById(R.id.userType_spinner);
        restaurantSpinner = findViewById(R.id.restaurant_spinner);
        apiInterface = Api.getClient();
        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Add User");
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validationPart();
        getdata();
        retryTextView();
        getRestaurants();

        //String[] Types = new String[]{"Select User Type", "Driver", "Restaurant"};
        ArrayList<String> TypeList = new ArrayList<String>();
        TypeList.add("Select User Type");
        TypeList.add("Driver");
        TypeList.add("Restaurant");

        ArrayAdapter<String> TypespinnerAdapter = new ArrayAdapter<String>(this,R.layout.usertype_spinner_item,TypeList){
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
        TypespinnerAdapter.setDropDownViewResource(R.layout.usertype_spinner_item);
        userTypeSpinner.setAdapter(TypespinnerAdapter);

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {


                if (position==1){
                    usertype = "D";
                    isTypeSelected = true;
                }
                else if (position == 2){
                    usertype = "R";
                    isTypeSelected = true;
                }
                else {
                    isTypeSelected = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isTypeSelected = false;
            }
        });



        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean everythingOK = true;

                if (userName_et.getText().toString().isEmpty()){
                    userName_et.setError("User Name Required");
                    everythingOK = false;
                }
                if (password_et.getText().toString().isEmpty()){
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    password_et.setError("Password Required");
                    everythingOK = false;
                }
                if (password_et.getText().toString().length()<6){
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    password_et.setError("Password should contain at least 6 letters");
                    everythingOK = false;
                }
                if (confirmPassword_et.getText().toString().isEmpty()){
                    confirmPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    confirmPassword_et.setError("Confirm Password Required");
                    everythingOK = false;
                }
                if (!password_et.getText().toString().equals(confirmPassword_et.getText().toString())){
                    Toast.makeText(getApplicationContext(),"'Confirm Password' must be same as 'Password'",Toast.LENGTH_LONG).show();
                    everythingOK =false;
                }
                else if (!isTypeSelected){
                    Toast.makeText(getApplicationContext(),"Please Select User Type",Toast.LENGTH_LONG).show();
                    everythingOK = false;
                }
                else if (!isRestaurantSelected){
                    Toast.makeText(getApplicationContext(),"Please Select User Restaurant",Toast.LENGTH_LONG).show();
                    everythingOK = false;
                }
                if (fullname_et.getText().toString().isEmpty()){
                    fullname_et.setError("Full Name Required");
                    everythingOK = false;
                }
                if (everythingOK){
                    addUser();
                }
            }
        });



    }


    public void addUser(){
        final ProgressDialog progressDialog = new ProgressDialog(AddUserActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        final String name = userName_et.getText().toString();
        String pass = password_et.getText().toString();
        fullname = fullname_et.getText().toString();
        Call<AddUserRes> resultCall= apiInterface.createUser(name,pass,usertype,fullname,1,restaurantID);
        resultCall.enqueue(new Callback<AddUserRes>() {
            @Override
            public void onResponse(Call<AddUserRes> call, Response<AddUserRes> response) {
                progressDialog.dismiss();
                if (response.body().getResult().getCode()==0){
                    Toast.makeText(getApplicationContext(),"User "+name+" Added",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    onBackPressed();
                }
                else{
                    Toast.makeText(getApplicationContext(),response.body().getResult().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddUserRes> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Network Error,Please try Later",Toast.LENGTH_SHORT).show();
            }
        });
    }

public void getRestaurants(){
    retry_tv.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
        Call<RestaurantsListRes> restaurantsListResCall = apiInterface.getAllRestaurants();
        restaurantsListResCall.enqueue(new Callback<RestaurantsListRes>() {
            @Override
            public void onResponse(Call<RestaurantsListRes> call, Response<RestaurantsListRes> response) {
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.GONE);
                if (response.body().getResult().getCode()==0){
                    restaurants = response.body().getData();
                    restaurantNames = new ArrayList<String>();
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
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                isRestaurantSelected = false;
                            }
                        });

                }
            }

            @Override
            public void onFailure(Call<RestaurantsListRes> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                retry_tv.setVisibility(View.VISIBLE);
            }
        });
}
    public void getdata(){
        if (getIntent().getExtras() !=null) {
            token = getIntent().getStringExtra("token");
            userId = getIntent().getIntExtra("id",0);

        }
    }
    public void retryTextView(){
        SpannableString ss = new SpannableString("Please Check Internet Connection and Try Again");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                    getRestaurants();
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
    public void validationPart(){
        userName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (userName_et.getText().toString().length() <= 0) {
                    userName_et.setError("Username required");
                } else {
                    userName_et.setError(null);
                }
            }
        });
        fullname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fullname_et.getText().toString().length() <= 0) {
                    fullname_et.setError("FullName required");
                } else {
                    fullname_et.setError(null);
                }
            }
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password_et.getText().toString().length() <= 0) {
                    password_et.setError("Password required");
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                } else {
                    password_et.setError(null);
                }
            }
        });
        confirmPassword_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasswordInputLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (confirmPassword_et.getText().toString().length() <= 0) {
                    confirmPassword_et.setError("Password Required");
                    confirmPasswordInputLayout.setPasswordVisibilityToggleEnabled(false);
                } else {
                    confirmPassword_et.setError(null);
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
                }
            }
        });
    }
}
