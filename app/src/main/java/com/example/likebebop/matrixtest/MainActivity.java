package com.example.likebebop.matrixtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_480_640).copy(Bitmap.Config.ARGB_8888, true);
        forceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);
        w = bitmap.getWidth();
        h = bitmap.getHeight();

        w2 = forceBitmap.getWidth();
        h2 = forceBitmap.getHeight();

        paint.setAlpha(200);

        build180();
        ImageView iv = (ImageView) findViewById(R.id.image_view);
        iv.setImageBitmap(bitmap);

    }

    Bitmap bitmap, forceBitmap;
    Canvas canvas;
    float w, h, w2, h2;
    Paint paint = new Paint();

    //-- canvas adjust와 draw bitmap의 matrix 설정은 같다.
    private void build() {
        Matrix m = new Matrix();
        float scale = h/w2;
        m.postRotate(90);
        m.postTranslate(w, 0);
        m.preScale(scale, scale);
        //m.postScale(h/h2, w/w2);
        canvas.setMatrix(m);
        //canvas.scale(scale, scale);
        canvas.drawBitmap(forceBitmap, 0, 0, paint);

    }

    private void build0() {
        Matrix m = new Matrix();
        float scale = w/w2;
        m.postScale(scale, scale);
        m.postRotate(0);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build90() {
        Matrix m = new Matrix();
        float scale = h/w2;
        m.postScale(scale, scale);
        m.postRotate(90);
        m.postTranslate(w, 0);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build270() {
        Matrix m = new Matrix();
        float scale = h/w2;
        m.postScale(scale, scale);
        m.postRotate(270);
        m.postTranslate(0, h);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build180() {
        Matrix m = new Matrix();
        float scale = w/w2;
        m.postScale(scale, scale);
        m.postRotate(180);
        m.postTranslate(w, h);
        canvas.drawBitmap(forceBitmap, m, paint);
    }


}
