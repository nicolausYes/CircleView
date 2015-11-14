# CircleView
===============
Simple library to draw one or two color Circle Views with or without a border with click and select support.

Quick Look
-----
One color views:

![](https://raw.githubusercontent.com/nicolausYes/CircleView/master/gifs/1.gif)

One color views with border on click:

![](https://raw.githubusercontent.com/nicolausYes/CircleView/master/gifs/2.gif)

Two color views + select example:

![](https://raw.githubusercontent.com/nicolausYes/CircleView/master/gifs/3.gif)

Usage
-----

Library contains two types of views. `CircleView` and `TwoColorsCircleView`. They have some common and unique properties.

```xml
<com.nicolausyes.circleview.CircleView
    android:layout_width="@dimen/view_size"
    android:layout_height="@dimen/view_size"
    android:clickable="true"
    app:cv_color="#FFCA28"
    app:cv_border="true"
    app:cv_borderColor="#FF2074b9"
    app:cv_borderWidth="@dimen/border_size"
    app:cv_selectable="true"
    app:cv_selectColor="#E040FB"
    app:cv_selectBorderColor="#18FFFF"
    app:cv_selectBorderWidth="@dimen/border_size" />
```
```xml
<com.nicolausyes.circleview.TwoColorsCircleView
    android:layout_width="@dimen/view_size"
    android:layout_height="@dimen/view_size"
    android:clickable="true"
    app:cv_fillRadius="@dimen/view_radius"
    app:cv_firstColor="#00BCD4"
    app:cv_secondColor="#FF5722"
    app:cv_useCenter="true"
    app:cv_firstColorStartAngle="-90"
    app:cv_firstColorSweepAngle="-180"
    app:cv_secondColorStartAngle="90"
    app:cv_secondColorSweepAngle="-180"
    app:cv_selectable="true"
    app:cv_selectBorderColor="#FF2074b9"
    app:cv_selectBorderWidth="@dimen/border_size" />
```

Common properties:
* `cv_fillRadius`. Specifies radius if circle suppose to be not a full size of a view. By default circle is full size;
* `cv_border`. Whether or not to draw a border around the view.
* `cv_borderColor`. The color of the border draw around the view (if enabled).
* `cv_borderWidth`. Border width (if enabled).
* `cv_selectable`. Whether or not to draw a selector on this view upon touch events.
* `cv_selectBorderColor`. The color of the selector stroke drawn around the view upon touch events (if enabled).
* `cv_selectBorderWidth`.  The selector stroke width drawn around the view upon touch events (if enabled).

CircleView properties:
* `cv_color`. Circle's background color.
* `cv_selectColor`. Circle's background color on touch and select events (if enabled).

TwoColorsCircleView properties:
* `cv_firstColor`. Circle's first background color.
* `cv_secondColor`. Circle's second background color.
* `cv_firstColorStartAngle`.  Corresponds to [Canvas.drawArc()](http://developer.android.com/reference/android/graphics/Canvas.html#drawArc(android.graphics.RectF, float, float, boolean, android.graphics.Paint)) parameter. Starting angle (in degrees) where the arc begins. 
* `cv_firstColorSweepAngle`.  Corresponds to [Canvas.drawArc()](http://developer.android.com/reference/android/graphics/Canvas.html#drawArc(android.graphics.RectF, float, float, boolean, android.graphics.Paint)) parameter. Sweep angle (in degrees) measured clockwise.
* `cv_secondColorStartAngle`. The same as for first color.
* `cv_secondColorSweepAngle`. The same as for first color.
* `cv_useCenter`. Corresponds to [Canvas.drawArc()](http://developer.android.com/reference/android/graphics/Canvas.html#drawArc(android.graphics.RectF, float, float, boolean, android.graphics.Paint)) parameter. If true, include the center of the oval in the arc, and close it if it is being stroked. This will draw a wedge.


You can also set this parameters programatically.


Gradle
------
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.nicolausYes:CircleView:0.1'
}

```

