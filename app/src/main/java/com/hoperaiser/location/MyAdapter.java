package com.hoperaiser.location;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static List<UserModal> list_data;
    private Context context;
    List<UserModal> newlist;
    SelectListner listner;
    public MyAdapter(List<UserModal> list_data, Context context,SelectListner listner) {
        this.list_data=list_data;
        this.context = context;
        this.newlist=list_data;
        this.listner=listner;
//        this.list_data=new ArrayList<>(newlist);

    }


    public void filterlist(List<UserModal> filteredlist){
        list_data = filteredlist;
        if (list_data.isEmpty())
        {
            Toast.makeText(context, "No Result Matching Your Search....", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModal listData=list_data.get(position);
        holder.txtname.setText(listData.getDoc_name());
        holder.distance.setText(listData.getDistance());
        holder.id.setText(listData.getName());
holder.maincardview.startAnimation(AnimationUtils.loadAnimation(context,R.anim.slide_down));
        holder.maincardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClicked(listData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView txtname,id,distance;
        private CardView maincardview;
        public ViewHolder(View itemView) {
            super(itemView);
//            img=(ImageView)itemView.findViewById(R.id.image_view);
            txtname=(TextView)itemView.findViewById(R.id.idTVFirstName);
            id=(TextView)itemView.findViewById(R.id.idTVLastName);
            distance=(TextView)itemView.findViewById(R.id.idTVEmail);
            maincardview=(CardView)itemView.findViewById(R.id.maincardview);
        }
    }
    public MyAdapter(List<UserModal> list_data) {
        this.list_data=list_data;
    }
}