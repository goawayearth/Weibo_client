package login;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import util.SingleFragmentActivity;

/*
这个类继承的父类的功能是自动调用createFragment用创建组件放在空白的容器中
 */
public class LoginActivity extends SingleFragmentActivity {

    // 静态函数，可以用类名调用，方便调用和跳转到这个类，被别的类调用，引流到这个页面
    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        return intent;
    }
    // 加载这个页面上的组件，本身只有一个框架，调用这个实例，才是页面的主要内容，创建实例时候会自动执行它里面的一些函数，
    // 和生命周期有关
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
