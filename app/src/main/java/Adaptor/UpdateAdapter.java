package Adaptor;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charity.meshu.charity.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Model.Comments;
import Model.Updates;

/**
 * Created by meshu on 5/8/2017.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateRecyclerViewHolder> {
    public ArrayList<Updates> updateList;

    Context mContext;


    public UpdateAdapter(ArrayList<Updates> updateList, Context mContext) {
        this.updateList = updateList;
        this.mContext = mContext;
    }

    @Override
    public UpdateRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.updatelayout, parent, false);

        UpdateRecyclerViewHolder viewHolder = new UpdateRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpdateRecyclerViewHolder holder, final int position) {
        String updateDate;
        DateFormat dateFormatPrevious = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("EEE dd yyyy h:mm a");
        try {
            Date d1 = dateFormatPrevious.parse(updateList.get(position).getCreatedIn());
            updateDate = dateFormat.format(d1);
            Log.d("date",updateDate);

            holder.updateDate.setText(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.updateNumber.setText("Update #"+position+1);

        String desc = updateList.get(position).getDesc();
        String firts_part=(String) desc.subSequence(0, desc.length() / 2);
        String second_part=(String) desc.subSequence((desc.length() / 2)+1,desc.length()-1 );

        holder.updateTitle.setText(updateList.get(position).getTitle());

        if(!firts_part.equals(""))
         holder.updateDesc1.setText(firts_part);
        else
            holder.updateDesc1.setText(desc);

        if(!second_part.equals(""))
          holder.updateDesc2.setText(second_part);


        if(!updateList.get(position).getImageUri().equals(""))
           Picasso.with(mContext).load(updateList.get(position).getImageUri().toString()).into(holder.imageView);
        else
            holder.imageView.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return updateList.size();
    }


    public static class UpdateRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView updateTitle,updateNumber,updateDate,updateDesc1,updateDesc2;
        ImageView imageView;

        public UpdateRecyclerViewHolder(View itemView) {
            super(itemView);
            updateNumber = (TextView) itemView.findViewById(R.id.updateNumber);
            updateDate = (TextView) itemView.findViewById(R.id.updateDate);
            updateTitle = (TextView) itemView.findViewById(R.id.title);
            updateDesc1 = (TextView) itemView.findViewById(R.id.updateDes);
            updateDesc2 = (TextView) itemView.findViewById(R.id.updateDes1);
            imageView = (ImageView) itemView.findViewById(R.id.updateImage);
        }

    }
}
