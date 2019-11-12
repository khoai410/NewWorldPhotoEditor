package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.ColorListener;
import com.example.newworldphotoeditor.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorListener colorListener;

    public ColorAdapter(Context context, List<Integer> colorList, ColorListener colorListener) {
        this.context = context;
        this.colorList = colorList;
        this.colorListener = colorListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.color_picker.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView color_picker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_picker = itemView.findViewById(R.id.color_picker);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorListener.onColorPicked(colorList.get(getAdapterPosition()));
                }
            });
        }
    }
}
