package com.walk.onyourside.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.walk.onyourside.R;
import com.walk.onyourside.adapters.ConceptsAdapter;

public class MainActivity extends AppCompatActivity{

    RecyclerView homeRecycler;
    RecyclerView.Adapter homeAdapter;
    RecyclerView.LayoutManager homeLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         homeRecycler = findViewById(R.id.home_recycler);
         homeLayoutManager = new LinearLayoutManager(this);

         homeRecycler.setLayoutManager(homeLayoutManager);
         homeRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
         //specify adapter
         //Change this and read from Couchbase
         homeAdapter = new ConceptsAdapter(this,new String[]{"Linked List","Quick Sort"}, new String[]{"Data structure","Sort Algorithm"});
         homeRecycler.setAdapter(homeAdapter);

    }

}
