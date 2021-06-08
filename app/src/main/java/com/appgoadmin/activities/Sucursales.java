package com.appgoadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import com.appgoadmin.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sucursales extends AppCompatActivity {
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private RecyclerView mRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressSucursal);
        mRec = findViewById(R.id.recSuc);

        getSucursales();
    }

    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Sucursales.this, 1);
        mRec.setLayoutManager(gridLayoutManager);
        mRec.setItemAnimator(new DefaultItemAnimator());
    }

    public void getSucursales(){
        initRecyclerView();
    }

    private void recyclerViewAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils
                .loadLayoutAnimation(context, R.anim.layout_slide_right);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}