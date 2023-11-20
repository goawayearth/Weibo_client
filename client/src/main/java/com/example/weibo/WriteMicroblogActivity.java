package com.example.weibo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import util.SingleFragmentActivity;

public class WriteMicroblogActivity extends SingleFragmentActivity {


    public static Intent newIntent(FragmentActivity activity) {
        Intent intent = new Intent(activity, WriteMicroblogActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        return new WriteMicroblogFragment();
    }
}
