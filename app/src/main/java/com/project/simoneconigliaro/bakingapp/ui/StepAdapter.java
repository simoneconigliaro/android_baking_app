package com.project.simoneconigliaro.bakingapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.simoneconigliaro.bakingapp.R;
import com.project.simoneconigliaro.bakingapp.models.Recipe;
import com.project.simoneconigliaro.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 10/12/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private ArrayList<Step> mSteps;
    stepAdapterOnClickHandler mOnClickHandler;

    public StepAdapter(stepAdapterOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.idTextView.setText(String.valueOf(mSteps.get(position).getId()));
        holder.shortDescriptionTextView.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }


    public interface stepAdapterOnClickHandler{
        void onListItemClick(ArrayList<Step> steps, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_id)
        TextView idTextView;
        @BindView(R.id.tv_short_description)
        TextView shortDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            int position = mSteps.get(adapterPosition).getId();
            mOnClickHandler.onListItemClick(mSteps, position);

        }
    }
    public void setStepData (ArrayList<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }
}
