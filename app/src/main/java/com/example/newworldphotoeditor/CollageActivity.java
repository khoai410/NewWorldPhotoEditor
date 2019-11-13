package com.example.newworldphotoeditor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.newworldphotoeditor.Adapter.ViewPagerAdapter;
import com.example.newworldphotoeditor.Interface.BrushFragmentListener;
import com.example.newworldphotoeditor.Interface.EditImageFragmentListener;
import com.example.newworldphotoeditor.Interface.EmojiFragmentListener;
import com.example.newworldphotoeditor.Interface.FiltersListFragmentListener;
import com.example.newworldphotoeditor.Interface.TextFragmentListener;
import com.example.newworldphotoeditor.Ultis.BitmapUltis;
import com.google.android.material.snackbar.Snackbar;
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

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class CollageActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener,
        TextFragmentListener {
    public static String pictureName ="test.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_ADD_IMAGE = 1001;
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    CoordinatorLayout coordinatorLayout;
    Bitmap ogBitmap;
    Bitmap filterBitmap;
    Bitmap lastBitmap;
    FilterFragment filterFragment;
    TuneFragment tuneFragment;
    CardView cv_filter;
    CardView cv_tune;
    CardView cv_brush;
    CardView cv_emoji;
    CardView cv_text;
    CardView cv_image;
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
        cv_filter = findViewById(R.id.cv_filter);
        cv_tune = findViewById(R.id.cv_tune);
        cv_brush = findViewById(R.id.cv_brush);
        cv_emoji = findViewById(R.id.cv_emoji);
        cv_text = findViewById(R.id.cv_text);
        cv_image = findViewById(R.id.cv_image);
        cv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = FilterFragment.getInstance();
                filterFragment.setListener(CollageActivity.this);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });
        cv_tune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuneFragment tuneFragment = TuneFragment.getInstance();
                tuneFragment.setListener(CollageActivity.this);
                tuneFragment.show(getSupportFragmentManager(), tuneFragment.getTag());
            }
        });
        cv_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(CollageActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });
        cv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(CollageActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });
        cv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextFragment textFragment = TextFragment.getInstance();
                textFragment.setListener(CollageActivity.this);
                textFragment.show(getSupportFragmentManager(),textFragment.getTag());
            }
        });
        cv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPhoto();
            }
        });
        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                    .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                    .build();
        coordinatorLayout = findViewById(R.id.coordinator);

        loadImage();



    }

    private void addImageToPhoto() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_ADD_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(CollageActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void loadImage() {
        ogBitmap = BitmapUltis.getBitmapFromAssets(this, pictureName,300, 300);
        filterBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        lastBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(ogBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filterFragment = new FilterFragment();
        filterFragment.setListener(this);

        tuneFragment = new TuneFragment();
        tuneFragment.setListener(this);

        adapter.addFragment(filterFragment, "FILTERS");
        adapter.addFragment(tuneFragment, "TUNE EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightness1 = brightness;
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturation1 = saturation;
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstraintChanged(float constraint) {
        constraint1 = constraint;
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(constraint));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(lastBitmap.copy(Bitmap.Config.ARGB_8888,true)));
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
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filterBitmap));
        lastBitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    private void reset() {
        if(tuneFragment !=null){
            tuneFragment.reset();
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
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    try {
                                        final String path = BitmapUltis.insertImage(getContentResolver(),saveBitmap,System.currentTimeMillis() + "_profile.jpg",null);

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

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
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

        if (resultCode == RESULT_OK) {
            if(requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUltis.getBitmapFromGallery(this, data.getData(), 800, 800);
                ogBitmap.recycle();
                lastBitmap.recycle();
                filterBitmap.recycle();
                //Xóa bộ nhớ Bitmap
                ogBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                lastBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                filterBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(ogBitmap);
                bitmap.recycle();

                filterFragment.displayThumbnail(ogBitmap);
            }
            else if(requestCode == PERMISSION_ADD_IMAGE){
                 Bitmap bitmap = BitmapUltis.getBitmapFromGallery(this,data.getData(),300,300);
                 photoEditor.addImage(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if(isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onTextChanged(String text, int color) {
        photoEditor.addText(text, color);
    }
}
