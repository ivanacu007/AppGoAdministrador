package com.appgoadmin.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.appgoadmin.MainActivity;
import com.appgoadmin.PedidosFragment;
import com.appgoadmin.R;
import com.appgoadmin.activities.PedidosActivity;
import com.appgoadmin.models.pedidosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private FirebaseFirestore db;
    private CardView cardTodos, cardEntregados, cardEncurso, cardCancelados, cardPendientes;
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
        cardEntregados = root.findViewById(R.id.cardEntregados);
        cardEncurso = root.findViewById(R.id.cardEncurso);
        cardPendientes = root.findViewById(R.id.cardPendientes);
        cardCancelados = root.findViewById(R.id.cardCancelados);
        textTodos = root.findViewById(R.id.textTodosCount);
        textEntregados = root.findViewById(R.id.textentregados);
        textCancelados = root.findViewById(R.id.textcancelados);
        textPendientes = root.findViewById(R.id.textpendientes);
        textEncamino = root.findViewById(R.id.textencamino);

        cardTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(0);
            }
        });
        cardEntregados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(1);
            }
        });
        cardEncurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(2);
            }
        });
        cardPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(3);
            }
        });
        cardCancelados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(4);
            }
        });


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
                            if (encamino && !cancel && !entregado) {
                                encaminoCount += 1;
                            }
                            if (!entregado && !cancel && !encamino) {
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

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); //Empty the old menu
        inflater.inflate(R.menu.menumain, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                return true;
            default:
                break;
        }
        return false;
    }

    public void goTo(int option) {
        saveOption(option);
        startActivity(new Intent(getActivity(), PedidosActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public void saveOption(int option) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("optiondata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("option", option);
        editor.apply();
    }

    public void refreshData() {
        db.collection("pedidos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            int oldIndex = dc.getOldIndex();
                            int newIndex = dc.getNewIndex();
                            int size = value.getDocumentChanges().size();
                            switch (dc.getType()) {
                                case ADDED:
                                    if (size == 1) {
                                        getPedidosData();
                                    }
                                    break;
                                case MODIFIED:
                                    if (oldIndex == newIndex) {
                                        //getPedidosData();
                                    }
                                    break;
                                case REMOVED:
                                    //getPedidosData();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        getPedidosData();
        refreshData();
    }
}