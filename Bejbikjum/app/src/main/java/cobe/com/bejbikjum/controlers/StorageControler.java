package cobe.com.bejbikjum.controlers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cobe.com.bejbikjum.DAO.LocalDB;
import cobe.com.bejbikjum.activities.HomeActivity;
import cobe.com.bejbikjum.activities.MyShopActivity;
import cobe.com.bejbikjum.activities.RegisterActivity;
import cobe.com.bejbikjum.activities.ShopInfoActivity;
import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;
import cobe.com.bejbikjum.models.User;


public class StorageControler {

    //current Context
    private Context currentContext;
    private Activity currentActivity;

    private static final String TAG = "StorageControler";
    private static StorageControler instance;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private int progressCounter;
    private ArrayList<String> uriList;


    private StorageControler(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    };

    public static StorageControler getInstance(){
        if(instance == null){
            instance = new StorageControler();
        }
        return instance;
    }

    public void showProgressBar(LinearLayout progressLayout){
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(LinearLayout progressLayout){
        progressLayout.setVisibility(View.INVISIBLE);
    }

    public void uploadImage(Uri[] imageData, final Item item, final LinearLayout progressLayout) {

        String uid = FirebaseControler.getInstance().getCurrentUser().getUid();

        progressCounter = 0;
        for(int i = 0; i<imageData.length; i++){
            if(imageData[i]!=null)
                progressCounter++;
        }

        if(imageData != null)
        {
            Log.d(TAG, ""+progressCounter);
            uriList = new ArrayList<String>();

            showProgressBar(progressLayout);

            for(int i = 0; i<imageData.length; i++) {


                if(imageData[i]!=null) {

                    final StorageReference ref = storageRef.child("images/" + uid + "/" + item.getName() + "/" + i);
                    UploadTask uploadTask = ref.putFile(imageData[i]);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        hideProgressBar(progressLayout);
                            Toast.makeText(FirebaseControler.getInstance().getCurrentActivity(), "Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    hideProgressBar(progressLayout);
                                    Toast.makeText(FirebaseControler.getInstance().getCurrentActivity(), "Uploading failed", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    // TODO: 11.9.18. Make a progress bar with progress
//                                Log.d(TAG, "Uploaded " + (int) progress + "%");
                                }
                            });


                    //GET THE IMAGE URL
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                uriList.add(downloadUri.toString());

                                Log.d(TAG, "Upload complete, URI: " + downloadUri.toString());
                                progressCounter--;

                                if(progressCounter == 0) {
                                    item.setUrlStandard(uriList.toString());
                                    FirebaseControler.getInstance().addItem(item);

                                }



                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });

                }

            }

        }
    }





}
