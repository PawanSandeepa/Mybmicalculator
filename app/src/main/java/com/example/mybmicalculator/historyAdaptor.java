package com.example.mybmicalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class historyAdaptor extends ArrayAdapter {
    private Context context;
    private int resource;
    List<model> models;

    historyAdaptor(Context context, int resource, List<model> models){
        super(context,resource,models);

        this.context = context;
        this.resource = resource;
        this.models = models;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource,parent,false);

        TextView date = row.findViewById(R.id.date);
        TextView weight = row.findViewById(R.id.weight);
        TextView height = row.findViewById(R.id.height);
        TextView bmi = row.findViewById(R.id.bmi);

        model model = models.get(position);
        date.setText(model.getDate().toString());
        weight.setText(model.getWeight().toString());
        height.setText(model.getHeight().toString());
        bmi.setText(model.getBmi().toString());


        return row;
    }
}
