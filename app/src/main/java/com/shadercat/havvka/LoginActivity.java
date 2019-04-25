package com.shadercat.havvka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    Button login_btn;
    ImageView logo;
    EditText email;
    EditText password;
    TextView createAccount;
    TextView loginGuest;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    Animation logo_anim1;
    Animation logo_anim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logo = (ImageView) findViewById(R.id.logo_loginActivity);
        login_btn = (Button) findViewById(R.id.btn_login);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        createAccount = (TextView) findViewById(R.id.link_signup);
        loginGuest = (TextView) findViewById(R.id.link_guest);

        logo_anim1 = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);
        logo_anim2 = AnimationUtils.loadAnimation(this, R.anim.shake_logo);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(logo_anim1);
                LoginCheckout();

                //simulate authentication
                mTimer = new Timer();
                mMyTimerTask = new MyTimerTask(new Action() {
                    @Override
                    public void action() {
                        logo.clearAnimation();
                        if (!UserInfo.IsCheckedAccount) {
                            logo.startAnimation(logo_anim2);
                        } else {
                            onBackPressed();
                        }
                    }
                });
                mTimer.schedule(mMyTimerTask, 5000);


            }
        });

        loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.GuestMode = true;
                UserInfo.IsCheckedAccount = true;
                onBackPressed();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void LoginCheckout() {
        // implement check out logic there

        //this is simulation of checking information
        if (email != null && password != null) {
            if (email.getText().toString().trim().equals("hola@nure.ua") && password.getText().toString().trim().equals("12345")) {
                UserInfo.IsCheckedAccount = true;
                UserInfo.UserEmail = email.getText().toString().trim();
                //onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (UserInfo.IsCheckedAccount) {
            super.onBackPressed();
        }
    }


    class MyTimerTask extends TimerTask {
        Action action;

        MyTimerTask(Action act) {
            action = act;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    action.action();
                }
            });
        }
    }

    interface Action {
        void action();
    }
}
