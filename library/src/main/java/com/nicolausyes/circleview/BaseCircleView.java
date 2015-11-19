package com.nicolausyes.circleview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nick on 13-Nov-15.
 */
public class BaseCircleView extends View {

    private boolean mIsRadiusSetByUser;
    private int mFillRadius;

    private boolean mHasBorder;
    private boolean mIsSelectable;
    private boolean mIsSelected;
    private int mBorderWidth;

    private int mCanvasSize;
    private int mSelectBorderWidth;
    private RectF mBorderRect;
    private RectF mSelectBorderRect;
    private RectF mRect;

    // Objects used for the actual drawing
    private Paint mPaintBorder;
    private Paint mPaintSelectBorder;

    private final static int DEFAULT_FILL_RADIUS = -1;

    public BaseCircleView(Context context) {
        this(context, null, R.styleable.CircleViewStyle_circleViewDefault);
    }

    public BaseCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CircleViewStyle_circleViewDefault);
    }

    public BaseCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        // Initialize mPaint objects
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintSelectBorder = new Paint();
        mPaintSelectBorder.setAntiAlias(true);
        mPaintSelectBorder.setStyle(Paint.Style.STROKE);

        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BaseCircleView, defStyle, 0);

        // radius
        mFillRadius = attributes.getDimensionPixelOffset(R.styleable.BaseCircleView_cv_fillRadius, DEFAULT_FILL_RADIUS);
        mIsRadiusSetByUser = mFillRadius != DEFAULT_FILL_RADIUS;

        // Check for extra features being enabled
        setHasBorder(attributes.getBoolean(R.styleable.BaseCircleView_cv_border, false));

        setIsSelectable(attributes.getBoolean(R.styleable.BaseCircleView_cv_selectable, false));
        // Set border properties, if enabled
        //if(mHasBorder) {
        setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.BaseCircleView_cv_borderWidth, 0));
        setBorderColor(attributes.getColor(R.styleable.BaseCircleView_cv_borderColor, Color.TRANSPARENT));
        //}

        // Set select properties, if enabled
        //if(mIsSelectable) {
        setSelectBorderWidth(attributes.getDimensionPixelOffset(R.styleable.BaseCircleView_cv_selectBorderWidth, 0));
        setSelectBorderColor(attributes.getColor(R.styleable.BaseCircleView_cv_selectBorderColor, Color.TRANSPARENT));
        //}

        mBorderRect = new RectF();
        mSelectBorderRect = new RectF();
        mRect = new RectF();

        // We no longer need our attributes TypedArray, give it back to cache
        attributes.recycle();
    }

    /**
     * Returns is View can be selected
     * @return is View can be selected
     */
    public boolean isIsSelectable() {
        return mIsSelectable;
    }

    /**
     * Sets View selectable
     * @param isSelectable is view selectable
     */
    public void setIsSelectable(boolean isSelectable) {
        mIsSelectable = isSelectable;
        requestLayout();
        invalidate();
    }

    /**
     *
     * @param hasBorder is view has border
     */
    public void setHasBorder(boolean hasBorder) {
        mHasBorder = hasBorder;
        invalidate();
    }

    /**
     *
     * @return is view has border
     */
    public boolean isHasBorder() {
        return mHasBorder;
    }

    /**
     *
     * @return rect to draw circle
     */
    protected RectF getRect() {
        return mRect;
    }

    /**
     * Sets the CircleView's border width in pixels.
     * @param borderWidth Width in pixels for the border.
     */
    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        if(mPaintBorder != null)
            mPaintBorder.setStrokeWidth(borderWidth);
        requestLayout();
        invalidate();
    }

    /**
     * Sets the CircleView's basic border color.
     * @param borderColor The new color (including alpha) to set the border.
     */
    public void setBorderColor(int borderColor) {
        if (mPaintBorder != null)
            mPaintBorder.setColor(borderColor);
        invalidate();
    }

    /**
     * Sets the border width to be drawn around the CircleView
     * during click events when the select is enabled.
     * @param selectBorderWidth Width in pixels for the select border.
     */
    public void setSelectBorderWidth(int selectBorderWidth) {
        mSelectBorderWidth = selectBorderWidth;
        if(mPaintSelectBorder != null)
            mPaintSelectBorder.setStrokeWidth(selectBorderWidth);
        requestLayout();
        invalidate();
    }

    /**
     *
     * @return selector border width
     */
    public int getSelectBorderWidth() {
        return mSelectBorderWidth;
    }

    /**
     * Sets the border color to be drawn around the CircleView
     * during click events when the select is enabled.
     * @param selectBorderColor The color (including alpha) to set for the select border.
     */
    public void setSelectBorderColor(int selectBorderColor) {
        if (mPaintSelectBorder != null)
            mPaintSelectBorder.setColor(selectBorderColor);
        invalidate();
    }

    public int getSelectBorderColor() {
        return mPaintSelectBorder.getColor();
    }

    @Override
    public void onDraw(Canvas canvas) {

        int outerWidth = 0;

        if(mIsSelectable && isSelected()) { // Draw the select border & apply the select filter, if applicable
            outerWidth = mSelectBorderWidth;
            mSelectBorderRect.set(outerWidth / 2, outerWidth / 2, mCanvasSize - outerWidth / 2, mCanvasSize - outerWidth / 2);
            canvas.drawArc(mSelectBorderRect, 0, 360, false, mPaintSelectBorder);
        }
        else if(mHasBorder) { // If no select was drawn, draw a border and clear the filter instead... if enabled
            outerWidth = mBorderWidth;
            mBorderRect.set(outerWidth / 2, outerWidth / 2, mCanvasSize - outerWidth / 2, mCanvasSize - outerWidth / 2);
            canvas.drawArc(mBorderRect, 0, 360, false, mPaintBorder);
        }

        int radius = ((mCanvasSize - (Math.max(mCanvasSize - mFillRadius * 2, outerWidth * 2))) / 2);
        int margin = (mCanvasSize - radius * 2) / 2;
        mRect.set(margin, margin, mCanvasSize - margin, mCanvasSize - margin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCanvasSize = Math.max(getWidth(), getHeight());
        if(!mIsRadiusSetByUser)
            mFillRadius = mCanvasSize / 2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Check for clickable state and do nothing if disabled
        if(!isClickable()) {
            mIsSelected = false;
            return super.onTouchEvent(event);
        }

        // Set selected state based on Motion Event
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsSelected = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_SCROLL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_HOVER_EXIT:
                mIsSelected = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Rect rect = new Rect();
                getHitRect(rect);
                if(!rect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY()))
                    mIsSelected = false;
                break;
        }

        invalidate();
        return super.dispatchTouchEvent(event);
    }

    /**
     * @return Whether or not this view is currently
     * in its selected state.
     */
	public boolean isSelected() {
        return (mIsSelected || isPressed() || super.isSelected());
	}

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        invalidate();
    }
}
