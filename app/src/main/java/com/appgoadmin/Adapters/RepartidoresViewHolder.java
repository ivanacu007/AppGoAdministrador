package com.appgoadmin.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appgoadmin.R;

public class RepartidoresViewHolder extends RecyclerView.ViewHolder {
    TextView repName, repStatus;
    public RepartidoresViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        repName = itemView.findViewById(R.id.delivername);
        repStatus = itemView.findViewById(R.id.deliverstatus);
    }

    private RepartidoresViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnClickListener(RepartidoresViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
