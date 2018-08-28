package cobe.com.bejbikjum.controlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import cobe.com.bejbikjum.activities.HomeActivity;
import cobe.com.bejbikjum.activities.RegisterActivity;
import cobe.com.bejbikjum.models.User;


public class FirebaseControler {

    //current Context
    private Context currentContext;
    private Activity currentActivity;
    private Boolean registrationFailed;

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
        registrationFailed = false;
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

    public void createAccount(final String email, final String password) {

        Log.d(TAG, "createAccount:" + email);

//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            sendEmailVerification();
                            addFirestoreUser(user, null, password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(currentContext, "Email registration failed", Toast.LENGTH_LONG);
                            registrationFailed = true;
                            user = null;
                            updateUI(user);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void sendEmailVerification() {
        // Disable button

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(currentActivity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Toast.makeText(currentContext,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(currentContext,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser u){
        if (user != null) {
            // TODO: 27.8.18.
            //Ubaciti load podataka sa firestore-a

            Log.d(TAG, "User logged in");

            Intent homeIntent = new Intent(currentContext.getApplicationContext(), HomeActivity.class);
            currentContext.startActivity(homeIntent);

//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));


        } else {
            registrationFailed = true;
            Log.d(TAG, "User not logged in");
            Intent registerIntent = new Intent(currentContext.getApplicationContext(), RegisterActivity.class);
            currentContext.startActivity(registerIntent);
        }
    }

    public void checkCurrentUser(){
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(currentContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }

    public void setContext(Context context, Activity activity){
        currentContext = context;
        currentActivity = activity;
    }

    public void addFirestoreUser(final FirebaseUser u, User appUser, String password){
        Map<String, Object> userMap = null;

        if(appUser != null) {
             userMap = appUser.toMap();
        }
        else if(u != null){
            User newUser = new User(u.getUid(),"", u.getEmail(), password, "", "");
            userMap = newUser.toMap();
        }
        else{
            //ERROR
            updateUI(null);
            return;
        }

        db.collection("users")
                .document(u.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateUI(u);
                        Log.d(TAG, "DocumentSnapshot added with ID: " + u.getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        updateUI(null);
                    }
                });
    }

    public Boolean getRegistrationFailed() {
        return registrationFailed;
    }

    public void setRegistrationFailed(Boolean registrationFailed) {
        this.registrationFailed = registrationFailed;
    }
}
