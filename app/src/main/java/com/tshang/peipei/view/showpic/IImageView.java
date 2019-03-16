package com.tshang.peipei.view.showpic;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;


public interface IImageView {
    /**
     * Returns true if the PhotoView is set to allow zooming of Photos.
     *
     * @return true if the PhotoView allows zooming.
     */
    boolean canZoom();

    /**
     * Gets the Display Rectangle of the currently displayed Drawable. The
     * Rectangle is relative to this View and includes all scaling and
     * translations.
     *
     * @return - RectF of Displayed Drawable
     */
    RectF getDisplayRect();

    /**
     * @return The current minimum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    float getMinScale();

    /**
     * @return The current middle scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    float getMidScale();

    /**
     * @return The current maximum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    float getMaxScale();

    /**
     * Returns the current scale value
     *
     * @return float - current scale value
     */
    float getScale();

    /**
     * Return the current scale type in use by the ImageView.
     */
    ImageView.ScaleType getScaleType();

    /**
     * Whether to allow the ImageView's parent to intercept the touch event when the photo is scroll to it's horizontal edge.
     */
    void setAllowParentInterceptOnEdge(boolean allow);

    /**
     * Sets the minimum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    void setMinScale(float minScale);

    /**
     * Sets the middle scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    void setMidScale(float midScale);

    /**
     * Sets the maximum scale level. What this value represents depends on the current {@link android.widget.ImageView.ScaleType}.
     */
    void setMaxScale(float maxScale);

    /**
     * Register a callback to be invoked when the Photo displayed by this view is long-pressed.
     *
     * @param listener - Listener to be registered.
     */
    void setOnLongClickListener(View.OnLongClickListener listener);
    void setScaleType(ImageView.ScaleType scaleType);

    /**
     * Allows you to enable/disable the zoom functionality on the ImageView.
     * When disable the ImageView reverts to using the FIT_CENTER matrix.
     *
     * @param zoomable - Whether the zoom functionality is enabled.
     */
    void setZoomable(boolean zoomable);

    /**
     * Zooms to the specified scale, around the focal point given.
     *
     * @param scale  - Scale to zoom to
     * @param focalX - X Focus Point
     * @param focalY - Y Focus Point
     */
    void zoomTo(float scale, float focalX, float focalY);
    
    void setOnPhotoTapListener(ControlImageView.OnPhotoTapListener listener);
    
}
