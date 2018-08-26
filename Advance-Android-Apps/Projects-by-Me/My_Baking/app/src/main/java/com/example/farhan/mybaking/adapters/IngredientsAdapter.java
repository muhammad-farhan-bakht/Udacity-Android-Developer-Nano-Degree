package com.example.farhan.mybaking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Ingredient;

import java.util.List;
import java.util.Locale;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {

    private List<Ingredient> ingredientsArrayList;

    @NonNull
    @Override
    public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.details_ingreidents_item_view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {

        StringBuilder stringBuilderValue = new StringBuilder();

        for (int i = 0; i < ingredientsArrayList.size(); i++) {
            Ingredient ingredientsObj = ingredientsArrayList.get(i);
            stringBuilderValue.append(String.format(Locale.getDefault(), "• %s (%d %s)", ingredientsObj.getIngredient(), ingredientsObj.getQuantity(), ingredientsObj.getMeasure()));
            if (i != ingredientsArrayList.size() - 1)
                stringBuilderValue.append("\n");
        }
        holder.recipeName.setText(stringBuilderValue);
    }

    @Override
    public int getItemCount() {
        if (null == ingredientsArrayList) return 0;
        return 1;
    }

    class IngredientsHolder extends RecyclerView.ViewHolder {

        TextView recipeName;

        IngredientsHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.tv_details_item_view_ingredients);
        }
    }

    public void setIngredientsData(List<Ingredient> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
        notifyDataSetChanged();
    }
}
