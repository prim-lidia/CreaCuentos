package org.ieselcaminas.pmdm.creacuento;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTaleActivity extends AppCompatActivity {

    private StagesAdapter adaptador;
    private ArrayList<Stage> stages;
    private RecyclerView recView;
    private LinearLayout listContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tale);
        Bundle extras = getIntent().getExtras();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Button buttonShowStages = findViewById(R.id.showStages);
        buttonShowStages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

        listContainer = findViewById(R.id.listContainer);
        listContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideList();
            }
        });

        // Set fragment
        if (extras != null) {

            String taleId = extras.getString(MyTalesActivity.TAG_TALE);
            getStageList(taleId);

            EditTaleFragment editTaleFragment = new EditTaleFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle args = new Bundle();
            args.putString(MyTalesActivity.TAG_TALE, taleId);
            editTaleFragment.setArguments(args);

            fragmentTransaction.add(R.id.edit_tale_fragment, editTaleFragment);
            fragmentTransaction.commit();
            setStagesAdapter(taleId);

        }

    }

    private void showList(){

        float width = recView.getWidth();
        listContainer.setVisibility(View.VISIBLE);
        Animation anim = new TranslateAnimation(width, 0, 0, 0);
        anim.setFillAfter(true);
        anim.setDuration(500);
        recView.startAnimation(anim);
        listContainer.setBackgroundColor(getResources().getColor(R.color.transparentBlack));

    }

    private void hideList() {
        if(listContainer.getVisibility() == View.VISIBLE){
            float width = recView.getWidth();
            Animation anim = new TranslateAnimation(0, width, 0, 0);
            anim.setFillAfter(true);
            anim.setDuration(500);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    listContainer.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            recView.startAnimation(anim);
        }
    }

    private void setStagesAdapter(String taleId) {
        //Next Stages
        stages = new ArrayList<Stage>();
        recView = findViewById(R.id.stageList);

        //el RecyclerView tendrá tamaño fijo
        recView.setHasFixedSize(true);
        recView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adaptador = new StagesAdapter(stages);
        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recView.setItemAnimator(new DefaultItemAnimator());
        adaptador.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Stage stage = stages.get(recView.getChildAdapterPosition(v));
                startStageFragment(stage);
            }
        });
    }

    public void getStageList(String taleId){
        Log.d("Stages", taleId);
        final DatabaseReference stagesRef = FirebaseDatabase.getInstance().getReference("stages");
        final Query taleStages = stagesRef.orderByChild("tale").equalTo(taleId);
        taleStages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Stage stage = dataSnapshot.getValue(Stage.class);
                Log.d("Stages", stage.getTitle());
                adaptador.addItem(stage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Stage stage = dataSnapshot.getValue(Stage.class);
                adaptador.updateItem(stage);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Stage stage = dataSnapshot.getValue(Stage.class);
                adaptador.removeItem(stage);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startStageFragment(Stage stage){
        EditStageFragmet editStageFragmet = new EditStageFragmet();
        Bundle args =  new Bundle();
        args.putString("taleId", stage.getTale());
        String title = getTitleTale(stage.getTale());
        args.putString("taleTitle", title);
        editStageFragmet.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.edit_tale_fragment, editStageFragmet)
                .addToBackStack(null)
                .commit();
        hideList();
    }

    private String getTitleTale(String taleId){
        final String[] title = {""};
        DatabaseReference taleRef = FirebaseDatabase.getInstance().getReference("tales/"+taleId);
        taleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tale t = dataSnapshot.getValue(Tale.class);
                title[0] = t.getTitle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  title[0];
    }
}
