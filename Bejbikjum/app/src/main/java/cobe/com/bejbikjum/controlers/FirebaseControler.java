package cobe.com.bejbikjum.controlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

import cobe.com.bejbikjum.activities.HomeActivity;
import cobe.com.bejbikjum.activities.MyShopActivity;
import cobe.com.bejbikjum.activities.RegisterActivity;
import cobe.com.bejbikjum.activities.SellerInfoActivity;
import cobe.com.bejbikjum.activities.ShopInfoActivity;
import cobe.com.bejbikjum.models.Item;
import cobe.com.bejbikjum.models.Seller;
import cobe.com.bejbikjum.models.User;


public class FirebaseControler {

    //current Context
    private Context currentContext;

    public Context getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    private Activity currentActivity;
    private Boolean registrationFailed;

    private static final String TAG = "FirebaseControler";
    private static FirebaseControler instance;
    private FirebaseUser user;
    private FirebaseFirestore db;

    //Authentication
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    public DocumentSnapshot getLastVisibleRandom() {
        return lastVisibleRandom;
    }

    public void setLastVisibleRandom(DocumentSnapshot lastVisibleRandom) {
        this.lastVisibleRandom = lastVisibleRandom;
    }

    public DocumentSnapshot getLastVisibleNew() {
        return lastVisibleNew;
    }

    public void setLastVisibleNew(DocumentSnapshot lastVisibleNew) {
        this.lastVisibleNew = lastVisibleNew;
    }

    private DocumentSnapshot lastVisibleRandom;

    public void setLastVisibleTopRated(DocumentSnapshot lastVisibleTopRated) {
        this.lastVisibleTopRated = lastVisibleTopRated;
    }

    public DocumentSnapshot getLastVisibleTopRated() {

        return lastVisibleTopRated;
    }

    public void setLastVisibleSearched(DocumentSnapshot lastVisibleSearched) {
        this.lastVisibleSearched = lastVisibleSearched;
    }

    public DocumentSnapshot getLastVisibleSearched() {

        return lastVisibleSearched;
    }

    private DocumentSnapshot lastVisibleNew;
    private DocumentSnapshot lastVisibleTopRated;
    private DocumentSnapshot lastVisibleSearched;


    private FirebaseControler(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        registrationFailed = false;
        resetTopRatedListener();
    };

    public void resetTopRatedListener(){
        this.listener = null;
    }

    public static FirebaseControler getInstance(){
        if(instance == null){
            instance = new FirebaseControler();
        }
        return instance;
    }

    public void setUser(FirebaseUser user){
        this.user = user;
    }

    public FirebaseUser getCurrentUser(){
        return this.user;
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

    public void createSellerAccount(final String email, final String password) {

        Log.d(TAG, "createAccount:" + email);

//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createSellerWithEmail:success");
                            user = mAuth.getCurrentUser();
                            AppControler.getInstance().getCurrentSeller().setUid(user.getUid());
                            sendEmailVerification();
                            addFirestoreSeller(null, AppControler.getInstance().getCurrentSeller(), password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(currentContext, "Email registration failed", Toast.LENGTH_LONG);
                            registrationFailed = true;
                            user = null;
                            updateUI(user);
                        }

                        // TODO: 8.9.18.
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

    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser u){
        if (u != null) {
            // TODO: 27.8.18.
            //Ubaciti load podataka sa firestore-a
            loadFirestoreUserById(u);
            Log.d(TAG, "User logged in");

        } else {
            registrationFailed = true;
            Log.d(TAG, "User not logged in");
            Intent registerIntent = new Intent(currentContext.getApplicationContext(), RegisterActivity.class);
            currentContext.startActivity(registerIntent);
        }
    }

    private void goToHomeScreen(Map<String, Object> document){
        saveUserToLocalDB(document);

        Intent homeIntent = new Intent(currentContext.getApplicationContext(), HomeActivity.class);
        currentContext.startActivity(homeIntent);
    }

    private void updateUISeller(){
        if (user != null) {
            // TODO: 27.8.18.
            //Ubaciti load podataka sa firestore-a
            loadFirestoreSellerById(user.getUid());
            Log.d(TAG, "User logged in");


        } else {
            registrationFailed = true;
            Log.d(TAG, "User not logged in");
            Intent registerIntent = new Intent(currentContext.getApplicationContext(), RegisterActivity.class);
            currentContext.startActivity(registerIntent);
        }
    }

    private void registrationFailed(){
        registrationFailed = true;
        Log.d(TAG, "User not logged in");
        Intent registerIntent = new Intent(currentContext.getApplicationContext(), RegisterActivity.class);
        currentContext.startActivity(registerIntent);
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

    public void handleRegisterFacebookAccessToken(AccessToken token) {
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
                            addFirestoreUser(user, null, "");
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

    public void handleFacebookAccessTokenSeller(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessTokenSeller:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(currentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            updateUISeller();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(currentContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUISeller();
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
            User newUser = new User(u.getUid(),"", u.getEmail(), password, "", "", "");
            userMap = newUser.toMap();
        }
        else{
            //ERROR
            updateUI(null);
            return;
        }

        final Map<String, Object> finalUserMap = userMap;

        db.collection("users")
                .document(u.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        goToHomeScreen(finalUserMap);
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

    public void addFirestoreSeller(final FirebaseUser u, Seller appSeller, String password){
        Map<String, Object> userMap = null;
        String uid;

        if(appSeller != null) {
            userMap = appSeller.toMap();
            uid = appSeller.getUid();
        }
        else if(u != null){
            Seller newSeller = new Seller(u.getUid(),"",  u.getEmail(), password, "", "", "");
            userMap = newSeller.toMap();
            uid = u.getUid();
        }
        else{
            //ERROR
            updateUISeller();
            return;
        }

        final Map<String, Object> finalUserMap = userMap;

        db.collection("sellers")
                .document(uid)
                .set(finalUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        saveSellerToLocalDB(finalUserMap);

                        Intent myShopIntent = new Intent(currentContext.getApplicationContext(), MyShopActivity.class);
                        currentContext.startActivity(myShopIntent);

                        Log.d(TAG, "DocumentSnapshot added with ID: " + finalUserMap.get("uid"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        registrationFailed();
                    }
                });
    }


    public void loadFirestoreUserById(final FirebaseUser u){

        final String uid = u.getUid();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        saveUserToLocalDB(document.getData());

                        Intent homeIntent = new Intent(currentContext.getApplicationContext(), HomeActivity.class);
                        currentContext.startActivity(homeIntent);

                    } else {
                        Log.d(TAG, "No such document");
                        checkForSellerOnFirestore(u);
                        // TODO: 30.8.18.
                        //Resiti sve mogucnosti
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void checkForSellerOnFirestore(final FirebaseUser user){
        DocumentReference docRef = db.collection("sellers").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // TODO: 17.9.18. A seller account with this email adress is already active
                        //Would you like to log in?
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        saveSellerToLocalDB(document.getData());

                        Intent myShopIntent = new Intent(currentContext.getApplicationContext(), MyShopActivity.class);
                        currentContext.startActivity(myShopIntent);

                    } else {
                        // TODO: 9/19/18 Ovome definitivno nije mesto ovde, proveriti o cemu se radi
                        //Radi se o facebook registraciji
//                        addFirestoreUser(user, null, "");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void logInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = task.getResult().getUser();
                            mAuth.updateCurrentUser(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            // TODO: 9/19/18 Videti razlog neuspeha i izbaciti poruku
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void loadFirestoreSellerById(String uid){

        DocumentReference docRef = db.collection("sellers").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        saveSellerToLocalDB(document.getData());

                        Intent myShopIntent = new Intent(currentContext.getApplicationContext(), MyShopActivity.class);
                        currentContext.startActivity(myShopIntent);

                    } else {
                        Log.d(TAG, "No such document");

                        Intent shopInfoIntent = new Intent(currentContext.getApplicationContext(), ShopInfoActivity.class);
                        currentContext.startActivity(shopInfoIntent);

                        // TODO: 30.8.18.
                        //Resiti sve mogucnosti
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void saveUserToLocalDB(Map<String, Object> userData){
        User user = new User(userData.get("uid").toString(),
                userData.get("username").toString(),
                userData.get("email").toString(),
                userData.get("password").toString(),
                userData.get("fbid").toString(),
                userData.get("fullName").toString(),
                userData.get("itemsLiked").toString());

        AppControler.getInstance().isSeller = false;
        AppControler.getInstance().setCurrentUser(user);
    }

    public void saveSellerToLocalDB(Map<String, Object> userData){
        Seller seller = new Seller(userData.get("uid").toString(),
                userData.get("shopName").toString(),
                userData.get("email").toString(),
                userData.get("password").toString(),
                userData.get("fbid").toString(),
                userData.get("fullName").toString(),
                userData.get("phoneNum").toString(),
                userData.get("itemTypes").toString());
        AppControler.getInstance().isSeller = true;
        AppControler.getInstance().setCurrentSeller(seller);
    }

    public Boolean getRegistrationFailed() {
        return registrationFailed;
    }

    public void setRegistrationFailed(Boolean registrationFailed) {
        this.registrationFailed = registrationFailed;
    }


    public void addItem(final Item item){
        String uid;

        final Map<String, Object> finalItemMap = item.toMap();

        db.collection("items/")
                .document()
                .set(finalItemMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: 12.9.18. Add item to local database 
//                        saveSellerToLocalDB(finalUserMap);
                        LocalDBControler.getInstance().saveItem(item);
                        
                        Intent myShopIntent = new Intent(currentContext.getApplicationContext(), MyShopActivity.class);
                        currentContext.startActivity(myShopIntent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: 13.9.18. Odraditi odgovarajucu akciju
                        Log.w(TAG, "Error adding document", e);
                        registrationFailed();
                    }
                });
    }


    public void showProgressBar(LinearLayout progressLayout){
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(LinearLayout progressLayout){
        progressLayout.setVisibility(View.INVISIBLE);
    }

    public void getAdvertized(){

    }

    public void getTopRated(){
        Log.d(TAG, "Getting top rated items");
        CollectionReference colRef = db.collection("items");

        if(lastVisibleTopRated != null){
            Log.d(TAG, "Last visible not null");

            colRef.orderBy("likes", Query.Direction.DESCENDING)
                    .startAfter(lastVisibleTopRated)
                    .limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleTopRated = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            Log.d(TAG, "No new top rated items");

                            // TODO: 9/26/18 Display a message: No new items
                            if (listener != null)
                                listener.onTopRated(null);
                            return;
                        }

                        ArrayList<Item> topRatedItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            topRatedItems.add(newItem);
                        }

                        if (listener != null)
                            listener.onTopRated(topRatedItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else {
            Log.d(TAG, "Getting top rated items, last visible is null");

            colRef.orderBy("likes", Query.Direction.DESCENDING).limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleTopRated = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            Log.d(TAG, "No new top rated items");

                            // TODO: 9/26/18 Display a message: No new items
                            return;
                        }
                        ArrayList<Item> topRatedItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            topRatedItems.add(newItem);
                        }

                        if (listener != null)
                            listener.onTopRated(topRatedItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    }

    public void getNewItems(){

        CollectionReference colRef = db.collection("items");

        if(lastVisibleNew != null){
            colRef.orderBy("timestamp")
                    .startAfter(lastVisibleNew)
                    .limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleNew = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            // TODO: 9/26/18 Display a message: No new items
                            return;
                        }
                        ArrayList<Item> newItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            newItems.add(newItem);
                        }

                        if(listener!=null)
                            listener.onNewItems(newItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else{
            colRef.orderBy("timestamp")
                    .limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleNew = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            // TODO: 9/26/18 Display a message: No new items
                            return;
                        }
                        ArrayList<Item> newItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            newItems.add(newItem);
                        }

                        if(listener!=null)
                            listener.onNewItems(newItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }






    }

    public void getRandom(){
        Random r = new Random();
        int result = r.nextInt(10000);

        CollectionReference postsRef = db.collection("items");
        Query next;

        if(lastVisibleRandom != null) {
            next = postsRef.whereGreaterThan("likes", result)
                    .orderBy("likes")
                    .startAfter(lastVisibleRandom)
                    .limit(50);
        }
        else{
            next = postsRef.whereGreaterThan("likes", result)
                    .orderBy("likes")
                    .limit(50);
        }

        next.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        // ...

                        if(documentSnapshots.size()==0) {
                            Log.d(TAG, "No adequate random items");
                        }

                        // Get the last visible document
                        if(documentSnapshots.size()>0) {
                            lastVisibleRandom = documentSnapshots.getDocuments()
                                    .get(documentSnapshots.size() - 1);
                        }
                        else{
                            // TODO: 9/26/18 isto ko i gore
                            return;
                        }

                        ArrayList<Item> randomItems = new ArrayList<Item>();

                        for(DocumentSnapshot document : documentSnapshots.getDocuments()){
                            Item item = new Item().parseMap(document.getData());
                            randomItems.add(item);
                        }

                        if(listener!=null)
                            listener.onRandomItems(randomItems);
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting random items", e);
                        if(listener!=null)
                            listener.onRandomItemsFailed();
                    }
                });


    }

    public void likeItem(Item item){

        int likes = item.getLikes()+1;

        final Map<String, Object> finalItemMap = new HashMap<>();
        finalItemMap.put("likes", likes);

        db.collection("items")
                .document(item.getId())
                .set(finalItemMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(listener!=null)
                            listener.onItemLiked();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        if(listener!=null)
                            listener.onItemLikeFailed();
                    }
                });

    }


    public void getSearchedItems(String shopName, String itemName, String itemType, int priceMin, int priceMax, String color){
        Log.d(TAG, "Getting items: " + shopName + ", " + itemName + ", " + itemType + ", "
                + priceMin + ", " + priceMax + ", " + color + ".");
        CollectionReference colRef = db.collection("items");

        if(lastVisibleSearched != null){
            colRef.whereEqualTo("itemType", itemType)
                    .whereEqualTo("shopName", shopName)
                    .whereEqualTo("name", itemName)
                    .whereEqualTo("color", color)
                    .whereGreaterThanOrEqualTo("price", priceMin)
                    .whereLessThanOrEqualTo("price", priceMax)
                    .startAfter(lastVisibleSearched)
                    .limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleSearched = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            // TODO: 9/26/18 Display a message: No new items
                            if (listener != null)
                                listener.onSearched(null);
                            return;
                        }

                        ArrayList<Item> searchedItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            searchedItems.add(newItem);
                        }

                        if (listener != null)
                            listener.onSearched(searchedItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else {
            colRef.whereEqualTo("itemType", itemType)
                    .whereEqualTo("shopName", shopName)
                    .whereEqualTo("name", itemName)
                    .whereEqualTo("colorString", color)
                    .whereGreaterThanOrEqualTo("price", priceMin)
                    .whereLessThanOrEqualTo("price", priceMax).limit(50)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if(documentSnapshots.size()>0)
                            lastVisibleSearched = documentSnapshots.get(documentSnapshots.size()-1);
                        else{
                            // TODO: 9/26/18 Display a message: No new items
                            return;
                        }
                        ArrayList<Item> searchedItems = new ArrayList<Item>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> itemMap = document.getData();
                            Item newItem = new Item().parseMap(itemMap);
                            newItem.setId(document.getId());
                            searchedItems.add(newItem);
                        }

                        if (listener != null)
                            listener.onSearched(searchedItems);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

    }

    public interface DownloadListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        // or when data has been loaded
        public void onTopRated(ArrayList<Item> topRatedItems);
        public void onTopRatedFailed();

        public void onNewItems(ArrayList<Item> newItems);
        public void onNewItemsFailed();

        public void onItemLiked();
        public void onItemLikeFailed();

        public void onRandomItems(ArrayList<Item> randomItems);
        public void onRandomItemsFailed();

        public void onSearched(ArrayList<Item> searchedItems);
        public void onSearchedFailed();
    }

    // Step 2 - This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private DownloadListener listener;

    // Assign the listener implementing events interface that will receive the events
    public void setDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }


}
