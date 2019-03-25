package com.example.ukeje.parentapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final  String BASE_URL = "http://192.168.91.107:8080/";

    public LatLng location;
    public double latitude;
    public double longitude;
    boolean isRequestRunning = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //while(isRequestRunning){




        //}


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
       // location = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Child Location"));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
        mMap.animateCamera(zoom);

        for(int i = 0; i <1000; i++){

            APIServices service = RetrofitClient.getClient(BASE_URL).create(APIServices.class);

            Call<GetData> call = service.getLocation();

            call.enqueue(new Callback<GetData>() {
                @Override
                public void onResponse(Call<GetData> call, Response<GetData> response) {

                    double  latitude = response.body().getLatitude();
                    double longitude = response.body().getLongitude();
                    LatLng sydney = new LatLng(latitude, longitude);
                    // location = new LatLng(latitude,longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Child Location"));
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
                    mMap.animateCamera(zoom);

                    //isRequestRunning = false;
                    addNotification();

                }

                @Override
                public void onFailure(Call<GetData> call, Throwable t) {

                    //Toast.makeText(getApplicationContext(),"REQUEST FAILED",Toast.LENGTH_LONG).show();

                }
            });

        }

    }


    public void addNotification(){

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        long[] v = {2000,3000};


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_warning_black_24dp)
                .setContentTitle("WARNING WARNING WARNING")
                .setContentText("YOUR CHILD IS IN DANGER")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("DISTRESS ALERT BEING SENT BY YOUR CHILD"))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(uri)
                .setVibrate(v);

        Intent resultIntent = new Intent(this, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapsActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, builder.build());


    }

}
