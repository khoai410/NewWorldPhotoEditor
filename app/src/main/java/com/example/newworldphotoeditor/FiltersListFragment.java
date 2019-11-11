package com.example.newworldphotoeditor;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newworldphotoeditor.Adapter.ThumbnailAdapter;
import com.example.newworldphotoeditor.Interface.FiltersListFragmentListener;
import com.example.newworldphotoeditor.Ultis.BitmapUltis;
import com.example.newworldphotoeditor.Ultis.ItemDecoration;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersListFragment extends BottomSheetDialogFragment implements FiltersListFragmentListener {
    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FiltersListFragmentListener listener;
    //Singleton
    static FiltersListFragment instance;
    public static FiltersListFragment getInstance() {
        if (instance == null)
            instance = new FiltersListFragment();
            return instance;

    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FiltersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);
        thumbnailItems = new ArrayList<>();
        adapter = new ThumbnailAdapter(thumbnailItems, this, getActivity());

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new ItemDecoration(space));
        recyclerView.setAdapter(adapter);
        displayThumbnail(null);
        return view;
    }

    public void displayThumbnail(final Bitmap bitmap) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImage;
                if(bitmap == null){
                    thumbImage = BitmapUltis.getBitmapFromAssets(getActivity(),CollageActivity.pictureName,100,100);
                }else{
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }
                if(thumbImage == null)
                    return;
                    ThumbnailsManager.clearThumbs();
                    thumbnailItems.clear();

                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName="Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());
                for(Filter filter : filters){
                    ThumbnailItem item = new ThumbnailItem();
                    item.image=thumbImage;
                    item.filter = filter;
                    item.filterName = filter.getName();
                    ThumbnailsManager.addThumb(item);
                }
                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onFiltersSelected(Filter filter) {
        if(listener!=null){
            listener.onFiltersSelected(filter);
        }
    }
}
