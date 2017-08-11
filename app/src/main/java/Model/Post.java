package Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by meshu on 4/26/2017.
 */
@Table(name = "Post")
public class Post extends Model {
    @Column(name = "category")
    public String category;

    @Column(name = "title")
    public String title;

    @Column(name = "desc")
    public String desc;

    @Column(name = "backers")
    public String backers;

    @Column(name = "funded")
    public String funded;

    @Column(name = "remainDays")
    public String remainDays;

    public Post(String category, String title, String desc, String backers, String funded, String remainDays) {
        super();
        this.category = category;
        this.title = title;
        this.desc = desc;
        this.backers = backers;
        this.funded = funded;
        this.remainDays = remainDays;
    }

    public Post() {
        super();
    }
}
