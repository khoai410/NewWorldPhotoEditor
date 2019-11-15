package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.StickerListener;
import com.example.newworldphotoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    List<Integer> stickerList;
    StickerListener listener;

    public StickerAdapter(Context context, StickerListener listener) {
        this.context = context;
        this.stickerList = getStickerList();
        this.listener = listener;
    }

    private List<Integer> getStickerList() {
        List<Integer> kq = new ArrayList<>();

        kq.add(R.drawable.stickera);
        kq.add(R.drawable.stickerb);
        kq.add(R.drawable.stickerc);
        kq.add(R.drawable.stickerd);
        kq.add(R.drawable.stickere);
        kq.add(R.drawable.stickerf);
        kq.add(R.drawable.stickerg);
        kq.add(R.drawable.stickerh);
        kq.add(R.drawable.stickeri);
        kq.add(R.drawable.stickerj);
        kq.add(R.drawable.stickerk);
        kq.add(R.drawable.stickerl);
        kq.add(R.drawable.stickerm);
        kq.add(R.drawable.stickern);
        kq.add(R.drawable.stickero);
        kq.add(R.drawable.stickerp);
        kq.add(R.drawable.stickerq);
        kq.add(R.drawable.stickerr);
        kq.add(R.drawable.stickeru);
        kq.add(R.drawable.stickerw);
        kq.add(R.drawable.stickerx);
        kq.add(R.drawable.stickerz);

        return kq;
    }

    int row = -1;
    @NonNull
    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sticker, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerAdapter.ViewHolder holder, int position) {
        if (row == position) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        }else {
            holder.imgCheck.setVisibility(View.INVISIBLE);
        }
        holder.imgSticker.setImageResource(stickerList.get(position));
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCheck;
        ImageView imgSticker;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSticker = itemView.findViewById(R.id.imgSticker);
            imgCheck = itemView.findViewById(R.id.imgCheck2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStickerPicked(stickerList.get(getAdapterPosition()));
                    row = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
