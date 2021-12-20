package com.mcommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcommerce.model.Recipe;
import com.mcommerce.nhom8.R;

import java.util.List;

public class ListRecipeAdapter extends BaseAdapter {

    Activity context;
    int item_layout;
    List<Recipe> listRecipe;

    public ListRecipeAdapter(Activity context, int item_layout, List<Recipe> listRecipe) {
        this.context = context;
        this.item_layout = item_layout;
        this.listRecipe = listRecipe;
    }

    @Override
    public int getCount() {
        return listRecipe.size();
    }

    @Override
    public Object getItem(int i) {
        return listRecipe.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(item_layout,null);
            holder.imvRecipeThumb=view.findViewById(R.id.imvRecipeThumb_ListRecipe);
            holder.txtRecipeName=view.findViewById(R.id.txtRecipeName_ListRecipe);
            holder.txtRecipeLike=view.findViewById(R.id.txtRecipeLike_ListRecipe);
            holder.chkLike1=view.findViewById(R.id.chkLike1);

            view.setTag(holder);
        }else {
            holder= (ListRecipeAdapter.ViewHolder) view.getTag();
        }

        return view;
    }

    public static class ViewHolder{
        TextView txtRecipeName,txtRecipeLike;
        ImageView imvRecipeThumb;
        CheckBox chkLike1;
    }
}
