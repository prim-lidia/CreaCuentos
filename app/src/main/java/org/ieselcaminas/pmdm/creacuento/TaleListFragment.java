package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ListFragment;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class TaleListFragment extends ListFragment {
    private TaleAdapter adapter;
    private FirebaseDatabase database;
    private ArrayList<Tale> taleList;
    public TaleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tale_list, container, false);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taleList = new ArrayList<Tale>();

        database =FirebaseDatabase.getInstance();
        initList();

        adapter = new TaleAdapter(getActivity(),R.layout.tale_info,taleList);
        setListAdapter(adapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        //Toast.makeText(getActivity(),
        //        "You have selected " + presidents[position], Toast.LENGTH_SHORT).show();
        Tale tale = (Tale)   parent.getItemAtPosition(position);
        startTaleDetailActivityFragment(tale.getId(), v);
    }

    public void initList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference talesRef = database.getReference("tales");
        talesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tale tale = dataSnapshot.getValue(Tale.class);
                adapter.add(tale);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Tale tale = dataSnapshot.getValue(Tale.class);
                for(int i=0; i< taleList.size(); i++ ) {
                    if (taleList.get(i).getId().equals(tale.getId())) {
                        taleList.set(i, tale);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Tale tale = dataSnapshot.getValue(Tale.class);
               for (int i = 0; i < taleList.size(); i++) {
                   if (taleList.get(i).getId().equals(tale.getId())) {
                       adapter.remove(adapter.getItem(i));
                       break;
                   }

               }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void startTaleDetailActivityFragment(String taleId, View v){
        final View sharedimageview = v.findViewById(R.id.imageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedimageview.setTransitionName(getResources().getString(R.string.image_trans));
        }

        TaleDetailFragment taleDetaileFragment = (TaleDetailFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.detail_tale_fragment);

        if (taleDetaileFragment != null && taleDetaileFragment.isInLayout()) {
            Bundle args =  new Bundle();
            args.putString("taleId", taleId);
            taleDetaileFragment.setArguments(args);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Transition changeBounds = TransitionInflater.from(getActivity()).inflateTransition(R.transition.changebounds);
                taleDetaileFragment.setSharedElementEnterTransition(changeBounds);
                taleDetaileFragment.setSharedElementReturnTransition(changeBounds);
            }


        } else {
            Intent intent = new Intent(getContext(), TaleDetailActivity.class);
            if(taleId != null && !taleId.equals("")) {
                intent.putExtra(ViewTalesActivity.TAG_TALE,taleId);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Pair<View, String> p1 = new Pair(sharedimageview, sharedimageview.getTransitionName());
                Bundle options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), p1).toBundle();
                startActivity(intent, options);
            } else {
                startActivity(intent);
            }

        }
    }

}
