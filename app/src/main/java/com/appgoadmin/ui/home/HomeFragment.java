package com.appgoadmin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.appgoadmin.R;
import com.appgoadmin.models.pedidosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {
    private FirebaseFirestore db;
    private CardView cardTodos;
    private TextView textTodos, textCancelados, textEntregados, textPendientes, textEncamino;
    private int totalCount, cancelCount, completeCount, standbyCount, encaminoCount;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        cardTodos = root.findViewById(R.id.cardPedidos);
        textTodos = root.findViewById(R.id.textTodosCount);
        textEntregados = root.findViewById(R.id.textentregados);
        textCancelados = root.findViewById(R.id.textcancelados);
        textPendientes = root.findViewById(R.id.textpendientes);
        textEncamino = root.findViewById(R.id.textencamino);
        getPedidosData();
        return root;
    }

    public void getPedidosData() {
        totalCount = 0;
        standbyCount = 0;
        completeCount = 0;
        cancelCount = 0;
        encaminoCount = 0;

        db.collection("pedidos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            boolean cancel = doc.getBoolean("cancelado");
                            boolean entregado = doc.getBoolean("entregado");
                            boolean encamino = doc.getBoolean("encamino");
                            totalCount += 1;
                            if (encamino && !cancel && !entregado){
                                encaminoCount += 1;
                            }
                            if (!entregado && !cancel && !encamino){
                                standbyCount += 1;
                            }
                            if (cancel && !entregado && !encamino) {
                                cancelCount += 1;
                            }
                            if (entregado && !cancel && !encamino) {
                                completeCount += 1;
                            }
                        }
                        textTodos.setText(String.valueOf(totalCount));
                        textCancelados.setText(String.valueOf(cancelCount));
                        textPendientes.setText(String.valueOf(standbyCount));
                        textEntregados.setText(String.valueOf(completeCount));
                        textEncamino.setText(String.valueOf(encaminoCount));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}