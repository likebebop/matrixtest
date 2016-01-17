package com.example.likebebop.matrixtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by likebebop on 2016. 1. 16..
 */
public class MatrixView extends View {

    public MatrixActivity a;
    public final Bitmap bitmap;
    private int cx;
    private int cy;
    private Matrix matrix = new Matrix();
    private RectF r = new RectF();
    private float[] pts = new float[8];
    private float[] dstPts = new float[8];
    private Paint refPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint mapPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = (MatrixActivity) context;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
        r.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        refPaint.setShader(new LinearGradient(0, 0, r.width(), r.height(), Color.BLACK, Color.BLUE, Shader.TileMode.MIRROR));
        bitmapPaint.setAlpha(128);
        guidePaint.setColor(Color.RED);
        mapPaint.setColor(Color.CYAN);
        pts = new float[]{r.left, r.bottom, r.right, r.bottom, r.left, r.top, r.right, r.top};
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
        canvas.drawLine(cx, 0, cx, getHeight(), guidePaint);
        canvas.drawLine(0, cy, getWidth(), cy, guidePaint);

        if (a.canvasCb.isChecked()) {
            canvas.translate(cx, cy);
        }

        canvas.drawRect(r, refPaint);
        drawPts(canvas, pts, guidePaint);
        matrix.mapPoints(dstPts, pts);
        drawPts(canvas, dstPts, mapPaint);
        canvas.drawBitmap(bitmap, matrix, bitmapPaint);
    }

    private void drawPts(Canvas canvas, float[] pts, Paint p) {
        Path path = new Path();
        int idx = 0;
        path.moveTo(pts[idx++], pts[idx++]);
        for (;idx < 8;) {
            path.lineTo(pts[idx++], pts[idx++]);
        }
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pts[0], pts[1], 10, p);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, p);

    }


}
