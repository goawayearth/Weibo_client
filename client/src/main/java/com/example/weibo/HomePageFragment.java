package com.example.weibo;

/*
主页的组件
*/

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
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import model.Microblog;

/**
 * 微博主页
 */
public class HomePageFragment extends Fragment {
    private static final String TAG = "HomePageFragment";
    /*
    类似于滚动窗口
    */
    private RecyclerView mRecyclerView;
    /*
    存储微博的列表
    */
    private List<Microblog> mMicroblogs;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /*
        在Android中，`setHasOptionsMenu(true)`是Fragment类的一个方法，
        用于指示该Fragment拥有自己的选项菜单。当调用这个方法并传递参数为`true`时，
        系统会调用Fragment的`onCreateOptionsMenu(Menu, MenuInflater)`方法，
        允许Fragment为其自己创建菜单项。
        具体来说，这个方法的作用是告诉系统这个Fragment需要有自己的选项菜单，
        而不是依赖于Activity的选项菜单。这样，当用户进入或离开这个Fragment时，相关的选项菜单就会被创建或销毁。
        在上述代码中，`setHasOptionsMenu(true)`告诉系统这个Fragment需要自己处理选项菜单。
        然后，在`onCreateOptionsMenu`方法中，开发者可以定义该Fragment自己的菜单项。
        这种方式使得Fragment可以独立地管理和更新自己的菜单，而不影响Activity的选项菜单。
         */
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        getActivity().setTitle("主页");
        //承载微博的容器
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homepage);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    //程序正常启动：onCreate()->onStart()->onResume();
    //正常退出：onPause()->onStop()->onDestory()
    @Override
    public void onResume() {
        super.onResume();
        /*
        调用这个方法之后，会执行FetchBlogTask类的execute函数
        */
        new FetchBlogTask().execute();
    }
    /*
    这就是组件创建自己的菜单项的函数，在页面的右上角创建一个+号用来新建微博
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_microblog_list,menu);  //新建微博
    }

    // 点击微博之后
    @Override
    public boolean onOptionsItemSelected(MenuItem item ){
        switch(item.getItemId()){
            /*
            监听到右上角的加号被点击之后，通过WriteMicroblogActivity的bewIntent函数实现页面跳转功能
            */
            case R.id.item_add_microblog:
                Log.i(TAG,"点击了");
                Intent intent = WriteMicroblogActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    //异步的从微博的数据库里取出所有的微博数据，并且将它转化为Microblog的形式
    private class FetchBlogTask extends AsyncTask<Void,Void,List<Microblog>> {
        @Override
        protected List<Microblog> doInBackground(Void... voids) {
            /*
            微博列表，用来存储所有的微博数据
             */
            List<Microblog> list = new ArrayList<Microblog>();
            try {
                /*
                请求路径
                 */
                String path = "http://192.168.207.235:8080/Weibo_war_exploded/get_blog";
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

        // 后台加载完成后，执行函数将数据传递给MicroblogAdapter
        @Override
        protected void onPostExecute(List<Microblog> microblogs) {
            mMicroblogs = microblogs;
            /*
            Adapter用于将数据转化成视图并显示在控件上
             */
            mRecyclerView.setAdapter(new MicroblogAdapter(mMicroblogs));
        }
    }



    /*
    适配器类
    */
    private class MicroblogAdapter extends RecyclerView.Adapter<MicroblogHolder>{

        public MicroblogAdapter(List<Microblog> microblogs){
            mMicroblogs = microblogs;
        }

        @NonNull
        @Override
        public MicroblogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MicroblogHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MicroblogHolder holder, int position) {
            Microblog microblog = mMicroblogs.get(position);
            holder.bind(microblog);
        }

        @Override
        public int getItemCount() {
            return mMicroblogs.size();
        }
    }



    // holder用户微博内容
    private class MicroblogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView usernameTextView;
        private TextView dateTextView;
        private TextView contentTextView;
        private Microblog mMicroblog;

        /*
        每个微博内容的结构类
        */
        public MicroblogHolder(LayoutInflater inflater, ViewGroup parent){
            // 微博列表
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

        /*
        给每个微博内容设置监听，点击之后通过newIntent函数跳转到新的页面
         */
        @Override
        public void onClick(View v) {
            Intent intent = null;
            intent = ReadMicroblogActivity.newIntent(getActivity(), mMicroblog);
            startActivity(intent);
        }
    }


}
