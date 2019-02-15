package org.ieselcaminas.pmdm.creacuento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TaleDetailFragment extends Fragment {
    private Tale tale = null;
    private ImageView detailImage;
    private TextView detailTitle;
    private TextView detailCategory;
    private TextView detailAuthor;
    private TextView detailIllustrator;
    private TextView detailDescription;

    public TaleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_tale_detail, container, false);

        detailImage = (ImageView) viewRoot.findViewById(R.id.detailImageView);
        detailTitle = (TextView) viewRoot.findViewById(R.id.detailTitle);
        detailCategory = (TextView) viewRoot.findViewById(R.id.detailCategory);
        detailAuthor = (TextView) viewRoot.findViewById(R.id.detailAuthor);
        detailIllustrator = (TextView) viewRoot.findViewById(R.id.detailIllustrator);
        detailDescription = (TextView) viewRoot.findViewById(R.id.detailText);

        Bundle args = getArguments();
        if (args != null) {
            String taleId = args.getString(MyTalesActivity.TAG_TALE);
            setTaleInfo(taleId);
        }



        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.get(MyTalesActivity.TAG_TALE) != null) {
                String taleId = savedInstanceState.getString(MyTalesActivity.TAG_TALE);
                setTaleInfo(taleId);
            }

        }
    }

    public void setTaleInfo(String taleId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference taleRef = database.getReference("tales/"+taleId);
        taleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tale = dataSnapshot.getValue(Tale.class);
                String[] categories = getActivity().getResources().getStringArray(R.array.categories);
                Log.d("Cuentos", tale.getTitle());
                StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(tale.getFrontImage());
                if(tale.getFrontImage() != null) {
                    GlideApp.with(getContext())
                            .load(fileRef)
                            .centerCrop()
                            .into(detailImage);
                }
                detailTitle.setText(tale.getTitle());
                detailAuthor.setText(tale.getAuthor());
                detailCategory.setText(categories[tale.getCategory()]);
                detailIllustrator.setText(tale.getIllustrationAuthor());
                detailDescription.setText(tale.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable(MyTalesActivity.TAG_TALE, tale);
        super.onSaveInstanceState(bundle);

    }
}
