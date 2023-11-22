package com.example.weibo;

/*
登录之后最先跳转到这里
 */

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
    /*
    用来管理组件的类
     */
    private FragmentManager mFragmentManager;
    /*
    用来记录当前显示的组件
     */
    private Fragment mfragment;

    /*
    被其他类调用以实现跳转的函数
     */
    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, MainMicroblogActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*
        目前只有底部的三个按钮
         */
        setContentView(R.layout.activity_main_microblog);

        mFragmentManager = getSupportFragmentManager();
        mfragment = mFragmentManager.findFragmentById(R.id.fragment_homepage);

        if (mfragment == null) {
            /*
            刚打开应用的时候这个是空的，因为还没有哪一个fragment创建了实例
            所以就新建homepage的实例
            然后将实例添加到xml文件中的对应的id的部分
             */
            mfragment = new HomePageFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_homepage, mfragment)
                    .commit();
        }

        /*
        给底部的三个按钮添加事件监控更改fragment处的页面(主页、搜索、我的)
         */
        mBottomNavigationView = findViewById(R.id.bottom_menu);
            mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    /*
                    case列举是哪个按钮被点下了，然后就新建哪个按钮对应的页面
                    这个时候是底部的三个按钮是不变化的，一直是三个按钮，三个按钮上面的就是fragment
                    上面的fragment在不断地变化
                     */
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
                    /*
                    然后通过fragment使得新建的fragment替换掉原来的页面
                     */
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_homepage, mfragment)
                            .commit();
                    return true;
                }
            });
    }
}
