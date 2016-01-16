package com.example.likebebop.matrixtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        build0();
        iv = (ImageView) findViewById(R.id.image_view);
        iv.setImageBitmap(bitmap);
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_480_640).copy(Bitmap.Config.ARGB_8888, true);
        forceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(bitmap);
        w = bitmap.getWidth();
        h = bitmap.getHeight();

        forceW = forceBitmap.getWidth();
        forceH = forceBitmap.getHeight();

        paint.setAlpha(200);
    }

    enum Type {
        D_0 {
            @Override
            void run(MainActivity a) {
                a.build0();
            }
        },
        D_90 {
            @Override
            void run(MainActivity a) {
                a.build90();
            }
        },
        D_180 {
            @Override
            void run(MainActivity a) {
                a.build180();

            }
        },
        D_270 {
            @Override
            void run(MainActivity a) {
                a.build270();

            }
        },
        CANVAS {
            @Override
            void run(MainActivity a) {
                a.build();
            }
        };
        abstract void run(MainActivity a);

        public static String[] names() {
            Type[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].name();
            }

            return names;
        }
    }


    public void onClickSelectCase(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select type").setSingleChoiceItems(Type.names(), 0,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init();
                Type.values()[which].run(MainActivity.this);
                iv.setImageBitmap(bitmap);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


        Bitmap bitmap, forceBitmap;
    Canvas canvas;
    float w, h, forceW, forceH;
    Paint paint = new Paint();

    //-- canvas adjust와 draw bitmap의 matrix 설정은 같다.
    private void build() {
        Matrix m = new Matrix();
        float scale = h/ forceW;
        m.postRotate(90);
        m.postTranslate(w, 0);
        m.preScale(scale, scale);
        //m.postScale(h/forceH, w/forceW);
        canvas.setMatrix(m);
        //canvas.scale(scale, scale);
        canvas.drawBitmap(forceBitmap, 0, 0, paint);

    }

    private void build0() {
        Matrix m = new Matrix();
        float scale = w/ forceW;
        m.postScale(scale, scale);
        m.postRotate(0);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build90() {
        Matrix m = new Matrix();
        float scale = h/ forceW;
        m.postScale(scale, scale);
        m.postRotate(90);
        m.postTranslate(w, 0);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build270() {
        Matrix m = new Matrix();
        float scale = h/ forceW;
        m.postScale(scale, scale);
        m.postRotate(270);
        m.postTranslate(0, h);
        canvas.drawBitmap(forceBitmap, m, paint);
    }

    private void build180() {
        Matrix m = new Matrix();
        float scale = w/ forceW;
        m.postScale(scale, scale);
        m.postRotate(180);
        m.postTranslate(w, h);
        canvas.drawBitmap(forceBitmap, m, paint);
    }


}
