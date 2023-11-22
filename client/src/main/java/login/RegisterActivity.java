package login;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import util.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {

    /*
    首先还是一个被其他类调用的一个函数，通过这个函数跳转到这个界面
    */
    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, RegisterActivity.class);
        return intent;
    }

    /*
    创建这个页面的组件，以及组件上的实现的功能
    */
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }

}
