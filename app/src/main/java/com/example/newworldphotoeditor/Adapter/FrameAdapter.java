package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.FrameListener;
import com.example.newworldphotoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    Context context;
    List<Integer> frameList;
    FrameListener listener;

    int row = -1;

    public FrameAdapter(Context context, FrameListener listener) {
        this.context = context;
        this.frameList = getFrameList();
        this.listener = listener;
    }

    private List<Integer> getFrameList() {
        List<Integer> kq = new ArrayList<>();

        kq.add(R.drawable.frame1);
        kq.add(R.drawable.barak_obama_face);
        kq.add(R.drawable.batman_mask_picture);
        kq.add(R.drawable.batman_joker_smile_vector);
        kq.add(R.drawable.black_electric_guitar_image);
//        kq.add(R.drawable.deadpool_lint_spiderman_barton_face_black);
//        kq.add(R.drawable.deadpool_transparent);
        kq.add(R.drawable.deadpool_transparent_background);
        kq.add(R.drawable.ghost_high_quality);
        kq.add(R.drawable.glasses_image);
        kq.add(R.drawable.glasses_picture);
        kq.add(R.drawable.glasses_png_hd);
        kq.add(R.drawable.love_frame_transparent_picture);
        kq.add(R.drawable.vladimir_putin_face);
        kq.add(R.drawable.grunge);
        kq.add(R.drawable.simple_blue_watercolor_frame);




        return kq;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_frame, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (row == position) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        }else {
            holder.imgCheck.setVisibility(View.INVISIBLE);
        }
        holder.imgFrame.setImageResource(frameList.get(position));
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCheck, imgFrame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            imgFrame = itemView.findViewById(R.id.imgFrame);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFramePicked(frameList.get(getAdapterPosition()));
                    row = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
