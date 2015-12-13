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
 * Custom circle view
 */
public class CircleView extends BaseCircleView {

	// Objects used for the actual drawing
	private Paint mPaint;
	private ColorFilter mColorFilter;
	private ColorFilter mSelectColorFilter;

	int mColor;

	public CircleView(Context context) {
		this(context, null, R.styleable.CircleViewStyle_circleViewDefault);
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, R.styleable.CircleViewStyle_circleViewDefault);
	}

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyle, 0);

		// get main color
		setColor(attributes.getColor(R.styleable.CircleView_cv_color, Color.TRANSPARENT));
		setSelectColor(attributes.getColor(R.styleable.CircleView_cv_selectColor, Color.TRANSPARENT));

		// We no longer need our attributes TypedArray, give it back to cache
		attributes.recycle();
	}

	/**
	 * Sets the CircleView's basic color.
	 * @param color The new color (including alpha) to set.
	 */
	public void setColor(int color) {
		mColor = color;
		mColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		invalidate();
	}

	/**
	 * Sets the color of the select to be draw over the
	 * CircleView. Be sure to provide some opacity.
	 * @param selectColor The color (including alpha) to set for the select overlay.
	 */
	public void setSelectColor(int selectColor) {
		mSelectColorFilter = new PorterDuffColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);
		invalidate();
	}

	/**
	 * Returns color
	 * @return color
	 */
	public int getColor() {
		return mColor;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPaint.setColorFilter( isSelected() ? mSelectColorFilter : mColorFilter);
		canvas.drawArc(getRect(), 0, 360, false, mPaint);
	}
}