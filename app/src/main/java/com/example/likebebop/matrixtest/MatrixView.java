package com.example.likebebop.matrixtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
    public RectF r = new RectF();
    private float[] pts = new float[6];
    private float[] dstPts = new float[6];
    private Paint refPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint guidePaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint orgPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    private Paint dstPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);

    public MatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = (MatrixActivity) context;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.force_400_300).copy(Bitmap.Config.ARGB_8888, true);
        r.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //refPaint.setShader(new LinearGradient(0, 0, r.width(), r.height(), Color.BLACK, Color.BLUE, Shader.TileMode.MIRROR));
        bitmapPaint.setAlpha(128);
        guidePaint.setColor(Color.RED);
        orgPaint.setColor(Color.MAGENTA);
        dstPaint.setColor(Color.CYAN);
        pts = new float[]{r.left, r.bottom, r.right, r.bottom, r.left, r.top};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cx = w / 2;
        cy = h / 2;
    }

    public void updateLayerType() {
        setLayerType(a.swLayerCb.isChecked() ? LAYER_TYPE_SOFTWARE : LAYER_TYPE_HARDWARE, null);
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //int sc = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawLine(cx, 0, cx, getHeight(), guidePaint);
        canvas.drawLine(0, cy, getWidth(), cy, guidePaint);

        if (a.canvasCb.isChecked()) {
            canvas.translate(cx, cy);
        }

        //canvas.drawRect(r, refPaint);
        drawPts(canvas, pts, orgPaint);

        if (a.canvasMatrixCb.isChecked()) {
            canvas.concat(matrix);
            drawPts(canvas, pts, dstPaint);
            canvas.drawBitmap(bitmap, new Matrix(), bitmapPaint);
        } else {
            matrix.mapPoints(dstPts, pts);
            drawPts(canvas, dstPts, dstPaint);
            canvas.drawBitmap(bitmap, matrix, bitmapPaint);
        }
        //canvas.restoreToCount(sc);

    }

    private void drawPts(Canvas canvas, float[] pts, Paint p) {
        Path path = new Path();
        int idx = 0;
        path.moveTo(pts[idx++], pts[idx++]);
        for (;idx < pts.length;) {
            path.lineTo(pts[idx++], pts[idx++]);
        }
        p.setAlpha(128);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.FILL);
        //-- vertex left, top
        canvas.drawCircle(pts[4], pts[5], 10, p);
        //-- vertex left, bottom
        RectF r = new RectF(pts[0], pts[1], pts[0], pts[1]);
        r.inset(-10, -10);
        canvas.drawRect(r, p);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, p);

    }

    float[] values = new float[9];

    public String toString() {
        matrix.getValues(values);
        return String.format("input = %s\noutput = %s\nmatrix = %s"
                , pointArrayToString(pts), pointArrayToString(dstPts), matrix.toShortString());
    }

    private String pointArrayToString(float[] pts) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < pts.length;) {
            sb.append(String.format("(%.2f, %.2f),", pts[i++], pts[i++]));
        }
        sb.append("]");
        return sb.toString();
    }



}
