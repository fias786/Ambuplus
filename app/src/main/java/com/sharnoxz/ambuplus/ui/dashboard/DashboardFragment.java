package com.sharnoxz.ambuplus.ui.dashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.sharnoxz.ambuplus.R;
import com.sharnoxz.ambuplus.data.GeoPointData;
import com.sharnoxz.ambuplus.data.NotificationData;
import com.sharnoxz.ambuplus.mqtt.MessagingOptions;
import com.sharnoxz.ambuplus.mqtt.MqttClientHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment implements LocationListener, OnMapReadyCallback, TileProvider {

    private DashboardViewModel dashboardViewModel;
    CardView requestAmbulance,requestBlood;

    private final String TAG = "DashboardFragment";
    public static final int PERMISSIONS_REQUEST = 2;

    double lat=0.0,lng=0.0;
    GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static String documentId;
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    GeoPoint latLng;
    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    LocationManager locationManager;
    List<LatLng> list =new ArrayList<LatLng>();
    GeoPoint geoPoint;
    LatLng latLng2;

    MqttClientHelper mqttClientHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        requestAmbulance = root.findViewById(R.id.CardView2);
        requestBlood = root.findViewById(R.id.CardView3);
        mqttClientHelper = new MqttClientHelper(getActivity().getApplicationContext());
        requestContactPermission(root);
        setMqttCallBack();
        mqttClientHelper.subscribe("Blood",0);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.GoogleMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null){
                    Log.d(TAG,"onLocationResult: location error");
                    return;
                }
                Location location = locationResult.getLastLocation();
                lat = location.getLatitude();
                lng = location.getLongitude();
                LatLng latLng = new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.flash_icon)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                GeoPointData geoPointData = new GeoPointData(geoPoint);
                try {
                    db.collection("users")
                            .whereEqualTo("userUid",firebaseAuth.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                        documentId = documentSnapshot.getId();
                                        db.collection("users")
                                                .document(documentId)
                                                .set(geoPointData, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {


                                                    }
                                                });
                                    }
                                }}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        getLocationUpdate(root);
        getLocation(root);
        addHeatMap();

        requestAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setMessage("Do you want book an Ambulance ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                assert vibrator != null;
                                vibrator.vibrate(1000);
                                Calendar e = Calendar.getInstance();
                                Date date = e.getTime();
                                db.collection("users")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                                String userPic  = documentSnapshots.get(0).get("profilePic").toString();
                                                String title = "Location : https://maps.google.com/maps?daddr="+lat+","+lng+"\nAmbulance is arriving within 5 mints";
                                                String notificationIconUrl = "https://firebasestorage.googleapis.com/v0/b/wosafe-5e1fc.appspot.com/o/notification_icon%2Fnotification_icon.png?alt=media&token=4b204bf6-9485-420b-b4c6-58a060d635a0";
                                                NotificationData notificationData = new NotificationData(userPic,notificationIconUrl,
                                                        title,date.toString(),firebaseAuth.getUid());
                                                try {
                                                    db.collection("Notification")
                                                            .add(notificationData)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    documentId = documentReference.getId();
                                                                    Log.d(TAG,documentReference.getId());
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            });
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Ambulance Booking");
                alertDialog.show();
            }
        });

        requestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setMessage("Do you want blood ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                assert vibrator != null;
                                vibrator.vibrate(1000);
                                Calendar e = Calendar.getInstance();
                                Date date = e.getTime();
                                db.collection("users")
                                        .whereEqualTo("userUid",firebaseAuth.getUid())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                                                String userPic  = documentSnapshots.get(0).get("profilePic").toString();
                                                String phone = documentSnapshots.get(0).get("phone").toString();
                                                String blood = documentSnapshots.get(0).get("pinCode").toString();
                                                String name = documentSnapshots.get(0).get("name").toString();
                                                String title = "Location : https://maps.google.com/maps?daddr="+lat+","+lng+"\nPhone : "+phone+"\nHi, I am "+name+" and I need urgent "+blood+" blood";
                                                String notificationIconUrl = "https://firebasestorage.googleapis.com/v0/b/wosafe-5e1fc.appspot.com/o/notification_icon%2Fnotification_icon.png?alt=media&token=4b204bf6-9485-420b-b4c6-58a060d635a0";
                                                NotificationData notificationData = new NotificationData(userPic,notificationIconUrl,
                                                        title,date.toString(),firebaseAuth.getUid());
                                                mqttClientHelper.publish("Blood",notificationData,0);
                                                db.collection("Notification")
                                                        .add(notificationData)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                documentId = documentReference.getId();
                                                                Log.d(TAG,documentReference.getId());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Blood Request");
                alertDialog.show();
            }
        });
        return root;
    }

    private void getLocationUpdate(View root) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ContextCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
    }

    void getLocation(View root) {
        try {
            locationManager = (LocationManager) root.getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public void requestContactPermission(View root) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                    builder.setTitle("Location");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable gps location.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
                                    , PERMISSIONS_REQUEST);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_REQUEST);
                }
            }
        }
    }

    private void setMqttCallBack(){
        mqttClientHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                String snackMsg = "Connect to host";
                Snackbar.make(getView(),snackMsg,Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }

            @Override
            public void connectionLost(Throwable cause) {
                String snackMsg = "Connect to host lost";
                Snackbar.make(getView(),snackMsg,Snackbar.LENGTH_LONG).setAction("Action",null).show();
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Debug","Message received from host "+message);
                String[] str = message.toString().split("##");
                NotificationData notificationData = new NotificationData(str[0],str[1],str[2],str[3],firebaseAuth.getUid());
                db.collection("Notification")
                        .add(notificationData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentId = documentReference.getId();
                               Log.d(TAG,documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.w("Debug","Message publish to host");
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        LatLng latLng = new LatLng(lat,lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
    }

    private  void addHeatMap(){
        try {
            db.collection("users")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                            int count =0;
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                geoPoint = (GeoPoint) documentSnapshot.get("latLng");
                                assert geoPoint != null;
                                latLng2 = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.flash_icon)));
                                Log.d(TAG,""+latLng2.latitude);
                                list.add(latLng2);
                                count = count + 1;
                            }
                            int[] colors = {
                                    Color.rgb(102,255,0),
                                    Color.rgb(255,0,0)
                            };

                            float[] startPoints = {
                                    0.2f,1f
                            };

                            Gradient gradient = new Gradient(colors,startPoints);
                            mProvider = new HeatmapTileProvider.Builder()
                                    .data(list)
                                    .gradient(gradient)
                                    .build();
                            mOverlay = mMap.addTileOverlay(new
                                    TileOverlayOptions().tileProvider(mProvider));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public Tile getTile(int i, int i1, int i2) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
