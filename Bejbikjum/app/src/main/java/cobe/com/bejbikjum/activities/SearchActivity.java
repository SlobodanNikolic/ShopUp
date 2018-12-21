package cobe.com.bejbikjum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.w3c.dom.Text;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.ProductTypeAdapter;
import cobe.com.bejbikjum.controlers.AppControler;

public class SearchActivity extends AppCompatActivity implements ProductTypeAdapter.ItemClickListener{


    public ImageView[] colorViews = new ImageView[10];
    public int imageViewSelected = 0;
    ProductTypeAdapter adapter;
    private EditText itemName;
    private EditText shopName;
    private RangeSeekBar<Integer> priceBar;
    private TextView minPrice;
    private TextView maxPrice;
    private TextView startSearchButton;
    private String itemTypeString;

    private String selectedColor;
    private String selectedItemName;
    private String selectedShopName;
    private int selectedPriceMin;
    private int selectedPriceMax;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_search);

        colorViews[0]=(ImageView)findViewById(R.id.color1s);
        colorViews[1]=(ImageView)findViewById(R.id.color2s);
        colorViews[2]=(ImageView)findViewById(R.id.color3s);
        colorViews[3]=(ImageView)findViewById(R.id.color4s);
        colorViews[4]=(ImageView)findViewById(R.id.color5s);
        colorViews[5]=(ImageView)findViewById(R.id.color6s);
        colorViews[6]=(ImageView)findViewById(R.id.color7s);
        colorViews[7]=(ImageView)findViewById(R.id.color8s);
        colorViews[8]=(ImageView)findViewById(R.id.color9s);
        colorViews[9]=(ImageView)findViewById(R.id.color10s);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.type_search_recycler);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new ProductTypeAdapter(this, AppControler.getInstance().getData(), AppControler.getInstance().getIconIds());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        priceBar = (RangeSeekBar<Integer>) findViewById(R.id.rangeSeekBar);
        priceBar.setRangeValues(0, 100000);
        priceBar.setNotifyWhileDragging(true);

        minPrice = (TextView) findViewById(R.id.min_price);
        maxPrice = (TextView) findViewById(R.id.max_price);

        itemName = (EditText) findViewById(R.id.item_name_search);
        shopName = (EditText) findViewById(R.id.shop_name_search);


        for(ImageView view : colorViews){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAllColorBorders();
                    view.setBackgroundResource(R.drawable.square_border);
                    // TODO: 10/8/18 Uraditi akciju
                    selectedColor = view.getTag().toString();
                }
            });
        }

        priceBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                minPrice.setText(minValue.toString());
                maxPrice.setText(maxValue.toString());
                selectedPriceMin = minValue;
                selectedPriceMax = maxValue;
            }
        });

        startSearchButton = (TextView) findViewById(R.id.start_search_button);
        startSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPriceMax == 0)
                    selectedPriceMax = 100000;

                // TODO: 10/8/18 Start search
                selectedItemName = itemName.getText().toString();
                selectedShopName = shopName.getText().toString();

                Intent homeSearchIntent = new Intent(getApplicationContext(), HomeActivity.class);
                homeSearchIntent.putExtra("action", "search");
                homeSearchIntent.putExtra("itemName", selectedItemName);
                homeSearchIntent.putExtra("shopName", selectedShopName);
                homeSearchIntent.putExtra("itemType",  itemTypeString);
                homeSearchIntent.putExtra("itemPriceMin", selectedPriceMin);
                homeSearchIntent.putExtra("itemPriceMax", selectedPriceMax);
                homeSearchIntent.putExtra("itemColor", selectedColor);
                startActivity(homeSearchIntent);
            }
        });

    }

    public void removeAllColorBorders(){
        for(ImageView image : colorViews){
            image.setBackgroundResource(0);
        }
    }

    @Override
    public void onItemClick(View view, int position, LinearLayout productLayout, ImageView productTypeIcon, TextView productTypeName, boolean selected) {
        if(selected){
            productLayout.setBackgroundResource(R.drawable.square_border);
            // TODO: 10/8/18 Akcija
            itemTypeString = productTypeName.getText().toString();
        }
        else {
            productLayout.setBackgroundResource(0);
        }

    }
}
