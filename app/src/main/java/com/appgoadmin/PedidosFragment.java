package com.appgoadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.appgoadmin.activities.PedidosActivity;
import com.appgoadmin.models.pedidosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.appgoadmin.Adapters.PedidosAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class PedidosFragment extends Fragment {


    private FirebaseFirestore db;
    private RecyclerView mRec;
    private PedidosAdapter pedidosAdapter;
    private List<pedidosModel> pedidosModelList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textPedidos;
    private int optionFrom = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_pedidos, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
//
//        mRec = root.findViewById(R.id.mRecy);
//        textPedidos = findViewById(R.id.textNoPedidos);
//        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        //assert getArguments() != null;
        if (getArguments() != null) {
            optionFrom = getArguments().getInt("option", 0);
        }
        return root;
    }
//
//    public void initRecyclerView() {
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
//        mRec.setLayoutManager(gridLayoutManager);
//        mRec.setItemAnimator(new DefaultItemAnimator());
//    }
//
//    public void getPedidos(int option) {
//        Query query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING);
//
//        mRec.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        initRecyclerView();
//        pedidosModelList.clear();
//
//        if (option == 1) {
//            query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING);
//        }
//        if (option == 2) {
//            query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.ASCENDING);
//        }
//
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (DocumentSnapshot doc : task.getResult()) {
//                            Timestamp dateOrder = doc.getTimestamp("fecha_orden");
//                            Date fecha = dateOrder.toDate();
//                            String fechaaux = getDate(fecha);
//
//                            pedidosModel model = new pedidosModel(
//                                    doc.getString("id"),
//                                    fechaaux,
//                                    doc.getString("direccion"),
//                                    doc.getString("user_name"),
//                                    doc.getString("user_phone"),
//                                    Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
//                                    Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
//                                    doc.getBoolean("entregado"),
//                                    doc.getBoolean("cancelado"),
//                                    doc.getBoolean("encamino")
//                            );
//                            pedidosModelList.add(model);
//                            pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
//                            pedidosAdapter.notifyDataSetChanged();
//                            mRec.setAdapter(pedidosAdapter);
//                            recyclerViewAnimation(mRec);
//                        }
//                        if (pedidosModelList.size() > 0) {
//                            progressBar.setVisibility(View.GONE);
//                            textPedidos.setVisibility(View.GONE);
//                            mRec.setVisibility(View.VISIBLE);
//                            saveComprasPreferences();
//                        } else {
//                            textPedidos.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), "No hay pedidos", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            mRec.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressBar.setVisibility(View.GONE);
//                        mRec.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void getPedidosEstatus(int option) {
//        textPedidos.setVisibility(View.GONE);
//        mRec.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        initRecyclerView();
//        pedidosModelList.clear();
//
//        db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (DocumentSnapshot doc : task.getResult()) {
//                            if (option == 1) {
//                                if (doc.getBoolean("entregado")) {
//                                    Timestamp dateOrder = doc.getTimestamp("fecha_orden");
//                                    Date fecha = dateOrder.toDate();
//                                    String fechaaux = getDate(fecha);
//
//                                    pedidosModel model = new pedidosModel(
//                                            doc.getString("id"),
//                                            fechaaux,
//                                            doc.getString("direccion"),
//                                            doc.getString("user_name"),
//                                            doc.getString("user_phone"),
//                                            Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
//                                            Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
//                                            doc.getBoolean("entregado"),
//                                            doc.getBoolean("cancelado"),
//                                            doc.getBoolean("encamino")
//                                    );
//                                    pedidosModelList.add(model);
//                                    pedidosAdapter = new PedidosAdapter(pedidosModelList, getActivity());
//                                    pedidosAdapter.notifyDataSetChanged();
//                                    mRec.setAdapter(pedidosAdapter);
//                                    recyclerViewAnimation(mRec);
//                                }
//                            }
//                            if (option == 2) {
//                                if (doc.getBoolean("encamino")) {
//                                    Timestamp dateOrder = doc.getTimestamp("fecha_orden");
//                                    Date fecha = dateOrder.toDate();
//                                    String fechaaux = getDate(fecha);
//
//                                    pedidosModel model = new pedidosModel(
//                                            doc.getString("id"),
//                                            fechaaux,
//                                            doc.getString("direccion"),
//                                            doc.getString("user_name"),
//                                            doc.getString("user_phone"),
//                                            Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
//                                            Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
//                                            doc.getBoolean("entregado"),
//                                            doc.getBoolean("cancelado"),
//                                            doc.getBoolean("encamino")
//                                    );
//                                    pedidosModelList.add(model);
//                                    pedidosAdapter = new PedidosAdapter(pedidosModelList, getActivity());
//                                    pedidosAdapter.notifyDataSetChanged();
//                                    mRec.setAdapter(pedidosAdapter);
//                                    recyclerViewAnimation(mRec);
//                                }
//                            }
//                            if (option == 3) {
//                                if (!doc.getBoolean("encamino") && !doc.getBoolean("entregado") && !doc.getBoolean("cancelado")) {
//                                    Timestamp dateOrder = doc.getTimestamp("fecha_orden");
//                                    Date fecha = dateOrder.toDate();
//                                    String fechaaux = getDate(fecha);
//
//                                    pedidosModel model = new pedidosModel(
//                                            doc.getString("id"),
//                                            fechaaux,
//                                            doc.getString("direccion"),
//                                            doc.getString("user_name"),
//                                            doc.getString("user_phone"),
//                                            Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
//                                            Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
//                                            doc.getBoolean("entregado"),
//                                            doc.getBoolean("cancelado"),
//                                            doc.getBoolean("encamino")
//                                    );
//                                    pedidosModelList.add(model);
//                                    pedidosAdapter = new PedidosAdapter(pedidosModelList, getActivity());
//                                    pedidosAdapter.notifyDataSetChanged();
//                                    mRec.setAdapter(pedidosAdapter);
//                                    recyclerViewAnimation(mRec);
//                                }
//                            }
//                            if (option == 4) {
//                                if (doc.getBoolean("cancelado")) {
//                                    Timestamp dateOrder = doc.getTimestamp("fecha_orden");
//                                    Date fecha = dateOrder.toDate();
//                                    String fechaaux = getDate(fecha);
//
//                                    pedidosModel model = new pedidosModel(
//                                            doc.getString("id"),
//                                            fechaaux,
//                                            doc.getString("direccion"),
//                                            doc.getString("user_name"),
//                                            doc.getString("user_phone"),
//                                            Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
//                                            Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
//                                            doc.getBoolean("entregado"),
//                                            doc.getBoolean("cancelado"),
//                                            doc.getBoolean("encamino")
//                                    );
//                                    pedidosModelList.add(model);
//                                    pedidosAdapter = new PedidosAdapter(pedidosModelList, getActivity());
//                                    pedidosAdapter.notifyDataSetChanged();
//                                    mRec.setAdapter(pedidosAdapter);
//                                    recyclerViewAnimation(mRec);
//                                }
//                            }
//                        }
//                        if (pedidosModelList.size() > 0) {
//                            textPedidos.setVisibility(View.GONE);
//                            progressBar.setVisibility(View.GONE);
//                            mRec.setVisibility(View.VISIBLE);
//                            saveComprasPreferences();
//                        } else {
//                            textPedidos.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), "No hay pedidos", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            mRec.setVisibility(View.GONE);
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        textPedidos.setVisibility(View.GONE);
//                        progressBar.setVisibility(View.GONE);
//                        mRec.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    public void saveComprasPreferences() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("comprasdata", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(pedidosModelList);
//        editor.putString("mylistcompras", json);
//        editor.apply();
//    }
//
//    private String getDate(Date date) {
//        String dateFormated = DateFormat.format("dd/MM/yyyy", date).toString();
//        return dateFormated;
//    }
//
//    private void recyclerViewAnimation(RecyclerView recyclerView) {
//        Context context = recyclerView.getContext();
//        LayoutAnimationController layoutAnimationController = AnimationUtils
//                .loadLayoutAnimation(context, R.anim.layout_slide_right);
//        recyclerView.setLayoutAnimation(layoutAnimationController);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main, menu);
//        final MenuItem item = menu.findItem(R.id.search_cat_product);
//
//        SearchView search = (SearchView) item.getActionView();
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String tags) {
//                searchData(tags);
//                return true;
//            }
//        });
//    }
//
//    public void searchData(String tag) {
//        ArrayList<pedidosModel> modelArrayList = new ArrayList<>();
//        for (pedidosModel obj : pedidosModelList) {
//            if (obj.getId().toLowerCase().contains(tag.toLowerCase())) {
//                modelArrayList.add(obj);
//            }
//        }
//        pedidosAdapter = new PedidosAdapter(modelArrayList, getActivity());
//        pedidosAdapter.notifyDataSetChanged();
//        mRec.setAdapter(pedidosAdapter);
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_date_desc:
//                getPedidos(1);
//                return true;
//
//            case R.id.action_date_asc:
//                getPedidos(2);
//                return true;
//
//            case R.id.action_complete:
//                getPedidosEstatus(1);
//                return true;
//
//            case R.id.action_onroad:
//                getPedidosEstatus(2);
//                return true;
//
//            case R.id.action_pendientes:
//                getPedidosEstatus(3);
//                return true;
//            case R.id.action_canceled:
//                getPedidosEstatus(4);
//                return true;
//
//            default:
//                break;
//        }
//        return false;
//    }
//
////    public void refreshData() {
////        db.collection("pedidos").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
////            @Override
////            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
////                if (e != null) {
////                    Toast.makeText(getActivity(), "Error getting data", Toast.LENGTH_SHORT).show();
////                    return;
////                }
////                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
////                    int oldIndex = dc.getOldIndex();
////                    int newIndex = dc.getNewIndex();
////                    int size = queryDocumentSnapshots.getDocumentChanges().size();
////                    switch (dc.getType()) {
////                        case ADDED:
////                            if (size == 1) {
////                                pdString = "Nuevos datos agregados";
////                                modelList.clear();
////                                getAnuncios(collectionReference);
////                                pd.dismiss();
////                                Toast.makeText(MainActivity.this, "Documento agregado", Toast.LENGTH_SHORT).show();
////                            }
////                            break;
////                        case MODIFIED:
////                            pdString = "Actualizando datos...";
////                            modelList.clear();
////                            if (oldIndex == newIndex) {
////                                getAnuncios(collectionReference);
////                                pd.dismiss();
////                            }
////                            Toast.makeText(MainActivity.this, "Documentos actualizados", Toast.LENGTH_SHORT).show();
////                            break;
////                        case REMOVED:
////                            modelList.clear();
////                            getAnuncios(collectionReference);
////                            pd.dismiss();
////                            Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
////                            break;
////                    }
////                }
////            }
////        });
////    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (optionFrom == 0) {
//            getPedidos(1);
//        } else {
//            if (optionFrom == 1) {
//                getPedidosEstatus(optionFrom);
//            }
//            if (optionFrom == 2) {
//                getPedidosEstatus(optionFrom);
//            }
//            if (optionFrom == 3) {
//                getPedidosEstatus(optionFrom);
//            }
//            if (optionFrom == 4) {
//                getPedidosEstatus(optionFrom);
//            }
//        }
//    }
}