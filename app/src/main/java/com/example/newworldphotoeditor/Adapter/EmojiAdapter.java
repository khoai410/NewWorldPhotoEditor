package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.EmojiListener;
import com.example.newworldphotoeditor.R;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

    Context context;
    List<String> emojiList;
    EmojiListener listener;

    public EmojiAdapter(Context context, List<String> emojiList, EmojiListener listener) {
        this.context = context;
        this.emojiList = emojiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emojiconTextView.setText(emojiList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EmojiconTextView emojiconTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiconTextView = itemView.findViewById(R.id.tvEmoji);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEmojiItemPicked(emojiList.get(getAdapterPosition()));
                }
            });
        }
    }
}
