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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MatrixActivity extends Activity {

    ImageView iv;
    private TextView status;
    Matrix matrix = new Matrix();
    private MatrixView matrixView;
    ArrayList<Operation> opList = new ArrayList<Operation>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix_demo);
        init();
    }

    public void apply() {
        matrix.reset();
        for (Operation o : opList) {
            o.apply(matrix);
        }
        status.setText(opList.toString());
        matrixView.setMatrix(matrix);
    }

    public void onClickReset(View v) {
        opList.clear();
        for (OperationType type : OperationType.values()) {
            type.reset();
        }
        apply();
    }

    enum OperationType {
        ROTATE(R.id.rotate_seekbar, 15f) {
            @Override
            public void apply(Matrix matrix, float value) {
                matrix.postRotate(value);
            }
        },
        TRANSLATE_X(R.id.translate_x_seekbar, 20f) {
            @Override
            public void apply(Matrix matrix, float value) {
                matrix.postTranslate(value, 0);
            }
        },
        TRANSLATE_Y(R.id.translate_y_seekbar, 20f) {
            @Override
            public void apply(Matrix matrix, float value) {
                matrix.postTranslate(0, value);
            }
        },
        SCALE_X(R.id.scale_x_seekbar, 0.2f) {
            @Override
            public void apply(Matrix matrix, float value) {
                matrix.postScale(value, 1f);
            }
        },
        SCALE_Y(R.id.scale_y_seekbar, 0.2f) {
            @Override
            public void apply(Matrix matrix, float value) {
                matrix.postScale(1f, value);
            }
        };

        final int seekBarId;
        final float step;

        //-- just test
        private SeekBar sb;

        OperationType(int seekBarId, float step) {
            this.seekBarId = seekBarId;
            this.step = step;
        }

        void init(final MatrixActivity a) {
            sb = (SeekBar) a.findViewById(seekBarId);
            ViewGroup vg = (ViewGroup)sb.getParent();
            final TextView tv = (TextView)vg.getChildAt(2);
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tv.setText(String.format("%.2f", getValue()));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            Button btn = (Button)vg.getChildAt(0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.opList.add(new Operation(OperationType.this, getValue()));
                    a.apply();
                }
            });
        }

        int zeroProgress() {
            return sb.getMax() / 2;
        }

        void reset() {
            sb.setProgress(zeroProgress());
        }

        float getValue() {
            return (sb.getProgress() - zeroProgress()) * step;
        }


        public abstract void apply(Matrix matrix, float value);
    }

    static class Operation {
        OperationType type;
        float value;

        public Operation(OperationType type, float value) {
            this.type = type;
            this.value = value;
        }

        public void apply(Matrix matrix) {
            type.apply(matrix, value);
        }

        public String toString() {
            return String.format("(%s, %.2f)", type, value);
        }
    }

    private void init() {
        status = (TextView)findViewById(R.id.status_text);
        status.setMovementMethod(new ScrollingMovementMethod());
        for (OperationType type : OperationType.values()) {
            type.init(this);
        }
        matrixView = (MatrixView)findViewById(R.id.matrix_view);
    }
}
