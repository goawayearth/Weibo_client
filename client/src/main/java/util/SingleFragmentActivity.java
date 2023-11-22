package util;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.weibo.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    /*
    抽象函数，靠后面子类去继承实现之后这个调用的时候就会新实例化一个类，这个类的内容就会出现再屏幕上

     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        屏幕上仅有一个充满空间的容器
         */
        setContentView(R.layout.activity_fragment);
        /*
        有一个控件管理器
         */
        FragmentManager fm = getSupportFragmentManager();
        /*
        最大的容器赋值给Fragment
         */
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            // 设置点击应用图标可以返回主程序
            actionBar.setHomeButtonEnabled(true);
            // 应用程序启用左上角的返回箭头按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
    这里是定义了左上角的箭头的功能
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            左上角返回箭头的特殊ID
             */
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
