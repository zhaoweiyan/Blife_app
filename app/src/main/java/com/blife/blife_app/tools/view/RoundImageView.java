package com.blife.blife_app.tools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.blife.blife_app.R;
import com.blife.blife_app.utils.logcat.L;

/**
 * Created by w on 2016/5/12.
 */
public class RoundImageView extends ImageView {

    //圆形图片圆角大小
    private int borderRadius;
    private final int DEFAULT_RADIUS = 10;
    //圆形图片半径大小
    private int radius;

    //圆形图片类型  圆形或圆角
    private int type;
    private int TYPE_CIRCLE = 0;
    private int TYPE_ROUND = 1;
    //裁切模式
    private int CROP_TYPE;
    private int CROP_Center = 0;
    private int CROP_Top = 1;
    private int CROP_Bottom = 2;
    //BitmapShader
    private BitmapShader bitmapShader;
    //View的高宽
    private int ViewWidth;
    private int ViewHeight;
    private int viewMinSize;
    private RectF viewRectF;
    //画笔
    private Paint paint;
    //缩放
    private Matrix matrix;
    //状态保存
    private static String STATE_INSTANCE = "state_instance";
    private static String STATE_TYPE = "state_type";
    private static String STATE_BORDER_RADIUS = "state_border_radius";


    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化，获取属性
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        L.e("TAG", "init");
        paint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        borderRadius = (int) typedArray.getDimension(R.styleable.RoundImageView_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, getResources().getDisplayMetrics()));
        type = typedArray.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        CROP_TYPE = typedArray.getInt(R.styleable.RoundImageView_cropType, CROP_Center);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        L.e("TAG", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {//如果为原型图片，改变View的高宽一致
            ViewWidth = getMeasuredWidth();
            ViewHeight = getMeasuredHeight();
            viewMinSize = Math.min(ViewWidth, ViewHeight);
            radius = viewMinSize / 2;
            setMeasuredDimension(viewMinSize, viewMinSize);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        L.e("TAG", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND) {
            viewRectF = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        L.e("TAG", "onDraw");
        if (getDrawable() == null) return;
        initShader();
        if (bitmapShader == null) return;
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(viewRectF, borderRadius, borderRadius, paint);
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    /**
     * 初始化BitmapShader
     */
    private void initShader() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        Bitmap bitmap = drawableToBitmap(drawable);
        if (bitmap == null) return;
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            int minSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = viewMinSize * 1.0f / minSize;
            L.e("TAG", "CIRCLE:" + scale);
        } else if (type == TYPE_ROUND) {
            L.e("TAG", "ROUND:" + scale);
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        matrix = new Matrix();
        matrix.postTranslate(0, -bitmap.getHeight() / 4);
        matrix.postScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
    }


    /**
     * drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            if (width > 0 && height > 0) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, width, height);
                drawable.draw(canvas);
            }
        }
        return bitmap;
    }


//    @Override
//    protected Parcelable onSaveInstanceState() {
//        super.onSaveInstanceState();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
//        bundle.putInt(STATE_TYPE, type);
//        bundle.putInt(STATE_BORDER_RADIUS, borderRadius);
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (state != null && state instanceof Bundle) {
//            Bundle bundle = ((Bundle) state);
//            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
//            this.type = bundle.getInt(STATE_TYPE);
//            this.borderRadius = bundle.getInt(STATE_BORDER_RADIUS);
//        } else {
//            super.onRestoreInstanceState(state);
//        }
//    }
}
