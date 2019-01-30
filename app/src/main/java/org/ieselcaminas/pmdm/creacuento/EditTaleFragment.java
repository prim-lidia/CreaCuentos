package org.ieselcaminas.pmdm.creacuento;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditTaleFragment extends Fragment {
    public static final int PICK_IMAGE = 12;
    private String TALE_TAG = "tale";
    private Tale tale;
    private StorageReference storageRef;
    public EditTaleFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storageRef = FirebaseStorage.getInstance().getReference();
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_edit_tale, container, false);

        //Get dimension form imageView
        ImageView imageView = viewRoot.findViewById(R.id.frontImage);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        float width = display.getWidth();
        float height = display.getHeight();
        float ratioScale = width/height;
        Log.d("ratioScale", ""+ratioScale+" height:"+height);
        imageView.getLayoutParams().width = Math.round(height*ratioScale)-32-32;
        imageView.getLayoutParams().height = Math.round(width*ratioScale)-32-32;
        imageView.requestLayout();

        Button buttonUpload = viewRoot.findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        Spinner spinner=(Spinner) viewRoot.findViewById(R.id.spinnerCategories);
        String[] categories = getActivity().getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categories);

        spinner.setAdapter(adapter);

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            tale = (Tale) savedInstanceState.get(TALE_TAG);
            setTaleInfo(tale);
        }
    }

    public void setTaleInfo(Tale tale2){
        tale = tale2;

        EditText titleEditext = (EditText) getView().findViewById(R.id.edit_title);
        titleEditext.setText(tale.getTitle());
        /*ImageView detailImage = (ImageView) getView().findViewById(R.id.detailImageView);
        TextView detailTitle = (TextView) getView().findViewById(R.id.detailTitle);
        TextView detailCategory = (TextView) getView().findViewById(R.id.detailCategory);
        TextView detailAuthor = (TextView) getView().findViewById(R.id.detailAuthor);
        TextView detailIllustrator = (TextView) getView().findViewById(R.id.detailIllustrator);
        TextView detailDescription = (TextView) getView().findViewById(R.id.detailText);

        detailTitle.setText(tale.getTitle());
        detailAuthor.setText(tale.getAuthor());
        detailCategory.setText(tale.getCategory());
        detailIllustrator.setText(tale.getIllustrationAuthor());
        detailDescription.setText(tale.getDescription());*/

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable(TALE_TAG, tale);
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            Uri uri = null;
            if (resultData == null) {
                //Display an error
                return;
            } else {
                uri = resultData.getData();
                final StorageReference frontImageRef = storageRef.child("photos")
                        .child(uri.getLastPathSegment());

                frontImageRef.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Task<Uri> downloadUrl = frontImageRef.getDownloadUrl();
                                Log.d("UploadImage", "URL: "+downloadUrl.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Log.d("UploadImage", exception.toString());
                            }
                        });
            }
        }
    }
}
