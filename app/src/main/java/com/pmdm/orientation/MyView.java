package com.pmdm.orientation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyView extends View {

    private Paint paint;
    private float w = 0;
    private float h = 0;
    private float x = 0;
    private float y = 0;
    private float r = 20;

    public MyView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        x = w / 2;
        y = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(x, y, r, paint);
    }

    public void updateData(float a) {
        if ((x+a-r>0)&&(x+a+r<w)) {
            x += a;
            invalidate();
        }
    }

}