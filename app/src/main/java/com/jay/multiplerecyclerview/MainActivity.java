package com.jay.multiplerecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jay.multiplerecyclerview.adapter.SemesterAdapter;
import com.jay.multiplerecyclerview.model.CustomModel;
import com.jay.multiplerecyclerview.utils.RecyclerItemTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.parentLayout)
    LinearLayout parentLayout;

    SemesterAdapter parentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        parentAdapter = new SemesterAdapter(this,populateData());
        recyclerView.setAdapter(parentAdapter);

        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


/*        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                parentAdapter.removeItem(deletedIndex);

                Toast.makeText(MainActivity.this, "Data", Toast.LENGTH_SHORT).show();
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);*/
    }

    public String getAssetJsonData(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Log.i("data", json);
        return json;

    }

    public LinkedHashMap<String, List<CustomModel>> populateData()
    {
        LinkedHashMap<String, List<CustomModel>> objHashmap = new LinkedHashMap<>();
        JSONObject objRoot = null;
        try {
            objRoot = new JSONObject(getAssetJsonData(getApplicationContext()));
            List<CustomModel> lstCustomModel = null;
            String key = null;
            JSONObject jsonObjHeader = objRoot.getJSONObject("data");
            Iterator<String> iter = jsonObjHeader.keys();
            while (iter.hasNext()) {
                key = iter.next();
                try {
                    JSONArray jsonArray = jsonObjHeader.getJSONArray(key);
                    lstCustomModel = new ArrayList<>();
                    //loop through every JSONArray
                    for (int j = 0; j < jsonArray.length(); j++) {
                        //loop through every JSONObject
                        JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                        String name = (String) jsonObject.get("Name");
                        String marks = (String) jsonObject.get("Marks");
                        lstCustomModel.add(new CustomModel(name, marks));
                    }
                    objHashmap.put(key, lstCustomModel);
                } catch (JSONException e) {
                    // Something went wrong!
                    Toast.makeText(this, getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return objHashmap;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        try {
            if (viewHolder instanceof SemesterAdapter.ViewHolder) {
                if (parentAdapter != null && parentAdapter.hashMap.size() > 0) {
                    // get the removed item name to display it in snack bar

                    final String key = parentAdapter.headerTitle.get(viewHolder.getAdapterPosition());
                    final List<CustomModel> items = parentAdapter.hashMap.get(key);

                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    parentAdapter.removeItem(deletedIndex);

                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(parentLayout, key + " removed from list!!!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            parentAdapter.restoreItem(key, items, deletedIndex);
                            //if last item's undo action is triggered in which case take care
                            showRecyclerView();
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();

                }
                if (parentAdapter.hashMap.size() == 0)
                    showListEmptyMessage();
            }
        } catch (Exception ex) {
            Toast.makeText(this, getResources().getString(R.string.Error), Toast.LENGTH_SHORT).show();
        }
    }


    public void showListEmptyMessage() {
        try {
            findViewById(R.id.recyclerView).setVisibility(View.GONE);
            findViewById(R.id.includeId).setVisibility(View.VISIBLE);
        } catch (Exception ex) {

        }
    }

    public void showRecyclerView() {
        try {
            findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
            findViewById(R.id.includeId).setVisibility(View.GONE);
        } catch (Exception ex) {

        }
    }
}