package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_button) Button registerButton;


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


        //Click listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellerInfoIntent = new Intent(getApplicationContext(), SellerInfoActivity.class);
                startActivity(sellerInfoIntent);
            }
        });
    }
}
