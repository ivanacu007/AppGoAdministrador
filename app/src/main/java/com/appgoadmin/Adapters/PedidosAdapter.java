package com.appgoadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appgoadmin.MainActivity;
import com.appgoadmin.R;
import com.appgoadmin.activities.DetallePedido;
import com.appgoadmin.activities.PedidosActivity;
import com.appgoadmin.models.pedidosModel;

import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosViewHolder> {

    List<pedidosModel> pedidosModelList;
    PedidosActivity context;

    public PedidosAdapter(List<pedidosModel> pedidosModelList, PedidosActivity context) {
        this.pedidosModelList = pedidosModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedidos_item_design, parent, false);
        PedidosViewHolder view = new PedidosViewHolder(itemView);
        view.setOnClickListener(new PedidosViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = pedidosModelList.get(position).getId();
                Intent i = new Intent(context, DetallePedido.class);
                i.putExtra("ID", id);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidosViewHolder holder, int position) {
        holder.textCode.setText(pedidosModelList.get(position).getId());
        holder.textDate.setText(pedidosModelList.get(position).getFecha());
        if (pedidosModelList.get(position).isCancelado()) {
            holder.textEstatus.setText("Cancelado");
            holder.textEstatus.setTextColor(Color.parseColor("#f44336"));
        }
        if (pedidosModelList.get(position).isEncamino()) {
            holder.textEstatus.setText("En camino");
            holder.textEstatus.setTextColor(Color.parseColor("#3f51b5"));
        }
        if (pedidosModelList.get(position).isEntregado()) {
            holder.textEstatus.setText("Entregado");
            holder.textEstatus.setTextColor(Color.parseColor("#4caf50"));
        }
        if (!pedidosModelList.get(position).isCancelado() && !pedidosModelList.get(position).isEncamino() && !pedidosModelList.get(position).isEntregado()) {
            holder.textEstatus.setText("Pendiente");
            holder.textEstatus.setTextColor(Color.parseColor("#424242"));
        }
    }

    @Override
    public int getItemCount() {
        return pedidosModelList.size();
    }
}
