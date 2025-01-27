package com.example.paintapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    private Paint paint;
    private Path path;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;
    private Bitmap bitmap;
    private Canvas canvas;

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);


        path = new Path();
        paths = new ArrayList<>();
        paints = new ArrayList<>();
    }

    @Override
    protected  void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);

        for (int i = 0; i < paths.size(); i++){
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        canvas.drawPath(path, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x= event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                paths.add(new Path(path));
                paints.add(new Paint(paint));
                path.reset();
                invalidate();
                break;
        }

        return true;
    }

    public void setBrushColor(int color){
        paint.setColor(color);
    }

    public void setBrushSize(float size){
        paint.setStrokeWidth(size);
    }

    public void setEraserMode(boolean isEraser){
        paint.setColor(isEraser ? Color.WHITE: Color.BLACK);
    }

    public void clearCanvas(){
        paths.clear();
        paints.clear();
        invalidate();
    }

    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}
