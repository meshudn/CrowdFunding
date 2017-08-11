package Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charity.meshu.charity.R;
import com.charity.meshu.charity.UserHome;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Posts;

/**
 * Created by meshu on 4/26/2017.
 */

public class MainActivityRecyclerView extends RecyclerView.Adapter<MainActivityRecyclerView.MyReclyclerViewHolder> {


    public ArrayList<Posts> allposts;
    Context mContext;
    int editing=0;

    public MainActivityRecyclerView(ArrayList<Posts> allposts, Context mContext) {
        this.allposts = allposts;
        this.mContext = mContext;
    }

    public MainActivityRecyclerView(ArrayList<Posts> allposts, Context mContext, int editing) {
        this.allposts = allposts;
        this.mContext = mContext;
        this.editing = editing;
    }

    @Override
    public MyReclyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parition_home,parent,false);

        MyReclyclerViewHolder vholder = new MyReclyclerViewHolder(view,mContext,allposts,editing);
        return vholder;
    }

    @Override
    public void onBindViewHolder(MyReclyclerViewHolder holder, final int position) {

        holder.category.setText(allposts.get(position).tag);
        holder.title.setText(allposts.get(position).title);

        String str = allposts.get(position).desc;
        if(str.length() > 100)
          str = str.substring(0,100);

        holder.desc.setText(str);
        holder.backers.setText(allposts.get(position).backers);



        int totalFund = 0;
        if(Integer.parseInt(allposts.get(position).currentFund) > 0){
             totalFund = Integer.parseInt(allposts.get(position).currentFund) / Integer.parseInt(allposts.get(position).fund);
        }

        holder.funded.setText(Integer.toString(totalFund)+"%");


        holder.remainDays.setText(allposts.get(position).remainDays);
        holder.target.setText("$"+allposts.get(position).fund);

        if(!allposts.get(position).coverUri.equals(""))
         Picasso.with(mContext).load(allposts.get(position).coverUri.toString()).into(holder.cover);


    }

    @Override
    public int getItemCount() {
        return allposts.size();
    }


    public static class MyReclyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView category,title,desc,backers,funded,remainDays,target;
        ImageView cover;
        ArrayList<Posts> allpostss;

        Context ctx;
        int editing;

        public MyReclyclerViewHolder(View itemView,Context ctx,ArrayList<Posts> allposts,int editing) {
            super(itemView);
            this.ctx = ctx;
            this.editing = editing;
            this.allpostss = allposts;

            itemView.setOnClickListener(this);
            category = (TextView) itemView.findViewById(R.id.category);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            backers = (TextView) itemView.findViewById(R.id.backers);
            funded = (TextView) itemView.findViewById(R.id.funded);
            remainDays = (TextView) itemView.findViewById(R.id.remainDays);
            target = (TextView) itemView.findViewById(R.id.targetFund);
            cover = (ImageView) itemView.findViewById(R.id.postPhoto);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();


            if(editing == 1){
                UserHome userhome = (UserHome) ctx;
                userhome.switchFragmentForEdit(allpostss.get(position));
            }else{
                UserHome userhome = (UserHome) ctx;
                userhome.switchFragment(allpostss.get(position));
            }

        }
    }
}
