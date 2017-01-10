package com.example.user.loginrregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 8/8/2016.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater=null;

    ArrayList<Entity> entityArrayList;


    CustomAdapter(Context context, ArrayList<Entity> entityArrayList){
        this.context = context;
        this.entityArrayList = entityArrayList;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return entityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv, tv1;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_design, null);
        holder.tv=(TextView) rowView.findViewById(R.id.text1);
        holder.tv1=(TextView) rowView.findViewById(R.id.text2);
        holder.tv.setText(entityArrayList.get(position).getName());
        holder.tv1.setText(entityArrayList.get(position).getComment());
        return rowView;
    }

}
