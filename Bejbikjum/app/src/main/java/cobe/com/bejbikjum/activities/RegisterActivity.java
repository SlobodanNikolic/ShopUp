package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.controlers.FacebookControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_button) Button registerButton;
    @BindView(R.id.sellerButton)
    TextView sellerButton;
    @BindView(R.id.email_input) EditText emailInput;
    @BindView(R.id.password_input) EditText passwordInput;
    @BindView(R.id.password_repeat_input) EditText passwordRepeatInput;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;



    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        FirebaseControler.getInstance().setContext(this, this);

        hideProgressBar();
        setFacebookLoginButton();

        //Click listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateForm()) {
                    showProgressBar();
                    FirebaseControler.getInstance().createAccount(emailInput.getText().toString(), passwordInput.getText().toString());
                }
            }
        });

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellerInfoIntent = new Intent(getApplicationContext(), SellerInfoActivity.class);
                startActivity(sellerInfoIntent);
            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Required.");
            valid = false;
        } else {
            emailInput.setError(null);
        }

        String password = passwordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Required.");
            valid = false;
        } else {
            passwordInput.setError(null);
        }

        String passwordRepeat = passwordRepeatInput.getText().toString();
        if (TextUtils.isEmpty(passwordRepeat)) {
            passwordRepeatInput.setError("Required.");
            valid = false;
        } else if(password.compareTo(passwordRepeat)!=0){
            passwordRepeatInput.setError("Passwords don't match.");
            valid = false;
        }
        else{
            passwordRepeatInput.setError(null);
        }


        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(FirebaseControler.getInstance().getRegistrationFailed()){
            Toast.makeText(this, "Registration not completed. Please check your internet connection.",  Toast.LENGTH_LONG);
            return;
        }else {
            FirebaseControler.getInstance().checkCurrentUser();
        }
    }

    public void setFacebookLoginButton() {

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebook_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
            }
        });

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                FacebookControler.getInstance().setToken(loginResult.getAccessToken());
                FirebaseControler.getInstance().handleFacebookAccessToken(FacebookControler.getInstance().getToken());
                hideProgressBar();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                hideProgressBar();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                hideProgressBar();
                // ...
            }
        });
    }

    public void showProgressBar(){
        registerButton.setVisibility(View.INVISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        registerButton.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.INVISIBLE);
    }

// ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



}
