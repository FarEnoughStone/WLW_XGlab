package com.xglab.wlw_xglab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * Created by Administrator on 2016/7/18.
 */

public class RoundImageView extends ImageView {

    private Context mContext;

    private int BitmapID;
    private Paint textPaint,arcPaint, roundPaint;
    int temperature=28,humidity=56,light_intensity=300;

    public RoundImageView(Context context) {
        super(context);
        mContext = context;
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.RoundImageView);
            BitmapID = a.getResourceId(R.styleable.RoundImageView_Image,0);
        }

        textPaint = new Paint();
        arcPaint = new Paint();
        roundPaint = new Paint();
        arcPaint.setStrokeWidth(30);//画笔宽度
        arcPaint.setAntiAlias(true); //消除锯齿
        arcPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
        roundPaint.setColor(Color.WHITE);//画笔颜色
        roundPaint.setStrokeWidth(40);//画笔宽度
        roundPaint.setAntiAlias(true); //消除锯齿
        roundPaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        PathEffect effects = new DashPathEffect(new float[]{5,30},0);
        roundPaint.setPathEffect(effects);//设置线性为虚线
        textPaint.setStrokeWidth(3);//笔宽
        textPaint.setTextAlign(Paint.Align.LEFT);//字体中心为基准
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(35);//字号
    }
//
//    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        mContext = context;
//        setCustomAttributes(attrs);
//    }
//
//    private void setCustomAttributes(AttributeSet attrs) {
//        TypedArray a = mContext.obtainStyledAttributes(attrs,
//                R.styleable.roundedimageview);
//        mBorderThickness = a.getDimensionPixelSize(
//                R.styleable.roundedimageview_border_thickness, 0);
//        mBorderOutsideColor = a
//                .getColor(R.styleable.roundedimageview_border_outside_color,
//                        defaultColor);
//        mBorderInsideColor = a.getColor(
//                R.styleable.roundedimageview_border_inside_color, defaultColor);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth() * 3 / 10, getHeight() * 2 / 9);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), BitmapID);
        Bitmap bitmap_background=BitmapFactory.decodeResource(getResources(), R.drawable.ic_background);
        bitmap_background=Bitmap.createScaledBitmap(bitmap_background, getWidth(),getHeight(), true);
        canvas.drawBitmap(bitmap_background,0,0,null);
        Bitmap roundBitmap =getRoundBitmap.get(bitmap, radius);
        Rect dst = new Rect();
        dst.left = getWidth() / 2 - roundBitmap.getWidth() / 2;
        dst.right = getWidth() / 2 + roundBitmap.getWidth() / 2;
        dst.bottom = getHeight() / 2 + roundBitmap.getHeight() / 2;
        dst.top = getHeight() / 2 - roundBitmap.getHeight() / 2;
        canvas.drawBitmap(roundBitmap, null, dst, null);
        arcPaint.setColor(Color.BLUE);
        drawArc(roundBitmap.getWidth()/2 + 35, 122, canvas, arcPaint);
        arcPaint.setColor(Color.GREEN);
        drawArc(roundBitmap.getWidth() / 2 + 70, 200, canvas, arcPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, roundBitmap.getWidth() / 2 + 110, roundPaint);
        canvas.drawText("温度："+temperature+"C",20,getHeight()*4/5+50,textPaint);
        canvas.drawText("湿度："+humidity+"%",20,getHeight()*4/5+100,textPaint);
        canvas.drawText("光强："+light_intensity+"lux",20,getHeight()*4/5+150,textPaint);
    }

    private void drawArc(int radius, int angle, Canvas canvas,Paint paint) {
        RectF oval=new RectF();//RectF对象
        oval.left = getWidth() / 2 - radius;
        oval.right = getWidth() / 2 + radius;
        oval.bottom = getHeight() / 2 + radius;
        oval.top = getHeight() / 2 - radius;
        canvas.drawArc(oval,270,angle,false,paint);
    }

}
