package org.ieselcaminas.pmdm.creacuento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditStageFragmet extends Fragment {
    public static final int PICK_IMAGE = 12;
    private View viewRoot;
    private StageOptionAdapter stageAdapter;
    private StorageReference storageRef;
    private Uri filePath;
    private Stage stage;
    private String idStage;
    private long count;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String taleId;
    private EditText editTitle;
    private EditText editText;
    private ImageView imageView;

    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_edit_stage, container, false);
        storageRef = FirebaseStorage.getInstance().getReference();

        Bundle args = getArguments();
        if (args != null) {
            String taleTitle = args.getString("taleTitle");
            TextView titleTextView = viewRoot.findViewById(R.id.textTitle);
            titleTextView.setText(taleTitle);
            taleId = args.getString(MyTalesActivity.TAG_TALE);
            String idStage = args.getString("idStage");
        }

        editText = viewRoot.findViewById(R.id.editStageText);
        editTitle = viewRoot.findViewById(R.id.editStageTitle);
        imageView = viewRoot.findViewById(R.id.stageImage);

        setNextStagesAdapter();

        sizeImageView();

        Button buttonSelectImage =  viewRoot.findViewById(R.id.buttonChooseImage);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Button buttonSave = viewRoot.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStage();
            }
        });

        return viewRoot;
    }

    private void setNextStagesAdapter() {
        //Next Stages
        setStageView();
        ArrayList<StageOption> nextStages = new ArrayList<StageOption>();
        ListView listStage = viewRoot.findViewById(R.id.stageList);
        stageAdapter = new StageOptionAdapter(getContext(),nextStages);
        listStage.setAdapter(stageAdapter);
    }

    private void sizeImageView() {
        //Get dimension form imageView
        ImageView imageView = viewRoot.findViewById(R.id.stageImage);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        float width = display.getWidth();
        float height = display.getHeight();
        float ratioScale = width/height;
        Log.d("ratioScale", ""+ratioScale+" height:"+height);
        imageView.getLayoutParams().width = Math.round(height*ratioScale)-32-32;
        imageView.getLayoutParams().height = Math.round(width*ratioScale)-32-32;
        imageView.requestLayout();
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void uploadImage(){
        Intent intent = new Intent(getContext(), UploadImageIntentService.class);
        intent.putExtra(UploadImageIntentService.TAG_URI,filePath);
        intent.putExtra(MyTalesActivity.TAG_TALE, taleId);
        intent.putExtra(UploadImageIntentService.TAG_STAGE, stage.getIdStage());
        getActivity().startService(intent);
    }

    public void updateStage() {
        stage.setText(editText.getText().toString());
        stage.setTitle(editTitle.getText().toString());

        if(filePath!= null) {
            uploadImage();
        }
    }

    public void updateStageViewFields() {
        editTitle.setText(stage.getTitle());
        editText.setText(stage.getText());
        if(stage.getImage() != null && imageView.getDrawable() == null) {
            StorageReference fileRef = FirebaseStorage.getInstance().getReferenceFromUrl(stage.getImage());
            if(fileRef!= null) {
                GlideApp.with(viewRoot.getContext())
                        .load(fileRef)
                        .into(imageView);
            }
        }
    }

    public void saveStage() {
        updateStage();
        DatabaseReference currentStageRef =  database.getReference("stages/"+stage.getIdStage());
        currentStageRef.setValue(stage);

    }

    public void setStageView(){
        count = 0;
        final DatabaseReference stagesRef = database.getReference("stages");
        final Query taleStages = stagesRef.orderByChild("tale").equalTo(taleId);
        taleStages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                ArrayList<String> stages = new ArrayList<String>();
                listAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_single_choice, stages);
                Log.d("Cuentos total escenas", String.valueOf(count));
                if (idStage == null && count == 0) {
                    DatabaseReference stagesRef =  database.getReference("stages");
                    idStage = stagesRef.push().getKey();
                    stage = new Stage(idStage, taleId, 1);
                    stagesRef.child(idStage).setValue(stage);
                }else if(idStage == null) {
                    for(DataSnapshot item: dataSnapshot.getChildren()){
                        Stage s = item.getValue(Stage.class);
                        stages.add(s.getNumber()+": "+s.getTitle());
                        listAdapter.notifyDataSetChanged();
                        Log.d("Stages addd", String.valueOf(stages.size()));
                        if(s.getNumber() == 1){
                            stage = s;
                            updateStageViewFields();
                        }
                    }
                } else if (idStage != null) {

                }

                Button buttonAddStage = viewRoot.findViewById(R.id.buttonAddStage);
                buttonAddStage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNextStage();
                        openAddStageDialog();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void addNextStage(){
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
                    Log.d("Bitmap", bitmap.toString());
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openAddStageDialog(){

        final Context context = getContext();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.stages))
                .setMessage("")
                .setSingleChoiceItems(listAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.delete_btn_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Toast.makeText(context,
                                        "OK clicked!", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton(context.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(context,
                                        "Cancel clicked!", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .create();
        dialog.show();
    }
}
