package login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weibo.MainMicroblogActivity;
import com.example.weibo.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.MyUserInfo;
import util.StringUtil;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private EditText mEditTextId;
    private EditText mEditTextPassword;
    private TextView mWarning;
    private Button mButtonLogin;
    private Button mButtonRegister;

    private String mId;     //记录账号
    private String mPassword;     //记录密码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //获取各个元素
        mEditTextId = (EditText)view.findViewById(R.id.editText_id);
        mEditTextPassword = (EditText)view.findViewById(R.id.editText_password);
        mWarning = (TextView)view.findViewById(R.id.warning);
        mButtonLogin = (Button)view.findViewById(R.id.button_login);
        mButtonRegister = (Button)view.findViewById(R.id.button_register);

        // 点击了登录按钮，判断是否是空
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = mEditTextId.getText().toString();
                mPassword = mEditTextPassword.getText().toString();

                //检查用户输入的值是否为空
                if (StringUtil.isEmpty(mId) || StringUtil.isEmpty(mPassword)){
                    //界面提示内容
                    Toast.makeText(getActivity(),"账号和密码均不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }

                //请求后台，进行密码检查
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkPassword(mId,mPassword);
                    }
                }).start();
            }
        });

        // 注册按钮，跳转到注册页面
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegisterActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        return view;
    }

    private void openApp(){
        Intent intent = MainMicroblogActivity.newIntent(getActivity());
        // 改变了MyUserInfo的ID
        MyUserInfo.get().getMyUser().setId(mId);
        startActivity(intent);
    }

    //检查账户与密码
    public void checkPassword(String id,String password) {
        String path = "http://192.168.207.235:8080/Weibo_war_exploded/login?id=" + id + "&password=" + password;
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                if(result.equals("success")){    //登陆成功，跳转到首页
                    mWarning.setText("");
                    Log.i(TAG,"成功");
                    openApp();
                }else{                          //登陆失败，将失败信息展示
                    mWarning.setText(result);
                }
            }else{
                Log.i(TAG,"访问服务器失败");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
