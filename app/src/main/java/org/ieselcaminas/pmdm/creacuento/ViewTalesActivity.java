package org.ieselcaminas.pmdm.creacuento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ViewTalesActivity extends AppCompatActivity implements  TaleListFragment.Communicator{
    public static String TAG_TALE = "taleId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tales);
    }

    @Override
    public void setTale(String taleId) {
        TaleDetailFragment detailfragment = (TaleDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_Fragment);
        Log.d("taleCom", "taleView "+taleId);
        if (detailfragment != null && detailfragment.isInLayout()) {
            detailfragment.setTaleInfo(taleId);
        } else {
            Intent intent = new Intent(getApplicationContext(), TaleDetailActivity.class);
            intent.putExtra(TAG_TALE,taleId);
            startActivity(intent);
        }
    }
}
