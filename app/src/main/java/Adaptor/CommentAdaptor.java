package Adaptor;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charity.meshu.charity.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Comments;

/**
 * Created by meshu on 5/8/2017.
 */

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.MyRecyclerViewHolder> {
    public ArrayList<Comments> comments;


    public CommentAdaptor(ArrayList<Comments> comments) {
        this.comments = comments;
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment,parent,false);

        MyRecyclerViewHolder viewHolder = new MyRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, final int position) {
        holder.userName.setText(comments.get(position).getUserName().toUpperCase());
        holder.comment.setText(comments.get(position).getDesc());


        /*
        *  calculate time diff
        * */
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        String dateStart = comments.get(position).getCreatedIn();
        String dateStop = dateFormat.format(date);

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);



            if(diffDays > 0){
                holder.time.setText(comments.get(position).getCreatedIn());
            }
            else if(diffDays < 1 && diffHours > 0){
                holder.time.setText(diffHours+" hours ago");
            }else if(diffDays <1 && diffHours < 1 && diffMinutes > 0){
                holder.time.setText(diffMinutes+" minutes ago");
            }else if(diffDays <1 && diffHours < 1 && diffMinutes < 1 ){
                holder.time.setText("just now");
            }


           /*
            //successful tasted ..
            Log.d("diffsecods = ", Long.toString(diffSeconds));
            Log.d("diffMinutes = ",Long.toString(diffMinutes));
            Log.d("diffHours = ",Long.toString(diffHours));
            Log.d("diffDays = ",Long.toString(diffDays));*/

        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public static class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView userName,comment,time;
        ImageView imageView;

        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.name1);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (TextView) itemView.findViewById(R.id.timeOfComment);
            imageView = (ImageView) itemView.findViewById(R.id.imageIcon1);
        }

    }
}