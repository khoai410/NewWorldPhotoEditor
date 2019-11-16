package com.example.newworldphotoeditor.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Interface.FontListener;
import com.example.newworldphotoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {

    Context context;
    FontListener listener;
    List<String> fontList;

    int row = -1;

    public FontAdapter(Context context, FontListener listener) {
        this.context = context;
        this.listener = listener;
        this.fontList = loadFontList();
    }

    private List<String> loadFontList() {
        List<String> kq = new ArrayList<>();
        kq.add("After_Shok.ttf");
        kq.add("Allura-Regular.otf");
        kq.add("BrotherTattoo_Demo.ttf");
        kq.add("FFF_Tusj.ttf");
        kq.add("GreatVibes-Regular.otf");
        kq.add("Montserrat-Black.otf");
        kq.add("OpenSans-Bold.ttf");
        kq.add("Roboto-Black.ttf");
        kq.add("SnackerComic_PerosnalUseOnly.ttf");
        kq.add("Sofia-Regular.otf");

        kq.add("Amsterdam-VwYy.ttf");
        kq.add("BeautyMountainsPersonalUse-od7z.ttf");
        kq.add("BiteChocolate-2RGl.ttf");
        kq.add("BunchBlossomsPersonalUse-0nA4.ttf");
        kq.add("Calligraphy-D4pm.ttf");
        kq.add("CalligraphyWet-AX62.ttf");
        kq.add("CassandraPersonalUseRegular-3BjG.ttf");
        kq.add("Countryside-YdKj.ttf");
        kq.add("CountrysideTwo-r9WO.ttf");
        kq.add("Ginga-2VGe.ttf");
        kq.add("LemonJellyPersonalUse-dEqR.ttf");
        kq.add("QuickKissPersonalUse-PxlZ.ttf");
        kq.add("Sofia-Regular.otf");
        kq.add("ToySoldiersBold-o9Xa.ttf");
        kq.add("uniquely Sprayed.ttf");
        kq.add("VacationsInParadisePersonalUse-qwml.ttf");
        kq.add("VeganStylePersonalUse-5Y58.ttf");
        kq.add("Xiomara-Script.ttf");

        return kq;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_font, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(row == position){
            holder.imgCheck.setVisibility(View.VISIBLE);
        }else{
            holder.imgCheck.setVisibility(View.INVISIBLE);
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder("fonts/")
                .append(fontList.get(position)).toString());

        holder.fontName.setText(fontList.get(position));
        holder.demoFont.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView demoFont;
        TextView fontName;
        ImageView imgCheck;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            demoFont = itemView.findViewById(R.id.demoFont);
            fontName = itemView.findViewById(R.id.fontName);
            imgCheck = itemView.findViewById(R.id.imgCheck);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFontPicked(fontList.get(getAdapterPosition()));
                    row = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
