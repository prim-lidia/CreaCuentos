package org.ieselcaminas.pmdm.creacuento;


import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditTaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tale);
        Bundle extras = getIntent().getExtras();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        // Set fragment
        if (extras != null) {
            String taleId = extras.getString(MyTalesActivity.TAG_TALE);
            EditTaleFragment editTaleFragment = new EditTaleFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle args = new Bundle();
            args.putString(ViewTalesActivity.TAG_TALE, taleId);
            editTaleFragment.setArguments(args);

            fragmentTransaction.add(R.id.edit_tale_fragment, editTaleFragment);
            fragmentTransaction.commit();
        }
    }
}
