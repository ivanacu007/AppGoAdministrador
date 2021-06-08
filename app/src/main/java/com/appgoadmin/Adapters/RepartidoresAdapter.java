


package com.appgoadmin.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appgoadmin.R;
import com.appgoadmin.activities.AsignarFinal;
import com.appgoadmin.activities.AsignarPedido;
import com.appgoadmin.activities.DetallePedido;
import com.appgoadmin.activities.PedidosActivity;
import com.appgoadmin.models.repartidoresModel;

import java.util.List;

public class RepartidoresAdapter extends RecyclerView.Adapter<RepartidoresViewHolder> {
    public AsignarPedido asignarPedido;
    public List<repartidoresModel> repartidoresModelList;
    public String pedidoID;

    public RepartidoresAdapter(AsignarPedido asignarPedido, List<repartidoresModel> repartidoresModelList, String pedidoID) {
        this.asignarPedido = asignarPedido;
        this.repartidoresModelList = repartidoresModelList;
        this.pedidoID = pedidoID;
    }

    @NonNull
    @Override
    public RepartidoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivers_design, parent, false);
        RepartidoresViewHolder view = new RepartidoresViewHolder(itemView);
        view.setOnClickListener(new RepartidoresViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = repartidoresModelList.get(position).getId();
                Toast.makeText(asignarPedido.getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(asignarPedido, AsignarFinal.class);
                i.putExtra("ID", id);
                i.putExtra("PID", pedidoID);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                asignarPedido.startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull RepartidoresViewHolder holder, int position) {
        holder.repName.setText(repartidoresModelList.get(position).getNombre());
        int x = repartidoresModelList.get(position).getEncurso();
        if (repartidoresModelList.get(position).getEncurso() != 0){
            holder.repStatus.setText("En ruta");
            holder.repStatus.setTextColor(Color.parseColor("#3f51b5"));
        }
        else{
            holder.repStatus.setText("Disponible");
            holder.repStatus.setTextColor(Color.parseColor("#4caf50"));
        }

    }

    @Override
    public int getItemCount() {
        return repartidoresModelList.size();
    }
}
