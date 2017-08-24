package com.fire.day.one.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.RelativeLayout;


public class GestureLockView extends View {
    private static final String TAG = "GestureLockView";

    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP;
    }

    /**
     * 默认的圆状态
     */
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    /**
     * 宽度
     */
    private int mWidth;

    /**
     * 高度
     */
    private int mHeight;

    /**
     * 外圆半径
     */
    private int mRadius;

    /**
     * 画笔宽度
     */
    private int mStrokeWidth = 2;

    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    /**
     * 箭头 (小三角形最长边长的一般长度= mArrowRate * mWidth /2)
     */
    private float mArrowRate = 0.333f;
    private int mArrowDegree = -1;
    private Path mArrowPath;

    /**
     * 内圆半径 = mInnerCircleRadiusRate * mRadius
     */
    private float mInnerCircleRadiusRate = 0.3f;

    /**
     * 定义GestureLockView初始化时传入的颜色值
     */
    private int mColorNoFingerInner;
    private int mColorNoFingerOuter;
    private int mColorFinggerOn;
    private int mColorFinggerUp;

    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOuter, int colorFinggerOn, int colorFinggerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOuter = colorNoFingerOuter;
        this.mColorFinggerOn = colorFinggerOn;
        this.mColorFinggerUp = colorFinggerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        //去中间值
        mWidth = Math.min(mWidth, mHeight);
        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;

        //绘制三角形
        float mArrowLength = mWidth / 2 * mArrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        switch (mCurrentStatus) {
            case STATUS_FINGER_ON:
                //绘制外圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mColorFinggerOn);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                //绘制内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                break;
            case STATUS_FINGER_UP:
                //绘制外圆
                mPaint.setColor(mColorFinggerUp);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                //绘制内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                drawArrow(canvas);
                break;
            case STATUS_NO_FINGER:
                //绘制外圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorNoFingerOuter);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                //绘制内圆
                mPaint.setColor(mColorNoFingerInner);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                break;
        }

    }

    /**
     * 绘制箭头
     *
     * @param canvas
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Paint.Style.FILL);

            canvas.save();
            canvas.rotate(mArrowDegree, mCenterX, mCenterX);
            canvas.drawPath(mArrowPath, mPaint);

            canvas.restore();

        }
    }

    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setmArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

    public int getmArrowDegree() {
        return this.mArrowDegree;
    }


}
