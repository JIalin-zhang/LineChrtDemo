package com.example.zhangjl.linechrtdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjl on 2019/3/21.
 */

public class PointViewClass extends View {
    private float wScreenWidth; //整个view的宽度
    private float wHeight;//整个view的高度
    private float wLineWidth;//表格的宽度
    private float wLineHeight;//表格的高度
    private float wLineDis;//纵坐标的间距宽度
    private float wHeightDis;//横坐标间距
    private float wXinValueWidth;//心率数值区域的宽度
    private float wDisStart;
    private Paint mLinePaint;//线条的画笔
    private Paint mCricle;
    private Paint mTextPaint;//时间和心率的画笔
    private float eveYvalue; //把画布高度平均分150份
    private float startX ;//手指滑动的时候第一个触点
    private String [] xinValue = new String[]{"180","150","120","90","60","30"};
    private List<PointClass>  listPoint =  new ArrayList<>(); //点的坐标
    private List<Float> Xvalue = new ArrayList<>(); //20个坐标的x值
    private List<PointClass>  drawPointList = new ArrayList<>();//画的点坐标
    private float maxXvalue ;  //最大的x值
    private float xInt;//x轴的起点
    private Boolean isTest = true ;
    public PointViewClass(Context context) {
        super(context);
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setAntiAlias(true);
    }

    public PointViewClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointViewClass(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXYLine(canvas);
        drawPoint(canvas);
        drawLine(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        wHeight = MeasureSpec.getSize(heightMeasureSpec);
        wXinValueWidth = wScreenWidth/10;
        wLineWidth = wScreenWidth-wXinValueWidth;//表格的宽度-十分之一的屏幕宽度
        wDisStart = wHeight/20;
        wLineHeight = wHeight-wDisStart;//表格的高度-20分之一的画布宽度
        wHeightDis = wLineHeight/6; //纵坐标间距为画布的6分之一
        wLineDis = wLineWidth/20;//横坐标为宽度的20分之一
        eveYvalue = wLineHeight/150;//把画布高度平均分150份
        maxXvalue = 20+20*wLineDis;
    }
    /**
     * 画表格
     */
    private void drawXYLine(Canvas canvas){
        Xvalue.clear();
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLACK);
        for (int i = 1 ;i<7;i++){
            canvas.drawLine(10,wHeightDis*i,wLineWidth,wHeightDis*i,mLinePaint);
            if (i==0){
                canvas.drawText(xinValue[i-1],wLineWidth+wXinValueWidth/2,wHeightDis,mLinePaint);
            }else {
                canvas.drawText(xinValue[i-1],wLineWidth+wXinValueWidth/2,wHeightDis*i,mLinePaint);
            }

        }
        for (int j = 0;j<20;j++){
            //表格第一个点往右移动了20px
            canvas.drawLine(j*wLineDis+20,wHeightDis,j*wLineDis+20,wLineHeight,mLinePaint);
            Xvalue.add(j*wLineDis+20);
        }
    }

    /**
     * 画心率点
     * @param canvas
     */
    private void drawPoint (Canvas canvas){
        mCricle = new Paint();
        mCricle.setColor(Color.RED);
        if (isTest){   //正在测试
            //只画20个点
            if (listPoint.size()<=20){
                drawPointList.clear();
                for (int i = 0;i<listPoint.size();i++){
                    drawPointList.add(listPoint.get(i));
                }
            }else {
                PointClass point  = new PointClass();
                point = listPoint.get(listPoint.size()-1);
                drawPointList.remove(0);
                drawPointList.add(point);
            }
        }else {   //测试已经停止
            drawPointList.clear();
            if (xInt>maxXvalue){
                xInt = maxXvalue-20*wLineDis;
            }else if (xInt<20){
                xInt = 20;
            }

            int index = (int) (xInt/wLineDis); //计算要显示的第一个点再数组里面的下标值
            if (index>listPoint.size()-20){
                index = listPoint.size()-20;
            }
            Log.e("11111111111",String.valueOf(index));
            for (int j = 0;j<20;j++){
                PointClass point = listPoint.get(index+j);
                drawPointList.add(point);
            }
        }

        for (int i = 0;i<drawPointList.size();i++){
            PointClass pointClass = drawPointList.get(i);
            int pointSize = i%20;
            float xvalue = Xvalue.get(pointSize);
            pointClass.setXvalue(xvalue);
            canvas.drawCircle(xvalue,pointClass.getYvalue(),5,mCricle);
        }
    }

    /**
     * 画点之间的折线
     */
    private void drawLine(Canvas canvas){
        mCricle = new Paint();
        mCricle.setColor(Color.RED);
        for (int i =0;i<drawPointList.size()-1;i++){
            int start = i%20;
            float startxvalue = Xvalue.get(start);
            int end = (i+1)%20;;
            float endxvalue = Xvalue.get(end);
            PointClass startPoint = drawPointList.get(i);
            PointClass endPoint = drawPointList.get(i+1);
            canvas.drawLine(startxvalue,startPoint.getYvalue(),endxvalue,endPoint.getYvalue(),mCricle);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float movex = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                movex = event.getX();
                if (startX>20&&startX<maxXvalue&&!isTest&&listPoint.size()>20){
                    float dis = movex-startX;
                    startX = event.getX();
                    xInt= xInt-dis;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                startX=0;
                Log.e("ssssssss","抬起");
                break;
        }
        return true;
    }
    /**
     * 设置心率点
     * @param Yvalue
     */
    public void setPointValue(int Yvalue){
         int pointSize = listPoint.size()%20;
         float xvalue = Xvalue.get(pointSize);
         PointClass pointClass = new PointClass();
         pointClass.setXvalue(xvalue);
         DecimalFormat df=new DecimalFormat("0.0000000");//设置保留位数
         String precent = df.format((float)(Yvalue-30)/150);
         pointClass.setYvalue((180-Yvalue)*(eveYvalue)+Float.valueOf(precent)*wHeightDis);
         listPoint.add(pointClass);
         xInt = xInt+wLineDis;
         invalidate();
    }

    /**
     * 判断是否正在测试
     * @param isTest
     */
    public void setTest(Boolean isTest){
        this.isTest = isTest;
        if (!isTest){
            maxXvalue  = listPoint.size()*wLineDis;
        }
    }
}
