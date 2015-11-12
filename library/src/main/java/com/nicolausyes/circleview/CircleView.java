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
import android.view.MotionEvent;
import android.view.View;

/**
 * Custom circle view
 */
public class CircleView extends View {
	
	// For logging purposes
	private static final String TAG = CircleView.class.getSimpleName();

	// Border & Selector configuration variables
	private boolean mIsRadiusSetByUser;
	private int mFillRadius;
	private boolean mHasBorder;
	private boolean mHasSecondColor;
	private boolean mIsSelectable;
	private boolean mIsSelected;
	private int mBorderWidth;
	private int mCanvasSize;
	private int mSelectBorderWidth;
	private RectF mBorderRect;
	private RectF mSelectBorderRect;
	private RectF mRect;

	// Objects used for the actual drawing
	private Paint mPaint;
	private Paint mPaintBorder;
	private Paint mPaintSelectBorder;
	private ColorFilter mSelectFilter;
	private ColorFilter mFilter;
	private ColorFilter mSecondColorFilter;

	private final static int DEFAULT_FILL_RADIUS = -1;

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
	private void init(Context context, AttributeSet attrs, int defStyle) {
		// Initialize mPaint objects
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaintBorder = new Paint();
		mPaintBorder.setAntiAlias(true);
		mPaintBorder.setStyle(Paint.Style.STROKE);
		mPaintSelectBorder = new Paint();
		mPaintSelectBorder.setAntiAlias(true);
		mPaintSelectBorder.setStyle(Paint.Style.STROKE);

		// Load the styled attributes and set their properties
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyle, 0);

		// get main color
		mFilter = new PorterDuffColorFilter(attributes.getColor(R.styleable.CircleView_cv_color, Color.TRANSPARENT), PorterDuff.Mode.SRC_ATOP);
		
		// get scond color
		int secondColor = attributes.getColor(R.styleable.CircleView_cv_secondColor, Color.TRANSPARENT);
		mHasSecondColor = secondColor != Color.TRANSPARENT;
		mSecondColorFilter = new PorterDuffColorFilter(secondColor, PorterDuff.Mode.SRC_ATOP);

		// radius
		mFillRadius = attributes.getDimensionPixelOffset(R.styleable.CircleView_cv_fillRadius, DEFAULT_FILL_RADIUS);
		mIsRadiusSetByUser = mFillRadius != DEFAULT_FILL_RADIUS;

		// Check for extra features being enabled
		mHasBorder = attributes.getBoolean(R.styleable.CircleView_cv_border, false);
		mIsSelectable = attributes.getBoolean(R.styleable.CircleView_cv_selectable, false);
		// Set border properties, if enabled
		//if(mHasBorder) {
			int defaultBorderSize = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
			setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircleView_cv_borderWidth, defaultBorderSize));
			setBorderColor(attributes.getColor(R.styleable.CircleView_cv_borderColor, Color.TRANSPARENT));
		//}

		// Set select properties, if enabled
		//if(mIsSelectable) {
			int defaultSelectSize = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
			setSelectColor(attributes.getColor(R.styleable.CircleView_cv_selectColor, Color.TRANSPARENT));
			setSelectBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircleView_cv_selectBorderWidth, defaultSelectSize));
			setSelectBorderColor(attributes.getColor(R.styleable.CircleView_cv_selectBorderColor, Color.TRANSPARENT));
		//}

		mBorderRect = new RectF();
		mSelectBorderRect = new RectF();
		mRect = new RectF();

		// We no longer need our attributes TypedArray, give it back to cache
		attributes.recycle();
	}

	/**
	 * Sets the CircleView's basic color.
	 * @param color The new color (including alpha) to set.
	 */
	public void setColor(int color) {
		mFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		invalidate();
	}

	/**
	 * Sets the CircleView's second color.
	 * @param secondColor The new color (including alpha) to set.
	 */
	public void setSecondColor(int secondColor) {
		mHasSecondColor = secondColor != Color.TRANSPARENT;
		mSecondColorFilter = new PorterDuffColorFilter(secondColor, PorterDuff.Mode.SRC_ATOP);
		invalidate();
	}

	public void setHasBorder(boolean hasBorder) {
		mHasBorder = hasBorder;
		invalidate();
	}

	public boolean getHasBorder() {
		return mHasBorder;
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
	 * Sets the color of the select to be draw over the
	 * CircleView. Be sure to provide some opacity.
	 * @param selectColor The color (including alpha) to set for the select overlay.
	 */
	public void setSelectColor(int selectColor) {
		mSelectFilter = new PorterDuffColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);
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

		mPaint.setColorFilter(mFilter);

		// Keep track of selectBorder/border width
		int outerWidth = 0;


		if(mIsSelectable && (mIsSelected || isPressed() || isSelected())) { // Draw the select border & apply the select filter, if applicable
			outerWidth = mSelectBorderWidth;
			mPaint.setColorFilter(mSelectFilter);
			mSelectBorderRect.set(outerWidth / 2, outerWidth / 2, mCanvasSize - outerWidth / 2, mCanvasSize - outerWidth / 2);
			canvas.drawArc(mSelectBorderRect, 0, 360, false, mPaintSelectBorder);
		}
		else if(mHasBorder) { // If no select was drawn, draw a border and clear the filter instead... if enabled
			outerWidth = mBorderWidth;
			mBorderRect.set(outerWidth / 2, outerWidth / 2, mCanvasSize - outerWidth / 2, mCanvasSize - outerWidth / 2);
			canvas.drawArc(mBorderRect, 0, 360, false, mPaintBorder);
		}

		int radius = ((mCanvasSize - (Math.max(mCanvasSize - mFillRadius * 2, outerWidth * 2))) / 2);
		// Draw the fill circle itself
		//canvas.drawCircle(center + outerWidth, center + outerWidth, radius, mPaint);

		int margin = (mCanvasSize - radius * 2) / 2;
		mRect.set(margin, margin, mCanvasSize - margin, mCanvasSize - margin);
		if(!mHasSecondColor) {
			canvas.drawArc(mRect, 0, 360, false, mPaint);
		} else {
			mPaint.setColorFilter(mFilter);
			canvas.drawArc(mRect, -90, -180, true, mPaint);
			mPaint.setColorFilter(mSecondColorFilter);
			canvas.drawArc(mRect, 90, -180, true, mPaint);
		}
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

		//Log.d("TAG", Integer.toString(event.getAction()));

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

		// Redraw image and return super type
		invalidate();
		return super.dispatchTouchEvent(event);
	}

	/**
	 * @return Whether or not this view is currently
	 * in its selected state.
	 */
	/*public boolean isSelected() {
		return mIsSelected;
	}*/
}
