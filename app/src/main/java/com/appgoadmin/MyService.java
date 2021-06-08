package com.appgoadmin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MyService extends Service {
    private FirebaseFirestore db;
    private PendingIntent pendingIntent;
    private  MainActivity context;
    public MyService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        //Toast.makeText(this, "Servicio corriendo", Toast.LENGTH_LONG).show();
        db = FirebaseFirestore.getInstance();
        createNotificationChannel();
        refreshData();
        Intent intentNoti = new Intent(this, MainActivity.class);
        intentNoti.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intentNoti, 0);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    public void genNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "mainchannel")
                .setSmallIcon(R.drawable.ic_noti2)
                .setContentTitle("G&GO Pedidos")
                .setContentText("Hay un nuevo pedido")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(7, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("mainchannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                                        //getPedidosData();
                                        genNotification();
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
}