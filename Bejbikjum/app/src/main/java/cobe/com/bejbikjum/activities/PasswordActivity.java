package cobe.com.bejbikjum.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cobe.com.bejbikjum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordActivity extends AppCompatActivity {

    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.pass_input)
    EditText passInput;
    @BindView(R.id.pass_repeat_input) EditText passRepeatInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_password);

        ButterKnife.bind(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
