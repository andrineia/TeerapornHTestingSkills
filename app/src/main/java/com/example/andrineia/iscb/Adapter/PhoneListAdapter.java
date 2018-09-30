package com.example.andrineia.iscb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrineia.iscb.Model.Phones;
import com.example.andrineia.iscb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.Holder> {
    private Activity activity;
    private ArrayList<Phones> phonesArrayList;
    private OnItemClickListener onItemClickListener;
    public PhoneListAdapter(Activity activity, ArrayList<Phones> arrayList,OnItemClickListener onItemClickListener) {
        this.activity = activity;
        this.phonesArrayList = arrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.phones_items, viewGroup, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {


        try {
            Picasso.with(activity).load(phonesArrayList.get(i).getThumbImageURL()).into(holder.imgBanner);

        } catch (Exception e) {
            e.printStackTrace();
        }
        double ratting = phonesArrayList.get(i).getRating();
        if(ratting >= 4.5){
            holder.heart.setVisibility(View.VISIBLE);
        }else {
            holder.heart.setVisibility(View.GONE);
        }
        holder.tvBannername.setText(phonesArrayList.get(i).getName());
        holder.tvBannerDesc.setText(phonesArrayList.get(i).getDescription());
        holder.tvBannerPrice.setText("Price : " + phonesArrayList.get(i).getPrice());
        holder.tvBannerRatting.setText("Ratting : " + phonesArrayList.get(i).getRating());
        holder.lnBanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return phonesArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgBanner,heart;
        private TextView tvBannername, tvBannerDesc, tvBannerPrice, tvBannerRatting;
        private LinearLayout lnBanner;

        public Holder(@NonNull View v) {
            super(v);
            lnBanner = (LinearLayout)v.findViewById(R.id.lnBanner);
            imgBanner = (ImageView) v.findViewById(R.id.imgBanner);
            tvBannername = (TextView) v.findViewById(R.id.tvBannername);
            tvBannerDesc = (TextView) v.findViewById(R.id.tvBannerDesc);
            tvBannerPrice=  (TextView)v.findViewById(R.id.tvBannerPrice);
            tvBannerRatting = (TextView)v.findViewById(R.id.tvBannerRatting);
            heart = (ImageView)v.findViewById(R.id.heart);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }

}
