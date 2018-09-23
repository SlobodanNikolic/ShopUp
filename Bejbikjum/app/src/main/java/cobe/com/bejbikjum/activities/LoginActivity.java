package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.controlers.FacebookControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.password_input) EditText passwordInput;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;
    @BindView(R.id.textView2) TextView registrationButton;



    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";
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

        setContentView(R.layout.activity_login);
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
                    FirebaseControler.getInstance().logInWithEmailAndPassword(emailInput.getText().toString(),
                            passwordInput.getText().toString());
                }
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
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

        return valid;
    }


    @Override
    public void onStart() {
        super.onStart();

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
