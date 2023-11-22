package com.example.weibo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import model.Comment;
import model.Microblog;
import model.MyUserInfo;

public class ReadMicroblogFragment extends Fragment {
    private static final String TAG = "ReadMicroblogFragment";
    private Microblog mMicroblog;
    private TextView mMircoblogContent;
    private TextView mUserName;
    private TextView mDate;
    private TextView mCommentNum;
    private int num = 0;

    private RecyclerView mRecyclerViewComment;
    private EditText mEditTextComment;
    private Button mButtonComment;

    private CommentAdapter mCommentAdapter;
    private List<Comment> mCommentList;

    private Button mButtonDelete;

    //定义评论列表 和 微博内容
    public ReadMicroblogFragment(Microblog microblog){
        mCommentList = new ArrayList<>();
        this.mMicroblog = microblog;
    }

    // 创建的时候，拉取评论
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        new FetchCommentTask().execute();
        // true是显示顶部菜单栏，会紧接着自动调用onCreateOptionsMenu，实现顶部分享按钮
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstnceState){
        View v = inflater.inflate(R.layout.fragment_read_microblog, container, false);

        mMircoblogContent = (TextView) v.findViewById(R.id.detail_content);
        mMircoblogContent.setText(mMicroblog.getMicroblogTextContent());

        mUserName = (TextView)v.findViewById(R.id.detail_username);
        mUserName.setText((mMicroblog.getMicroblogWriter()));

        mDate = (TextView)v.findViewById(R.id.detail_date);
        mDate.setText(mMicroblog.getCreateDate());

        mCommentNum = (TextView)v.findViewById(R.id.detail_comment_num) ;
        mCommentNum.setText("评论"+num);

        mRecyclerViewComment = (RecyclerView)v.findViewById(R.id.recyclerview_comment);
        mEditTextComment = (EditText)v.findViewById(R.id.editText_comment);
        mButtonComment = (Button)v.findViewById(R.id.button_comment);

        mRecyclerViewComment.setLayoutManager(new LinearLayoutManager(getActivity()));

        //微博用户id == 登录用户id则添加删除按钮
        if (mMicroblog.getMicroblogWriter().equals(MyUserInfo.get().getMyUser().getId())){
            Fragment fragment = new DeleteBlogFragment(mMicroblog);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_delete, fragment)
                    .commit();
        }
        //

        mEditTextComment.setText("");
        mButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCommentContent = mEditTextComment.getText().toString();

                Comment comment = new Comment();
                comment.setContent(mCommentContent);
                comment.setMicroblogId(mMicroblog.getMicroblogID());
                comment.setWriterName(MyUserInfo.get().getMyUser().getId());

                AddCommentThread  addCommentThread = new AddCommentThread();
                addCommentThread.start();

                mCommentList.add(comment);
                mCommentAdapter.notifyDataSetChanged();
                num++;
                mCommentNum.setText("评论"+num);
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_share_mircroblog,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_share_microblog: //分享微博内容
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,createMessage());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String createMessage(){
        String message = "";
        message += mMicroblog.getMicroblogWriter() +"发布了微博：\n";
        message += mMicroblog.getMicroblogTextContent();
        return message;
    }

    private class CommentgHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewComment;
        private TextView mTextViewWriter;
        private Comment mComment;

        public CommentgHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_comment_item,parent,false));

            mTextViewWriter  = (TextView)itemView.findViewById(R.id.list_comment_writer);
            mTextViewComment = (TextView)itemView.findViewById(R.id.list_comment_content);
        }

        public void bind(Comment comment) {
            mComment = comment;
            mTextViewWriter.setText(comment.getWriterName());
            mTextViewComment.setText(comment.getContent());
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentgHolder>{

        private List<Comment> mComments;

        public CommentAdapter(List<Comment> comments){
            mComments = comments;
        }

        @NonNull
        @Override
        public CommentgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CommentgHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentgHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.bind(comment);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }
    }

    //异步的从评论的数据库中取出该微博的所有评论，并且将它转化为Comment的形式
    private class FetchCommentTask extends AsyncTask<Void,Void,List<Comment>> {
        @Override
        protected List<Comment> doInBackground(Void... voids) {
            List<Comment> list = new ArrayList<Comment>();
            try {
                String id = mMicroblog.getMicroblogID().toString();
                String path = "http://192.168.207.235:8080/Weibo_war_exploded/get_comment?id="+id;

                System.out.println("id is :"+id);

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
                        list.add(JSON.parseObject(JSON.toJSONString(object),Comment.class));
                    }

                    num = array.size();

                }else{
                    Log.i(TAG,"访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            };
            return list;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            mCommentList = comments;
            mCommentAdapter = new CommentAdapter(mCommentList);
            mRecyclerViewComment.setAdapter(mCommentAdapter);
            mCommentNum.setText("评论"+num);
        }
    }

    //发送评论
    private class AddCommentThread extends Thread {

        public void run() {

            String mCommentContent = mEditTextComment.getText().toString();

            Comment comment = new Comment();
            comment.setContent(mCommentContent);
            comment.setMicroblogId(mMicroblog.getMicroblogID());
            comment.setWriterName(MyUserInfo.get().getMyUser().getId());

            //mCommentList.add(comment);
            //mCommentAdapter.notifyDataSetChanged();
            //刷新不能写在线程上

            mEditTextComment.setText("");

            String json = JSON.toJSONString(comment);
            Log.i(TAG, json);

            try {
                String path = "http://192.168.207.235:8080/Weibo_war_exploded/write_comment";
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
                    if (result.equals("success")) {    //评论成功
                        mEditTextComment.setText("");
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
