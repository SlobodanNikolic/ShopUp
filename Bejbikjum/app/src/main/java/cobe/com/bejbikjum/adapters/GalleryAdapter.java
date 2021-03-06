package cobe.com.bejbikjum.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cobe.com.bejbikjum.DAO.LocalDB;
import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Item;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by Lincoln on 31/03/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> images;
    private Context mContext;
    private int currentImageIndex =0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView itemNameTextView;
        public TextView itemPriceTextView;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            itemNameTextView = (TextView) view.findViewById(R.id.item_name_preview);
            itemPriceTextView = (TextView) view.findViewById(R.id.item_price_preview);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        public TextView ad_title;

        public AdViewHolder(View view) {
            super(view);
            ad_title = (TextView) view.findViewById(R.id.ad_title);
        }
    }


    public GalleryAdapter(Context context, List<Item> images) {
        mContext = context;
        this.images = images;
        Log.d("Gallery", "Creating gallery adapter");
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(position%12==0) return 1;
        else return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d("GalleryAdapter", "ViewHolder created");
        if(viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_thumbnail, parent, false);

            return new MyViewHolder(itemView);
        }
        else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ad_text_thumbnail, parent, false);

            return new AdViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType()==0) {

            int positionWithAds = position - (position/12 +1);
            Item image = images.get(positionWithAds);
            List<String> imageUris = Arrays.asList(image.getStandard().replace("[","").replace("]", "").split("\\s*,\\s*"));

            Log.d("GalleryAdapter", imageUris.get(0));
            MyViewHolder imageHolder = (MyViewHolder) holder;

            RequestOptions myOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .timeout(10000).placeholder(R.drawable.handbag);
            Glide.with(mContext).load(imageUris.get(0))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(myOptions)
                    .into(imageHolder.thumbnail);
            imageHolder.itemNameTextView.setText(image.getName());
            imageHolder.itemPriceTextView.setText(image.getPrice()+"");
        }
        else{
            Log.d("GalleryAdapter", "View type AD");
        }
    }

    @Override
    public int getItemCount() {
        return images.size() + images.size()/12+1;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}