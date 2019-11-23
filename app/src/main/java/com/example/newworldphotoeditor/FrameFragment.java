package com.example.newworldphotoeditor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.newworldphotoeditor.Adapter.ColorAdapter;
import com.example.newworldphotoeditor.Adapter.FrameAdapter;
import com.example.newworldphotoeditor.Interface.FrameFragmentListener;
import com.example.newworldphotoeditor.Interface.FrameListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrameFragment extends BottomSheetDialogFragment implements FrameListener {

    RecyclerView rvFrame;
    TextView btnOK;

    int framePicked = -1;

    FrameFragmentListener listener;

    public void setListener(FrameFragmentListener listener) {
        this.listener = listener;
    }

    static FrameFragment instance;
    public static FrameFragment getInstance() {
        if (instance == null)
            instance = new FrameFragment();
        return instance;

    }
    public FrameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frame, container, false);
        rvFrame = view.findViewById(R.id.rvFrame);
        btnOK = view.findViewById(R.id.btnOK);
        rvFrame.setHasFixedSize(true);
        rvFrame.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        FrameAdapter frameAdapter = new FrameAdapter(getContext(), this);
        rvFrame.setAdapter(frameAdapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddedFrame(framePicked);
            }
        });
        return view;
    }

    @Override
    public void onFramePicked(int frame) {
        framePicked = frame;
    }
}
