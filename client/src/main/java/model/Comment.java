package model;

/*
和评论有关的类
 */

import java.util.UUID;

public class Comment {

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
