package Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charity.meshu.charity.R;

import java.util.ArrayList;

import Model.Posts;

/**
 * Created by meshu on 5/7/2017.
 */

public class UserPageAdapter extends RecyclerView.Adapter<UserPageAdapter.MyReclyclerViewHolder> {


    public ArrayList<Posts> allposts;
    Context mContext;

    public UserPageAdapter(ArrayList<Posts> allposts, Context mContext) {
        this.allposts = allposts;
        this.mContext = mContext;
    }

    @Override
    public UserPageAdapter.MyReclyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parition_home,parent,false);

        UserPageAdapter.MyReclyclerViewHolder vholder = new UserPageAdapter.MyReclyclerViewHolder(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(UserPageAdapter.MyReclyclerViewHolder holder, final int position) {

        holder.category.setText(allposts.get(position).tag);
        holder.title.setText(allposts.get(position).title);
        holder.desc.setText(allposts.get(position).desc);
        holder.backers.setText(allposts.get(position).backers);

        int totalFund = Integer.parseInt(allposts.get(position).fund) - Integer.parseInt(allposts.get(position).currentFund)  ;
        totalFund = (int) totalFund / 100;
        holder.funded.setText(Integer.toString(totalFund)+"%");


        holder.remainDays.setText(allposts.get(position).remainDays);
        holder.target.setText(allposts.get(position).fund);

        //Picasso.with(mContext).load(allposts.get(position).coverUri).into(holder.cover);


    }

    @Override
    public int getItemCount() {
        return allposts.size();
    }


    public static class MyReclyclerViewHolder extends RecyclerView.ViewHolder{

        TextView category,title,desc,backers,funded,remainDays,target;
        ImageView cover;


        public MyReclyclerViewHolder(View itemView) {
            super(itemView);

            category = (TextView) itemView.findViewById(R.id.category);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            backers = (TextView) itemView.findViewById(R.id.backers);
            funded = (TextView) itemView.findViewById(R.id.funded);
            remainDays = (TextView) itemView.findViewById(R.id.remainDays);
            target = (TextView) itemView.findViewById(R.id.targetFund);
            cover = (ImageView) itemView.findViewById(R.id.postPhoto);

        }

    }


}
