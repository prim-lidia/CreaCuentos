package org.ieselcaminas.pmdm.creacuento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyTalesActivity extends AppCompatActivity implements MyTalesListFragment.Communicator{

    public static String TAG_TALE = "tale";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tales);

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
