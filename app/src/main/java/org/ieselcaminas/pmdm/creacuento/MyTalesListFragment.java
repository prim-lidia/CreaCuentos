package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MyTalesListFragment extends Fragment {
    public interface Communicator {
        void setTale(Tale tale);
    }
    private  String creator;
    private FirebaseDatabase database;

    private Communicator communicator;

    private ArrayList<Tale> taleList;
    public MyTalesListFragment() {}
    private MyTaleAdapter adaptador;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        creator = mAuth.getCurrentUser().getEmail();
        creator = creator.replace(".","_");
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
                final DatabaseReference myTalesRef = database.getReference("creators/"+creator);
                final DatabaseReference talesRef = database.getReference("tales");
                String key = myTalesRef.push().getKey();
                Tale newTale = new Tale(key, creator);
                Log.d("Cuentos Crear", key +", "+newTale.getCreator());
                talesRef.child(key).setValue(new Tale(key, creator));
                myTalesRef.child(key).setValue(new Tale(key, creator));

                communicator.setTale(new Tale(key, creator));
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
                communicator.setTale(tale);
                /*MyTalesListFragment fragment2 = new Fragment2();
                Bundle args = new Bundle();
                args.putInt("Imagen", img);
                fragment2.setArguments(args);
                Transition slide = TransitionInflater.from(getActivity()).inflateTransition(R.transition.slide);
                Transition fade = TransitionInflater.from(getActivity()).inflateTransition(R.transition.fade);
                fragment2.setSharedElementEnterTransition(slide);
                fragment2.setSharedElementReturnTransition(fade);
                final View idlogo = v.findViewById(R.id.imageView);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentReceiver, fragment2)
                        .addToBackStack(null)
                        .addSharedElement(idlogo, idlogo.getTransitionName())
                        .commit();*/
            }
        });

        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
            if (activity instanceof Communicator) {
                communicator = (Communicator) activity;
            } else {
                throw new ClassCastException(activity.toString()
                        + " must implement MyTalesListFragment.Communicator");
            }
        }
    }


    public void initList() {
        final DatabaseReference myTalesRef = database.getReference("creators/"+creator);
        myTalesRef.addChildEventListener(new ChildEventListener() {
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

        /*//Recoger datos de firebase
        Tale tale1 = new Tale("sssss","Titulo1", "Autor Autor", "Autor ilustracion", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "NombreFichero", 1);
        tale1.setEditing(true);
        taleList.add(tale1);
        Tale tale2 = new Tale("aaaaa", "Titulo2", "Autor Autor", "Autor ilustracion", "Este libro NUM 2 trata de bla bla bla....", "NombreFichero", 2);
        tale2.setEditing(false);
        taleList.add(tale2);*/
    }
}
