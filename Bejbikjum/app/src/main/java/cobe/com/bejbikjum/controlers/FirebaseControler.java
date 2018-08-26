package cobe.com.bejbikjum.controlers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cobe.com.bejbikjum.models.User;


public class FirebaseControler {

    private static final String TAG = "FirebaseControler";
    private static FirebaseControler instance;
    private FirebaseUser user;
    private FirebaseFirestore db;

    //Authentication
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    private FirebaseControler(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    };

    public static FirebaseControler getInstance(){
        if(instance == null){
            instance = new FirebaseControler();
        }
        return instance;
    }

    public void setUser(FirebaseUser user){
        this.user = user;
    }

    public void addFirestoreUser(User u){
        Map<String, Object> user = u.toMap();

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
