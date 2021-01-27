package com.example.app_client.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.app_client.Adapter.PagerAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.User;
import com.example.app_client.R;
import com.example.app_client.Utils.LoginManager;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private TabLayout tabs;
    private ViewPager pager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = findViewById(R.id.toolbar);*/
        /*setSupportActionBar(toolbar);*/

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        User user = LoginManager.getUser();
        if (user != null){
            menu.findItem(R.id.logout_item).setVisible(true);
            menu.findItem(R.id.login_item).setVisible(false);
        }else {
            menu.findItem(R.id.logout_item).setVisible(false);
            menu.findItem(R.id.login_item).setVisible(true);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_item:
                logout();
                break;
            case R.id.login_item:
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0)
            pager.setCurrentItem(1);
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void logout(){
        RetrofitClient.getApi().logout().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((u, e) -> {
                            LoginManager.clear();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                );
    }


}