package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditTaleFragment extends Fragment {

    public static final int PICK_IMAGE = 12;
    private Tale tale;
    private StorageReference storageRef;
    private String[] categories;
    private EditText editTextTitle;
    private ImageView imageViewFrontImage;
    private EditText editTextAuthor;
    private EditText editTextIllustrator;
    private EditText editTextDescription;
    private Spinner spinner;
    private Uri filePath;

    public EditTaleFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            String taleId = args.getString(MyTalesActivity.TAG_TALE);
            setTaleView(taleId);
        }

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

        final Button buttonSelectImage =  viewRoot.findViewById(R.id.buttonChooseImage);
        //final Button buttonUpload = viewRoot.findViewById(R.id.buttonUpload);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //buttonUpload.setVisibility(View.VISIBLE);
                buttonSelectImage.setVisibility(View.GONE);
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
                updateTale();
                if(filePath!= null) {
                    uploadImage();
                }

            }
        });

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.get(MyTalesActivity.TAG_TALE) != null) {
                String taleId = savedInstanceState.getString(MyTalesActivity.TAG_TALE);
                setTaleView(taleId);
            }

        }
    }

    public void setTaleView(String taleId){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference taleRef = database.getReference("tales/"+taleId);
        taleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tale = dataSnapshot.getValue(Tale.class);
                editTextTitle.setText(tale.getTitle());
                editTextAuthor.setText(tale.getAuthor());
                editTextDescription.setText(tale.getDescription());
                editTextIllustrator.setText(tale.getIllustrationAuthor());
                spinner.setSelection(tale.getCategory());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateTale(){
        tale.setTitle(editTextTitle.getText().toString());
        tale.setAuthor((editTextAuthor.getText().toString()));
        tale.setIllustrationAuthor((editTextIllustrator.getText().toString()));
        tale.setCategory(spinner.getSelectedItemPosition());
        tale.setDescription(editTextDescription.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference taleRef = database.getReference("tales/"+tale.getId());
        taleRef.setValue(tale);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        updateTale();
        bundle.putSerializable(MyTalesActivity.TAG_TALE, tale);
        super.onSaveInstanceState(bundle);

    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    //Libreria ButterKnife -- R.if.loquesea
    //Glide para imagenes

    public void uploadImage(){
        final StorageReference frontImageRef = storageRef.child("photos")
                .child(filePath.getLastPathSegment());

        frontImageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        frontImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // getting image uri and converting into string
                                Uri downloadUrl = uri;
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference taleRef = database.getReference("tales/"+tale.getId());
                                taleRef.child("frontImage").setValue(downloadUrl.toString());
                                Log.d("UploadImage", "URL: "+downloadUrl.toString());

                            }
                        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {

            if (resultData == null) {
                //Display an error
                return;
            } else {
                filePath = null;
                filePath = resultData.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                    imageViewFrontImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
