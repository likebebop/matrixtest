package com.example.likebebop.matrixtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by likebebop on 2016. 1. 16..
 */
public class MatrixView extends View {

    public final Bitmap bitmap;
    private int cx;
    private int cy;
    private Matrix matrix = new Matrix();
    private RectF rect = new RectF();
    private Paint p1 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
        rect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        p1.setShader(new LinearGradient(0, 0, rect.width(), rect.height(), Color.BLACK, Color.BLUE, Shader.TileMode.MIRROR));
        p2.setAlpha(128);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cx = w / 2;
        cy = h / 2;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.translate(cx, cy);
        canvas.drawRect(rect, p1);
        canvas.drawBitmap(bitmap, matrix, p2);
    }


}
