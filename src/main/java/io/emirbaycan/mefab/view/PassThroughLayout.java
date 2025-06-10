package io.emirbaycan.mefab.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import io.emirbaycan.mefab.fabs.CenterFloatingActionButton;
import io.emirbaycan.mefab.enums.State;

/**
 * A FrameLayout that passes touch events through to its children and below,
 * but can intercept outside touches when the FAB menu is expanded.
 */
public class PassThroughLayout extends FrameLayout {

    /** Reference to the central FAB, for handling collapse on background tap. */
    private CenterFloatingActionButton centerFab;

    public PassThroughLayout(Context context) {
        super(context);
    }

    public PassThroughLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Always allow children and underlying views to receive touch events.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * If the FAB menu is expanded and user taps background, intercept and handle collapse.
     * (TODO: Uncomment centerFab.performClick() to enable collapse on outside touch.)
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Optionally collapse FAB menu on background tap:
        // centerFab.performClick();
        return centerFab != null
                && centerFab.getCurrentState() == State.EXPANDED
                && event.getAction() == MotionEvent.ACTION_DOWN;
    }

}
