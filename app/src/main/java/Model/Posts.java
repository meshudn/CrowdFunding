package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by meshu on 5/6/2017.
 */

public class Posts {
    public String userId;
    public String postId;
    public String tag;

    public String location;

    public String coverUri;

    public String active;

    public String projectDone;

    public String title;


    public String desc;

    public String createIn;


    public String backers;


    public String fund;
    public String currentFund;


    public String remainDays;


    public Posts(String userId,String postId,String tag,String location, String title, String desc,String coverUri, String fund, String remainDays) {
        this.userId = userId;
        this.postId = postId;
        this.tag = tag;
        this.location = location;
        this.title = title;
        this.desc = desc;
        this.coverUri = coverUri;
        this.active = "true";
        this.projectDone = "false";
        this.fund = fund;
        this.currentFund = "0";
        this.remainDays = remainDays;
        this.backers = "0";

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.createIn = dateFormat.format(date);

    }

    public Posts() {

    }

    @Override
    public String toString() {
        return "Posts{" +
                "userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", tag='" + tag + '\'' +
                ", location='" + location + '\'' +
                ", coverUri='" + coverUri + '\'' +
                ", active='" + active + '\'' +
                ", projectDone='" + projectDone + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", createIn='" + createIn + '\'' +
                ", backers='" + backers + '\'' +
                ", fund='" + fund + '\'' +
                ", currentFund='" + currentFund + '\'' +
                ", remainDays='" + remainDays + '\'' +
                '}';
    }
}
