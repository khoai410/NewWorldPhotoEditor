package com.example.newworldphotoeditor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.newworldphotoeditor.Adapter.ViewPagerAdapter;
import com.example.newworldphotoeditor.Interface.BrushFragmentListener;
import com.example.newworldphotoeditor.Interface.EditImageFragmentListener;
import com.example.newworldphotoeditor.Interface.EmojiFragmentListener;
import com.example.newworldphotoeditor.Interface.FiltersListFragmentListener;
import com.example.newworldphotoeditor.Interface.FrameFragmentListener;
import com.example.newworldphotoeditor.Interface.StickerFragmentListener;
import com.example.newworldphotoeditor.Interface.TextFragmentListener;
import com.example.newworldphotoeditor.Ultis.BitmapUltis;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

public class CollageActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener,
        TextFragmentListener, FrameFragmentListener, StickerFragmentListener {
    public static String pictureName ="test.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_ADD_IMAGE = 1001;
    public static final int PERMISSION_OPEN_CAMERA = 1002;
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
    CardView cv_eraser;
    CardView cv_emoji;
    CardView cv_text;
    CardView cv_image;
    CardView cv_frame;
    CardView cv_crop;
    CardView cv_sticker;
    CardView cv_wallpaper;
    int brightness1 = 0;
    float saturation1 = 1.0f;
    float constraint1 = 1.0f;
    Bitmap bitmapWallpaper1, bitmapWallpaper2;
    int width, height;
    Uri imageUri;
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
        getSupportActionBar().setTitle("Photo Editor");


        //View
        cv_filter = findViewById(R.id.cv_filter);
        cv_tune = findViewById(R.id.cv_tune);
        cv_brush = findViewById(R.id.cv_brush);
        cv_eraser = findViewById(R.id.cv_eraser);
        cv_emoji = findViewById(R.id.cv_emoji);
        cv_text = findViewById(R.id.cv_text);
        cv_image = findViewById(R.id.cv_image);
        cv_frame = findViewById(R.id.cv_frame);
        cv_crop = findViewById(R.id.cv_crop);
        cv_sticker = findViewById(R.id.cv_sticker);
        cv_wallpaper = findViewById(R.id.cv_wallpaper);
        cv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterFragment != null){
                    filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
                }else{
                    Toast.makeText(CollageActivity.this, "Filter Đã Được Chọn", Toast.LENGTH_SHORT).show();
                    FilterFragment filterFragment = FilterFragment.getInstance(null);
                    filterFragment.setListener(CollageActivity.this);
                    filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
                }
            }
        });
        cv_tune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Tune Đã Được Chọn", Toast.LENGTH_SHORT).show();
                TuneFragment tuneFragment = TuneFragment.getInstance();
                tuneFragment.setListener(CollageActivity.this);
                tuneFragment.show(getSupportFragmentManager(), tuneFragment.getTag());
            }
        });
        cv_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Brush Đã Được Chọn", Toast.LENGTH_SHORT).show();
                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(CollageActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });
        cv_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(CollageActivity.this, "Eraser Đã Được Chọn", Toast.LENGTH_SHORT).show();
                    photoEditor.brushEraser();
            }
        });
        cv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Emoji Đã Được Chọn", Toast.LENGTH_LONG).show();
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(CollageActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });
        cv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Text Đã Được Chọn", Toast.LENGTH_SHORT).show();
                TextFragment textFragment = TextFragment.getInstance();
                textFragment.setListener(CollageActivity.this);
                textFragment.show(getSupportFragmentManager(),textFragment.getTag());
            }
        });
        cv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Photo Đã Được Chọn", Toast.LENGTH_SHORT).show();
                addImageToPhoto();
            }
        });
        cv_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Frame Đã Được Chọn", Toast.LENGTH_SHORT).show();
                FrameFragment frameFragment = FrameFragment.getInstance();
                frameFragment.setListener(CollageActivity.this);
                frameFragment.show(getSupportFragmentManager(),frameFragment.getTag());
            }
        });
        cv_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Crop Đã Được Chọn", Toast.LENGTH_SHORT).show();
                cropImage(imageUri);
            }
        });
        cv_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollageActivity.this, "Sticker Đã Được Chọn", Toast.LENGTH_SHORT).show();
                StickerFragment stickerFragment = StickerFragment.getInstance();
                stickerFragment.setListener(CollageActivity.this);
                stickerFragment.show(getSupportFragmentManager(),stickerFragment.getTag());
            }
        });
        cv_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSetwallpaper();
                Toast.makeText(CollageActivity.this, "Đã Set Được Wallpaper", Toast.LENGTH_SHORT).show();
            }
        });
        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                    .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                    .build();
        coordinatorLayout = findViewById(R.id.coordinator);

//        loadImage();



    }

    private void cropImage(Uri uri) {
        String fileName = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), fileName)));

        uCrop.start(CollageActivity.this);
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

//    private void loadImage() {
//        ogBitmap = BitmapUltis.getBitmapFromAssets(this, pictureName,300, 300);
//        filterBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
//        lastBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
//        photoEditorView.getSource().setImageBitmap(ogBitmap);
//    }

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
        filterBitmap = ogBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filterBitmap));
        lastBitmap = filterBitmap.copy(Bitmap.Config.ARGB_8888,true);
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
        if(id == R.id.camera){
            openCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Image");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Chụp Ảnh Từ Camera");
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(cameraIntent, PERMISSION_OPEN_CAMERA);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
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

                imageUri = data.getData();
//                ogBitmap.recycle();
//                lastBitmap.recycle();
//                filterBitmap.recycle();
                //Xóa bộ nhớ Bitmap
                ogBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                lastBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                filterBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(ogBitmap);
                bitmap.recycle();

                filterFragment = FilterFragment.getInstance(ogBitmap);
                filterFragment.setListener(this);
            }
            if(requestCode == PERMISSION_OPEN_CAMERA){
                Bitmap bitmap = BitmapUltis.getBitmapFromGallery(this, imageUri, 800, 800);

                imageUri = data.getData();
//                ogBitmap.recycle();
//                lastBitmap.recycle();
//                filterBitmap.recycle();
                //Xóa bộ nhớ Bitmap
                ogBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                lastBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                filterBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(ogBitmap);
                bitmap.recycle();

                filterFragment = FilterFragment.getInstance(ogBitmap);
                filterFragment.setListener(this);
            }
            else if(requestCode == PERMISSION_ADD_IMAGE){
                 Bitmap bitmap = BitmapUltis.getBitmapFromGallery(this,data.getData(),300,300);
                 photoEditor.addImage(bitmap);
            }
            else if(requestCode == REQUEST_CROP){
                cropResult(data);
            }else if(resultCode == UCrop.RESULT_ERROR){
                cropError(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void cropError(Intent data) {
        Throwable cropError = UCrop.getError(data);
        if(cropError!= null){
            Toast.makeText(CollageActivity.this, "" + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(CollageActivity.this, "Lỗi" , Toast.LENGTH_SHORT).show();
        }
    }

    private void cropResult(Intent data) {
        Uri finalUri = UCrop.getOutput(data);
        if(finalUri != null){
            photoEditorView.getSource().setImageURI(finalUri);

            Bitmap bitmap = ((BitmapDrawable) photoEditorView.getSource().getDrawable()).getBitmap();
            ogBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filterBitmap = ogBitmap;
            lastBitmap =ogBitmap;
        }else{
            Toast.makeText(CollageActivity.this, "Ảnh Không Crop Được", Toast.LENGTH_SHORT).show();
        }
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

//    @Override
//    public void onBrushStateChangedListener(boolean isEraser) {
//        if(isEraser)
//            photoEditor.brushEraser();
//        else
//            photoEditor.setBrushDrawingMode(true);
//    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onTextChanged(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface, text, color);
    }

    @Override
    public void onAddedFrame(int frame) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), frame);
        photoEditor.addImage(bitmap);
    }
    private void setSetwallpaper() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) photoEditorView.getSource().getDrawable();
        bitmapWallpaper1 = bitmapDrawable.getBitmap();
        GetScreenWidthHeight();
        SetBitmapSize();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(CollageActivity.this);
        try {
            wallpaperManager.setBitmap(bitmapWallpaper2);
            wallpaperManager.suggestDesiredDimensions(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void GetScreenWidthHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = photoEditorView.getWidth();
        height = photoEditorView.getHeight();
    }

    public void SetBitmapSize() {
        bitmapWallpaper2 = Bitmap.createScaledBitmap(bitmapWallpaper1, width, height, false);
    }

    @Override
    public void onAddedSticker(int sticker) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), sticker);
        photoEditor.addImage(bitmap);
    }
}
