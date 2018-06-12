package com.blife.blife_app.index.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.blife.blife_app.R;
import com.blife.blife_app.utils.logcat.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/26.
 */
public class KeyWordsImageView extends View {

    private Bitmap bitmap;//背景图
    private Bitmap drawBitmap;//背景图
    private String keyWord;//关键字
    private Paint paint;//画笔
    private Matrix matrix;//矩阵
    private int viewWidth, viewHeight;
    private int textColor;
    private List<Object> list;

    public KeyWordsImageView(Context context) {
        super(context);
        init(context);
    }

    public KeyWordsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyWordsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dialogkeywords);
        textColor = context.getResources().getColor(R.color.colorRedLoginNormal);
        list = new ArrayList<>();
    }

    /**
     * 设置关键字
     *
     * @param keyWord 关键字
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        createRandom();
        invalidate();
    }

    /**
     * 刷新方法
     */
    public void refresh() {
        if (!TextUtils.isEmpty(keyWord)) {
            createRandom();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        L.e("TAG", "计算*-*****************");
        viewWidth = getMeasuredWidth();
        viewHeight = bitmap.getHeight();
        drawBitmap = scaleBitmap();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    /**
     * 缩放图片
     *
     * @return
     */
    private Bitmap scaleBitmap() {
        matrix.reset();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scale = viewWidth * 1.0f / w * 1.0f;
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(keyWord)) return;
        L.e("TAG", "绘制*-*****************:" + viewHeight + "############");
        drawBackground(canvas, drawBitmap);
        drawKeyWords(canvas);
    }

    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void drawBackground(Canvas canvas, Bitmap bmp) {
        canvas.drawBitmap(bmp, 0, 0, paint);
    }

    /**
     * 计算每个字的X轴的距离
     *
     * @return
     */
    private float dealTextPositionX(int i) {
        int length = keyWord.length() * 2;
        float X = viewWidth / length;
        return X * (i * 2 + 1) - X / 4;
    }

    /**
     * 计算关键字
     *
     * @param canvas
     */
    private void drawKeyWords(Canvas canvas) {
        paint.setColor(textColor);
        paint.setStrokeWidth(20);
        if (!TextUtils.isEmpty(keyWord)) {
            for (int i = 0; i < keyWord.length(); i++) {
                String text = keyWord.substring(i, i + 1);
                drawText(canvas, text, dealTextPositionX(i), viewHeight / 2, i);
            }
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param text
     * @param centerX
     * @param centerY
     */
    private void drawText(Canvas canvas, String text, float centerX, float centerY, int index) {
        canvas.save();
        matrix.reset();
        double random = ((Double) list.get(index));
        paint.setTextSize((float) (40 + (random * 50)));
        matrix.setRotate((float) (random * 90) - 45, centerX, centerY);
        canvas.concat(matrix);
        canvas.drawText(text, centerX, centerY, paint);
        canvas.restore();
    }

    /**
     * 创建随机数
     */
    private void createRandom() {
        list.clear();
        for (int i = 0; i < keyWord.length(); i++) {
            Double random = Math.random();
            list.add(random);
        }
    }

}
