package cobe.com.bejbikjum.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.Timestamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.GalleryAdapter;
import cobe.com.bejbikjum.adapters.PagingAdapter;
import cobe.com.bejbikjum.app.AppController;
import cobe.com.bejbikjum.controlers.FirebaseControler;
import cobe.com.bejbikjum.controlers.LocalDBControler;
import cobe.com.bejbikjum.models.Item;

public class HomeActivity extends AppCompatActivity {

    private String TAG = HomeActivity.class.getSimpleName();
    private static final String endpoint = "https://api.androidhive.info/json/glide.json";
    private ArrayList<Item> items;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private PagingAdapter pagingAdapter;
    private RecyclerView recyclerView;
    private Button topRatedButton;
    private Button hotButton;
    private Button editorsChoiceButton;
    private Button featuredButton;
    private Button randomButton;
    private Button newButton;
    private Button searchButton;
    private Button profileButton;
    private GridLayoutManager mlayoutManager;

    private int pageNumber = 1;
    private int itemCount = 50;

    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private int viewTreshold = 0;

    public enum TAB_ID{
        TOP, NEW, RANDOM, SEARCHED;
    }

    private TAB_ID currentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();

        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchButton = (Button) findViewById(R.id.search_button);

        pDialog = new ProgressDialog(this);
        items = new ArrayList<>();
//        mAdapter = new GalleryAdapter(getApplicationContext(), items);

        pagingAdapter = new PagingAdapter(items, HomeActivity.this);

//        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        mlayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);

        recyclerView.setAdapter(pagingAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                int positionWithAds = position - (position/12 +1);

                Intent imageDetailsIntent = new Intent(getApplicationContext(), ImageDetailsActivity.class);
                imageDetailsIntent.putExtra("item", items.get(position));
                startActivity(imageDetailsIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mlayoutManager.getChildCount();
                totalItemCount = mlayoutManager.getItemCount();
                pastVisibleItems = mlayoutManager.findFirstVisibleItemPosition();

                if(dy > 0){
                    Log.d(TAG, "Scrolling down...");

                    if(isLoading){
                        Log.d(TAG, "Currently downloading...");
                        if(totalItemCount > previousTotal){
                            Log.d(TAG, "Download stopped...");
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if(!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems+visibleItemCount)){

                        Log.d(TAG, "Time to refresh...");
                        isLoading = true;

                        if(currentTab == TAB_ID.TOP) {
                            getTopRated();
                            Log.d(TAG, "Getting top rated.");

                        }
                        else if(currentTab == TAB_ID.NEW) {
                            getNewItems();
                            Log.d(TAG, "Getting new items");

                        }
                        else if(currentTab == TAB_ID.SEARCHED){
                            getSearched();
                            Log.d(TAG, "Getting searched items");
                        }

                    }
                }
            }
        });

        setListeners();

        String action = getIntent().getExtras().getString("action");
        if(action != null && action.compareTo("search") == 0){
            Log.d(TAG, "Activity started with search action");
            getSearched();
        }
        else {
            Log.d(TAG, "Activity started with top rated action");

            getTopRated();
        }

        topRatedButton = (Button) findViewById(R.id.top_rated_button);
        hotButton = (Button)findViewById(R.id.hot_button);
        editorsChoiceButton = (Button)findViewById(R.id.editors_choice_button);
        featuredButton = (Button)findViewById(R.id.featured_button);
        randomButton = (Button)findViewById(R.id.random_button);
        newButton = (Button)findViewById(R.id.new_button);
        profileButton = (Button)findViewById(R.id.profile_button);

        topRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Top Rated clicked");
                items.clear();
                pagingAdapter.notifyDataSetChanged();
                FirebaseControler.getInstance().setLastVisibleTopRated(null);
                FirebaseControler.getInstance().setLastVisibleNew(null);
                FirebaseControler.getInstance().setLastVisibleRandom(null);
                FirebaseControler.getInstance().setLastVisibleSearched(null);

                getTopRated();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"New button clicked");
                items.clear();
                pagingAdapter.notifyDataSetChanged();
                FirebaseControler.getInstance().setLastVisibleNew(null);
                FirebaseControler.getInstance().setLastVisibleTopRated(null);
                FirebaseControler.getInstance().setLastVisibleRandom(null);
                FirebaseControler.getInstance().setLastVisibleSearched(null);

                getNewItems();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
    }

    public void getSearched(){
        FirebaseControler.getInstance().getSearchedItems(getIntent().getStringExtra("shopName"),
                getIntent().getStringExtra("itemName"),
                getIntent().getStringExtra("itemType"),
                getIntent().getIntExtra("itemPriceMin", 0), getIntent().getIntExtra("itemPriceMax", 100000),
                getIntent().getStringExtra("itemColor"));
        currentTab = TAB_ID.SEARCHED;
    }

    public void getTopRated(){

        FirebaseControler.getInstance().getTopRated();
        currentTab = TAB_ID.TOP;
    }

    public void getNewItems(){
//        items.clear();
//        FirebaseControler.getInstance().setLastVisibleTopRated(null);
//        FirebaseControler.getInstance().setLastVisibleRandom(null);
        FirebaseControler.getInstance().getNewItems();
        currentTab = TAB_ID.NEW;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        setListeners();
    }


    public void setListeners(){
        FirebaseControler.getInstance().setDownloadListener(new FirebaseControler.DownloadListener() {
            @Override
            public void onTopRated(ArrayList<Item> topRatedItems) {
                isLoading = false;
                if(topRatedItems!=null) {
                    Log.d(TAG, "Top rated items ready: " + topRatedItems.size());
//                    items.clear();

                    for(Item item : topRatedItems)
                        items.add(item);

//                    mAdapter.notifyDataSetChanged();
                    pagingAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "No top rated items");
                }
            }

            @Override
            public void onTopRatedFailed(){
                Log.d(TAG, "Error getting top rated items");
            }

            @Override
            public void onNewItems(ArrayList<Item> newItems) {
                isLoading = false;
                if(newItems!=null) {
                    Log.d(TAG, "New items ready: " + newItems.size());
//                    items.clear();

                    for(Item item : newItems)
                        items.add(item);

                    pagingAdapter.notifyDataSetChanged();
//                    mAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "No new items");
                }
            }

            @Override
            public void onNewItemsFailed(){
                Log.d(TAG, "Error getting new items");
            }

            @Override
            public void onItemLiked() {

            }

            @Override
            public void onItemLikeFailed() {

            }

            @Override
            public void onRandomItems(ArrayList<Item> randomItems) {
                isLoading = false;
                if(randomItems!=null) {
                    Log.d(TAG, "Random items ready: " + randomItems.size());
                    items.clear();

                    for(Item item : randomItems)
                        items.add(item);

                    pagingAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "No random items");
                }
            }

            @Override
            public void onRandomItemsFailed() {

            }

            @Override
            public void onSearched(ArrayList<Item> searchedItems) {
                isLoading = false;
                if(searchedItems!=null) {
                    Log.d(TAG, "Searched items ready: " + searchedItems.size());
                    items.clear();

                    for(Item item : searchedItems)
                        items.add(item);

                    pagingAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "No searched items");
                }
            }

            @Override
            public void onSearchedFailed() {

            }

        });
    }

}
