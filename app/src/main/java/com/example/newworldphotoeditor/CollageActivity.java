package com.example.newworldphotoeditor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newworldphotoeditor.Adapter.ViewPagerAdapter;
import com.example.newworldphotoeditor.Interface.EditImageFragmentListener;
import com.example.newworldphotoeditor.Interface.FiltersListFragmentListener;
import com.example.newworldphotoeditor.Ultis.BitmapUltis;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.IOException;
import java.util.List;

public class CollageActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener {
    public static String pictureName ="test.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    ImageView imagePreview;
    TabLayout tabLayout;
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;
    Bitmap ogBitmap;
    Bitmap filterBitmap;
    Bitmap lastBitmap;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;
    int brightness1 = 0;
    float saturation1 = 1.0f;
    float constraint1 = 1.0f;
    static {
        System.loadLibrary("NativeImageProcessor");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");

        //View
        imagePreview = findViewById(R.id.image_preview);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        coordinatorLayout = findViewById(R.id.coordinator);

        loadImage();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void loadImage() {
        ogBitmap = BitmapUltis.getBitmapFromAssets(this, pictureName,300, 300);
        filterBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        lastBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        imagePreview.setImageBitmap(ogBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment, "FILTERS");
        adapter.addFragment(editImageFragment, "TUNE EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightness1 = brightness;
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightness));
        imagePreview.setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturation1 = saturation;
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(saturation));
        imagePreview.setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstraintChanged(float constraint) {
        constraint1 = constraint;
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(constraint));
        imagePreview.setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness1));
        myFilter.addSubFilter(new SaturationSubfilter(saturation1));
        myFilter.addSubFilter(new ContrastSubFilter(constraint1));

        lastBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFiltersSelected(Filter filter) {
        reset();
        filterBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        imagePreview.setImageBitmap(filter.processFilter(filterBitmap));
        lastBitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    private void reset() {
        if(editImageFragment!=null){
            editImageFragment.reset();
            brightness1=0;
            saturation1=1.0f;
            constraint1=1.0f;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.open){
            openImageFromGallery();
            return true;
        }
        if(id == R.id.save){
            saveImageToGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImageToGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            try {
                                final String path = BitmapUltis.insertImage(getContentResolver(),lastBitmap,System.currentTimeMillis() + "_profile.jpg",null);

                                if(!TextUtils.isEmpty(path)){
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Đã Save Vào Thư Viện Ảnh", Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                             openImage(path);
                                        }
                                    });
                                    snackbar.show();
                                }
                                else{
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Không Save Được Ảnh Vào Thư Viện Ảnh", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(CollageActivity.this, "Chưa Cấp Permission",Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }else{
                            Toast.makeText(CollageActivity.this,"Chưa Cấp Quyền",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == PERMISSION_PICK_IMAGE) {
            Bitmap bitmap = BitmapUltis.getBitmapFromGallery(this, data.getData(), 800, 800);
            ogBitmap.recycle();
            lastBitmap.recycle();
            filterBitmap.recycle();
            //Xóa bộ nhớ Bitmap
            ogBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            lastBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filterBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(ogBitmap);
            bitmap.recycle();

            filtersListFragment.displayThumbnail(ogBitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
