package com.appgoadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appgoadmin.Adapters.PedidosAdapter;
import com.appgoadmin.Adapters.RepartidoresAdapter;
import com.appgoadmin.R;
import com.appgoadmin.models.repartidoresModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AsignarPedido extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView mRec;
    private ProgressBar progressBar;
    private List<repartidoresModel> modelList = new ArrayList<>();
    private RepartidoresAdapter adapter;
    private String pedidoID;
    int entregado, cancelado, encamino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_pedido);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Repartidores");
        db = FirebaseFirestore.getInstance();
        mRec = findViewById(R.id.deliverRec);
        progressBar = findViewById(R.id.progressDeliver);
        Intent intent = getIntent();
        pedidoID = intent.getExtras().getString("PID");
    }

    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AsignarPedido.this, 1);
        mRec.setLayoutManager(gridLayoutManager);
        mRec.setItemAnimator(new DefaultItemAnimator());
    }

    private void recyclerViewAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils
                .loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void getRepartidores() {
        initRecyclerView();
        modelList.clear();
        db.collection("repartidores").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            getPedidosData(doc.getId());
                            repartidoresModel model = new repartidoresModel(
                                    doc.getId(),
                                    doc.getString("nombre"),
                                    doc.getString("sucursal"),
                                    doc.getString("sucursalID"),
                                    0,
                                    0,
                                    encamino,
                                    0,
                                    0
                            );
                            modelList.add(model);
                            adapter = new RepartidoresAdapter(AsignarPedido.this, modelList, pedidoID);
                            adapter.notifyDataSetChanged();
                            mRec.setAdapter(adapter);
                            recyclerViewAnimation(mRec);
                        }
                        if (modelList.size() > 0) {

                            progressBar.setVisibility(View.GONE);
                            mRec.setVisibility(View.VISIBLE);
                            //saveComprasPreferences();
                        } else {
                            Toast.makeText(AsignarPedido.this, "No hay repartidores registrados", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            mRec.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AsignarPedido.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPedidosData(String id) {
        db.collection("pedidos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String repPID = doc.getString("repID");
                            if (!TextUtils.isEmpty(repPID)){
                                if (repPID.equals(id) && doc.getBoolean("encamino")){
                                    encamino += 1;
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AsignarPedido.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRepartidores();
    }
}