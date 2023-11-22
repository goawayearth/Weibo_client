package model;

/*
和评论有关的类
 */

import java.util.UUID;

public class Comment {

    /*
    UUID（通用唯一标识符）是一个标准的 128 位数字标识符，通常以 32 个十六进制数字的形式表示。
    UUID 的目的是在分布式系统中唯一标识信息，以防止重复标识的可能性。它是由时间、节点（通常是计算机的 MAC 地址），时钟序列和随机数生成的。
     */
    private UUID mCommentId;
    private String mContent;
    private String writerName;
    private UUID mMicroblogId;

    public Comment(){
        mCommentId = UUID.randomUUID();
    }

    public UUID getCommentId() {
        return mCommentId;
    }

    public void setCommentId(UUID commentId) {
        mCommentId = commentId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writeName) {
        this.writerName = writeName;
    }

    public UUID getMicroblogId() {
        return mMicroblogId;
    }

    public void setMicroblogId(UUID microblogId) {
        mMicroblogId = microblogId;
    }
}
