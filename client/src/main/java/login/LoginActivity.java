package login;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import util.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
