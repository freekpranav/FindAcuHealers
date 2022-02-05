package com.hoperaiser.location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.provider.SelfDestructiveThread;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity2 extends AppCompatActivity {

//Declaration
    private String TAG = MainActivity2.class.getSimpleName();
    Toolbar toolbar;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "https://script.google.com/macros/s/AKfycbx_9YimsfeuTkI36JdWZ-ZEagsC1pyzo08Nr5pBdU1gEezt91htl1N7rNPR2mO-IeeI/exec";
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> contactList;
    String title,date,longi,lat,landmark,time,doc_name,hos_name,number;
    String d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//Initialization
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }
//Sorting Method
    public static class mysort implements Comparator<HashMap<String, String>>{
        @Override
        public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
      int c;
      c=o1.get("longi").compareTo(o2.get("longi"));
      return c;
        }
    }
// Main Method
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity2.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject obj=new JSONObject(jsonStr);
                    JSONArray contacts = obj.getJSONArray("user");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        title = c.getString("name");
//                        date = c.getString("hos_name");
                        longi=c.getString("longi");
                        lat=c.getString("lat");
                        landmark=c.getString("landmark");
//                        time=c.getString("time");
                        doc_name=c.getString("doc_name");
//                        hos_name=c.getString("hos_name");
                        number=c.getString("number");
                        Double lat1=Double.parseDouble(lat);
                        Double long1=Double.parseDouble(longi);

                         HashMap<String, String> contact = new HashMap<>();
                            Location startPoint=new Location("locationA");
                            startPoint.setLatitude(12.8486578);
                            startPoint.setLongitude(80.1920791);

                            Location endPoint=new Location("locationA");
                            endPoint.setLatitude(lat1);
                            endPoint.setLongitude(long1);

                        double distance=startPoint.distanceTo(endPoint);
                        int d1=(int)distance;
                        int km=d1/1000;

                        d=String.valueOf(km)+" km ";




                        contact.put("name", doc_name);
                        contact.put("email", date);
                        contact.put("longi",d);
                        contactList.add(contact);
                        //Sorting
                        Collections.sort((ArrayList)contactList,new mysort());
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            adapter = new SimpleAdapter(
                    MainActivity2.this, contactList,
                    R.layout.user_rv_item, new String[]{"email", "name","longi"}, new int[]{R.id.idTVFirstName,
                    R.id.idTVLastName, R.id.idTVEmail});

            lv.setAdapter(adapter);

            // OnClick Listview
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i=new Intent(getApplicationContext(),DeatilsActivity.class);
                    i.putExtra("name",title);
                    i.putExtra("id",date);
                    i.putExtra("longi",longi);
                    i.putExtra("lat",lat);
                    i.putExtra("landmark",landmark);
//                    i.putExtra("time",time);
                    i.putExtra("doc_name",doc_name);
//                    i.putExtra("hos_name",hos_name);
                    i.putExtra("number",number);
                    i.putExtra("distance",d);
                    startActivity(i);

//                    Toast.makeText(MainActivity2.this, "Item Clicked"+title, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}
