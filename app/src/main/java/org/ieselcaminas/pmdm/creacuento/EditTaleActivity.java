package org.ieselcaminas.pmdm.creacuento;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditTaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tale);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Tale tale = (Tale) extras.get(ViewTalesActivity.TALE);
            EditTaleFragment editTaleFragment =
                    (EditTaleFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.edit_tale_fragment); // the id in detail_activity.xml
            editTaleFragment.setTaleInfo(tale);
        }
    }
}
