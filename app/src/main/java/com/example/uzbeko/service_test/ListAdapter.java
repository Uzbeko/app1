package com.example.uzbeko.service_test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ed on 8/27/2015.
 */
public class ListAdapter extends BaseAdapter {

    ArrayList<Items> data = new ArrayList<Items>();
//-------------------------------------------------------
    public ListAdapter(ArrayList<Items> myList){
        this.data = myList;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        System.out.println("LLLLLL getItem is vslled: "+position);
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        System.out.println("LLLLLL getItemId is vslled: "+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("LLLLLL getView is vslled: " + convertView);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listwiev_cell_nayout, parent,false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title_id);
        title.setText(data.get(position).getTitle());
        TextView pubData = (TextView) convertView.findViewById(R.id.pubDate_id);
        pubData.setText(data.get(position).getPubDate());
        TextView thumbnail = (TextView) convertView.findViewById(R.id.thumbnail_id);
        thumbnail.setText(data.get(position).getthumbnail());
        TextView link = (TextView) convertView.findViewById(R.id.link_id);
        link.setText(data.get(position).getLink());

        System.out.println("LLLLLL getView is vslled: "+position);

        return convertView;
    }
//--seters and geters-------------------------------------------------
    public void setData(ArrayList<Items> myList){
        this.data = myList;
    }
}
