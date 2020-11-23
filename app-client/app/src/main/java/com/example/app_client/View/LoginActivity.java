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
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {
    private Button btnLogin;
    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validate(String username, String password) {
        AlertDialog progressDialog = getProgressDialog(LoginActivity.this);
        progressDialog.show();

        RetrofitClient.getApi().login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(u -> {
                    progressDialog.hide();
                    LoginManager.setUser(u);
                    mainAct();
                    }, e -> {
                    progressDialog.hide();
                    showErrorMessage(e);
                    }
                );
    }


}
