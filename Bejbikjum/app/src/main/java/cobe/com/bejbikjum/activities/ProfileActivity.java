package cobe.com.bejbikjum.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.SlidingImageAdapter;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.models.Item;

public class ProfileActivity extends AppCompatActivity {


    @BindView(R.id.logout_button_textview)
    TextView logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        FirebaseControler.getInstance().setCurrentContext(this);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseControler.getInstance().signOut();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
    }


}
