package com.jay.multiplerecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.jay.multiplerecyclerview.adapter.SemesterAdapter;
import com.jay.multiplerecyclerview.model.CustomModel;

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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinkedHashMap<String, List<CustomModel>> objHashmap = new LinkedHashMap<>();
        JSONObject objRoot = null;
        try {
            objRoot = new JSONObject(getAssetJsonData(getApplicationContext()));
            List<CustomModel> lstCustomModel = null;
            String key = null;
            JSONObject jsonObjHeader = objRoot.getJSONObject("data");
//            for (int i = 0; i < jsonArrayHeader.length(); i++)
//            {
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
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        SemesterAdapter parentAdapter = new SemesterAdapter(objHashmap, this);
        recyclerView.setAdapter(parentAdapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
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

        Log.e("data", json);
        return json;

    }

}
