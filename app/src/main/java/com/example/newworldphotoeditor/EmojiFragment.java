package com.example.newworldphotoeditor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newworldphotoeditor.Adapter.EmojiAdapter;
import com.example.newworldphotoeditor.Interface.EmojiFragmentListener;
import com.example.newworldphotoeditor.Interface.EmojiListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ja.burhanrashid52.photoeditor.PhotoEditor;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmojiFragment extends BottomSheetDialogFragment implements EmojiListener {
    RecyclerView rvEmoji;
    EmojiFragmentListener listener;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    static EmojiFragment instance;
    public static EmojiFragment getInstance() {
        if (instance == null)
            instance = new EmojiFragment();
        return instance;

    }
    public EmojiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emoji, container, false);
        rvEmoji = view.findViewById(R.id.rv_Emoji);
        rvEmoji.setHasFixedSize(true);
        rvEmoji.setLayoutManager(new GridLayoutManager(getActivity(), 6  ));
        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
        rvEmoji.setAdapter(adapter);
        return view;
    }

    @Override
    public void onEmojiItemPicked(String emoji) {
        listener.onEmojiSelected(emoji);
    }
}
