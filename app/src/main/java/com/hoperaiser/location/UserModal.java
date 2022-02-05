package com.hoperaiser.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserModal implements Comparable<UserModal> {

    public UserModal(String id, String name, String longi, String lat, String landmark, String doc_name, String number, String distance, String label6, String label7, String label8, String label9, String label10, String label11, String label12, String label13) {
        this.id = id;
        this.name = name;
        this.longi = longi;
        this.lat = lat;
        this.landmark = landmark;
        this.doc_name = doc_name;
        this.number = number;
        this.distance = distance;
        this.label6 = label6;
        this.label7 = label7;
        this.label8 = label8;
        this.label9 = label9;
        this.label10 = label10;
        this.label11 = label11;
        this.label12 = label12;
        this.label13 = label13;
    }

    public UserModal(){

    }
    String id ;
    String name ,longi,lat,landmark,doc_name,number,distance;
    String label6,label7,label8,label9,label10,label11,label12,label13;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongi() {
        return longi;
    }

    public String getLabel6() {
        return label6;
    }

    public void setLabel6(String label6) {
        this.label6 = label6;
    }

    public String getLabel7() {
        return label7;
    }

    public void setLabel7(String label7) {
        this.label7 = label7;
    }

    public String getLabel8() {
        return label8;
    }

    public void setLabel8(String label8) {
        this.label8 = label8;
    }

    public String getLabel9() {
        return label9;
    }

    public void setLabel9(String label9) {
        this.label9 = label9;
    }

    public String getLabel10() {
        return label10;
    }

    public void setLabel10(String label10) {
        this.label10 = label10;
    }

    public String getLabel11() {
        return label11;
    }

    public void setLabel11(String label11) {
        this.label11 = label11;
    }

    public String getLabel12() {
        return label12;
    }

    public void setLabel12(String label12) {
        this.label12 = label12;
    }

    public String getLabel13() {
        return label13;
    }

    public void setLabel13(String label13) {
        this.label13 = label13;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

//    public UserModal(String id, String name, String longi, String lat, String landmark, String number, String doc_name,String distance) {
//        this.id = id;
//        this.name = name;
//        this.longi = longi;
//        this.lat = lat;
//        this.landmark = landmark;
//        this.doc_name = doc_name;
//        this.number = number;
//        this.distance=distance;
//    }

    @Override
    public int compareTo(UserModal o) {
//        return this.name.compareTo(o.name);
        int s=Integer.parseInt(this.distance);
        int s1=Integer.parseInt(o.getDistance());
        return s - s1;

    }
}



