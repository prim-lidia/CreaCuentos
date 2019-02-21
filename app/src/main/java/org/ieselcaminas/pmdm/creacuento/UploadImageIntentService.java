package org.ieselcaminas.pmdm.creacuento;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImageIntentService extends IntentService {
    public static final String TAG_URI = "Uri";
    public static final String TAG_STAGE = "StageId";
    public static final int PROGRESS_NOTIFICATION = 1234;
    public static final int SUCCESS_NOTIFICATION = 3456;
    public static final String CHANNEL_1 = "channel1";
    private NotificationManager notManager;

    public UploadImageIntentService() {
        super("UploadImageIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        notManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Uri filePath = intent.getExtras().getParcelable(TAG_URI);
        String taleId = intent.getExtras().getString(MyTalesActivity.TAG_TALE);
        String stageId = intent.getExtras().getString(TAG_STAGE);
        if (stageId == null) {
            uploadImage( filePath, taleId);
        }else {
            uploadImage(filePath, taleId, stageId);
        }


    }

    public void uploadImage(final Uri filePath, final String taleId){
        final StorageReference frontImageRef = FirebaseStorage.getInstance().getReference().child("images/"+taleId)
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
                                DatabaseReference taleRef = database.getReference("tales/"+taleId);
                                taleRef.child("frontImage").setValue(downloadUrl.toString());

                            }
                        });
                        notManager.cancel(PROGRESS_NOTIFICATION);
                        sendSuccesNotification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.d("UploadImage", exception.toString());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        sendProgressNotification(progress.intValue());
                    }

                });
    }
    public void uploadImage(final Uri filePath, String taleId, final String stageId){
        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/"+taleId)
                .child(filePath.getLastPathSegment());

        imageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // getting image uri and converting into string
                                Uri downloadUrl = uri;
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference taleRef = database.getReference("stages/"+stageId);
                                taleRef.child("image").setValue(downloadUrl.toString());
                            }
                        });
                        notManager.cancel(PROGRESS_NOTIFICATION);
                        sendSuccesNotification();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.d("UploadImage", exception.toString());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        sendProgressNotification(progress.intValue());
                    }

                });
    }

    public void sendProgressNotification(int progress){

        Intent intent = new Intent();
        PendingIntent contIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        int icon = android.R.drawable.stat_sys_upload;
        CharSequence textState = "Uploading image";
        CharSequence textContent = "Ups";
        long time = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_1, "Web site checker", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("My description");
            notManager.createNotificationChannel(mChannel);
        }
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1)
                .setColor(Color.GRAY)
                .setSmallIcon(icon)
                .setContentTitle(textState)
                .setProgress(100, progress, true)
                .setWhen(time)
                .setContentIntent(contIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notManager.notify(PROGRESS_NOTIFICATION, notification);

    }
    public void sendSuccesNotification(){
        Intent intent = new Intent();
        PendingIntent contIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        int icon = android.R.drawable.stat_sys_upload_done;
        CharSequence textState = "Upload Image";
        CharSequence contentText = "Upload complete successfully";
        long time = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_1, "Web site checker", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("My description");
            notManager.createNotificationChannel(mChannel);
        }
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1)
                .setColor(Color.GREEN)
                .setSmallIcon(icon)
                .setContentTitle(textState)
                .setContentText(contentText)
                .setWhen(time)
                .setContentIntent(contIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notManager.notify(SUCCESS_NOTIFICATION, notification);
    }
}
