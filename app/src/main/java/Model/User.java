package Model;

/**
 * Created by meshu on 5/5/2017.
 */

public class User {
    public String name, nid, phone, userId,imageUri,bio="Write something about yourself.";

    public User(String userId, String phone, String nid,String imageUri,String name) {
        this.userId = userId;
        this.nid = nid;
        this.phone = phone;
        this.imageUri = imageUri;
        this.name = name;
    }

    public User(String name, String nid, String phone, String userId, String imageUri, String bio) {
        this.name = name;
        this.nid = nid;
        this.phone = phone;
        this.userId = userId;
        this.imageUri = imageUri;
        this.bio = bio;
    }

    public User() {

    }
}
