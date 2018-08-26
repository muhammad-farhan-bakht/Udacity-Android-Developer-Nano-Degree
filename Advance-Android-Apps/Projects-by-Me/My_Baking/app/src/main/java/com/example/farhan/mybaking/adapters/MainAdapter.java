package com.example.farhan.mybaking.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private Context context;
    private List<Recipe> recipesArrayList;
    private MainAdapterOnClickHandler mClickHandler;

    public interface MainAdapterOnClickHandler {
        void onClick(Recipe recipeObj);
    }

    public void setMainAdapterOnClickHandler(MainAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.main_item_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        final Recipe recipeObj = recipesArrayList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickHandler != null) {
                    mClickHandler.onClick(recipeObj);
                }
            }
        });

        if (recipeObj.getImage() != null && !recipeObj.getImage().equals("")) {
            Picasso.with(context)
                    .load(recipeObj.getImage())
                    .placeholder(R.drawable.img_loading_from_url)
                    .error(R.drawable.img_no_image_found)
                    .into(holder.imgRecipe);
        } else {
            Picasso.with(context)
                    .load(R.drawable.img_no_image_found)
                    .into(holder.imgRecipe);
            holder.recipeName.setBackgroundColor(Color.parseColor("#BCAAA4"));
        }

        if (recipeObj.getName() != null) {
            holder.recipeName.setText(recipeObj.getName());
        } else {
            holder.recipeName.setText(context.getResources().getString(R.string.recipe_dummy_name));
        }
    }

    @Override
    public int getItemCount() {
        if (null == recipesArrayList) return 0;
        return recipesArrayList.size();
    }

    class MainHolder extends RecyclerView.ViewHolder {

        ImageView imgRecipe;
        TextView recipeName;

        MainHolder(View itemView) {
            super(itemView);

            imgRecipe = itemView.findViewById(R.id.img_main_item_view_recipe_image);
            recipeName = itemView.findViewById(R.id.tv_main_item_view_recipe_name);
        }
    }

    public void setRecipeData(List<Recipe> recipesArrayList) {
        this.recipesArrayList = recipesArrayList;
        notifyDataSetChanged();
    }
}
