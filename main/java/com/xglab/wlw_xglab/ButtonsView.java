package com.xglab.wlw_xglab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/22.
 */

public class ButtonsView extends ImageView {

    int Button_count = 3;
    private Button[] buttons=new Button[Button_count];
    private String[] names = {"空调", "灯光", "风扇"};
    private int[] bitmapID = {R.drawable.snow, R.drawable.lamp, R.drawable.fan};
    int width, height;
    private Paint textpaint;
    private int clickx, clicky;
    public onButtonsViewListener listener;

    public interface onButtonsViewListener{
        public void ButtonsonClick(int position);  //监听每个菜单的单击事件
    }
    public void setonButtonsViewListener(onButtonsViewListener listener){
        this.listener = listener;
    }

    public ButtonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitButtons();
        textpaint = new Paint();
        textpaint.setColor(Color.GRAY);
        textpaint.setAntiAlias(true);
        textpaint.setTextSize(40);
        textpaint.setStrokeWidth(3);//笔宽
        textpaint.setTextAlign(Paint.Align.CENTER);//字体中心为基准
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clickx = (int) event.getX();
                        clicky = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        int x, y;
                        x = (int) event.getX();
                        y = (int) event.getY();
                        if (x < clickx + 1 && x > clickx - 1 &&
                                y > clicky - 1 && y < clicky + 1) {
                            onClick();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 初始化
     */
    private void InitButtons(){
        for(int i=0;i<Button_count;i++) {
            Button button = new Button();
            button.text = names[i];
            button.bitmap = BitmapFactory.decodeResource(getResources(), bitmapID[i]);
            buttons[i] = button;
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        width = getWidth() / Button_count;
        height = getHeight() / 2;
        canvas.drawColor(Color.WHITE);
        for (int i=0;i<Button_count;i++) {
            ButtonDraw(canvas, i);
        }
    }

    private void ButtonDraw(Canvas canvas, int i) {
        Rect rect = new Rect(i * width+width/5, height/5 , width*4/5+i*width , height/5+width * 3 / 5);
        canvas.drawBitmap(buttons[i].bitmap, null, rect, null);
        canvas.drawText(buttons[i].text, width / 2 + width * i, height * 4 / 5 , textpaint);
    }

    private void onClick() {
        int i = -1;
        if (clicky < height) {
            switch (clickx / width) {
                case 0:
                    i = 0;
                    break;
                case 1:
                    i = 1;
                    break;
                case 2:
                    i = 2;
                    break;
                default:
                    break;
            }
        }
        (Toast.makeText(getContext(), "点中了"+i, Toast.LENGTH_SHORT)).show();
        clickx = 0;
        clicky = 0;
        listener.ButtonsonClick(i);
    }
}

class Button {
    Bitmap bitmap;
    String text;
}
