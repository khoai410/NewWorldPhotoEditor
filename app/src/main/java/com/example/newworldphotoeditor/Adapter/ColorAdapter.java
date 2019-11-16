package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.ColorListener;
import com.example.newworldphotoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorListener colorListener;

    int row = -1;

    public ColorAdapter(Context context, ColorListener colorListener) {
        this.context = context;
        this.colorList = colorList();
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
        if(row == position){
            holder.imgCheckColor.setVisibility(View.VISIBLE);
        }else{
            holder.imgCheckColor.setVisibility(View.INVISIBLE);
        }
        holder.color_picker.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView color_picker;
        ImageView imgCheckColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            color_picker = itemView.findViewById(R.id.color_picker);
            imgCheckColor = itemView.findViewById(R.id.imgCheckColor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorListener.onColorPicked(colorList.get(getAdapterPosition()));
                    row = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
    private List<Integer> colorList() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#FFFFFF"));
        colorList.add(Color.parseColor("#C0C0C0"));
        colorList.add(Color.parseColor("#808080"));
        colorList.add(Color.parseColor("#000000"));
        colorList.add(Color.parseColor("#FF0000"));
        colorList.add(Color.parseColor("#800000"));
        colorList.add(Color.parseColor("#FFFF00"));
        colorList.add(Color.parseColor("#808000"));
        colorList.add(Color.parseColor("#00FF00"));
        colorList.add(Color.parseColor("#008000"));
        colorList.add(Color.parseColor("#00FFFF"));
        colorList.add(Color.parseColor("#008080"));
        colorList.add(Color.parseColor("#0000FF"));
        colorList.add(Color.parseColor("#000080"));
        colorList.add(Color.parseColor("#FF00FF"));
        colorList.add(Color.parseColor("#800080"));
        return colorList;
    }
}
