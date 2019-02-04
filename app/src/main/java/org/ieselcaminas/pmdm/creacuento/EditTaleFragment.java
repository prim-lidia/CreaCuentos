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
    private String[] categories;
    private EditText editTextTitle;
    private ImageView imageViewFrontImage;
    private EditText editTextAuthor;
    private EditText editTextIllustrator;
    private EditText editTextDescription;
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storageRef = FirebaseStorage.getInstance().getReference();
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_edit_tale, container, false);
        editTextTitle = (EditText) viewRoot.findViewById(R.id.editTitle);
        imageViewFrontImage = (ImageView) viewRoot.findViewById(R.id.frontImage);
        editTextAuthor = (EditText) viewRoot.findViewById(R.id.editAuthor);
        editTextIllustrator = (EditText) viewRoot.findViewById(R.id.editIllustrator);
        editTextDescription = (EditText) viewRoot.findViewById(R.id.editDescription);

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

        //Categories
        spinner=(Spinner) viewRoot.findViewById(R.id.spinnerCategories);
        categories = getActivity().getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,categories);
        spinner.setAdapter(adapter);


        Button buttonNext = viewRoot.findViewById(R.id.buttonContinue);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTale(true);
            }
        });

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.get(TALE_TAG) != null) {
                tale = (Tale) savedInstanceState.get(TALE_TAG);
            }
            setTaleInfo(tale);
        }else {
            tale = new Tale();
        }
    }

    public void setTaleInfo(Tale tale2){
        tale = tale2;
        editTextTitle.setText(tale.getTitle());
        editTextAuthor.setText(tale.getAuthor());
        editTextDescription.setText(tale.getDescription());
        editTextIllustrator.setText(tale.getIllustrationAuthor());
    }

    public void setTaleInfo(String keyTale){
        tale = new Tale();
        tale.setToken(keyTale);
        editTextTitle.setText(tale.getTitle());
        editTextAuthor.setText(tale.getAuthor());
        editTextDescription.setText(tale.getDescription());
        editTextIllustrator.setText(tale.getIllustrationAuthor());
    }
    public void setTale(Boolean finished){
        tale.setTitle(editTextTitle.getText().toString());
        tale.setAuthor((editTextTitle.getText().toString()));
        tale.setIllustrationAuthor((editTextIllustrator.getText().toString()));
        tale.setCategory(spinner.getSelectedItemPosition());
        tale.setDescription(editTextDescription.getText().toString());
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        setTale(false);
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
