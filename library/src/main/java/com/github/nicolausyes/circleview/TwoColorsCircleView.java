package com.github.nicolausyes.circleview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.util.AttributeSet;

/**
 * Custom two color Circle View
 */
public class TwoColorsCircleView extends BaseCircleView {

    private Paint mPaint;
    private ColorFilter mFirstColorFilter;
    private ColorFilter mSecondColorFilter;

    private float mFirstColorStartAngle;
    private float mFirstColorSweepAngle;

    private float mSecondColorStartAngle;
    private float mSecondColorSweepAngle;

    private boolean mUseCenter;

    public TwoColorsCircleView(Context context) {
        this(context, null, R.styleable.CircleViewStyle_circleViewDefault);
    }

    public TwoColorsCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CircleViewStyle_circleViewDefault);
    }

    public TwoColorsCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TwoColorsCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes mPaint objects and sets desired attributes.
     * @param context Context
     * @param attrs Attributes
     * @param defStyle Default Style
     */
    protected void init(Context context, AttributeSet attrs, int defStyle) {

        super.init(context, attrs, defStyle);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TwoColorsCircleView, defStyle, 0);

        // get first and second colors
        setFirstColor(attributes.getColor(R.styleable.TwoColorsCircleView_cv_firstColor, Color.TRANSPARENT));
        setSecondColor(attributes.getColor(R.styleable.TwoColorsCircleView_cv_secondColor, Color.TRANSPARENT));


        mFirstColorStartAngle = attributes.getFloat(R.styleable.TwoColorsCircleView_cv_firstColorStartAngle, -90);
        mFirstColorSweepAngle = attributes.getFloat(R.styleable.TwoColorsCircleView_cv_firstColorSweepAngle, -180);

        mSecondColorStartAngle = attributes.getFloat(R.styleable.TwoColorsCircleView_cv_secondColorStartAngle, 90);
        mSecondColorSweepAngle = attributes.getFloat(R.styleable.TwoColorsCircleView_cv_secondColorSweepAngle, -180);

        mUseCenter = attributes.getBoolean(R.styleable.TwoColorsCircleView_cv_useCenter, true);

        // We no longer need our attributes TypedArray, give it back to cache
        attributes.recycle();
    }

    /**
     * Sets the CircleView's first color.
     * @param firstColor The new color (including alpha) to set.
     */
    public void setFirstColor(int firstColor) {
        mFirstColorFilter = new PorterDuffColorFilter(firstColor, PorterDuff.Mode.SRC_ATOP);
        invalidate();
    }

    /**
     * Sets the CircleView's second color.
     * @param secondColor The new color (including alpha) to set.
     */
    public void setSecondColor(int secondColor) {
        mSecondColorFilter = new PorterDuffColorFilter(secondColor, PorterDuff.Mode.SRC_ATOP);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        mPaint.setColorFilter(mFirstColorFilter);
        canvas.drawArc(getRect(), mFirstColorStartAngle, mFirstColorSweepAngle, mUseCenter, mPaint);
        mPaint.setColorFilter(mSecondColorFilter);
        canvas.drawArc(getRect(), mSecondColorStartAngle, mSecondColorSweepAngle, mUseCenter, mPaint);
    }
}
