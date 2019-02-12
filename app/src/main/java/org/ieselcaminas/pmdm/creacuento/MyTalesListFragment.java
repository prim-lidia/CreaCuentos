package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MyTalesListFragment extends Fragment {

    private  String currentUID;
    private FirebaseDatabase database;
    private ArrayList<Tale> taleList;
    public MyTalesListFragment() {}
    private MyTaleAdapter adaptador;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        taleList = new ArrayList<>();
        initList();
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_my_tale_list, container, false);
        // Inflate the layout for this fragment

        Button buttonNewTale = viewRoot.findViewById(R.id.buttonNewTale);
        buttonNewTale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference talesRef = database.getReference("tales");
                String key = talesRef.push().getKey();
                Tale newTale = new Tale(key, currentUID);
                Log.d("Cuentos Crear", key +", "+newTale.getCreator());
                talesRef.child(key).setValue(new Tale(key, currentUID));
            }
        });
        final RecyclerView recView = viewRoot.findViewById(R.id.my_tales_recyclerView);

        //el RecyclerView tendrá tamaño fijo
        recView.setHasFixedSize(true);
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adaptador = new MyTaleAdapter(taleList);
        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(viewRoot.getContext(), LinearLayoutManager.VERTICAL, false));
        recView.setItemAnimator(new DefaultItemAnimator());
        adaptador.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Tale tale = taleList.get(recView.getChildAdapterPosition(v));
                Log.d("Cuentos comunicator", tale.getCreator());
                startEditActivityFragment(tale.getId());
            }
        });

        return viewRoot;
    }

    public void initList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        final DatabaseReference talesRef = database.getReference("tales");
        Query myTales = talesRef.orderByChild("creator").equalTo(currentUID);

        Log.d("Cuentos", currentUID);
        myTales.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tale tale = dataSnapshot.getValue(Tale.class);
                adaptador.addItem(tale);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Tale tale = dataSnapshot.getValue(Tale.class);
                adaptador.updateItem(tale);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*Tale tale = dataSnapshot.getValue(Tale.class);
                adaptador.addItem(tale);*/
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void startEditActivityFragment(String taleId){
        EditTaleFragment editTaleFragment = (EditTaleFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.edit_tale_fragment);

        if (editTaleFragment != null && editTaleFragment.isInLayout()) {
            Bundle args =  new Bundle();
            args.putString("taleId", taleId);
            editTaleFragment.setArguments(args);
        } else {
            Intent intent = new Intent(getContext(), EditTaleActivity.class);
            if(taleId != null && !taleId.equals("")) {
                intent.putExtra(MyTalesActivity.TAG_TALE,taleId);
            }
            startActivity(intent);
        }
    }

}
