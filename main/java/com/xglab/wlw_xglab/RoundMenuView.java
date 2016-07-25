package com.xglab.wlw_xglab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/19.
 */

public class RoundMenuView extends View {

    private static int STONE_COUNT;// stone数目
    private Stone[] stones;//stone数组

    private int mPointX = 0, mPointY = 0;// 圆心坐标
    private int mRadius = 0;// 半径
    private int angle = 0;//圆盘角度,即第一个stone的角度
    private int mDegreeDelta = 0;// 每两个点间隔的角度
    private int menuRadius = 0; // 菜单的半径
    private double startAngle=0,currentAngle=0;
    private Paint mPaint = new Paint(),menuPaint = new Paint(), textPaint = new Paint();
    private String menutext;
    private String[] names;
    private int[] bitmapID;
    private int temperature = 24;
    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public RoundMenuView(Context context,AttributeSet attrs) {
        super(context,attrs);
        if(attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.RoundMenuView);
            menutext = a.getString(R.styleable.RoundMenuView_menuText);
        }
        switch (menutext) {
            case "空调":
                menutext="空调温度为："+temperature;
                STONE_COUNT = 3;
                names = new String[]{"电源", "温度减", "温度加"};
                bitmapID = new int[]{R.drawable.dianyuan, R.drawable.jianhao, R.drawable.jiahao};
                break;
            case "灯光":
                menutext = "灯光控制";
                STONE_COUNT = 4;
                names = new String[]{"电源1", "电源2", "电源3","电源4"};
                bitmapID = new int[]{R.drawable.dianyuan, R.drawable.dianyuan, R.drawable.dianyuan,R.drawable.dianyuan};
                break;
            case "风扇":
                menutext = "风扇控制";
                STONE_COUNT = 3;
                names = new String[]{"电源", "转速减", "转速加"};
                bitmapID = new int[]{R.drawable.dianyuan, R.drawable.jianhao, R.drawable.jiahao};
                break;
            default:
                break;
        }
        stones = new Stone[STONE_COUNT];
        mPaint.setColor(Color.WHITE);//画笔颜色
        mPaint.setStrokeWidth(5);//画笔宽度
        mPaint.setAntiAlias(true); //消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
        menuPaint.setColor(Color.WHITE);//画笔颜色
        menuPaint.setStrokeWidth(5);//画笔宽度
        menuPaint.setAntiAlias(true); //消除锯齿
        menuPaint.setStyle(Paint.Style.FILL);//实心圆
        textPaint.setStrokeWidth(3);
        textPaint.setTextAlign(Paint.Align.CENTER);
        /*PathEffect pe = new 一个具体的子类;（使用方式）
        DashPathEffect：这个类的作用就是将Path的线段虚线化。
        PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);
        float数组必须为偶数
        如本代码中,绘制长度1的实线,再绘制长度2的空白,再绘制长度4的实线,再绘制长度8的空白,依次重复。
        1是起始位置的偏移量。
        */
        PathEffect effects = new DashPathEffect(new float[]{20,20},1);
        mPaint.setPathEffect(effects);
        //4个象限触摸监控
        //quadrantTouched = new boolean[] { false, false, false, false, false };
        //手势监听
        //mGestureDetector = new GestureDetector(getContext(),new MyGestureListener());
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x, y;
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        startAngle = computeCurrentAngle(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        currentAngle = computeCurrentAngle(x, y);
                        angle = (int)(currentAngle - startAngle);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (angle > 1 || angle < -1) {
                            for (int i = 0; i < STONE_COUNT; i++) {
                                stones[i].angle += angle;
                            }
                            angle = 0;
                            invalidate();
                        } else {
                            x = (int) event.getX();
                            y = (int) event.getY();
                            onClick(x, y);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        setupStones();
    }

    private void onClick(int x, int y) {
        int menu_munber = -1;
        for(int i=0;i<STONE_COUNT;i++) {
            if (x < stones[i].x + menuRadius && x > stones[i].x - menuRadius && y < stones[i].y + menuRadius && y > stones[i].y - menuRadius) {
                menu_munber = i;
                break;
            }
        }
        if (stones[menu_munber].color == Color.RED) {
            stones[menu_munber].color = Color.GREEN;
        } else if (stones[menu_munber].color == Color.GREEN) {
            stones[menu_munber].color = Color.RED;
        }
        invalidate();
        (Toast.makeText(getContext(), "点中了"+menu_munber, Toast.LENGTH_SHORT)).show();
    }

    /**
     * 重写onMeasure()方法，获取宽度与高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPointX = this.getMeasuredWidth()/2;
        mPointY = this.getMeasuredHeight()/2;

        //初始化半径和菜单半径
        mRadius = mPointX-mPointX/4;
        menuRadius = (int)(mPointX/5.5);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        int action=event.getAction();
//        int x, y;
//        double startAngle=0,currentAngle=0;
//        switch(action) {
//            case MotionEvent.ACTION_DOWN:
//                x = (int) event.getX();
//                y = (int) event.getY();
//                startAngle = computeCurrentAngle(x, y);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                x = (int) event.getX();
//                y = (int) event.getY();
//                currentAngle = computeCurrentAngle(x, y);
//                angle += (currentAngle - startAngle);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                invalidate();
//                break;
//        }
//        mGestureDetector.onTouchEvent(event);
//        return true;
//    }

    /**
     * 初始化每个点
     */
    private void setupStones() {
        Stone stone;
        mDegreeDelta = 360 / STONE_COUNT;
        for (int index = 0; index < STONE_COUNT; index++) {
            stone = new Stone();//实例化
            if (angle >= 360) {
                angle -= 360;
            }else if(angle < 0){
                angle += 360;
            }
            stone.angle = angle;
            stone.bitmap = BitmapFactory.decodeResource(getResources(), bitmapID[index]);
            stone.name = names[index];
            if (stone.bitmap.equals((BitmapFactory.decodeResource(getResources(), R.drawable.dianyuan)))) {
                stone.color = Color.RED;
            } else if (stone.bitmap.equals((BitmapFactory.decodeResource(getResources(), R.drawable.jiahao)))) {
                stone.color = 0x7FFFD4;
            }else if (stone.bitmap.equals((BitmapFactory.decodeResource(getResources(), R.drawable.jianhao)))) {
                stone.color = 0xFFFF00;
            }
            angle += mDegreeDelta;
            stones[index] = stone;
        }
    }

    /**
     * 计算某点的角度
     * @param x
     * @param y
     * @return
     */
    private int computeCurrentAngle(float x, float y) {
        float distance = (float) Math.sqrt(((x - mPointX) * (x - mPointX) + (y - mPointY) * (y - mPointY)));
        int degree = (int) (Math.acos((x - mPointX) / distance) * 180 / Math.PI);
        if (y < mPointY) {
            degree = -degree;
        }
        return degree;
    }

    /**
     * stones角度算坐标
     */
    private void computeCoordinates() {
        for (int index = 0; index < STONE_COUNT; index++) {
            stones[index].x = mPointX + (float) (mRadius * Math.cos(Math.toRadians(stones[index].angle+angle)));
            stones[index].y = mPointY + (float) (mRadius * Math.sin(Math.toRadians(stones[index].angle+angle)));
        }
    }

    /**
     * onDraw绘制
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        computeCoordinates();//计算每个stone的坐标
        //画一个白色的圆环
        canvas.drawCircle(mPointX, mPointY, mRadius, mPaint);
        //将每个菜单画出来
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(35);
        for (int index = 0; index < STONE_COUNT; index++) {
            if (!stones[index].isVisible) {continue;}
            drawInCenter(canvas, stones[index].bitmap,stones[index].name, stones[index].x,
                    stones[index].y);
        }
        //画字
        Rect mBound = new Rect();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);
        textPaint.getTextBounds(menutext, 0, menutext.length(), mBound);
        canvas.drawText(menutext, getWidth() / 2 , getHeight() / 2 + mBound.height() / 2, textPaint);

    }
    /**
     * 画stone
     */
    private void drawInCenter(Canvas canvas, Bitmap bitmap,String name, float left, float top) {
        Rect dst = new Rect();
        dst.left = (int) (left - 2*menuRadius/3);
        dst.right = (int) (left + 2*menuRadius/3);
        dst.top = (int) (top - menuRadius);
        dst.bottom = (int) (top + menuRadius/3);

        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(pfd);//消除锯齿设置
        canvas.drawCircle(left,top,menuRadius,menuPaint);
        canvas.drawBitmap(bitmap, null, dst, menuPaint);
        canvas.drawText(name, left,top+2*menuRadius/3, textPaint);

    }

}

class Stone {
    String name;
    int color;
    Bitmap bitmap;// 图片
    int angle;// 角度
    float x;// x坐标
    float y;// y坐标
    boolean isVisible = true;// 是否可见
}