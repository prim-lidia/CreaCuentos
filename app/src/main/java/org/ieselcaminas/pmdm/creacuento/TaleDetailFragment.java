package org.ieselcaminas.pmdm.creacuento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaleDetailFragment extends Fragment {
    private Tale tale = null;
    public TaleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_tale_detail, container, false);

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            tale = (Tale) savedInstanceState.get("tale");
            setTaleInfo(tale.getId());
        }

    }

    public void setTaleInfo(String taleId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference taleRef = database.getReference("tales/"+taleId);
        taleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tale = dataSnapshot.getValue(Tale.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String[] categories = getActivity().getResources().getStringArray(R.array.categories);
        ImageView detailImage = (ImageView) getView().findViewById(R.id.detailImageView);
        TextView detailTitle = (TextView) getView().findViewById(R.id.detailTitle);
        TextView detailCategory = (TextView) getView().findViewById(R.id.detailCategory);
        TextView detailAuthor = (TextView) getView().findViewById(R.id.detailAuthor);
        TextView detailIllustrator = (TextView) getView().findViewById(R.id.detailIllustrator);
        TextView detailDescription = (TextView) getView().findViewById(R.id.detailText);

        detailTitle.setText(tale.getTitle());
        detailAuthor.setText(tale.getAuthor());
        detailCategory.setText(categories[tale.getCategory()]);
        detailIllustrator.setText(tale.getIllustrationAuthor());
        detailDescription.setText(tale.getDescription());

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable("tale", tale);
        super.onSaveInstanceState(bundle);

    }
}
