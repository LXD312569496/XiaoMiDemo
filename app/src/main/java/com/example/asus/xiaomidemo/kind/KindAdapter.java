package com.example.asus.xiaomidemo.kind;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.kind.more.KindDetailActivity;

import java.util.List;

/**
 * Created by asus on 2017/10/25.
 */

public class KindAdapter extends RecyclerView.Adapter<KindAdapter.KindViewHolder> {

    private List<Kind> mKindList;
    private Context mContext;
    private boolean isNotLoadBitmap = false;

    public KindAdapter(List<Kind> mKindList, boolean isLoadBitmap) {
        this.mKindList = mKindList;
        this.isNotLoadBitmap = isLoadBitmap;
    }

    @Override
    public KindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new KindViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kind, parent, false));
    }

    @Override
    public void onBindViewHolder(KindViewHolder holder, int position) {
        final Kind kind = mKindList.get(position);
        holder.tvName.setText(kind.getName());
        if (!isNotLoadBitmap) {
            Glide.with(mContext).load(kind.getIcon()).into(holder.ivIcon);
        } else {
            Log.d("test","不加载图片");
            Glide.with(mContext).load(R.drawable.ic_place_holder).into(holder.ivIcon);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KindDetailActivity.startActivity(mContext, kind.getCategory(), kind.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mKindList.size();
    }

    public static class KindViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon, ivDetail;
        TextView tvName;

        public KindViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.kind_iv_icon);
            ivDetail = (ImageView) itemView.findViewById(R.id.kind_iv_detail);
            tvName = (TextView) itemView.findViewById(R.id.kind_tv_name);
        }
    }

    public void setNotLoadBitmap(boolean isNotLoadBitmap){
        this.isNotLoadBitmap=isNotLoadBitmap;
        notifyDataSetChanged();
    }
}
