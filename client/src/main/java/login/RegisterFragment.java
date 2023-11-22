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

import com.example.weibo.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import util.StringUtil;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private EditText mEditTextRegisterNickName;
    private EditText mEditTextRegisterPassword;
    private EditText mEditTextRegisterPhone;
    private TextView mWarning;
    private Button mButtonCommit;

    private String mNickName;
    private String mPassword;
    private String mPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mEditTextRegisterNickName = (EditText)view.findViewById(R.id.editText_register_nickName);
        mEditTextRegisterPassword = (EditText)view.findViewById(R.id.editText_register_password);
        mEditTextRegisterPhone = (EditText)view.findViewById(R.id.editText_register_phone);
        mWarning = (TextView)view.findViewById(R.id.register_warning);
        mButtonCommit = (Button)view.findViewById(R.id.button_commit);

        mButtonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNickName = mEditTextRegisterNickName.getText().toString();
                mPassword = mEditTextRegisterPassword.getText().toString();
                mPhone = mEditTextRegisterPhone.getText().toString();

                //检查用户输入的值是否为空
                if (StringUtil.isEmpty(mNickName) || StringUtil.isEmpty(mPassword)||StringUtil.isEmpty(mPhone)){
                    Toast.makeText(getActivity(),"账号 密码 手机号均不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }

                //请求后台，进行密码检查
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createUser(mNickName,mPassword,mPhone);
                    }
                }).start();
            }
        });

        return view;
    }

    public void createUser(String id,String pwd,String phone){

        String path = "http://192.168.207.235:8080/Weibo_war_exploded/register?id=" + id + "&password=" + pwd+"&phone="+phone;
//        String path = "http://100.65.146.41:8080/Weibo_war_exploded";
        try {
            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//获取服务器数据
            connection.setReadTimeout(10000);//设置读取超时的毫秒数
            connection.setConnectTimeout(10000);//设置连接超时的毫秒数

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.i(TAG,"1成功");
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                Log.i(TAG,"2成功");
                if(result.equals("success")){    //注册成功，跳转到登陆页面
                    mWarning.setText("");
                    Log.i(TAG,"success 成功");
                    Intent intent = LoginActivity.newIntent(getActivity());
                    startActivity(intent);
                }else{                            //登陆失败，将失败信息展示
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
