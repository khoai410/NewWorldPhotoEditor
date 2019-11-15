package com.example.newworldphotoeditor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.newworldphotoeditor.Adapter.ColorAdapter;
import com.example.newworldphotoeditor.Adapter.FrameAdapter;
import com.example.newworldphotoeditor.Adapter.StickerAdapter;
import com.example.newworldphotoeditor.Interface.FrameFragmentListener;
import com.example.newworldphotoeditor.Interface.FrameListener;
import com.example.newworldphotoeditor.Interface.StickerFragmentListener;
import com.example.newworldphotoeditor.Interface.StickerListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class StickerFragment extends BottomSheetDialogFragment implements StickerListener {

    RecyclerView rvSticker;
    Button btnApply;

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
        btnApply = view.findViewById(R.id.btnApply);
        rvSticker.setHasFixedSize(true);
        rvSticker.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        StickerAdapter stickerAdapter = new StickerAdapter(getContext(), this);
        rvSticker.setAdapter(stickerAdapter);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddedSticker(stickerPicked);
            }
        });
        return view;
    }

    @Override
    public void onStickerPicked(int sticker) {
        stickerPicked = sticker;
    }
}
