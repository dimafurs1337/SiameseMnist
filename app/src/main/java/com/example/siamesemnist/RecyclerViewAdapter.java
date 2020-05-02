package com.example.siamesemnist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<HistoryItem> historyItems;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridcell, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        holder.txtSelectedNum.setText("Selected number: " + item.getSelectedNum());
        holder.txtScore.setText("Score: " + item.getScore());
        byte[] historyImage = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(historyImage, 0, historyImage.length);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public void removeItem(int position){
        historyItems.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<HistoryItem> getHistoryItems(){
        return historyItems;
    }

    public RecyclerViewAdapter(ArrayList<HistoryItem> historyItems){
        this.historyItems = historyItems;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtSelectedNum, txtScore;
        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgNum);
            txtSelectedNum = itemView.findViewById(R.id.txtSelectedNum);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }

}
