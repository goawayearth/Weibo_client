package model;

/*
对微博对象进行管理
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MicroblogLab {

    private static MicroblogLab sMicroblogLab;

    private List<Microblog> mMicroblogs;

    public static MicroblogLab get(Context context){
        if(sMicroblogLab == null){
            sMicroblogLab = new MicroblogLab(context);
        }
        return sMicroblogLab;
    }

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
