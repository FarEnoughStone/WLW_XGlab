package com.xglab.wlw_xglab;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by Administrator on 2016/7/22.
 */

public class getRoundBitmap {
    public static Bitmap get(Bitmap bitmap, int radius) {
        Bitmap out = get(bitmap);
        out=Bitmap.createScaledBitmap(out, radius*2,radius*2, true);
        return out;
    }

    public static Bitmap get(Bitmap bitmap) {
        int width,height;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        int radius = Math.min(width/2, height/2);

        Bitmap output = Bitmap.createBitmap(radius*2, radius*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(radius, radius,radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式
        Rect src = new Rect(bitmap.getWidth() / 2 - radius, bitmap.getHeight() / 2 - radius,
                bitmap.getWidth() / 2 + radius, bitmap.getHeight() / 2 + radius);
        Rect dst = new Rect(0, 0, radius * 2, radius * 2);
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
}
