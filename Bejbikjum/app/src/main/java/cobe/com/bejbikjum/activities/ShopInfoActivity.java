package cobe.com.bejbikjum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import cobe.com.bejbikjum.activities.HomeActivity;
import cobe.com.bejbikjum.adapters.ProductTypeAdapter;

public class ShopInfoActivity extends AppCompatActivity implements ProductTypeAdapter.ItemClickListener {

    ProductTypeAdapter adapter;
    @BindView(R.id.continue_button)
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        ButterKnife.bind(this);

        String[] data = {"Women's shoes", "Men's shoes", "Sneakers", "Handbags", "Dresses", "Sports", "Men's jeans", "Women's jeans",
        "Men's suits", "Men's underwear", "Lingerie", "Skirts", "Men's hats", "Sunglasses", "Accessories"};
        int[] iconIds = {R.drawable.high_heels, R.drawable.shoes, R.drawable.running, R.drawable.handbag, R.drawable.dress,
                R.drawable.sport, R.drawable.jeansm, R.drawable.jeansw, R.drawable.suit, R.drawable.boxer, R.drawable.underwear,
                R.drawable.skirt, R.drawable.hatm, R.drawable.sunglasses, R.drawable.jewel};

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_types_recycler);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ProductTypeAdapter(this, data, iconIds);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("ShopInfo", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }


}
