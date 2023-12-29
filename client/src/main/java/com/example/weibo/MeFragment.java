package com.example.weibo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import login.LoginActivity;
import model.Microblog;
import model.MyUserInfo;
import model.User;

/**
 * 我的
 */
public class MeFragment extends Fragment {
    private static final String ip = "192.168.221.235";
    private static final String TAG = "MeFragment";
    private TextView mMeId;
    private RecyclerView mRecyclerView;
    private List<Microblog> mMicroblogs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchBlogTask().execute();
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        getActivity().setTitle("我的");
        User me = MyUserInfo.get().getMyUser();

        mMeId = (TextView)view.findViewById(R.id.me_id);
        mMeId.setText(me.getId());

        mRecyclerView = (RecyclerView)view.findViewById(R.id.me_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){   //注销
            case R.id.logout:
                Intent intent = LoginActivity.newIntent(getActivity());
                //清除当前这个栈，再从新建一个栈
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //holder
    private class MicroblogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView usernameTextView;
        private TextView dateTextView;
        private TextView contentTextView;
        private Microblog mMicroblog;

        public MicroblogHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_microblog_item,parent,false));
            itemView.setOnClickListener(this);
            usernameTextView = (TextView)itemView.findViewById(R.id.list_microblog_item_username);
            dateTextView =  (TextView)itemView.findViewById(R.id.list_microblog_item_date);
            contentTextView = (TextView)itemView.findViewById(R.id.list_microblog_item_content);
        }

        public void bind(Microblog microblog) {
            mMicroblog = microblog;
            usernameTextView.setText(mMicroblog.getMicroblogWriter());
            dateTextView.setText(mMicroblog.getCreateDate());
            contentTextView.setText(mMicroblog.getMicroblogTextContent());
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            intent = ReadMicroblogActivity.newIntent(getActivity(), mMicroblog);
            startActivity(intent);
        }
    }

    //adapter
    private class MicroblogAdapter extends RecyclerView.Adapter<MeFragment.MicroblogHolder>{

        public MicroblogAdapter(List<Microblog> microblogs){
            mMicroblogs = microblogs;
        }

        @NonNull
        @Override
        public MeFragment.MicroblogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MeFragment.MicroblogHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MeFragment.MicroblogHolder holder, int position) {
            Microblog microblog = mMicroblogs.get(position);
            holder.bind(microblog);
        }

        @Override
        public int getItemCount() {
            return mMicroblogs.size();
        }
    }

    //异步的从微博的数据库里取出所有的微博数据，并且将它转化为Microblog的形式
    private class FetchBlogTask extends AsyncTask<Void,Void,List<Microblog>> {
        @Override
        protected List<Microblog> doInBackground(Void... voids) {
            List<Microblog> list = new ArrayList<Microblog>();
            try {
                String id = MyUserInfo.get().getMyUser().getId();
                String path = "http://"+ip+":8080/Weibo_war_exploded/get_blog?id="+id;

                URL url = new URL(path);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//获取服务器数据
                connection.setReadTimeout(10000);//设置读取超时的毫秒数
                connection.setConnectTimeout(10000);//设置连接超时的毫秒数

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                    JSONArray array = JSON.parseArray(result);
                    Log.i(TAG,""+array);

                    //将JSON数组转变为微博数组
                    for(int i=0;i<array.size();i++){
                        JSONObject object = array.getJSONObject(i);
                        list.add(JSON.parseObject(JSON.toJSONString(object),Microblog.class));
                    }

                }else{
                    Log.i(TAG,"访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            };
            return list;
        }

        @Override
        protected void onPostExecute(List<Microblog> microblogs) {
            mMicroblogs = microblogs;
            mRecyclerView.setAdapter(new MeFragment.MicroblogAdapter(mMicroblogs));
        }
    }
}
