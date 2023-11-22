package com.example.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMicroblogActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment mfragment;

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, MainMicroblogActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_microblog);

        mFragmentManager = getSupportFragmentManager();
        mfragment = mFragmentManager.findFragmentById(R.id.fragment_homepage);

        if (mfragment == null) {
            mfragment = new HomePageFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_homepage, mfragment)
                    .commit();
        }

        //更改fragment处的页面(主页、搜索、我的)
        mBottomNavigationView = findViewById(R.id.bottom_menu);
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //点一下按钮，就将这个按钮对应的fragment重新刷新一下就是重新new一下
                    switch (menuItem.getItemId()) {
                        case R.id.item_homepage:
                            mfragment = new HomePageFragment();
                            break;
                        case R.id.item_my_microblog:
                            mfragment = new MyMicroblogFragment();
                            break;
                        case R.id.item_me:
                            mfragment = new MeFragment();
                            break;
                    }

                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_homepage, mfragment)
                            .commit();
                    return true;
                }
            });
    }
}
