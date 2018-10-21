package com.jay.multiplerecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jay.multiplerecyclerview.R;
import com.jay.multiplerecyclerview.model.CustomModel;

import java.util.List;

/**
 * Created by Jay on 14-10-2018.
 */

public  class MarksChildAdapter extends RecyclerView.Adapter<MarksChildAdapter.ViewHolder> {

    List<CustomModel> lstCustom;
    public MarksChildAdapter(List<CustomModel> customModels) {
        this.lstCustom =  customModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_recycler_view_rowlayout,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSubjectName.setText(lstCustom.get(position).getName() != null ?
                lstCustom.get(position).getName() : "");

        holder.tvMarks.setText(lstCustom.get(position).getMarks() != null ?
                lstCustom.get(position).getMarks() : "");

    }

    @Override
    public int getItemCount() {
        return lstCustom != null ? lstCustom.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSubjectName;
        public TextView tvMarks;

        public ViewHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);
            tvSubjectName = (TextView) view.findViewById(R.id.tvSubjectName);
            tvMarks = (TextView) view.findViewById(R.id.tvMarks);
        }
    }
}