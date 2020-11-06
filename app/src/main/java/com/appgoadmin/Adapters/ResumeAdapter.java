package com.appgoadmin.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appgoadmin.R;
import com.appgoadmin.activities.DetallePedido;
import com.appgoadmin.models.resumeModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {
    List<resumeModel> modelList;
    DetallePedido context;

    public ResumeAdapter(List<resumeModel> modelList, DetallePedido context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_resume, parent, false);
        return new ResumeViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ResumeViewHolder holder, int position) {
        Picasso.get().load(modelList.get(position).getImg()).into(holder.itemImg);
        holder.itemName.setText(modelList.get(position).getNombre());
        if (modelList.get(position).getCantidad() > 1){
            holder.itemTotal.setText(String.format("$ %.2f", modelList.get(position).getPrecio()) + " x "
                    + modelList.get(position).getCantidad() + " unidades");
        }
        else{
            holder.itemTotal.setText(String.format("$ %.2f", modelList.get(position).getPrecio()) + " x "
                    + modelList.get(position).getCantidad() + " unidad");
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ResumeViewHolder extends RecyclerView.ViewHolder{
        TextView itemName, itemTotal;
        ImageView itemImg;
        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.itemImg);
            itemName = itemView.findViewById(R.id.itemname);
            itemTotal = itemView.findViewById(R.id.itemcantidad);
        }
    }
}
