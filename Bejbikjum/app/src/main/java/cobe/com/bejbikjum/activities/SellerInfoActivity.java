package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.os.Bundle;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.activities.ShopInfoActivity;
import cobe.com.bejbikjum.controlers.AppControler;
import cobe.com.bejbikjum.controlers.FacebookControler;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.models.Seller;

public class SellerInfoActivity extends AppCompatActivity {

    @BindView(R.id.continue_button)
    Button continueButton;
    @BindView(R.id.password_input)
    EditText passwordInput;
    @BindView(R.id.password_repeat_input)
    EditText repeatPassInput;
    @BindView(R.id.email_input)
    EditText emailInput;
    @BindView(R.id.full_name_input)
    EditText fullNameInput;
    @BindView(R.id.phone_number_input)
    EditText phoneInput;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;


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

        setContentView(R.layout.activity_seller_info);
        ButterKnife.bind(this);

        hideProgressBar();
        setFacebookLoginButton();


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Seller seller = validateForm();
                if(seller!=null) {
                    AppControler.getInstance().setSeller(true);
                    AppControler.getInstance().setCurrentSeller(seller);

                    Intent shopInfoIntent = new Intent(getApplicationContext(), ShopInfoActivity.class);
                    startActivity(shopInfoIntent);
                }
            }
        });


    }

    private Seller validateForm() {

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

        String passwordRepeat = repeatPassInput.getText().toString();
        if (TextUtils.isEmpty(passwordRepeat)) {
            repeatPassInput.setError("Required.");
            valid = false;
        } else if(password.compareTo(passwordRepeat)!=0){
            repeatPassInput.setError("Passwords don't match.");
            valid = false;
        }
        else{
            repeatPassInput.setError(null);
        }

        String fullName = fullNameInput.getText().toString();
        if (TextUtils.isEmpty(fullName)) {
            fullNameInput.setError("Required.");
            valid = false;
        } else {
            fullNameInput.setError(null);
        }

        String phone = phoneInput.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneInput.setError("Required.");
            valid = false;
        } else {
            phoneInput.setError(null);
        }

        if(valid){
            Seller seller = new Seller("", "", email, password, "", fullName, phone);
            return seller;
        }
        else return null;
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
                FirebaseControler.getInstance().handleFacebookAccessTokenSeller(FacebookControler.getInstance().getToken());
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
        continueButton.setVisibility(View.INVISIBLE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        continueButton.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
