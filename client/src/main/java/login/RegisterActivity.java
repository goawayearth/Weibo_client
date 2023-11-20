package login;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import util.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, RegisterActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }

}
