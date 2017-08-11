package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meshu on 5/8/2017.
 */

public class Comments {
    private String userId,postId,desc,createdIn,userName,joinId;

    public Comments() {

    }

    public Comments(String userId, String postId, String desc, String userName) {
        this.userId = userId;
        this.postId = postId;
        this.desc = desc;
        this.userName = userName;
        this.joinId = userId+"_"+postId;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.createdIn = dateFormat.format(date);

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
                ", desc='" + desc + '\'' +
                ", createdIn='" + createdIn + '\'' +
                '}';
    }
}
