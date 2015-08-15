package com.rjokela.zoolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Randon K. Jokela on 8/14/2015.
 */
public class AnimalAdapter extends ArrayAdapter<Animal> {
    private int layoutResourceId;
    public final static String TAG = "AnimalAdapter";
    private LayoutInflater inflater;
    private List<Animal> animals;
    public AnimalAdapter(Context context, int layoutResourceId,
                         List<Animal> animals ) {
        super(context, layoutResourceId, animals);
        this.layoutResourceId = layoutResourceId;
        this.animals = animals;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnimalHolder holder = null;
        if (null == convertView) {
            Log.d(TAG, "getView: rowView null: position " + position);
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new AnimalHolder();
            holder.imgSpecies = (ImageView)convertView.findViewById(R.id.icon);
            holder.txtName = (TextView)convertView.findViewById(R.id.name);
            holder.txtLocation = (TextView)convertView.findViewById(R.id.location);
// Tags can be used to store data
            convertView.setTag(holder);
        }
        else {
            Log.d(TAG, "getView: rowView !null - reuse holder: position " + position);
            holder = (AnimalHolder)convertView.getTag();
        }
        Log.d(TAG, " getView animals " + animals.size());
// Put inside a try/catch block;
// animal.get can return an IndexOutOfBoundsException
// and if the exception occurs and is not handed, it will crash your program.
        try {
            Animal animal = animals.get(position);
            holder.txtName.setText(animal.getName());
            holder.txtLocation.setText(animal.getLocation());
            if (animal.getType().equals(Animal.MAMMAL)) {
                holder.imgSpecies.setImageResource(R.drawable.ic_lion);
            }
            else if (animal.getType().equals(Animal.BIRD)) {
                holder.imgSpecies.setImageResource(R.drawable.ic_bird);
            }
            else {
                holder.imgSpecies.setImageResource(R.drawable.ic_lizard);
            }
        } catch(Exception e) {
            Log.e(TAG, " getView animals " + e + " position was : " + position +
                    " animals.size: " + animals.size());
        }
        return convertView;
    }
    // This is used to cache the imageView and TextView of the
// ImageTextArrayAdapter class
// so they can be reused for every row in the ListView
    static class AnimalHolder {
        ImageView imgSpecies;
        TextView txtName;
        TextView txtLocation;
    }
}
