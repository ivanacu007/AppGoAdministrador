package com.appgoadmin.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appgoadmin.R;

public class PedidosViewHolder extends RecyclerView.ViewHolder {
    TextView textCode, textDate, textEstatus;
    public PedidosViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        textCode = itemView.findViewById(R.id.pedidoID);
        textDate = itemView.findViewById(R.id.pedidoDate);
        textEstatus = itemView.findViewById(R.id.pedidoEstatus);
    }

    private PedidosViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnClickListener(PedidosViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
