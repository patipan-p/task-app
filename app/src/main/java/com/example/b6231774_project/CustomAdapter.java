package com.example.b6231774_project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<User>{
    Context ct;
    ArrayList<User> users;
    int res;
    public CustomAdapter(Context applicationContext,int res, ArrayList<User> userarr){
        super(applicationContext, res, userarr);
        this.res = res;
        this.ct = applicationContext;
        this.users = userarr;
    }


    static class ViewHolder{
        ImageView iw;
        TextView t;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        User us = getItem(position);

        ViewHolder viewHolder;
        final View result;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.iw = (ImageView) convertView.findViewById(R.id.icsx);
            viewHolder.t = (TextView) convertView.findViewById(R.id.textsp);

            result = convertView;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        int cs = Color.parseColor(us.getColor());
        Drawable dr = getContext().getDrawable(R.drawable.round);
        dr.setColorFilter(cs, PorterDuff.Mode.SRC_ATOP);
        viewHolder.iw.setBackground(dr);
        viewHolder.t.setText(us.getN());
        if (us.getDef().equals("Default"))
            viewHolder.t.setTypeface(null, Typeface.BOLD);
        return convertView;
    }
}
