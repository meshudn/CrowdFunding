package Adaptor;

import android.content.Context;
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
import java.util.Date;

import Model.Payments;
import Model.Updates;

/**
 * Created by meshu on 5/8/2017.
 */

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.UpdateRecyclerViewHolder> {
    public ArrayList<Payments> paymentList;

    Context mContext;


    public PaymentHistoryAdapter(ArrayList<Payments> paymentList, Context mContext) {
        this.paymentList = paymentList;
        this.mContext = mContext;
    }

    @Override
    public PaymentHistoryAdapter.UpdateRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_single_row, parent, false);

        PaymentHistoryAdapter.UpdateRecyclerViewHolder viewHolder = new PaymentHistoryAdapter.UpdateRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentHistoryAdapter.UpdateRecyclerViewHolder holder, final int position) {
        String updateDate;
        DateFormat dateFormatPrevious = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("EEE dd yyyy h:mm a");
        try {
            Date d1 = dateFormatPrevious.parse(paymentList.get(position).getCreatedIn());
            updateDate = dateFormat.format(d1);
            Log.d("date",updateDate);

            holder.time.setText(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String str = paymentList.get(position).getPostTitle().toUpperCase();
        if(str.length() > 20)
           str = str.substring(0,20);

        holder.postTitle.setText("#"+str);
        holder.payUserName.setText(paymentList.get(position).getUserName());
        holder.paidAmount.setText("$"+paymentList.get(position).getPaidAmount());


        if(!paymentList.get(position).getUserImageUri().equals("") && paymentList.get(position).getUserImageUri() != null)
            Picasso.with(mContext).load(paymentList.get(position).getUserImageUri().toString()).into(holder.imageView);
        else
            holder.imageView.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }


    public static class UpdateRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle,payUserName,paidAmount,time;
        ImageView imageView;

        public UpdateRecyclerViewHolder(View itemView) {
            super(itemView);
            payUserName = (TextView) itemView.findViewById(R.id.payUserName);
            postTitle = (TextView) itemView.findViewById(R.id.name1);
            paidAmount = (TextView) itemView.findViewById(R.id.paidAmount);
            time = (TextView) itemView.findViewById(R.id.payTime);
            imageView = (ImageView) itemView.findViewById(R.id.imageIcon1);
        }

    }
}
