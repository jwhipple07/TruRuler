package com.truruler.truruler;


        import android.Manifest;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Matrix;
        import android.graphics.Paint;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.View.OnTouchListener;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import java.io.OutputStream;
        import java.util.ArrayList;
        import java.util.List;

        import android.content.ContentValues;
        import android.graphics.Bitmap.CompressFormat;
        import android.provider.MediaStore.Images.Media;
        import android.widget.Toast;

        import sticker.StickerImageView;
        import sticker.StickerView;

public class DrawOnPhotoActivity extends Activity implements OnClickListener{
    protected ImageView chosenImageView;
    protected Button choosePicture;
    protected Button savePicture;
    protected Button addNew;

    protected Bitmap bmp;
    protected Bitmap alteredBitmap;
    protected Canvas canvas;
    protected FrameLayout fl;
    protected Paint paint;
    protected Matrix matrix;

    protected  List<StickerView> allStickerViews = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_photo);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        chosenImageView = (ImageView) this.findViewById(R.id.ChoosenImageView);
        choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);
        savePicture = (Button) this.findViewById(R.id.SavePictureButton);

        savePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);

        fl = (FrameLayout) findViewById(R.id.DrawOnFrameLayout);
        addNew = (Button) findViewById(R.id.AddNewLengthButton);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StickerImageView iv_sticker = new StickerImageView(DrawOnPhotoActivity.this);
                iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.arrow));
                allStickerViews.add(iv_sticker);
                fl.addView(iv_sticker);
            }
        });

        fl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for(StickerView s : allStickerViews) {
                    if(s!= null)
                        s.setControlsVisibility(false);
                }
                return true;
            }
        });
    }

    public void onClick(View v) {
        if (v == choosePicture) {
            Intent choosePictureIntent = new Intent(
                    Intent.ACTION_PICK,
                    Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, 0);

        } else if (v == savePicture) {
            if (alteredBitmap != null) {
                ContentValues contentValues = new ContentValues(3);
                alteredBitmap = mergeLayers();

                Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
                try {
                    OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
                    alteredBitmap.compress(CompressFormat.JPEG, 90, imageFileOS);
                    Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
                    t.show();
                    t.wait();

                } catch (Exception e) {
                    Log.v("EXCEPTION", e.getMessage());
                }
                finish();
            }
        }
    }

    private Bitmap mergeLayers(){
        for(StickerView sv: allStickerViews){
            sv.setControlsVisibility(false);
        }
        setButtonsVisible(View.INVISIBLE);
        fl.setDrawingCacheEnabled(true);
        fl.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(fl.getDrawingCache());
        fl.setDrawingCacheEnabled(false);
        return bmp;
    }

    public void setButtonsVisible(Integer visibility){
        choosePicture.setVisibility(visibility);
        savePicture.setVisibility(visibility);
        addNew.setVisibility(visibility);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                        .getHeight(), bmp.getConfig());
                canvas = new Canvas(alteredBitmap);
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(5);
                matrix = new Matrix();
                canvas.drawBitmap(bmp, matrix, paint);

                chosenImageView.setImageBitmap(bmp);
               // chosenImageView.setOnTouchListener(this);
            } catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }

}
