package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by meshu on 5/10/2017.
 */

public class Friend {
    public String userId,friendUserId,createdIn,joinId;

    public Friend(String userId, String friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.joinId  = userId + "_" + friendUserId;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.createdIn = dateFormat.format(date);
    }

    public Friend() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getCreatedIn() {
        return createdIn;
    }

    public String getJoinId() {
        return joinId;
    }

    public void setJoinId(String joinId) {
        this.joinId = joinId;
    }
}
