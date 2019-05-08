package com.shadercat.havvka;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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

    //TODO update function for checking info
    Button login_btn;
    ImageView logo;
    EditText email;
    EditText password;
    TextView createAccount;
    TextView loginGuest;
    private int clickCounter = 0;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private Timer clickTimer;
    private MyTimerTask clickTimerTask;

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
// when api is working
/*
                if (WebAPI.CheckUserInfo(email.getText().toString(), password.getText().toString())) {
                    finish();
                } else {
                    logo.startAnimation(logo_anim2);
                }
*/

                //simulate authentication
                mTimer = new Timer();
                mMyTimerTask = new MyTimerTask(new Action() {
                    @Override
                    public void action() {
                        logo.clearAnimation();
                        if (UserInfo.IsCheckedAccount) {
                            finish();
                        } else {
                            logo.startAnimation(logo_anim2);
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
                UserInfo.IsCheckedAccount = false;
                finish();
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
        WebAPI.CheckUserInfo(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void onBackPressed() {
        if (clickCounter >= 1) {
            finishAffinity();
        } else {
            SnackbarShow();
            clickCounter++;
            clickTimer = new Timer();
            clickTimerTask = new MyTimerTask(new Action() {
                @Override
                public void action() {
                    clickCounter = 0;
                }
            });
            clickTimer.schedule(clickTimerTask, 3000);
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

    private void SnackbarShow() {
        Snackbar mSnackbar = Snackbar.make(email, getString(R.string.clickToExit), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorBlue));
        View snackbarView = mSnackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimaryDark);
        TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        mSnackbar.show();
    }

    interface Action {
        void action();
    }
}
