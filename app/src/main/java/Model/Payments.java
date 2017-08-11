package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meshu on 5/8/2017.
 */

public class Payments {
    private String userId,postId,paidAmount,createdIn,userName,joinId,userImageUri,postTitle;

    public Payments() {

    }

    public Payments(String userId, String postId, String paidAmount, String userName,String userImageUri,String postTitle) {
        this.userId = userId;
        this.postId = postId;
        this.paidAmount = paidAmount;
        this.userName = userName;
        this.postTitle = postTitle;
        this.userImageUri = userImageUri;
        this.joinId = userId+"_"+postId;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.createdIn = dateFormat.format(date);

    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getUserImageUri() {
        return userImageUri;
    }

    public void setUserImageUri(String userImageUri) {
        this.userImageUri = userImageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setJoinId(String joinId) {
        this.joinId = joinId;
    }

    public String getCreatedIn() {
        return createdIn;
    }

    public String getUserName() {
        return userName;
    }

    public String getJoinId() {
        return joinId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreatedIn(String createdIn) {
        this.createdIn = createdIn;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", amount='" + paidAmount + '\'' +
                ", createdIn='" + createdIn + '\'' +
                '}';
    }
}
