package com.example.newworldphotoeditor;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.newworldphotoeditor.Adapter.ColorAdapter;
import com.example.newworldphotoeditor.Interface.BrushFragmentListener;
import com.example.newworldphotoeditor.Interface.ColorListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrushFragment extends BottomSheetDialogFragment implements ColorListener {

    SeekBar brushSize;
    SeekBar brushOpacity;
    RecyclerView rvColor;
    ColorAdapter colorAdapter;

    BrushFragmentListener listener;
    static BrushFragment instance;
    public static BrushFragment getInstance() {
        if (instance == null)
            instance = new BrushFragment();
        return instance;

    }

    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    public BrushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_brush, container, false);

        brushSize = view.findViewById(R.id.seekbar_brushSize);
        brushOpacity = view.findViewById(R.id.seekbar_brushOpacity);
        rvColor = view.findViewById(R.id.rvBrush);
        rvColor.setHasFixedSize(true);
        rvColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        colorAdapter = new ColorAdapter(getContext(), this);
        rvColor.setAdapter(colorAdapter);

        brushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brushOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                listener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }



    @Override
    public void onColorPicked(int color) {
        listener.onBrushColorChangedListener(color);
    }
}
