package com.example.weibo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.UUID;

import model.Microblog;
import util.SingleFragmentActivity;

public class ReadMicroblogActivity extends SingleFragmentActivity {

    private static final String EXTRA_MICROBLOGID = "android.bignerdranch.mymicroblog.microblogId";
    private Microblog mMicroblog;

    public static Intent newIntent(FragmentActivity activity, Microblog microblog) {
        //将点击的微博的ID传给intent
        Intent intent = new Intent(activity, ReadMicroblogActivity.class);
        intent.putExtra(EXTRA_MICROBLOGID, microblog);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMicroblog = (Microblog) getIntent().getSerializableExtra(EXTRA_MICROBLOGID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment(){
        return new ReadMicroblogFragment(mMicroblog);
    }
}