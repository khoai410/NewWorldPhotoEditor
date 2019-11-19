package com.example.newworldphotoeditor;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newworldphotoeditor.Adapter.StickerAdapter;
import com.example.newworldphotoeditor.Interface.StickerFragmentListener;
import com.example.newworldphotoeditor.Interface.StickerListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class StickerFragment extends BottomSheetDialogFragment implements StickerListener {

    RecyclerView rvSticker;
    ImageView imgClose;
    ImageView imgDone;

    int stickerPicked = -1;

    StickerFragmentListener listener;

    public void setListener(StickerFragmentListener listener) {
        this.listener = listener;
    }

    static StickerFragment instance;
    public static StickerFragment getInstance() {
        if (instance == null)
            instance = new StickerFragment();
        return instance;

    }
    public StickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sticker, container, false);
        rvSticker = view.findViewById(R.id.rvSticker);
        imgClose = view.findViewById(R.id.imgClose);
        imgDone =view.findViewById(R.id.imgDone);
        rvSticker.setHasFixedSize(true);
        rvSticker.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        StickerAdapter stickerAdapter = new StickerAdapter(getContext(), this);
        rvSticker.setAdapter(stickerAdapter);

        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddedSticker(stickerPicked);
                getFragmentManager().beginTransaction().remove(StickerFragment.this).commit();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(StickerFragment.this).commit();
            }
        });
        return view;
    }

    @Override
    public void onStickerPicked(int sticker) {
        stickerPicked = sticker;
    }
}
