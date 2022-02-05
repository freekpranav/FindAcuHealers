package com.hoperaiser.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Locale;

public class DeatilsActivity extends AppCompatActivity {
    String name,id,longi,lat,landmark,time,doc_name,hos_name,number,distance,label6,label7,label8,label9,label10,label11,label12,label13;
    TextView name1,id1,longi1,lat1,landmark1,time1,doc_name1,hos_name1,number1,label61,label71,label81,label91,label101,label111,label121,label131;
    FloatingActionButton back;
    ImageView share;
    CardView call_card,location_card;
  String label9_title1;
  String label10_title1;
  String label11_title1;
  String label12_title1;
  String label13_title1;

    TextView label9_title;
    TextView label10_title;
    TextView label11_title;
    TextView label12_title;
    TextView label13_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatils);
        back=(FloatingActionButton)findViewById(R.id.homeback);
        share=(ImageView)findViewById(R.id.share);
        call_card=(CardView)findViewById(R.id.call_card);
        location_card=(CardView)findViewById(R.id.loaction_card);

        label9_title=(TextView)findViewById(R.id.label9_title);
        label10_title=(TextView)findViewById(R.id.label10_title);
        label11_title=(TextView)findViewById(R.id.label11_title);
        label12_title=(TextView)findViewById(R.id.label12_title);
        label13_title=(TextView)findViewById(R.id.label13_title);
        Intent intent=getIntent();
        name=intent.getExtras().getString("name");
//        id=intent.getExtras().getString("id");
        longi=intent.getExtras().getString("longi");
        lat=intent.getExtras().getString("lat");
        landmark=intent.getExtras().getString("landmark");
//        time=intent.getExtras().getString("time");
        doc_name=intent.getExtras().getString("doc_name");
//        hos_name=intent.getExtras().getString("hos_name");
        number=intent.getExtras().getString("number").trim();
        distance=intent.getExtras().getString("distance");


        label6=intent.getExtras().getString("label6");
        label7=intent.getExtras().getString("label7");
        label8=intent.getExtras().getString("label8");
        label9=intent.getExtras().getString("label9");
        label10=intent.getExtras().getString("label10");
        label11=intent.getExtras().getString("label11");
        label12=intent.getExtras().getString("label12");
        label13=intent.getExtras().getString("label13");



        name1=(TextView) findViewById(R.id.Addressdetails);
        hos_name1=(TextView) findViewById(R.id.hosname);

        doc_name1=(TextView) findViewById(R.id.doctor_name);
        label61=(TextView) findViewById(R.id.label6);
        label71=(TextView) findViewById(R.id.label7);
        label81=(TextView) findViewById(R.id.label8);
        label91=(TextView) findViewById(R.id.label9);
        label101=(TextView) findViewById(R.id.label10);
        label111=(TextView) findViewById(R.id.label11);
        label121=(TextView) findViewById(R.id.label12);
        label131=(TextView) findViewById(R.id.label13);
//        time1=(TextView) findViewById(R.id.time);
        number1=(TextView) findViewById(R.id.Phone);
//        hos_name1.setText(hos_name);
        Cursor cursor=new Database(DeatilsActivity.this).fetchTitle();
        while(cursor.moveToNext()){

            label9_title.setText(cursor.getString(11));
            label10_title.setText(cursor.getString(12));
            label11_title.setText(cursor.getString(13));
            label12_title.setText(cursor.getString(14));
            label13_title.setText(cursor.getString(15));

        }

        if(label6.length()>0) {
            label61.setVisibility(View.VISIBLE);
            label61.setVisibility(View.VISIBLE);
            label61.setText(label6);
        }
        if(label7.length()>0) {
            label71.setVisibility(View.VISIBLE);
            label71.setText(label7);
        }
        if(label8.length()>0) {
            label81.setVisibility(View.VISIBLE);
            label81.setText(label8);
        }
        if(label9.length()>0) {
            label91.setVisibility(View.VISIBLE);
            label9_title.setVisibility(View.VISIBLE);
            label91.setText(label9);
        }
        if(label10.length()>0) {
            label101.setVisibility(View.VISIBLE);
            label10_title.setVisibility(View.VISIBLE);
            label101.setText(label10);
        }
        if(label11.length()>0) {
            label111.setVisibility(View.VISIBLE);
            label11_title.setVisibility(View.VISIBLE);
            label111.setText(label11);
        }
        if(label12.length()>0) {
            label121.setVisibility(View.VISIBLE);
            label12_title.setVisibility(View.VISIBLE);
            label121.setText(label12);
        }

        if(label13.length()>0) {
            label131.setVisibility(View.VISIBLE);
            label13_title.setVisibility(View.VISIBLE);
            label131.setText(label13);
        }

        doc_name1.setText(doc_name);


//        time1.setText("Distance : "+ distance+" Km");
        name1.setText(""+name+", "+" \n"+landmark+".");
        number1.setText("+91 "+number);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String body="Doctor Name : "+doc_name+"\n"+"Address : " +landmark+" , "+name+"\n"+"Phone Number : "+number+"\n"+"Location : http://maps.google.com/maps?q=+"+lat+","+longi;
                i.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(i,"Share via"));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i=new Intent(getApplicationContext(),SearchActivity.class);
              startActivity(i);
            }
        });
        call_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String dail="tel:"+number;
                    startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse(dail)));

            }
        });

        location_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        String uri=String.format(Locale.ENGLISH,"geo:%f,%f",lat,longi);

                Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?q=+"+lat+","+longi));
                startActivity(i);
            }
        });
    }
    public void onBackPressed() {

        Intent i =new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

}