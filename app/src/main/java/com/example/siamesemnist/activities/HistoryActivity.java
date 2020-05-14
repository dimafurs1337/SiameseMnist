package com.example.siamesemnist.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siamesemnist.views.HistoryItem;
import com.example.siamesemnist.R;
import com.example.siamesemnist.views.RecyclerViewAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<HistoryItem> historyItems;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        historyItems = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(historyItems);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                MainActivity.sqLiteHelper.deleteData(historyItems.get(position).getId());
                mAdapter.removeItem(position);

            }
        }).attachToRecyclerView(recyclerView);
        showItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemBack) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showItems(){
        Cursor cursor = MainActivity.sqLiteHelper.getData();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String selectedNum = cursor.getString(1);
            String score = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            historyItems.add(new HistoryItem(id,selectedNum, score, image));
        }
        mAdapter.notifyDataSetChanged();
    }




}
