package com.hoperaiser.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.CoreProtocolPNames;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
//import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.google.android.play.core.review.ReviewInfo;
//import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;


public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SelectListner{
//    private static final String HI = "https://script.google.com/macros/s/AKfycbzqIzm-tKVNZJqAQn4HgXjawlosuEvdfDlZHBxaruy5Y7kn6MCnsJDFCx5GKIHGuzMz/exec";
    private static final String HI = "https://script.google.com/macros/s/AKfycbym1MZMM7FPtrmCTrSHiTvX6pNIhw16he1TaqS_t9HwQ4tIIIA1hBMWlvgyXCquFlvrPw/exec";
    private RecyclerView rv;
    private List<UserModal>list_data;
    private MyAdapter adapter;
    String title, date, longi, lat,landmark,time,doc_name,hos_name,number,d,label6,label7,label8,label9,label10,label11,label12,label13;
    EditText searchview;
    ImageView refresh;
    private ProgressDialog pDialog,PDialog2;
    boolean connected=false;
    String shared_lat,shared_long;
    ReviewInfo reviewInfo;
    ReviewManager manager;
    double longitude,latitude;
    Database database;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView i1;
    Toolbar toolbar;
    int km;
    // Location
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView area;
    double latitude_get,longitude_get;
    private AppUpdateManager mAppUpdateManager;
    private int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;
    AlertDialog.Builder adb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        area=(TextView)findViewById(R.id.textView3);
        refresh=(ImageView)findViewById(R.id.refresh);
        adb=new AlertDialog.Builder(this);
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();
        //lambda operation used for below listener
        //For flexible update
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        inAppUpdateType = AppUpdateType.IMMEDIATE; //1
        inAppUpdate();

        activateReviewInfo();
        rv=(RecyclerView)findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        if(list_data== null)
            list_data=new ArrayList<>();
        adapter=new MyAdapter(list_data,this,this);
        pDialog = new ProgressDialog(SearchActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        PDialog2=new ProgressDialog(SearchActivity.this);
        pDialog.setMessage("Loading data...");
        pDialog.setCancelable(false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED ){
            pDialog.show();

            getLastLocation();

            getData();



        }else{
            localData();

        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED ) {
//                    Intent i=new Intent(getApplicationContext(),SearchActivity.class);
//                    startActivity(i);
                    list_data.clear();
                    getData();
                }else{
                    list_data.clear();
localData();
//                    Toast.makeText(SearchActivity.this, "Trun On Data, Your are in offline....", Toast.LENGTH_SHORT).show();
                }
                }
        });

        //Database
        database = new Database(this);
        database.getWritableDatabase();

        i1 = findViewById(R.id.menu);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.bringToFront();

        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) SearchActivity.this);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener)this);
        navigationView.setCheckedItem(R.id.home);
        i1.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat. START));


        searchview = (EditText) findViewById(R.id.searchview);


        //Dialog
        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().trim());
            }


        });

    }



    //filter
private void filter(String toString) {
    List<UserModal> filteredlist = new ArrayList<>();
    for (UserModal news : list_data) {
        if (news.getName().toLowerCase().contains(toString.toLowerCase())) {
            filteredlist.add(news);

        }else if(news.getLandmark().toLowerCase().contains(toString.toLowerCase())){
            filteredlist.add(news);

        }

    }
    adapter.filterlist(filteredlist);
}




    //location

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {


                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location location = task.getResult();
                        if (location == null) {

                            requestNewLocationData();
                            PDialog2.dismiss();
                        } else {

                            if(list_data==null)
                            longitude_get=location.getLongitude();
                            latitude_get=location.getLatitude();

                            Geocoder geocoder=new Geocoder(SearchActivity.this, Locale.getDefault());
                            try{
                                List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                String city=address.get(0).getLocality();
                                area.setText(city);

                                longitude_get=address.get(0).getLongitude();
                                latitude_get=address.get(0).getLatitude();

                                database.insertDatalatlong("0", city,String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()));
                                database.updateDatalonglat("0", city, String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });

            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {

            requestPermissions();
        }


    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude_get= mLastLocation.getLatitude();
            longitude_get=mLastLocation.getLongitude();
            Geocoder geocoder=new Geocoder(SearchActivity.this, Locale.getDefault());
            List<Address> address= null;
            try {
                address = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(),1);
                String city=address.get(0).getLocality();
                area.setText(city);
            } catch (IOException e) {
                e.printStackTrace();
            }


            getData();
        }
    };
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            }
        }
    }


    // Fetch Data

    private void getData() {


        
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PDialog2.show();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("user");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject ob = array.getJSONObject(i);
                        title = ob.getString("name");
                        date = ob.getString("id");
                        longi = ob.getString("longi");
                        lat = ob.getString("lat");
                        landmark=ob.getString("landmark");
                        doc_name=ob.getString("doc_name");
                        number=ob.getString("number");
                        label6=ob.getString("label6");
                        label7=ob.getString("label7");
                        label8=ob.getString("label8");
                        label9=ob.getString("label9");
                        label10=ob.getString("label10");
                        label11=ob.getString("label11");
                        label12=ob.getString("label12");
                        label13=ob.getString("label13");
                        if(title.equals("Name")){
                            database.insertData("0", title, longi, lat, landmark, number, doc_name, "0",label6, label7, label8, label9, label10, label11, label12, label13);
                            database.updateData("0", title, longi, lat, landmark, number, doc_name, "0", label6, label7, label8, label9, label10, label11, label12, label13);

                        }else {

                            if (longi.length() > 0 && lat.length() > 0) {
                                double long1 = Double.parseDouble(longi);
                                double lat1 = Double.parseDouble(lat);

                                Location startPoint = new Location("locationA");
                                startPoint.setLatitude(latitude_get);
                                startPoint.setLongitude(longitude_get);
                                if (latitude_get == 0.0 && longitude_get == 0.0) {

                                    PDialog2.show();
                                } else {

                                    Location endPoint = new Location("locationA");
                                    endPoint.setLatitude(lat1);
                                    endPoint.setLongitude(long1);

                                    double distance = startPoint.distanceTo(endPoint);
                                    int d1 = (int) distance;
                                    int km = d1 / 1000;
                                    String distance1 = String.valueOf(km);

                                    UserModal ld = new UserModal(date, title, longi, lat, landmark, doc_name, number, distance1, label6, label7, label8, label9, label10, label11, label12, label13);

//
                                    list_data.add(ld);
                                    Collections.sort(list_data);
//                           if( database.delete(date)){
////                               Toast.makeText(SearchActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
//                           }

                                    database.insertData(date, title, longi, lat, landmark, number, doc_name, distance1, label6, label7, label8, label9, label10, label11, label12, label13);
                                    database.updateData(date, title, longi, lat, landmark, number, doc_name, distance1, label6, label7, label8, label9, label10, label11, label12, label13);

                                }

                            } else {

                            }
                        }
                    }
//                    Cursor cursor=new Database(SearchActivity.this).fetchData();
//
//                    while(cursor.moveToNext()){
//                        double lat1 = Double.parseDouble(cursor.getString(3));
//
//
//                        UserModal  obj=new UserModal(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(6),cursor.getString(5),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13), cursor.getString(14),cursor.getString(15));
//
//                        list_data.add(obj);
//                        Collections.sort(list_data);
//
//                    }
                    rv.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                    PDialog2.dismiss();
                    if (pDialog.isShowing())
                        pDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void localData(){

        Cursor cursor1=new Database(SearchActivity.this).fetchlocalData();
        Cursor cursor=new Database(SearchActivity.this).fetchData();

//        while(cursor1.moveToNext()){
//
//            longitude = Double.parseDouble(cursor1.getString(2));
//            latitude = Double.parseDouble(  cursor1.getString(3));
//            area.setText(cursor1.getString(1));
//
//        }

        while(cursor.moveToNext()){

    UserModal obj = new UserModal(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(6), cursor.getString(5), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15));


    list_data.add(obj);
    Collections.sort(list_data);

        }

        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
            Intent i=new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(i);
                break;
            case R.id.about:
                Intent i2=new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i2);
                break;

            case R.id.feedback:
                startReviewFlow();
                break;

            case R.id.share:
                Intent i1=new Intent(Intent.ACTION_SEND);
                i1.setType("text/plain");
                String body="Hi, I invite you to check out the awesome FINDACUHEALERS app , ** Play Store App Url **";
                i1.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(i1,"Share via"));
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    @Override
    public void onItemClicked(UserModal userModal) {
        Intent i=new Intent(getApplicationContext(),DeatilsActivity.class);
        i.putExtra("name",userModal.getName());
        i.putExtra("longi",userModal.getLongi());
        i.putExtra("lat",userModal.getLat());
        i.putExtra("landmark",userModal.getLandmark());
//        i.putExtra("time",userModal.getTime());
        i.putExtra("doc_name",userModal.getDoc_name());
//        i.putExtra("hos_name",userModal.hos_name);
        i.putExtra("number",userModal.getNumber());
        i.putExtra("distance",userModal.getId());
        i.putExtra("label6",userModal.getLabel6());
        i.putExtra("label7",userModal.getLabel7());
        i.putExtra("label8",userModal.getLabel8());
        i.putExtra("label9",userModal.getLabel9());
        i.putExtra("label10",userModal.getLabel10());
        i.putExtra("label11",userModal.getLabel11());
        i.putExtra("label12",userModal.getLabel12());
        i.putExtra("label13",userModal.getLabel13());
        startActivity(i);

    }
    @Override
    public void onBackPressed () {
//        super.onBackPressed();
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("Exit App");
        adb.setMessage("Are you sure,You want to exit");
        adb.setCancelable(false);
        adb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in=new Intent(Intent.ACTION_MAIN);
                in.addCategory(Intent.CATEGORY_HOME);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SearchActivity.this, "You clicked over No", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SearchActivity.this, "You clicked over cancel", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog ad=adb.create();
        ad.show();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }






    }
    void activateReviewInfo(){
        manager=ReviewManagerFactory.create(this);
       com.google.android.play.core.tasks.Task<ReviewInfo> managerInfoTask=manager.requestReviewFlow();
       managerInfoTask.addOnCompleteListener((task)->
               {
                  if(task.isSuccessful()){
                      reviewInfo=task.getResult();
                  }
                  else{
                      Toast.makeText(this, "failed to start", Toast.LENGTH_SHORT).show();
                  }
               });
    }
void startReviewFlow(){
        if(reviewInfo!=null){
            com.google.android.play.core.tasks.Task<Void> flow=manager.launchReviewFlow(this,reviewInfo);
            flow.addOnCompleteListener(task->
                    {
                        Toast.makeText(this, "Rating is completed", Toast.LENGTH_SHORT).show();
                    });
        }
}
    @Override
    protected void onDestroy() {
        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        SharedPreferences sh=getSharedPreferences("myshared",MODE_PRIVATE);
         shared_lat=sh.getString("lat","");
        shared_long=sh.getString("long","");

        if (checkPermissions()) {
            getLastLocation();
        }
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() ==
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                inAppUpdateType,
                                this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });


            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                //For flexible update
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences sh=getSharedPreferences("myshared",MODE_PRIVATE);
        SharedPreferences.Editor myedit=sh.edit();
        myedit.putString("lat", String.valueOf(latitude_get));
        myedit.putString("long", String.valueOf(longitude_get));
        myedit.apply();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == RESULT_OK) {
                Toast.makeText(SearchActivity.this, "App download starts...", Toast.LENGTH_LONG).show();
            } else if (resultCode != RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(SearchActivity.this, "App download canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(SearchActivity.this, "App download failed.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void inAppUpdate() {

        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            // For a flexible update, use AppUpdateType.FLEXIBLE
                            && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)) {
                        // Request the update.

                        try {
                            mAppUpdateManager.startUpdateFlowForResult(
                                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                    appUpdateInfo,
                                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                    inAppUpdateType,
                                    // The current activity making the update request.
                                    SearchActivity.this,
                                    // Include a request code to later monitor this update request.
                                    RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(R.id.drawer_layout),
                            "An update has just been downloaded.\nRestart to update",
                            Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("INSTALL", view -> {
                if (mAppUpdateManager != null){
                    mAppUpdateManager.completeUpdate();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.install_color));
            snackbar.show();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

}