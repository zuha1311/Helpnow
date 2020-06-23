package com.akshit.helpnow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class signUp extends AppCompatActivity {

    TextView  phone;
    TextInputLayout nameLayout, phoneLayout;
    Button getOtp;


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phone = findViewById(R.id.phone);
        phoneLayout = findViewById(R.id.phoneLayout);
        getOtp = findViewById(R.id.enter_otp);

        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userPhone = phone.getText().toString();

                if(userPhone.isEmpty()){
                    phoneLayout.setError(null);
                    phoneLayout.setErrorEnabled(false);
                    phoneLayout.setError("Please enter your phone no.");
                    phoneLayout.requestFocus();
                }else if(userPhone.length() < 10){
                    phoneLayout.setError(null);
                    phoneLayout.setErrorEnabled(false);
                    phoneLayout.setError("Please enter a valid phone no.");
                    phoneLayout.requestFocus();
                }else{
                    User user = new User(userPhone);

                    API api = retrofit.create(API.class);
                    Call<response> call = api.getOtp(user);

                    call.enqueue(new Callback<response>() {
                        @Override
                        public void onResponse(Call<response> call, Response<response> response) {
                            Toast.makeText(signUp.this, ""+response.body().isSuccess(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(signUp.this, otp.class);
                                Bundle b = new Bundle();
                                b.putString("mobileNo",userPhone);
                                intent.putExtras(b);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            try {
                                if(response.body().isSuccess()){
                                    Intent i=new Intent(signUp.this,otp.class);
                                    startActivity(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<response> call, Throwable t) {
                            Log.e("ERROR", t.toString());

                        }
                    });

                }
            }


        });

    }
}
