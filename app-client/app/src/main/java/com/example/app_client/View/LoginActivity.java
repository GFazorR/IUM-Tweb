package com.example.app_client.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.R;
import com.example.app_client.Utils.LoginManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {
    private MaterialButton btnLogin;
    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (LoginManager.isLoggedIn()){
            mainAct();
            return;
        }

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPw);
        btnLogin = findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(
                v -> validate(inputUsername.getText().toString(),
                inputPassword.getText().toString()));
        findViewById(R.id.guestBtn).setOnClickListener(v -> mainAct());

    }

    private void mainAct() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void resumeBooking(){
        Intent openMainActivity = new Intent(LoginActivity.this, BookingActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validate(String username, String password) {
        AlertDialog progressDialog = getProgressDialog(LoginActivity.this);
        progressDialog.show();

        RetrofitClient.getApi().login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(u -> {
                    progressDialog.dismiss();
                    LoginManager.setUser(u);
                    if (BookingActivity.isRunning)resumeBooking();
                    else mainAct();
                    }, e -> {
                    progressDialog.dismiss();
                    showErrorMessage(e);
                    }
                );
    }


}
