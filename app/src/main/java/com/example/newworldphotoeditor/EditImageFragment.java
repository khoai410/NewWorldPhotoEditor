package com.example.newworldphotoeditor;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.newworldphotoeditor.Interface.EditImageFragmentListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekBar_brightness;
    SeekBar seekBar_saturation;
    SeekBar seekBar_constraint;

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImageFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_image, container, false);

        seekBar_brightness = view.findViewById(R.id.seekBar_brightness);
        seekBar_saturation = view.findViewById(R.id.seekBar_saturation);
        seekBar_constraint = view.findViewById(R.id.seekBar_constraint);

        seekBar_brightness.setMax(200);
        seekBar_brightness.setProgress(100);

        seekBar_constraint.setMax(20);
        seekBar_constraint.setProgress(0);

        seekBar_saturation.setMax(30);
        seekBar_saturation.setProgress(10);

        seekBar_saturation.setOnSeekBarChangeListener(this);
        seekBar_constraint.setOnSeekBarChangeListener(this);
        seekBar_brightness.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener!=null){
            if(seekBar.getId() == R.id.seekBar_brightness){
                listener.onBrightnessChanged(progress-100);
            }
            else if(seekBar.getId() == R.id.seekBar_constraint){
                progress=progress+10;
                float value = .10f*progress;
                listener.onConstraintChanged(value);
            }
            else if (seekBar.getId() == R.id.seekBar_saturation){
                float value = .10f*progress;
                listener.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener!=null){
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener!=null){
            listener.onEditCompleted();
        }
    }
    public void reset(){
        seekBar_brightness.setProgress(100);
        seekBar_constraint.setProgress(0);
        seekBar_saturation.setProgress(10);
    }
}
