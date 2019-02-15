package org.ieselcaminas.pmdm.creacuento;

import com.bumptech.glide.module.AppGlideModule;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;

@GlideModule
public final class TheGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Register FirebaseImageLoader to handle StorageReference
        //Needed for use GlideApp and use it with Firebase
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }

}