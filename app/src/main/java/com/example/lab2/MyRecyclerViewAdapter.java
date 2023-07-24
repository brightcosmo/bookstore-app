package com.example.lab2;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.provider.BookEntity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<BookEntity> data = new ArrayList<>();
    public MyRecyclerViewAdapter(){
    }
    public void setData(List<BookEntity> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.card_id.setText("ID: " + data.get(position).getId());
        holder.card_title.setText("Title: " + data.get(position).getTitle());
        holder.card_isbn.setText("ISBN: " + data.get(position).getIsbn());
        holder.card_author.setText("Author: " + data.get(position).getAuthor());
        holder.card_desc.setText("Description: " + data.get(position).getDesc());
        holder.card_price.setText("Price: " + data.get(position).getPrice());
        holder.position.setText("Position: " + position);





        final int fPosition = position;
        holder.itemView.setOnClickListener(v -> Snackbar.make(v, "Item at position " + fPosition + " was clicked!", Snackbar.LENGTH_LONG).show());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView card_id;
        public TextView card_title;
        public TextView card_isbn;
        public TextView card_author;
        public TextView card_desc;
        public TextView card_price;
        public TextView position;


        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
            card_id = itemView.findViewById(R.id.card_id);
            card_title = itemView.findViewById(R.id.card_title);
            card_isbn = itemView.findViewById(R.id.card_isbn);
            card_author = itemView.findViewById(R.id.card_author);
            card_desc = itemView.findViewById(R.id.card_desc);
            card_price = itemView.findViewById(R.id.card_price);
            position = itemView.findViewById(R.id.position);
        }
    }
}
