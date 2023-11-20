package com.example.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import model.Microblog;
import model.MyUserInfo;

public class WriteMicroblogFragment extends Fragment {
    private static final String TAG = "WriteMicroblogFragment";
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstnceState){
        View v = inflater.inflate(R.layout.fragment_write_microblog, container, false);

        mEditText = (EditText)v.findViewById(R.id.editText_write_microblog);
        mEditText.setText("");

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_write_microblog,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item_commit_microblog:
                AddMicroblogThread addMicroblogThread = new AddMicroblogThread();
                addMicroblogThread.start();
                //发送成功后返回到主界面
                Intent intent = MainMicroblogActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AddMicroblogThread extends Thread{

        private Microblog mMicroblog;

        public void run(){
            String content = mEditText.getText().toString();

            mMicroblog = new Microblog();
            mMicroblog.setCreateDate(new Date());
            mMicroblog.setMicroblogID(UUID.randomUUID());
            mMicroblog.setCommentSum(0);
            mMicroblog.setMicroblogWriter(MyUserInfo.get().getMyUser().getId());
            mMicroblog.setMicroblogTextContent(content);
            String json = JSON.toJSONString(mMicroblog);
            Log.i(TAG,json);

            try{
                String path = "http://100.65.146.41:8080/Weibo_war_exploded/write";
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                connection.setDoOutput(true);
                //将数据通过POST的方式传给服务器端
                byte[] bytes = json.getBytes();
                connection.getOutputStream().write(bytes);

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine(); //读取服务器进行逻辑处理后页面显示的数据
                    System.out.println("result is "+result);
                    if(result.equals("success")){    //微博成功
                        mEditText.setText("");
                        Looper.prepare();
                        Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }else{                            //登陆失败，将失败信息展示
                        Looper.prepare();
                        Toast.makeText(getActivity(),"发送失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }else{
                    Log.i(TAG,"访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
