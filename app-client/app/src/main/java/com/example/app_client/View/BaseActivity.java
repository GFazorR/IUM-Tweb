package com.example.app_client.View;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.app_client.R;
import com.example.app_client.Utils.ErrorUtil;
import com.example.app_client.Utils.LoginManager;
import com.example.app_client.Utils.UnauthorizedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public final void onUnauthorizedEvent(UnauthorizedEvent e){handleUnauthorizedEvent();}

    private void handleUnauthorizedEvent() {
        LoginManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showErrorMessage(Throwable throwable){
        ErrorUtil.showErrorMessage(getApplicationContext(), throwable);
    }

    public AlertDialog getProgressDialog(Context context){
        return getProgressDialog(context, null);
    }

    public AlertDialog getProgressDialog(Context context, String title){
        View view = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        TextView textView = view.findViewById(R.id.progressTitle);
        if (title != null)
            textView.setText(title);
        return new AlertDialog.Builder(context,R.style.Theme_AppCompat_Light_Dialog)
                .setView(view).setCancelable(false).create();
    }

    public AlertDialog getErrorDialog(Context context, Throwable error, View.OnClickListener listener){
        View view = getLayoutInflater().inflate(R.layout.error_layout, null);
        final TextView errorTxt = view.findViewById(R.id.error_text_view);
        final Button errorButton = view.findViewById(R.id.retry_button);
        errorButton.setOnClickListener(listener);
        if (error != null)
            errorTxt.setText(ErrorUtil.getErrorMessage(error));
        return new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog)
                .setView(view).setCancelable(false).create();
    }
}
