package edu.mit.media.cameraculture.oralimaging.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by judychang on 11/28/15.
 */
public class TriangleShapeView extends View {

    public TriangleShapeView(Context context) {
        super(context);
    }

    public TriangleShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TriangleShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int margin = 10;
        int side = 100;

        Path path = new Path();

        path.moveTo(w - margin, 0);
        path.lineTo(w - margin, side);
        path.lineTo(w - margin - side, 0);
        path.lineTo(w - margin, 0);
        path.close();

        Paint p = new Paint();
        p.setColor( Color.RED );

        canvas.drawPath(path, p);
    }
}
