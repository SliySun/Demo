package com.example.sun.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by sun on 2018/3/20.
 */

public class LoveScoreView extends View{

    private String text;
    private int max;
    private int score;
    private float mRadius;
    private float mRingWidth;
    private int mRingSecondColor;
    private int mIntRingColor;
    private int mStartColor;
    private int mEndColor;
    private int mTextSize;
    private int mTextColor;
    private int mSubTextSize;
    private int mSubTextColor;
    private String mSubText;

    private Paint mSecondRingPaint;




    public LoveScoreView(Context context) {
        this(context,null);
    }

    public LoveScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.LoveScoreView);
        text = typedArray.getString(R.styleable.LoveScoreView_text);
        max = typedArray.getInt(R.styleable.LoveScoreView_maxScore,100);
        score = typedArray.getInt(R.styleable.LoveScoreView_score,0);
        mRadius = typedArray.getDimension(R.styleable.LoveScoreView_radius,0);
        mRingWidth = typedArray.getDimension(R.styleable.LoveScoreView_ring_width,0);
        mRingSecondColor = typedArray.getColor(R.styleable.LoveScoreView_ring_color, Color.parseColor("#CBCBCB"));
        mIntRingColor = typedArray.getColor(R.styleable.LoveScoreView_int_ring_color,Color.parseColor("#909090"));
        mStartColor = typedArray.getColor(R.styleable.LoveScoreView_start_color,Color.parseColor("#FFC044"));
        mEndColor = typedArray.getColor(R.styleable.LoveScoreView_end_color,Color.parseColor("#FFEEB3"));
        mTextSize = typedArray.getInt(R.styleable.LoveScoreView_text_size,120);
        mTextColor = typedArray.getColor(R.styleable.LoveScoreView_text_color,Color.parseColor("#464444"));
        mSubTextSize = typedArray.getInt(R.styleable.LoveScoreView_sub_text_size,26);
        mSubTextColor = typedArray.getColor(R.styleable.LoveScoreView_sub_text_color,Color.parseColor("#909090"));
        mSubText = typedArray.getString(R.styleable.LoveScoreView_sub_text_String);
        typedArray.recycle();

        init(context);
    }

    private float startDegree;
    private void init(Context context){
        startDegree = 128;
        initSecondPaint();
        initIntPaint();
        initProgressPaint();
        initTextPaint1();
        initTextPaint2();
    }

    private Paint mTextPaint2;
    private Rect textBoundRect2;
    private void initTextPaint2() {
        mTextPaint2 = new Paint();
        mTextPaint2.setColor(mSubTextColor);
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setTextSize(mSubTextSize);
        mTextPaint2.setTextAlign(Paint.Align.CENTER);
        textBoundRect2 = new Rect();
    }

    private Paint mTextPint1;
    private Rect textBoundRect1;
    private void initTextPaint1() {
        mTextPint1 = new Paint();
        mTextPint1.setColor(mTextColor);
        mTextPint1.setAntiAlias(true);
        mTextPint1.setTextSize(mTextSize);  //以px为单位
        mTextPint1.setTextAlign(Paint.Align.CENTER);
        //文字的边界矩形
        textBoundRect1 = new Rect();
    }




    /**
     * 进度条圆环渐变shader
     */
    private Shader mProgressRingShader;
    private int[] arcColors;
    private Paint mProgressPaint;

    private void initProgressPaint() {
        arcColors = new int[]{mStartColor,mEndColor};
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);// 抗锯齿效果
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形笔头
    }

    private Paint mIntRingPaint;

    private void initIntPaint() {
        mIntRingPaint = new Paint();
        mIntRingPaint.setAntiAlias(true);
        mIntRingPaint.setStyle(Paint.Style.STROKE);
        mIntRingPaint.setColor(mIntRingColor);
    }


    private void initSecondPaint() {
        mSecondRingPaint = new Paint();
        mSecondRingPaint.setAntiAlias(true);// 抗锯齿效果
        mSecondRingPaint.setStyle(Paint.Style.STROKE);
        mSecondRingPaint.setColor(mRingSecondColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec),measure(heightMeasureSpec));

    }

    private int measure(int measureSpec){
        int result;
        int defaultSize = 400;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    private RectF mArcRectF;
    private RectF mIntArcRectF;

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth();
        float y = getHeight();
        //如果未设置半径，则半径的值为view的宽、高一半的较小值
        mRadius = mRadius == 0 ? Math.min(getWidth() / 2, getHeight() / 2) : mRadius;
        //圆环的宽度默认为半径的1／10
//        mRingWidth = mRingWidth == 0 ? mRadius / 10 : mRingWidth;

        int degree2 = getDegree();

        //画外圆
        drawOutRing(canvas, x, y, degree2);

        //画内圆
        drawInRing(canvas, x, y, degree2);

        // 刻度
        drawProgress(canvas);

        drawText(canvas,x, y);

//        drawSubText(canvas,x,y);
    }



    private void drawInRing(Canvas canvas, float x, float y, int degree2) {
        float intRadius = mRadius - 50;
        mIntRingPaint.setStrokeWidth(1);
        mIntArcRectF = new RectF(x/2-intRadius,y/2-intRadius,x/2+intRadius,y/2+intRadius);
        // 内环
//        canvas.drawCircle(x/2,y/2,intRadius,mIntRingPaint);
        canvas.drawArc(mIntArcRectF,startDegree,degree2,false,mIntRingPaint);
    }

    private void drawOutRing(Canvas canvas, float x, float y, int degree2) {
        //设置圆环宽度
        mSecondRingPaint.setStrokeWidth(mRingWidth);
        //半径长度
        mRadius = mRadius - mRingWidth/2;
        //圆环内切的矩形
        mArcRectF = new RectF(x/2-mRadius, y/2-mRadius, x/2+mRadius, y/2+mRadius);
        // 外环
//        canvas.drawCircle(x/2, y/2, mRadius, mSecondRingPaint);
        canvas.drawArc(mArcRectF,startDegree,degree2,false,mSecondRingPaint);
    }

    private int getDegree() {
        double aa = 125/200.0;
        double asin = Math.asin(aa);
        double degree = Math.toDegrees(asin);
        return 360-(int)degree*2;
    }

    private void drawText(Canvas canvas,float x,float y ) {
//        canvas.drawText(String.valueOf(score),,mTextPint1);
        String data= String.valueOf(score) ;
        mTextPint1.getTextBounds(data,0,data.length(),textBoundRect1);
//        canvas.drawText(data,x/2,y/2+textBoundRect1.height()/2,mTextPint1);
//        textBoundRect1.height()+65
        float baseLine = textBoundRect1.height() + 130*y/2/200 ;
        Log.e("baseLine",baseLine+"");
        Log.e("baseLine",textBoundRect1.height()+"");
        canvas.drawText(data,x/2,baseLine , mTextPint1);

        if (mSubText!=null){
            mTextPaint2.getTextBounds(mSubText, 0, mSubText.length(), textBoundRect2);
            float baseLine2 = textBoundRect2.height() + baseLine + ((float) 24 / 200) * y / 2;
            canvas.drawText(mSubText, x/2, baseLine2, mTextPaint2);
        }
    }

    private void drawProgress(Canvas canvas) {
        // 刻度
        if (mProgressRingShader == null){
            mProgressRingShader = new SweepGradient(getWidth(), getHeight(), arcColors,null);
            mProgressPaint.setShader(mProgressRingShader);
        }

        float start = 90-radianToAngle(mRadius);
        mProgressPaint.setStrokeWidth(mRingWidth);

        float sweepAngle = ((float)score/ (float)max)*360;
        canvas.drawArc(mArcRectF,startDegree,sweepAngle,false,mProgressPaint);
    }

    private float radianToAngle(float radios) {
        double aa = mRingWidth / 2 / radios;
        double asin = Math.asin(aa);
        double radian = Math.toDegrees(asin);
        return (float)radian;
    }
}
