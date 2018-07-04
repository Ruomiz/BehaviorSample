package com.ruomiz.coordinatorheader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;


/**
 * Ruomiz on 2018/6/15.
 * Time  waits  for  none
 * desc ： 华为应用市场搜索栏效果
 */

public class SearchBar extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mRadius;
    private int mOffset;
    private int mSumOffset;
    private String mTextHint;  //searchbar 显示的文字
    private int mBitmapResId;  //设置icon
    private int defaultSize = 40;
    private int mDuration = 200;
    private State mCurrentState = State.OPEN;

    public enum State {
        CLOSED,     //收缩状态
        EXPANDING,  //展开和收缩的过程
        OPEN;       //展开状态
    }

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        mRadius = Math.min(mWidth, mHeight) / 2;
        mSumOffset = mWidth - mRadius * 2 - (getPaddingLeft() + getPaddingRight());

        if (mCurrentState == State.CLOSED) {
            mOffset = mSumOffset;
        } else {
            mOffset = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int right = mWidth - getPaddingRight();
        int left = getPaddingLeft();
        mPaint.setColor(Color.WHITE);
        RectF rectF = new RectF(left + mOffset, getPaddingTop(), right, mHeight);
        canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);

        if (mCurrentState == State.OPEN && !TextUtils.isEmpty(mTextHint)) {
            //绘制文字
            // ascent  绘制普通字符的顶部  // descent底部范围。
            mPaint.setTextSize(40);
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
            float textBottom = mRadius + (mPaint.descent() - mPaint.ascent()) / 2 - mPaint.descent();
            canvas.drawText(mTextHint, left + mRadius, textBottom, mPaint);
            setAlpha(0.6f);
        }

        if (mCurrentState == State.CLOSED && mBitmapResId != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBitmapResId);
            //计算绘制图片的dstRect
            float leftF = (float) (mWidth - getPaddingRight() - (mRadius + mRadius * Math.sin(Math.PI * 135 / 180)));
            float topF = (float) (mRadius - Math.abs(mRadius * Math.cos(Math.PI * 135 / 180)));

            float rightF = (float) (mWidth - getPaddingRight() - (mRadius - mRadius * Math.cos(Math.PI * 45 / 180)));
            float bottomF = (float) (mRadius + mRadius * Math.sin(Math.PI * 45 / 180));

            Log.d("ruomiz", "leftF--" + leftF + "--topF--" + topF);
            Log.d("ruomiz", "rightF--" + rightF + "--bottomF--" + bottomF);

            RectF dstRectF = new RectF(leftF, topF, rightF, bottomF);
            canvas.drawBitmap(bitmap, null, dstRectF, mPaint);
        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);       //宽度测量模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);       //宽度确切数值
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = Math.min(defaultSize, widthSize);
        }
        return mWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);     //高度测量模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);     //高度确切数值
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = Math.min(defaultSize, heightSize);
        }
        return mHeight;
    }

    /**
     * searchbar展开动画
     */
    public void startAnimation() {
        AnimatorSet startAnimSet = new AnimatorSet();
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(this,"alpha",1f,0.6f);
        ValueAnimator startAnimator = ObjectAnimator.ofFloat(1, 0);
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mOffset = (int) (animatedValue * mSumOffset);
                invalidate();
            }
        });
        startAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCurrentState = State.EXPANDING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentState = State.OPEN;
            }
        });
//        startAnimator.start();
        startAnimSet.setDuration(mDuration) ;
        startAnimSet.setInterpolator(new LinearInterpolator());
        startAnimSet.playTogether(startAnimator,alphaAnimator);
        startAnimSet.start();
    }

    /**
     * searchbar收缩动画
     */
    public void closeAnimation() {
        AnimatorSet closeAnimSet = new AnimatorSet();
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(this,"alpha",0.6f,1f);
        ValueAnimator closeAnimator = ObjectAnimator.ofFloat(0, 1);
        closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mOffset = (int) (animatedValue * mSumOffset);
                invalidate();
            }
        });
        closeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCurrentState = State.EXPANDING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentState = State.CLOSED;
            }
        });
        closeAnimSet.setDuration(mDuration);
        closeAnimSet.setInterpolator(new LinearInterpolator());
        closeAnimSet.playTogether(closeAnimator,alphaAnimator);
        closeAnimSet.start();
    }

    public SearchBar setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public SearchBar setSearchIcon(@DrawableRes int resId) {
        mBitmapResId = resId;
        return this;
    }

    public SearchBar setSearchTextHint(String textHint) {
        mTextHint = textHint;
        return this;
    }

    public State getCurrentState() {
        return mCurrentState;
    }
}
