package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//微博类 UUID是唯一标识

public class Microblog implements Serializable {
    private UUID microblogID;
    private String microblogWriter;
    private Date mCreateDate;
    private String microblogTextContent;
    private int commentSum;

    private SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public Microblog(){
        microblogID = UUID.randomUUID();
        mCreateDate = new Date();
        commentSum = 0;
    }

    public UUID getMicroblogID() {
        return microblogID;
    }

    public void setMicroblogID(UUID microblogID) {
        this.microblogID = microblogID;
    }

    public String getMicroblogWriter() {
        return microblogWriter;
    }

    public void setMicroblogWriter(String microblogWriter) {
        this.microblogWriter = microblogWriter;
    }

    public String getCreateDate() {
        return mSDF.format(mCreateDate);
    }

    public void setCreateDate(String createDate){
        try{
            mCreateDate = mSDF.parse(createDate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCreateDate(Date createDate) {
        mCreateDate = createDate;
    }

    public String getMicroblogTextContent() {
        return microblogTextContent;
    }

    public void setMicroblogTextContent(String microblogTextContent) {
        this.microblogTextContent = microblogTextContent;
    }

    public int getCommentSum() {
        return commentSum;
    }

    public void setCommentSum(int commentSum) {
        this.commentSum = commentSum;
    }

    public SimpleDateFormat getSDF() {
        return mSDF;
    }

    public void setSDF(SimpleDateFormat SDF) {
        mSDF = SDF;
    }
}
