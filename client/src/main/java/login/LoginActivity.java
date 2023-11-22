package login;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import util.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    // 被别的类调用，引流到这个页面
    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        return intent;
    }

    // 加载这个fragment,fragment是在activity上存在的组件
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
