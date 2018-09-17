package cobe.com.bejbikjum.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Item;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class SlidingImageAdapter extends PagerAdapter {

    private Item clickedItem;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> imageUrls;


    public SlidingImageAdapter(Context context,Item clickedItem) {
        this.context = context;
        this.clickedItem = clickedItem;
        imageUrls = new ArrayList<String>();
        ArrayList<String> urls = new ArrayList<String>(Arrays.asList(clickedItem.getStandard().replace("[", "").replace("]","").split("\\s*,\\s*")));
        for (String url : urls){
            imageUrls.add(url);
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        ArrayList<String> urls = new ArrayList<String>(Arrays.asList(clickedItem.getStandard().split(",")));
        return urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_image, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        RequestOptions myOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(imageUrls.get(position))
                .thumbnail(0.5f)
                .transition(withCrossFade())
                .apply(myOptions)
                .into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}