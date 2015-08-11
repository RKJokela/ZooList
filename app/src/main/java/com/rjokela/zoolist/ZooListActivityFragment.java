package com.rjokela.zoolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ZooListActivityFragment extends Fragment {
    List<Animal> animals = new ArrayList<Animal>();
    ArrayAdapter<Animal> adapter = null;

    public ZooListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zoo_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ListView list=(ListView)getActivity().findViewById(R.id.zoo_animals);
        adapter=new ArrayAdapter<Animal>(getActivity(),
                android.R.layout.simple_list_item_1,
                animals);
        list.setAdapter(adapter);

        Button saveButton = (Button) getActivity().findViewById(R.id.zoo_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
    }

    private void onSave(){
        Animal animal=new Animal();
        EditText name=(EditText)getActivity().findViewById(R.id.zoo_name);
        EditText location=(EditText)getActivity().findViewById(R.id.zoo_location);
        animal.setName(name.getText().toString());
        animal.setLocation(location.getText().toString());
        RadioGroup types=(RadioGroup)getActivity().findViewById(R.id.zoo_animalType);
        switch (types.getCheckedRadioButtonId()) {
            case R.id.zoo_animalTypeMammal:
                animal.setType("mammal");
                break;
            case R.id.zoo_animalTypeBird:
                animal.setType("bird");
                break;
            case R.id.zoo_animalTypeReptile:
                animal.setType("reptile");
                break;
        }
        // Add the object at the end of the array.
        adapter.add(animal);
        // Notifies the adapter that the underlying data has changed,
        // any View reflecting the data should refresh itself.
        adapter.notifyDataSetChanged();
    }
}
