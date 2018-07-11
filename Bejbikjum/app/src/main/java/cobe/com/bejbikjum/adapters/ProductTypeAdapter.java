package cobe.com.bejbikjum.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cobe.com.bejbikjum.R;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ViewHolder> {

    private String[] data = new String[0];
    private int[] iconIds = new int[0];
    private LayoutInflater inflater;
    private ItemClickListener clickListener;
    Context context;

    // data is passed into the constructor
    public ProductTypeAdapter(Context context, String[] data, int[] iconIds) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.iconIds = iconIds;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_types_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productTypeName.setText(data[position]);
        holder.productTypeIcon.setImageDrawable(context.getResources().getDrawable(iconIds[position]));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return data.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productTypeName;
        ImageView productTypeIcon;

        ViewHolder(View itemView) {
            super(itemView);
            productTypeName = (TextView) itemView.findViewById(R.id.product_type_name);
            productTypeIcon = (ImageView) itemView.findViewById(R.id.product_type_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return data[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
