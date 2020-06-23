package com.akshit.helpnow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class otp extends AppCompatActivity {

    TextView phTextView, resend,enterTheOtp;
    Button verify;
    OtpTextView otpTextView;
    String OTP;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);



        phTextView = findViewById(R.id.phTextView);
        resend = findViewById(R.id.resend);
        verify = findViewById(R.id.verify_button);
        otpTextView = findViewById(R.id.otp_view);
        enterTheOtp = findViewById(R.id.enter_the_otp_sent_to);
        Bundle bundle = getIntent().getExtras();
        String phtextview = bundle.getString("mobileNo");
        phTextView.setText(phtextview);
        final String mobileNo = enterTheOtp + phtextview;

        verify.setEnabled(false);


        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                verify.setEnabled(true);
                verify.setAlpha(1.0f);
                OTP = otp;
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Verify verify = new Verify(mobileNo, OTP);

                API api = retrofit.create(API.class);
                Call<response> call = api.verifyOtp(verify);

                call.enqueue(new Callback<response>() {
                    @Override
                    public void onResponse(Call<response> call, Response<response> response) {
                        if(response.body().isSuccess()){
                            SharedPreferences sp =getSharedPreferences("helpNow", MODE_PRIVATE);
                            SharedPreferences.Editor et = sp.edit();
                            et.putBoolean("login", true);
                            et.putString("name", response.body().getName());
                            et.putString("mobileNo", response.body().getMobileNo());
                            et.apply();

                            Intent intent = new Intent(otp.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<response> call, Throwable t) {

                    }
                });

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API api = retrofit.create(API.class);
                Call<Response> call = api.resendOtp(mobileNo);
                call.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, Response<Response> response) {

                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });
            }
        });
    }
}
