package org.ieselcaminas.pmdm.creacuento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TaleAdapter extends ArrayAdapter<Tale> {
    private ArrayList<Tale> listTales;
    private Context context;
    int resourceId;

    public TaleAdapter(Context context, int resourceID,
                                 ArrayList<Tale> objects) {
        super(context, resourceID, objects);
        listTales = objects;
        this.context = context;
        this.resourceId = resourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resourceId, parent, false);
        }
        Tale tale = listTales.get(position);
        if (tale != null) {
            TextView tv = (TextView) v.findViewById(R.id.labelTitle);
            TextView tn = (TextView) v.findViewById(R.id.labelCategory);
            if (tv != null)
                tv.setText(tale.getTitle());
            if (tn != null)
                tn.setText("Version number: " + tale.getCategory());
            String imageID = tale.getFrontImage();
            if (imageID!= null && !imageID.equals("")) {
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
                //Download image from Firebase
                StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(tale.getFrontImage());
                if(tale.getFrontImage() != null) {
                    GlideApp.with(context)
                            .load(fileRef)
                            .centerCrop()
                            .into(imageView);
                }
            }
        }
        return  v;
    }

    public void addItem(Tale tale) {
        add(tale);

    }
}
