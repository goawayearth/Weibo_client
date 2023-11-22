package model;

/*
以列表的形式对微博进行管理
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MicroblogLab {

    private static MicroblogLab sMicroblogLab;

    /*
    微博队列
     */
    private List<Microblog> mMicroblogs;

    /*
    单例模式，通过get可以返回这个类的唯一的实例
     */
    public static MicroblogLab get(Context context){
        if(sMicroblogLab == null){
            sMicroblogLab = new MicroblogLab(context);
        }
        return sMicroblogLab;
    }

    /*
    构造函数是为微博的列表进行初始化
     */
    private MicroblogLab(Context context){
        mMicroblogs = new ArrayList<>();
    }

    public void clearMicroblogs(){
        mMicroblogs.clear();
    }

    public void addMicroblog(Microblog m){
        mMicroblogs.add(m);
    }

    public List<Microblog> getMicroblogs(){
        return mMicroblogs;
    }

    public Microblog getMicroblog(UUID id){
        for(Microblog microblog : mMicroblogs){
            if(microblog.getMicroblogID().equals(id)){
                return microblog;
            }
        }

        return null;
    }
}
