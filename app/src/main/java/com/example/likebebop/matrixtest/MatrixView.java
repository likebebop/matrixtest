package com.example.likebebop.matrixtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by likebebop on 2016. 1. 16..
 */
public class MatrixView extends View {

    private final Bitmap bitmap;
    private int cx;
    private int cy;
    private Matrix matrix = new Matrix();

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
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
        canvas.translate(cx, cy);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
