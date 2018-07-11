package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.ShopInfoActivity;

public class SellerInfoActivity extends AppCompatActivity {

    @BindView(R.id.continue_button)
    Button continueButton;

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

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopInfoIntent = new Intent(getApplicationContext(), ShopInfoActivity.class);
                startActivity(shopInfoIntent);
            }
        });

    }
}
