package com.geo.smartisan;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by liwei on 2017/6/5.
 */

public class SmartisanView extends View {
    //第一圈画笔
    Paint mCirclePaint;
    //第二圈画笔
    Paint mCircleBPaint;
    //刻度画笔
    Paint mTextPaint;
    //时针画笔
    Paint mHourPaint;
    //秒针画笔
    Paint mSecondPaint;
    //文字
    Paint mTextTimePaint;
    //中间小圆画笔
    Paint mCircleTilePaint;
    private int mRadio = 230;

    public SmartisanView(Context context) {
        super(context);
        initPaint();
    }

    public SmartisanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SmartisanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(Color.parseColor("#A3A3A3"));


        mCircleBPaint = new Paint();
        mCircleBPaint.setStrokeWidth(1);
        mCircleBPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCircleBPaint.setColor(Color.parseColor("#DBDBDB"));
        //刻度画笔
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(25);

        //时针画笔

        mHourPaint = new Paint();
        mHourPaint.setAntiAlias(true);
        mHourPaint.setStrokeWidth(25);
        mHourPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHourPaint.setColor(Color.parseColor("#5E5E5E"));

        //秒针画笔
        mSecondPaint = new Paint();
        mSecondPaint.setStrokeWidth(8);
        mSecondPaint.setAntiAlias(true);
        mSecondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSecondPaint.setColor(Color.RED);

        //下面时间文字画笔
        mTextTimePaint=new Paint();
        mTextTimePaint.setStrokeWidth(3);
        mTextTimePaint.setAntiAlias(true);
        mTextTimePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextTimePaint.setColor(Color.RED);
        mTextTimePaint.setTextSize(98);

        mCircleTilePaint = new Paint();
        mCircleTilePaint = new Paint();
        mCircleTilePaint.setAntiAlias(true);
        mCircleTilePaint.setStrokeWidth(1);
        mCircleTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCircleTilePaint.setColor(Color.parseColor("#4D4D4D"));


        initAnimation();


    }
    int curMin;
    private void initAnimation() {
        final ValueAnimator animator = ValueAnimator.ofInt(60,1);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                curMin = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setInterpolator(null);
        animator.setRepeatCount(-1);//无限重复
        animator.setDuration(1000*60);
        animator.start();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        //++++++++++++  第一步，绘制圆环区域
        //绘制外圈
        mCirclePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.OUTER));
        canvas.drawCircle(cx, cy, mRadio, mCirclePaint);
        //绘制内圈
        mCircleBPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(cx, cy, mRadio - 30, mCircleBPaint);

        //+++++++++++++  第二步，绘制刻度
        canvas.drawText("12", cx - mTextPaint.measureText("12") / 2, cy - mRadio + 60, mTextPaint);
        canvas.drawText("3", cx + mRadio - 45 - mTextPaint.measureText("3") / 2, cy, mTextPaint);
        canvas.drawText("6", cx - mTextPaint.measureText("6") / 2, cy + mRadio - 40, mTextPaint);
        canvas.drawText("9", cx - mRadio + 45 - mTextPaint.measureText("9") / 2, cy, mTextPaint);

        //+++++++++++++  第三部，绘制时、分、秒针

        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curSecond = calendar.get(Calendar.SECOND);
        Log.e("DATE",curHour+"时"+curMinute+"分"+curSecond);

        //绘制时针
        int radio = mRadio-30-100;
        int  angle = 360-(360/12*calendar.get(Calendar.HOUR)-90);
       // mHourPaint.setColor(Color.BLACK);
        int endHX = (int) (cx+radio*Math.cos(angle* Math.PI / 180));
        int endHY = (int) (cy-radio*Math.sin(angle*Math.PI / 180));
        mHourPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(cx,cy,endHX,endHY,mHourPaint);

        //绘制分针
        int mangle = 360-(360/60*curMinute-90);
        radio = mRadio-90;
        int endMX = (int) (cx+radio*Math.cos(mangle* Math.PI / 180));
        int endMY = (int) (cy-radio*Math.sin(mangle*Math.PI / 180));
       // mHourPaint.setColor(Color.GRAY);
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(cx,cy,endMX,endMY,mHourPaint);
        //绘制中间的那个小圆
        mCircleTilePaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
         canvas.drawCircle(cx,cy,20,mCircleTilePaint);


        //绘制秒针
         angle = 360-(360/60*curSecond-90);
        radio=mRadio-35;
        int endX = (int) (cx+radio*Math.cos(angle* Math.PI / 180));
        int endY = (int) (cy-radio*Math.sin(angle*Math.PI / 180));
        Log.e("Smart",endX+"   "+endY+"    "+angle);
        mSecondPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(cx,cy,endX,endY,mSecondPaint);
        String text;
        if(curMinute<10){
            text=curHour+":0"+curMinute+":"+curSecond;

        }else{
            text=curHour+":"+curMinute+":"+curSecond;
        }
        canvas.drawText(text,cx-mTextTimePaint.measureText(text)/2,cy+mRadio+160,mTextTimePaint);

    }
}
