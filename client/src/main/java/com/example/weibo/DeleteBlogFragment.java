package com.example.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.Comment;
import model.Microblog;
import model.MyUserInfo;

//监听删除按钮，点击时删除微博即对应评论
public class DeleteBlogFragment extends Fragment {
    private static final String ip = "192.168.221.235";
    public String TAG = "DeleteBlogFragment";
    private ImageButton mButtonDelete;
    private Microblog mMicroblog;

    public DeleteBlogFragment(Microblog microblog){
        this.mMicroblog = microblog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstnceState){
        View v = inflater.inflate(R.layout.mircroblog_delete, container, false);
        mButtonDelete = (ImageButton) v.findViewById(R.id.deleteBlog);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除微博
                DeleteBlogThread deleteBlogThread = new DeleteBlogThread();
                deleteBlogThread.start();
                //删除成功后返回到主界面
                Intent intent = MainMicroblogActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });
        return v;
    }

    //删除微博
    private class DeleteBlogThread extends Thread {

        public void run() {

            String json = JSON.toJSONString(mMicroblog);
            Log.i(TAG, json);

            try {
                String path = "http://"+ip+":8080/Weibo_war_exploded/delete_blog";
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent",
                        "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                //将数据通过POST的方式传给服务器端
                byte[] bytes = json.getBytes();
                connection.getOutputStream().write(bytes);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                    System.out.println("result is " + result);
                    if (result.equals("success")) {    //删除微博成功
                        Looper.prepare();
                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        //mCommentAdapter.notifyDataSetChanged();    //评论成功后刷新评论
                    } else {                            //登陆失败，将失败信息展示
                        Looper.prepare();
                        Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } else {
                    Log.i(TAG, "访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}