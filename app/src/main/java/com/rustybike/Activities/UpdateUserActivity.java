package com.rustybike.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rustybike.R;
import com.rustybike.models.Result;
import com.rustybike.response.UpdateOrderRes;
import com.rustybike.retrofit.Api;
import com.rustybike.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    String token,ID,fullName,userType,password,userName;
    int userId;
    EditText userName_et,fullName_et,password_et;
    Spinner userTypeSpinner;
    String[] Types;
    Button updateBtn;
    TextView Tooltv;
    RadioButton activebtn;
    ImageView tool_img;
    String status;
    TextInputLayout passwordInputLayout;
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
        setContentView(R.layout.activity_update_user);
        userName_et = findViewById(R.id.username_etUpdate);
        fullName_et = findViewById(R.id.fullName_etUpdate);
        password_et = findViewById(R.id.password_etUpdate);
        passwordInputLayout = findViewById(R.id.etRestpassupdate);
        userTypeSpinner = findViewById(R.id.userType_spinner_update);
        updateBtn = findViewById(R.id.update_user_btn);
        apiInterface = Api.getClient();
        activebtn = findViewById(R.id.radio_active);
        Tooltv = findViewById(R.id.tool_tv);
        tool_img = findViewById(R.id.tool_img);
        Tooltv.setText("Update User");
        tool_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validationPart();
        getdata();
        //setting data in editTexts bydefault
        userName_et.setText(userName);
        fullName_et.setText(fullName);
        password_et.setText(password);
        //setting userType in spinner
        if (userType.equals("D")){
            Types = new String[]{"Driver", "Restaurant"};
        }
        else {
            Types = new String[]{"Restaurant","Driver"};
        }

        final List<String> TypeList = new ArrayList<>(Arrays.asList(Types));
        ArrayAdapter<String> TypespinnerAdapter = new ArrayAdapter<String>(this,R.layout.usertype_spinner_item,TypeList);
        TypespinnerAdapter.setDropDownViewResource(R.layout.usertype_spinner_item);
        userTypeSpinner.setAdapter(TypespinnerAdapter);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userName_et.getText().toString().isEmpty()){
                    userName_et.setError("User Name Required");
                }
                else if (password_et.getText().toString().isEmpty()){
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    password_et.setError("Password Required");
                }
                else if (password_et.getText().toString().length()<6){
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    password_et.setError("Password should contain at least 6 letters");
                }
                else if (fullName_et.getText().toString().isEmpty()){
                    fullName_et.setError("Full Name Required");
                }
                else if (userTypeSpinner.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"User Type is Mandatory",Toast.LENGTH_SHORT).show();
                }
                else {
                    updateUser();
                }
            }
        });
    }
    public void updateUser(){
        userName = userName_et.getText().toString();
        password = password_et.getText().toString();
        fullName = fullName_et.getText().toString();

        if (activebtn.isChecked()){
            status = "1";
        }
        else {
            status = "0";
        }

        if (userTypeSpinner.getSelectedItem().toString().equals("Restaurant")){
            userType = "R";
        }
        else if (userTypeSpinner.getSelectedItem().toString().equals("Driver")){
            userType = "D";
        }
        final ProgressDialog progressDialog = new ProgressDialog(UpdateUserActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<UpdateOrderRes> updateCall = apiInterface.UpdateUser(userName,password,userType,fullName,userId,token,ID,status);
        updateCall.enqueue(new Callback<UpdateOrderRes>() {
            @Override
            public void onResponse(Call<UpdateOrderRes> call, Response<UpdateOrderRes> response) {
                progressDialog.dismiss();
                if (response.body().getResult().getCode()==0){
                    setResult(RESULT_OK);
                    Toast.makeText(getApplicationContext(),"User Updated",Toast.LENGTH_SHORT).show();
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
            ID = getIntent().getStringExtra("id");
            userId = getIntent().getIntExtra("UserId",0);
            fullName = getIntent().getStringExtra("FullName");
            userType = getIntent().getStringExtra("UserType");
            password = getIntent().getStringExtra("Password");
            userName = getIntent().getStringExtra("UserName");
        }
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
        fullName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fullName_et.getText().toString().length() <= 0) {
                    fullName_et.setError("FullName required");
                } else {
                    fullName_et.setError(null);
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
                    passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                    password_et.setError("Password required");
                } else {
                    password_et.setError(null);
                }
            }
        });
    }
}
