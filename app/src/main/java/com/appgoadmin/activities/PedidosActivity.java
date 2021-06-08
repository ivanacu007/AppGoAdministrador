package com.appgoadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.appgoadmin.Adapters.PedidosAdapter;
import com.appgoadmin.R;
import com.appgoadmin.models.pedidosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView mRec;
    private PedidosAdapter pedidosAdapter;
    private List<pedidosModel> pedidosModelList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textPedidos;
    private int optionFrom = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Pedidos");
        mRec = findViewById(R.id.mRecy);
        textPedidos = findViewById(R.id.textNoPedidos);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        //assert getArguments() != null;
    }

    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PedidosActivity.this, 1);
        mRec.setLayoutManager(gridLayoutManager);
        mRec.setItemAnimator(new DefaultItemAnimator());
    }

    public void getPedidos(int option) {
        Query query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING).limit(100);

        mRec.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
        pedidosModelList.clear();

        if (option == 1) {
            query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING).limit(100);
        }
        if (option == 2) {
            query = db.collection("pedidos").orderBy("fecha_orden", Query.Direction.ASCENDING).limit(100);
        }

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.getBoolean("cancelado") != null && doc.getBoolean("entregado") != null && doc.getBoolean("encamino") != null) {
                                Timestamp dateOrder = doc.getTimestamp("fecha_orden");
                                Date fecha = dateOrder.toDate();
                                String fechaaux = getDate(fecha);

                                pedidosModel model = new pedidosModel(
                                        doc.getString("id"),
                                        fechaaux,
                                        doc.getString("direccion"),
                                        doc.getString("user_name"),
                                        doc.getString("user_phone"),
                                        Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
                                        Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
                                        doc.getBoolean("entregado"),
                                        doc.getBoolean("cancelado"),
                                        doc.getBoolean("encamino"),
                                        doc.getString("repID"),
                                        doc.getString("repName")
                                );
                                pedidosModelList.add(model);
                                pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
                                pedidosAdapter.notifyDataSetChanged();
                                mRec.setAdapter(pedidosAdapter);
                                recyclerViewAnimation(mRec);
                            }
                            if (pedidosModelList.size() > 0) {
                                progressBar.setVisibility(View.GONE);
                                textPedidos.setVisibility(View.GONE);
                                mRec.setVisibility(View.VISIBLE);
                                saveComprasPreferences();
                            } else {
                                textPedidos.setVisibility(View.VISIBLE);
                                //Toast.makeText(PedidosActivity.this, "No hay pedidos", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                mRec.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        mRec.setVisibility(View.VISIBLE);
                        Toast.makeText(PedidosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getPedidosEstatus(int option) {
        textPedidos.setVisibility(View.GONE);
        mRec.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
        pedidosModelList.clear();

        db.collection("pedidos").orderBy("fecha_orden", Query.Direction.DESCENDING).limit(100).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.getBoolean("cancelado") != null && doc.getBoolean("entregado") != null && doc.getBoolean("encamino") != null) {
                                if (option == 1) {
                                    if (doc.getBoolean("entregado")) {
                                        Timestamp dateOrder = doc.getTimestamp("fecha_orden");
                                        Date fecha = dateOrder.toDate();
                                        String fechaaux = getDate(fecha);

                                        pedidosModel model = new pedidosModel(
                                                doc.getString("id"),
                                                fechaaux,
                                                doc.getString("direccion"),
                                                doc.getString("user_name"),
                                                doc.getString("user_phone"),
                                                Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
                                                Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
                                                doc.getBoolean("entregado"),
                                                doc.getBoolean("cancelado"),
                                                doc.getBoolean("encamino"),
                                                doc.getString("repID"),
                                                doc.getString("repName")
                                        );
                                        pedidosModelList.add(model);
                                        pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
                                        pedidosAdapter.notifyDataSetChanged();
                                        mRec.setAdapter(pedidosAdapter);
                                        recyclerViewAnimation(mRec);
                                    }
                                }
                                if (option == 2) {
                                    if (doc.getBoolean("encamino")) {
                                        Timestamp dateOrder = doc.getTimestamp("fecha_orden");
                                        Date fecha = dateOrder.toDate();
                                        String fechaaux = getDate(fecha);

                                        pedidosModel model = new pedidosModel(
                                                doc.getString("id"),
                                                fechaaux,
                                                doc.getString("direccion"),
                                                doc.getString("user_name"),
                                                doc.getString("user_phone"),
                                                Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
                                                Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
                                                doc.getBoolean("entregado"),
                                                doc.getBoolean("cancelado"),
                                                doc.getBoolean("encamino"),
                                                doc.getString("repID"),
                                                doc.getString("repName")
                                        );
                                        pedidosModelList.add(model);
                                        pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
                                        pedidosAdapter.notifyDataSetChanged();
                                        mRec.setAdapter(pedidosAdapter);
                                        recyclerViewAnimation(mRec);
                                    }
                                }
                                if (option == 3) {
                                    if (!doc.getBoolean("encamino") && !doc.getBoolean("entregado") && !doc.getBoolean("cancelado")) {
                                        Timestamp dateOrder = doc.getTimestamp("fecha_orden");
                                        Date fecha = dateOrder.toDate();
                                        String fechaaux = getDate(fecha);

                                        pedidosModel model = new pedidosModel(
                                                doc.getString("id"),
                                                fechaaux,
                                                doc.getString("direccion"),
                                                doc.getString("user_name"),
                                                doc.getString("user_phone"),
                                                Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
                                                Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
                                                doc.getBoolean("entregado"),
                                                doc.getBoolean("cancelado"),
                                                doc.getBoolean("encamino"),
                                                doc.getString("repID"),
                                                doc.getString("repName")
                                        );
                                        pedidosModelList.add(model);
                                        pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
                                        pedidosAdapter.notifyDataSetChanged();
                                        mRec.setAdapter(pedidosAdapter);
                                        recyclerViewAnimation(mRec);
                                    }
                                }
                                if (option == 4) {
                                    if (doc.getBoolean("cancelado")) {
                                        Timestamp dateOrder = doc.getTimestamp("fecha_orden");
                                        Date fecha = dateOrder.toDate();
                                        String fechaaux = getDate(fecha);

                                        pedidosModel model = new pedidosModel(
                                                doc.getString("id"),
                                                fechaaux,
                                                doc.getString("direccion"),
                                                doc.getString("user_name"),
                                                doc.getString("user_phone"),
                                                Double.parseDouble(String.valueOf(doc.getLong("monto_total"))),
                                                Integer.parseInt(String.valueOf(doc.getLong("total_prod"))),
                                                doc.getBoolean("entregado"),
                                                doc.getBoolean("cancelado"),
                                                doc.getBoolean("encamino"),
                                                doc.getString("repID"),
                                                doc.getString("repName")
                                        );
                                        pedidosModelList.add(model);
                                        pedidosAdapter = new PedidosAdapter(pedidosModelList, PedidosActivity.this);
                                        pedidosAdapter.notifyDataSetChanged();
                                        mRec.setAdapter(pedidosAdapter);
                                        recyclerViewAnimation(mRec);
                                    }
                                }
                            }
                            if (pedidosModelList.size() > 0) {
                                textPedidos.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                mRec.setVisibility(View.VISIBLE);
                                saveComprasPreferences();
                            } else {
                                textPedidos.setVisibility(View.VISIBLE);
                                //Toast.makeText(PedidosActivity.this, "No hay pedidos", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                mRec.setVisibility(View.GONE);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textPedidos.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        mRec.setVisibility(View.VISIBLE);
                        Toast.makeText(PedidosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveComprasPreferences() {
        SharedPreferences sharedPreferences = PedidosActivity.this.getSharedPreferences("comprasdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(pedidosModelList);
        editor.putString("mylistcompras", json);
        editor.apply();
    }

    private String getDate(Date date) {
        String dateFormated = DateFormat.format("dd/MM/yyyy hh:mm a", date).toString();
        return dateFormated;
    }

    private void recyclerViewAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils
                .loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void getOption() {
        SharedPreferences sharedPreferences = getSharedPreferences("optiondata", MODE_PRIVATE);
        optionFrom = sharedPreferences.getInt("option", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.search_cat_product);

        SearchView search = (SearchView) item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String tags) {
                searchData(tags);
                return true;
            }
        });
        return true;
    }

    public void searchData(String tag) {
        ArrayList<pedidosModel> modelArrayList = new ArrayList<>();
        for (pedidosModel obj : pedidosModelList) {
            if (obj.getId().toLowerCase().contains(tag.toLowerCase())) {
                modelArrayList.add(obj);
            }
        }
        pedidosAdapter = new PedidosAdapter(modelArrayList, PedidosActivity.this);
        pedidosAdapter.notifyDataSetChanged();
        mRec.setAdapter(pedidosAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date_desc:
                getPedidos(1);
                return true;

            case R.id.action_date_asc:
                getPedidos(2);
                return true;

            case R.id.action_complete:
                getPedidosEstatus(1);
                return true;

            case R.id.action_onroad:
                getPedidosEstatus(2);
                return true;

            case R.id.action_pendientes:
                getPedidosEstatus(3);
                return true;
            case R.id.action_canceled:
                getPedidosEstatus(4);
                return true;

            default:
                break;
        }
        return false;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOption();

        if (optionFrom == 0) {
            getPedidos(1);
        } else {
            if (optionFrom == 1) {
                getPedidosEstatus(optionFrom);
            }
            if (optionFrom == 2) {
                getPedidosEstatus(optionFrom);
            }
            if (optionFrom == 3) {
                getPedidosEstatus(optionFrom);
            }
            if (optionFrom == 4) {
                getPedidosEstatus(optionFrom);
            }
        }
    }
}