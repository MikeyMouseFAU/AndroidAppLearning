package learing01.android.mh.learningandroidapps.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import learing01.android.mh.learningandroidapps.Interface.ItemClickListener;
import learing01.android.mh.learningandroidapps.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.menu_name);
        imageView =  itemView.findViewById(R.id.menu_image);  //menu_image ??

        itemView.setOnClickListener(this);
    }

    public ItemClickListener setItemClickListener(ItemClickListener itemClickListener) {
        return this.itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
