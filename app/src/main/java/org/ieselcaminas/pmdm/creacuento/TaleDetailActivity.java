package org.ieselcaminas.pmdm.creacuento;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TaleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_detail);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            Tale tale = (Tale) extras.get(ViewTalesActivity.TALE);
            TaleDetailFragment detailFragment =
                    (TaleDetailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.detail_Fragment); // the id in detail_activity.xml
            detailFragment.setTaleInfo(tale);
        }
    }
}
