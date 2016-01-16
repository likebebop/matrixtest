package com.example.likebebop.matrixtest;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private TextView etcStatus;
    private CheckBox preCb;
    public CheckBox canvasCb;


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
        apply();
    }

    public void onClickClearCtl(View v) {
        for (OperationType type : OperationType.values()) {
            type.reset();
        }
    }

    enum OperationType {
        ROTATE(R.id.rotate_seekbar, 15f) {
            @Override
            public void apply(Matrix matrix, float value, boolean pre) {
                if (pre) {
                    matrix.preRotate(value);
                } else {
                    matrix.postRotate(value);
                }
            }
        },
        TRANSLATE_X(R.id.translate_x_seekbar, 10f) {
            @Override
            public void apply(Matrix matrix, float value, boolean pre) {
                if (pre) {
                    matrix.preTranslate(value, 0);
                }  else {
                    matrix.postTranslate(value, 0);
                }
            }
        },
        TRANSLATE_Y(R.id.translate_y_seekbar, 10f) {
            @Override
            public void apply(Matrix matrix, float value, boolean pre) {
                if (pre) {
                    matrix.preTranslate(0, value);
                } else {
                    matrix.postTranslate(0, value);
                }

            }
        },
        SCALE_X(R.id.scale_x_seekbar, 0.2f) {
            @Override
            public void apply(Matrix matrix, float value, boolean pre) {
                if (pre) {
                    matrix.preScale(value, 1f);
                } else {
                    matrix.postScale(value, 1f);
                }
            }
        },
        SCALE_Y(R.id.scale_y_seekbar, 0.2f) {
            @Override
            public void apply(Matrix matrix, float value, boolean pre) {
                if (pre) {
                    matrix.preScale(1f, value);
                } else {
                    matrix.postScale(1f, value);
                }

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
                    a.opList.add(new Operation(OperationType.this, getValue(), a.preCb.isChecked()));
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


        public abstract void apply(Matrix matrix, float value, boolean pre);
    }

    static class Operation {
        OperationType type;
        float value;
        boolean pre;

        public Operation(OperationType type, float value, boolean pre) {
            this.type = type;
            this.value = value;
            this.pre = pre;
        }

        public void apply(Matrix matrix) {
            type.apply(matrix, value, pre);
        }

        public String toString() {
            return String.format("(%s_%s, %.2f)", type, pre ? "pre" : "post", value);
        }
    }

    private void init() {
        status = (TextView)findViewById(R.id.status_text);
        etcStatus = (TextView)findViewById(R.id.etc_status_text);
        status.setMovementMethod(new ScrollingMovementMethod());
        etcStatus.setMovementMethod(new ScrollingMovementMethod());

        for (OperationType type : OperationType.values()) {
            type.init(this);
        }
        matrixView = (MatrixView)findViewById(R.id.matrix_view);
        preCb = (CheckBox)findViewById(R.id.pre_cb);
        canvasCb = (CheckBox)findViewById(R.id.canvas_cb);
        canvasCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                matrixView.invalidate();
            }
        });

        matrixView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateEtcStatus();
            }
        });


    }

    public void updateEtcStatus() {
        etcStatus.setText(String.format("bitmap (%dx%d), view (%dx%d)",
                matrixView.bitmap.getWidth(), matrixView.bitmap.getHeight(),
                matrixView.getWidth(), matrixView.getHeight()));
    }
}
