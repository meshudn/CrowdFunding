package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meshu on 5/8/2017.
 */

public class Updates {
    private String userId,postId,desc,createdIn,userName,joinId,imageUri,title;

    public Updates() {
    }

    public Updates(String userId, String postId, String desc, String userName,String title, String imageUri) {
        this.userId = userId;
        this.postId = postId;
        this.desc = desc;
        this.userName = userName;
        this.title = title;
        this.joinId = userId+"_"+postId;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.createdIn = dateFormat.format(date);

        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setCreatedIn(String createdIn) {
        this.createdIn = createdIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJoinId() {
        return joinId;
    }

    public void setJoinId(String joinId) {
        this.joinId = joinId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Updates{" +
                "userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", desc='" + desc + '\'' +
                ", createdIn='" + createdIn + '\'' +
                ", userName='" + userName + '\'' +
                ", joinId='" + joinId + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
