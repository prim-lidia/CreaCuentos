package org.ieselcaminas.pmdm.creacuento;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTalesActivity extends AppCompatActivity implements MyTalesListFragment.Communicator{
    private  String creator;
    private  FirebaseDatabase database;
    public static String TAG_TALE = "tale";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tales);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        creator = mAuth.getCurrentUser().getEmail();
        creator = creator.replace(".","_");

        database = FirebaseDatabase.getInstance();

        final DatabaseReference myTalesRef = database.getReference("creators/"+creator);
        Log.d("dataSnapshot",myTalesRef.toString());
        myTalesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] value = dataSnapshot.getValue(String[].class);
                String[] myTales = {};
                if( value != null){
                    Log.d("dataSnapshot",dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setTale(Tale tale) {
        EditTaleFragment editTaleFragment = (EditTaleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.edit_tale_fragment);

        if (editTaleFragment != null && editTaleFragment.isInLayout()) {
            editTaleFragment.setTaleInfo(tale);
        } else {
            Intent intent = new Intent(getApplicationContext(), EditTaleActivity.class);
            if(tale != null) {
                intent.putExtra(TAG_TALE,tale);
            }
            startActivity(intent);
        }
    }

}
