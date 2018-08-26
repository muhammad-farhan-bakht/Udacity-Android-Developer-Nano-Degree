package com.example.farhan.mybaking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farhan.mybaking.R;
import com.example.farhan.mybaking.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    private List<Step> stepsArrayList;
    private StepsAdapterOnClickHandler mClickHandler;

    public interface StepsAdapterOnClickHandler {
        void onClick(Step stepObj);
    }

    public void StepsAdapterOnClickHandler(StepsAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public StepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.details_steps_item_view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsHolder holder, int position) {
        final Step stepObj = stepsArrayList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickHandler != null) {
                    mClickHandler.onClick(stepObj);
                }
            }
        });
        if (stepObj.getShortDescription() != null && !stepObj.getShortDescription().isEmpty())
            holder.stepName.setText(stepObj.getShortDescription());

        holder.stepCounter.setText(position+".");
    }

    @Override
    public int getItemCount() {
        if (null == stepsArrayList) return 0;
        return stepsArrayList.size();
    }

    class StepsHolder extends RecyclerView.ViewHolder {
        TextView stepCounter;
        TextView stepName;

        StepsHolder(View itemView) {
            super(itemView);
            stepCounter = itemView.findViewById(R.id.tv_details_item_view_steps_counter);
            stepName = itemView.findViewById(R.id.tv_details_item_view_steps_name);
        }
    }

    public void setStepsData(List<Step> stepsArrayList) {
        this.stepsArrayList = stepsArrayList;
        notifyDataSetChanged();
    }
}
