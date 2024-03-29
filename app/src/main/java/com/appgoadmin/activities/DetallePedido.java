package com.appgoadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appgoadmin.Adapters.ResumeAdapter;
import com.appgoadmin.R;
import com.appgoadmin.models.miscomprasModel;
import com.appgoadmin.models.pedidosModel;
import com.appgoadmin.models.resumeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetallePedido extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<resumeModel> resumeModelList = new ArrayList<>();
    private List<pedidosModel> miscomprasModelList = new ArrayList<>();
    private TextView textCantidadProd, textMontoTotal,
            textMontoXCantidad, textDirection, textEnvio, textcodedate,
            textRecibe, textNumber, textEntregado, textCancelado, textrepartidor;
    private String id;
    private RecyclerView mRec;
    private ResumeAdapter adapter;
    private ImageView resimg;
    private LinearLayout linearEstatus;
    private Switch swCancel, swEntrega, swEncamino;
    private FloatingActionButton fab;
    private String pedidoCode, direccion, nombre, telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        textrepartidor = findViewById(R.id.textrepartidor);
        textRecibe = findViewById(R.id.textuserrecibe);
        textCancelado = findViewById(R.id.textcancelado);
        textNumber = findViewById(R.id.textrecibephone);
        textcodedate = findViewById(R.id.textcodedate);
        textCantidadProd = findViewById(R.id.textcantidaddetalle);
        textMontoXCantidad = findViewById(R.id.textmontoporproductos);
        textEnvio = findViewById(R.id.textenvio);
        textMontoTotal = findViewById(R.id.textmontototal);
        textDirection = findViewById(R.id.textdirCompra);
        swCancel = findViewById(R.id.switchCancel);
        swEntrega = findViewById(R.id.switchEstado);
        swEncamino = findViewById(R.id.switchEncamino);
        mRec = findViewById(R.id.recyResume);
        fab = findViewById(R.id.fabShare);
        fab.setVisibility(View.GONE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEstatus();
                //startActivity(new Intent(DetallePedido.this, AsignarPedido.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                //openWhatsApp();
            }
        });

        swEncamino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isEntregado = false, isCancel = false;
                fab.setEnabled(false);
                if (isChecked) {
                    swEntrega.setChecked(false);
                    swCancel.setChecked(false);
                    isCancel = swCancel.isChecked();
                    isEntregado = swEntrega.isChecked();
                    updatePedido(isEntregado, isCancel, true);
                } else {
                    isCancel = swCancel.isChecked();
                    isEntregado = swEntrega.isChecked();
                    updatePedido(isEntregado, isCancel, false);
                }
            }
        });

        swEntrega.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isEncamino = false, isCancel = false;
                fab.setEnabled(false);
                if (isChecked) {
                    swEncamino.setChecked(false);
                    swCancel.setChecked(false);
                    isCancel = swCancel.isChecked();
                    isEncamino = swEncamino.isChecked();
                    updatePedido(true, isCancel, isEncamino);
                } else {
                    isCancel = swCancel.isChecked();
                    isEncamino = swEncamino.isChecked();
                    updatePedido(false, isCancel, isEncamino);
                }
            }
        });

        swCancel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isEncamino = false, isEntregado = false;
                fab.setEnabled(false);
                if (isChecked) {
                    swEntrega.setChecked(false);
                    swEncamino.setChecked(false);
                    isEncamino = swEncamino.isChecked();
                    isEntregado = swEntrega.isChecked();
                    updatePedido(isEntregado, true, isEncamino);
                } else {
                    isEncamino = swEncamino.isChecked();
                    isEntregado = swEntrega.isChecked();
                    updatePedido(isEntregado, false, isEncamino);
                }
            }
        });
    }

    public void getComprasData(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("comprasdata", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("mylistcompras", null);
        Type type = new TypeToken<ArrayList<pedidosModel>>() {
        }.getType();
        miscomprasModelList = gson.fromJson(json, type);

        if (miscomprasModelList == null) {
            miscomprasModelList = new ArrayList<>();
        }

        for (int i = 0; i < miscomprasModelList.size(); i++) {

            if (miscomprasModelList.get(i).getId().equals(id)) {
                if (miscomprasModelList.get(i).getTotalProductos() > 1) {
                    textCantidadProd.setText("Pago de " + miscomprasModelList.get(i).getTotalProductos() + " productos");
                } else {
                    textCantidadProd.setText("Producto");
                }
                boolean entregado = miscomprasModelList.get(i).isEntregado();
                boolean cancelado = miscomprasModelList.get(i).isCancelado();
                boolean encamino = miscomprasModelList.get(i).isEncamino();

                if (encamino) {
                    swEncamino.setChecked(true);
                }
                if (entregado) {
                    swEntrega.setChecked(true);
                }
                if (cancelado) {
                    swCancel.setChecked(true);
                }
                String repname = miscomprasModelList.get(i).getRepartidorName();
                if (!TextUtils.isEmpty(repname)) {
                    textrepartidor.setText(miscomprasModelList.get(i).getRepartidorName());
                } else {
                    textrepartidor.setText("Sin repartidor");
                }

                textRecibe.setText("Recibe " + miscomprasModelList.get(i).getUsername());
                textNumber.setText(miscomprasModelList.get(i).getUserphone());
                textMontoXCantidad.setText(String.format("$ %.2f", miscomprasModelList.get(i).getTotalCompra()));
                textEnvio.setText("$ 0.00");
                textMontoTotal.setText(String.format("$ %.2f", miscomprasModelList.get(i).getTotalCompra()));
                textDirection.setText(miscomprasModelList.get(i).getDireccion());
                textcodedate.setText(miscomprasModelList.get(i).getId() + " - " + miscomprasModelList.get(i).getFecha());
                pedidoCode = miscomprasModelList.get(i).getId();
                direccion = miscomprasModelList.get(i).getDireccion();
                nombre = miscomprasModelList.get(i).getUsername();
                telefono = miscomprasModelList.get(i).getUserphone();
                break;
            }
        }
    }

    public void updatePedido(boolean entregado, boolean cancel, boolean encamino) {
        swEntrega.setEnabled(false);
        swCancel.setEnabled(false);
        swEncamino.setEnabled(false);
        DocumentReference pedido = db.collection("pedidos").document(id);
        pedido.update("entregado", entregado);
        pedido.update("cancelado", cancel);
        pedido.update("encamino", encamino)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetallePedido.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//                        finish();
                        swEntrega.setEnabled(true);
                        swCancel.setEnabled(true);
                        swEncamino.setEnabled(true);
                        fab.setEnabled(true);
                        changeOption();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                swEntrega.setEnabled(true);
                swCancel.setEnabled(true);
                swEncamino.setEnabled(true);
                Toast.makeText(DetallePedido.this, "Ocurrió un error, intentalo más tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getResme() {
        initRecyclerView();
        resumeModelList.clear();
        db.collection("pedidos").document(id).collection("items")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    resumeModel model = new resumeModel(
                            doc.getString("item_name"),
                            doc.getString("item_img"),
                            Double.parseDouble(String.valueOf(doc.getLong("item_precio_base"))),
                            Integer.parseInt(String.valueOf(doc.getLong("item_cantidad")))
                    );
                    resumeModelList.add(model);
                    adapter = new ResumeAdapter(resumeModelList, DetallePedido.this);
                    adapter.notifyDataSetChanged();
                    mRec.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetallePedido.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void validateUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = mAuth.getCurrentUser().getUid();
            getUserPermission(id);
        } else {

        }
    }

    public void getUserPermission(String id) {
        db.collection("admins").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int permiso = Integer.parseInt(String.valueOf(documentSnapshot.getLong("permiso")));
                            if (permiso == 1) {
                                fab.setVisibility(View.VISIBLE);
                            } else {
                                fab.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(DetallePedido.this, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetallePedido.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRec.setLayoutManager(gridLayoutManager);
    }

    private void openWhatsApp() {
        PackageManager pm = this.getPackageManager();
        boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
        if (isInstalled) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "*Código*" + "\n" + pedidoCode +
                    "\n\n" + "*Dirección*" + "\n" + direccion +
                    "\n\n" + "*Recibe*" + "\n" + nombre +
                    "\n\n" + "*Contacto*" + "\n" + telefono);
            sendIntent.setType("text/plain");
            if (sendIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        } else {
            Toast.makeText(this, "No se encontró Whatsapp en tu dispositivo, contactanos por llamada.", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void changeOption() {
        SharedPreferences sharedPreferences = getSharedPreferences("optiondata", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("option", 0);
        editor.apply();
    }

    public void checkEstatus() {
        if (swCancel.isChecked() || swEncamino.isChecked() || swEntrega.isChecked()) {
            Toast.makeText(this, "No se puede asignar un pedido en camino, entregado o cancelado", Toast.LENGTH_SHORT).show();
        } else {
            openWhatsApp();
//            String nID = id;
//            Intent i = new Intent(DetallePedido.this, AsignarPedido.class);
//            i.putExtra("PID", nID);
//            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(i);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        id = intent.getExtras().getString("ID");
        validateUser();
        getComprasData(id);
        getResme();
    }
}