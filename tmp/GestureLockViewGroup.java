package com.fire.day.one.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.fire.day.one.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 整体包含n*n个GestureLockView,每个GestureLockView间隔mMarginBetweenLockView,
 * 最完成的GestureLockView与容器存在mMarginBetweenLockView的外边距
 * <p>
 * 关于GestureLockView的边长(n*n) n * mGestureLockViewWidth +(n+1) * mMarginBetweenLockView = mWidth;
 * mGestureLockViewWidth = 4*mWidth /(5*mCount+1) 注: mMarginBetweenLockView = mGestureLockViewWidth * 0.25;
 */
public class GestureLockViewGroup extends RelativeLayout {

    private static final String TAG = "GestureLockGroup";

    /**
     * 保存所有的GestureLockView
     */
    private GestureLockView[] mGestureLockViews;

    /**
     * 每个边上的GestureLockView的个数
     */
    private int mCount = 4;

    /**
     * 储存的答案
     */
    private int[] mAnswer = {0, 1, 2, 5, 8};

    /**
     * 保存用户选中的GestureLockView的id
     */
    private List<Integer> mChoose = new ArrayList<>();
    private Paint mPaint;

    /**
     * 每个GestureLockView中间的间距 mGestureLockViewWidth * 25%
     */
    private int mMarginBetweenLockView = 30;

    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mGestureLockViewWidth;

    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0xFF939090;

    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFE0DBDB;

    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xFF378FC9;

    /**
     * GestureLockView手指抬起的状态下内圆和外圆的颜色
     */
    private int mFingerUpColor = 0xFFFF0000;

    /**
     * 宽度
     */
    private int mWidth;

    /**
     * 高度
     */
    private int mHeight;

    private Path mPath;

    /**
     * 指引线的开始位置x
     */
    private int mLastPathX;

    /**
     * 指引线的开始位置Y
     */
    private int mLastPathY;

    /**
     * 指引线结束位置
     */
    private Point mTmpTarget = new Point();

    /**
     * 最大尝试次数
     */
    private int mTryTimes = 4;

    /**
     * 回调接口
     */
    private OnGestureLockViewListener mOnGestureLockViewListener;

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.GestureLockViewGroup, defStyle, 0);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr,
                            mNoFingerOuterCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColor = a.getColor(attr, mNoFingerOuterCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor = a.getColor(attr, mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpColor = a.getColor(attr, mFingerUpColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount = a.getInt(attr, 3);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:
                    mTryTimes = a.getInt(attr, 5);
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mHeight = Math.min(mWidth, mHeight);

        //初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mCount * mCount];
            //计算每个view的宽度
            mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
            //计算每个view的间距
            mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.25);
            //设置画笔宽度
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);

            for (int i = 0; i < mGestureLockViews.length; i++) {
                mGestureLockViews[i] = new GestureLockView(getContext(),
                        mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                        mFingerOnColor, mFingerUpColor);
                mGestureLockViews[i].setId(i + 1);
                //设置参数,主要是定位GestureLockView的位置
                RelativeLayout.LayoutParams lockerParams = new RelativeLayout.LayoutParams(
                        mGestureLockViewWidth, mGestureLockViewWidth);
                //不是每行第一个，设置为位置前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF, mGestureLockViews[i - 1].getId());
                }
                //从第二行开始设置为上行统一位置view下方
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.BELOW, mGestureLockViews[i - mCount].getId());
                }

                //设置右下坐上编剧
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMargin = 0;
                int topMargin = 0;

                /**
                 * 每个View都有右外边距和底外边距 第一行有上外边距 第一列有左外边距
                 */
                


            }

        }


    }

    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }


    private interface OnGestureLockViewListener {

        /**
         * 单独选中元素Id
         */
        void onBlockSelected(int cid);

        /**
         * 是否匹配
         */
        void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        void onUnmatchedExceedBoundary();


    }


}
