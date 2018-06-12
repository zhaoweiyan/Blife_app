package com.blife.blife_app.adv.advsend.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blife.blife_app.R;

/**
 * Created by w on 2016/6/1.
 */
public class MapArcView extends View {

    private Paint paint, p;
    private int W;
    private int H;


    public MapArcView(Context context) {
        super(context);
        init();
    }

    public MapArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        p = new Paint();
        paint = new Paint();
        paint.setColor(0xbb809aff);
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        p.setAlpha(50);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        W = canvas.getWidth();
        int x = W / 2;
        int y = x;
//        int y = H / 2;
        canvas.drawCircle(x, y, x, paint);
        canvas.drawCircle(x, y, x, p);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.location_point);
        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - bitmap.getHeight() / 2, paint);
    }
}
