package cobe.com.bejbikjum.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cobe.com.bejbikjum.R;
import cobe.com.bejbikjum.models.Item;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PagingAdapter extends RecyclerView.Adapter<PagingAdapter.MyViewHolder> {

    private ArrayList<Item> items;
    private Context context;

    public PagingAdapter(ArrayList<Item> itemList, Context context){
        this.items = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item image = items.get(position);
        List<String> imageUris = Arrays.asList(image.getStandard().replace("[","").replace("]", "").split("\\s*,\\s*"));

        Log.d("GalleryAdapter", imageUris.get(0));
        PagingAdapter.MyViewHolder imageHolder = (PagingAdapter.MyViewHolder) holder;

        RequestOptions myOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(10000).placeholder(R.drawable.handbag);
        Glide.with(context).load(imageUris.get(0))
                .thumbnail(0.5f)
                .transition(withCrossFade())
                .apply(myOptions)
                .into(imageHolder.itemImage);

        imageHolder.itemNameTextView.setText(image.getName());
        imageHolder.itemPriceTextView.setText(image.getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemNameTextView;
        private TextView itemPriceTextView;

        public MyViewHolder(View itemView){
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemNameTextView = (TextView) itemView.findViewById(R.id.item_name_preview);
            itemPriceTextView = (TextView) itemView.findViewById(R.id.item_price_preview);
        }
    }

    public void addItems(ArrayList<Item> items){
        for(Item item : items){
            this.items.add(item);
        }
        notifyDataSetChanged();
    }
}
