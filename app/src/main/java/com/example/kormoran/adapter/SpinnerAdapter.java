package com.example.kormoran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kormoran.R;
import com.example.kormoran.data.Kategori;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private LayoutInflater mInflater;
    private List<Kategori> mItems;

    public SpinnerAdapter(Context context, List<Kategori> mItems) {
        mInflater = LayoutInflater.from(context);
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
//        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_item, parent, false);
        }
        Kategori item = (Kategori) getItem(position);
        TextView textView = view.findViewById(R.id.spinner_item);
        textView.setText(item.getKategori());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_item, parent, false);
        }
        Kategori item = (Kategori) getItem(position);
        TextView textView = view.findViewById(R.id.spinner_item);
        textView.setText(item.getKategori());
        return view;
    }
}
