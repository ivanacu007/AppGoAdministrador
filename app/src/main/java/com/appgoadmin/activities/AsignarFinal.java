package com.appgoadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appgoadmin.R;
import com.appgoadmin.models.pedidosAsigModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AsignarFinal extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button btnAsignar;
    private TextView textname, textCurso, textPendientes;
    private String repID, pedidoID, repName;
    private ProgressBar progressBar;
    private LinearLayout linearData;
    private List<pedidosAsigModel> pedidosAsigModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_final);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Asignar pedido");
        db = FirebaseFirestore.getInstance();
        linearData = findViewById(R.id.linearData);
        progressBar = findViewById(R.id.progressData);
        btnAsignar = findViewById(R.id.btnAsignar);
        textname = findViewById(R.id.detallename);
        textCurso = findViewById(R.id.reparEncurso);
        textPendientes = findViewById(R.id.reparPendiente);
        Intent intent = getIntent();
        repID = intent.getExtras().getString("ID");
        pedidoID = intent.getExtras().getString("PID");

        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarPedido();
            }
        });
        getRepData(repID);
    }

    public void getData(String id) {
        db.collection("pedidos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String repID = doc.getString("repID");
                            if (!TextUtils.isEmpty(repID)){
                                if (repID.equals(id)) {
                                    pedidosAsigModel model = new pedidosAsigModel(
                                            doc.getId(),
                                            doc.getString("repID"),
                                            doc.getString("repName"),
                                            0,
                                            0
                                    );
                                    pedidosAsigModelList.add(model);
                                }
                            }

                        }
                        if (pedidosAsigModelList.size() == 0) {
                            Toast.makeText(AsignarFinal.this, "Sin pedidos", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            //getRepData();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AsignarFinal.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRepData(String id) {
        db.collection("repartidores").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            repName = documentSnapshot.getString("nombre");
                            textname.setText(repName);
                            progressBar.setVisibility(View.GONE);
                            linearData.setVisibility(View.VISIBLE);
                            //setData();
                        } else {
                            Toast.makeText(AsignarFinal.this, "Error al obtener los datos del repartidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AsignarFinal.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void asignarPedido(){
        //Toast.makeText(this, pedidoID, Toast.LENGTH_SHORT).show();
        DocumentReference pedido = db.collection("pedidos").document(pedidoID);
        pedido.update("repID", repID);
        pedido.update("repName", repName);
        pedido.update("encamino", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AsignarFinal.this, "Pedido asignado", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void setData() {
        //textname.setText(pedidosAsigModelList.get(0).);
    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}