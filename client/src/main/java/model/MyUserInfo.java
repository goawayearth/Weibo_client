package model;

/*
个人信息管理；个人信息和微博的叠加
*/

import java.util.ArrayList;
import java.util.List;

public class MyUserInfo {

    /*
    一个包含用户信息和这个用户所有的博客的人
    */
    private static MyUserInfo sMyUserInfo;
    private User myUser;
    private List<Microblog> mMyMicroblogList;

    /*
    构造函数是对用户信息和博客列表进行初始化
     */
    private MyUserInfo(){
        myUser = new User();
        mMyMicroblogList = new ArrayList<>();
    }

    /*
    单例模式返回唯一的实例化
     */
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
