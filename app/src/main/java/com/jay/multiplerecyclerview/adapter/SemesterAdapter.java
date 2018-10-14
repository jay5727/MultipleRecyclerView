package com.jay.multiplerecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jay.multiplerecyclerview.R;
import com.jay.multiplerecyclerview.model.CustomModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Jay on 14-10-2018.
 */
public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder>
        //implements View.OnClickListener
{
    public LinkedHashMap<String, List<CustomModel>> hashMap;

    public List<CustomModel> lstCustomModel;

    Context ctx;

    public List<String> headerTitle;

   /* RecyclerViewClickListener mListener;

    public interface RecyclerViewClickListener {

        void onClickCall(CustomModel item);

        void onClickMail(CustomModel item);

        void onClickMessage(CustomModel item);
    }*/


    public SemesterAdapter(LinkedHashMap<String, List<CustomModel>> hashMap, Context c)
    //, RecyclerViewClickListener listener)
    {
        this.ctx = c;
        this.hashMap = hashMap;
        headerTitle = getKeys();
        //this.lstCustomModel = lstCustomModel;

        //this.mListener = listener;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_header, parent, false);
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tvWhichSemester.setText(headerTitle.get(position));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx);
        holder.childRecycler.setLayoutManager(mLayoutManager);
        holder.childRecycler.addItemDecoration(new DividerItemDecoration(ctx, LinearLayoutManager.VERTICAL));
        holder.childRecycler.setItemAnimator(new DefaultItemAnimator());
        //holder.childRecycler.setNestedScrollingEnabled(false);

        final List<CustomModel> lst = hashMap.get(headerTitle.get(position));
        //Add the child's Marks count & bind to  Parent's tvtotalMarksCount
        if (lst != null && lst.size() > 0) {
            int sum = 0;
            for (int i = 0; i < lst.size(); i++) {
                sum += lst.get(i).getMarks() != null ? Integer.parseInt(lst.get(i).getMarks()) : 0;
            }
            holder.tvtotalMarksCount.setText(sum + "");
        }

        holder.childRecycler.setAdapter(new MarksChildAdapter(hashMap.get(headerTitle.get(position))));


    }


    /**
     * @return CustomModel Name
     */
    private List<String> getKeys() {
        if (hashMap == null)
            return null;
        List<String> keys = new ArrayList<>();
        for (String key : hashMap.keySet()) {
            keys.add(key);
        }
        return keys;
    }

    @Override
    public int getItemCount() {
        //return lstCustomModel != null ? lstCustomModel.size() : 0;
        return hashMap != null ? hashMap.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground, viewForeground;

        public TextView tvWhichSemester;
        public TextView tvtotalMarksCount;

        public RecyclerView childRecycler;

        public ViewHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);
            tvWhichSemester = (TextView) view.findViewById(R.id.tvWhichSemester);
            tvtotalMarksCount = (TextView) view.findViewById(R.id.tvtotalMarksCount);
            childRecycler = (RecyclerView) view.findViewById(R.id.childRecycler);
        }

    }

    /**
     * Called when user Swiped to delete from the list
     *
     * @param position
     */
    public void removeItem(int position) {
        if (hashMap != null && hashMap.size() > 0) {
            hashMap.remove(headerTitle.get(position));
            headerTitle.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
            //notifyDataSetChanged();
        }
    }

    public void restoreItem(CustomModel item, int position) {
        if (lstCustomModel != null && lstCustomModel.size() > 0) {
            lstCustomModel.add(position, item);
            // notify item added by position
            notifyItemInserted(position);
        }
    }
}