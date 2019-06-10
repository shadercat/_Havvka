package com.shadercat.havvka;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText repeatPassword;
    Button register;
    TextView loginAccount;
    ImageView logo;

    Animation logo_anim2;
    Animation logo_anim1;
    mWorkingThread parr;
    Handler mUIHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        logo_anim2 = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);
        logo_anim1 = AnimationUtils.loadAnimation(this, R.anim.shake_logo);

        email = (EditText) findViewById(R.id.input_email_reg);
        password = (EditText) findViewById(R.id.input_password_reg);
        repeatPassword = (EditText) findViewById(R.id.input_password2_reg);
        register = (Button) findViewById(R.id.btn_register);
        loginAccount = (TextView) findViewById(R.id.link_login);
        logo = (ImageView) findViewById(R.id.logo_SignupActivity);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logo.startAnimation(logo_anim2);
                boolean checked = true;
                if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                    repeatPassword.setError(getApplicationContext().getString(R.string.passwordError));
                    checked = false;
                }
                if (!email.getText().toString().trim().contains("@nure.ua")) {
                    email.setError(getApplicationContext().getString(R.string.emailError));
                    checked = false;
                }
                if (password.getText().toString().length() <= 4) {
                    password.setError(getApplicationContext().getString(R.string.smallPassword));
                    checked = false;
                }
                if (checked) {
                    parr = new mWorkingThread("signup");
                    parr.start();
                    parr.prepareHandler();
                    Runnable task = new Runnable() {
                        @Override
                        public void run() {
                            final boolean flag = WebAPI.CreateAccount(email.getText().toString().trim(), password.getText().toString());
                            mUIHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (flag) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.create_account), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.errorCreate), Toast.LENGTH_LONG).show();
                                    }
                                    logo.clearAnimation();
                                }
                            });
                        }
                    };
                    parr.postTask(task);
                } else {
                    logo.clearAnimation();
                    logo.startAnimation(logo_anim1);
                }
            }
        });
        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (parr != null) {
            parr.quit();
        }
        super.onDestroy();
    }
}
