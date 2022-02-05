package com.hoperaiser.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SelectListner {


    private static final String HI = "https://script.google.com/macros/s/AKfycbxPVjbYL0Ao2kCnUcqvElJqoSHKuavsPJcfNqG1qHffaWFKkIuLmxWDsbxQxqNcJPcikA/exec";
    private RecyclerView rv;
    private List<UserModal> list_data;
    private MyAdapter adapter;
    String title, date, longi, lat, landmark, time, doc_name, hos_name, number, d, label6, label7, label8, label9, label10, label11, label12, label13;
    EditText searchview;
    private ProgressDialog pDialog;
    String shared_lat, shared_long;
    ReviewInfo reviewInfo;
    ReviewManager manager;
    String longitude;
    String latitude;
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
    double latitude_get, longitude_get;
    private AppUpdateManager mAppUpdateManager;
    private int RC_APP_UPDATE = 999;
    private int inAppUpdateType;
    private com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener installStateUpdatedListener;

    LocationManager locationManager;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        i1 = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) MainScreen.this);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.setCheckedItem(R.id.home);
        i1.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        searchview = (EditText) findViewById(R.id.searchview);

        rv = (RecyclerView) findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        if (list_data == null)
            list_data = new ArrayList<>();
        adapter = new MyAdapter(list_data, this, this);
        pDialog = new ProgressDialog(MainScreen.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //Database
        database = new Database(this);
        database.getWritableDatabase();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            pDialog.show();
//
            getData();

        } else {
            localData();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent i=new Intent(getApplicationContext(),MainScreen.class);
                startActivity(i);
                break;
            case R.id.about:
                Intent i2=new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i2);
                break;

            case R.id.feedback:
//                startReviewFlow();
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
        i.putExtra("doc_name",userModal.getDoc_name());
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
                Toast.makeText(MainScreen.this, "You clicked over No", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainScreen.this, "You clicked over cancel", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog ad=adb.create();
        ad.show();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }


    }

    // Fetch Data

    private void getData() {
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pDialog.show();
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

                            database.insertData(date, title, longi, lat, landmark, number, doc_name, "0",label6, label7, label8, label9, label10, label11, label12, label13);
                            database.updateData(date, title, longi, lat, landmark, number, doc_name, "0", label6, label7, label8, label9, label10, label11, label12, label13);

                            Toast.makeText(MainScreen.this, title, Toast.LENGTH_SHORT).show();
                        }else {
                            if (longi.length() > 0 && lat.length() > 0) {
                                double long1 = Double.parseDouble(longi);
                                double lat1 = Double.parseDouble(lat);

                                Location startPoint = new Location("locationA");
                                startPoint.setLatitude(latitude_get);
                                startPoint.setLongitude(longitude_get);
                                if (latitude_get == 0.0 && longitude_get == 0.0) {

                                    pDialog.show();
                                } else {

                                    Location endPoint = new Location("locationA");
                                    endPoint.setLatitude(lat1);
                                    endPoint.setLongitude(long1);

                                    double distance = startPoint.distanceTo(endPoint);
                                    int d1 = (int) distance;
                                    int km = d1 / 1000;
                                    String distance1 = String.valueOf(km);

                                    UserModal ld = new UserModal(date, title, longi, lat, landmark, doc_name, number, distance1, label6, label7, label8, label9, label10, label11, label12, label13);


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

                    pDialog.dismiss();
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


        Cursor cursor1=new Database(MainScreen.this).fetchlocalData();
        Cursor cursor=new Database(MainScreen.this).fetchData();

        while(cursor1.moveToNext()){
//            longitude = Double.parseDouble(cursor1.getString(2));
//            latitude = Double.parseDouble(  cursor1.getString(3));
            area.setText(cursor1.getString(1));


        }
        while(cursor.moveToNext()){


            UserModal  obj=new UserModal(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(6),cursor.getString(5),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13), cursor.getString(14),cursor.getString(15));

            list_data.add(obj);
            Collections.sort(list_data);

        }

        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}