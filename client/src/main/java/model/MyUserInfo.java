package model;

import java.util.ArrayList;
import java.util.List;

public class MyUserInfo {

    private static MyUserInfo sMyUserInfo;
    private User myUser;
    private List<Microblog> mMyMicroblogList;

    private MyUserInfo(){
        myUser = new User();
        mMyMicroblogList = new ArrayList<>();
    }

    public static MyUserInfo get() {
        if(sMyUserInfo == null){
            sMyUserInfo = new MyUserInfo();
        }
        return sMyUserInfo;
    }

    public User getMyUser(){
        return myUser;
    }

    public List<Microblog> getMyMicroblogList() {
        return mMyMicroblogList;
    }

    public void addMyMicroblog(Microblog microblog){
        mMyMicroblogList.add(microblog);
    }

    public void clearMyMicroblogList(){
        mMyMicroblogList.clear();
    }

}
