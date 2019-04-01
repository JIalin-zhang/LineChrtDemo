package com.example.zhangjl.linechrtdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjl on 2019/3/19.
 */

public class LineChartView extends View {
    private int mDataList = 100;
    private int showData = 10 ;
    private List<PointClass> pointList = new ArrayList<>();
    float mWidth;

    float mScreenWidth;

    float mScreenPosition;

    float mHeight;

    float mDataLength;

    float mSpaceLength;

    float mSpaceHeight;

    float mTextHeight;

    float mAreaHeight;

    float  xInt = 0;
    float startX = 0;
    float maxX = 0;
    private Paint paint;

    public LineChartView(Context context) {
        super(context);

    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mDataLength = mScreenWidth / showData;
        mWidth = (mDataLength + mSpaceLength) * mDataList;
         maxX = mDataLength*mDataList;
        setMeasuredDimension((int) mWidth, (int) mHeight);
        for (int i =0;i<mDataList;i++){
            PointClass pointClass = new PointClass();
            pointClass.setYvalue(50);
            pointClass.setXvalue(xInt+mDataLength*i);
            pointList.add(pointClass);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawLine(xInt,100f,mWidth,100f,paint);

        if (xInt>maxX){
            xInt = maxX;
        }else if (xInt<0){
            xInt = 0;
        }
        int index = (int) (xInt/mDataLength);
        if (index>mDataList-10){
            index = mDataList-10;
        }
        for (int i = 0 ;i<10;i++){
           int X = index+i;
            canvas.drawText(X+"",mDataLength*i,pointList.get(X).getYvalue(),paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float movex = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                Log.e("111111111",startX+"");
                break;
            case MotionEvent.ACTION_MOVE:
                movex = event.getX();

                float dis = movex-startX;
                startX = event.getX();
                    xInt= xInt-dis;
                    invalidate();
                break;
                case MotionEvent.ACTION_UP:
                    startX=0;
                    Log.e("ssssssss","抬起");
                    break;
        }
        return true;
    }
}
