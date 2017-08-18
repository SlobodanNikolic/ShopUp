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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.adapters.GalleryAdapter;
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
    private RecyclerView recyclerView;

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

        pDialog = new ProgressDialog(this);
        items = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), items);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int positionWithAds = position - (position/12 +1);

                Intent imageDetailsIntent = new Intent(getApplicationContext(), ImageDetailsActivity.class);
                imageDetailsIntent.putExtra("item", items.get(positionWithAds));
                startActivity(imageDetailsIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FirebaseControler.getInstance().setDownloadListener(new FirebaseControler.DownloadListener() {
            @Override
            public void onTopRated(ArrayList<Item> topRatedItems) {
                if(topRatedItems!=null) {
                    Log.d(TAG, "Top rated items ready: " + topRatedItems.size());
                    items.clear();

                    for(Item item : topRatedItems)
                        items.add(item);

                    mAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "No top rated items");
                }
            }

            @Override
            public void onTopRatedFailed(){
                Log.d(TAG, "Error getting top rated items");
            }

        });

        getTopRated();
    }

    private void getTopRated(){
        items.clear();
        FirebaseControler.getInstance().getTopRated();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void fetchImages() {

        pDialog.setMessage("Downloading...");
        pDialog.show();

        Log.d(TAG,"Checking if there's any items in LocalDB");
        if(LocalDBControler.getInstance().getShopItems()!=null){
            items.clear();
            Log.d("MyShopActivity", "LocalDB has shop items");
            ArrayList<Item> itemsHelperList = new ArrayList<Item>(LocalDBControler.getInstance().getShopItems());

            for(Item item : itemsHelperList) {
                Log.d(TAG, item.toString());
                items.add(item);
            }
            mAdapter.notifyDataSetChanged();
            pDialog.hide();
        }
        else {
            JsonArrayRequest req = new JsonArrayRequest(endpoint,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            pDialog.hide();

                            items.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    Item image = new Item();
                                    image.setName(object.getString("name"));

                                    JSONObject url = object.getJSONObject("url");
//                                image.setSmall(url.getString("small"));
                                    image.setMedium(url.getString("medium"));
                                    image.setTimestamp(object.getString("timestamp"));

                                    items.add(image);

                                } catch (JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                }
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);
        }
    }

}
