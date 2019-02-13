package org.ieselcaminas.pmdm.creacuento;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ViewTalesActivity extends AppCompatActivity {
    public static String TAG_TALE = "taleId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tales);
    }
}
