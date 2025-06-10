package io.emirbaycan.mefab.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.MovableExpandedFloatingActionButton
import io.emirbaycan.mefab.R
import io.emirbaycan.mefab.fabs.CenterFloatingActionButton
import io.emirbaycan.mefab.view.PassThroughLayout

/**
 * FloatingFabOverlayManager manages the overlay window for the floating action button menu,
 * including display, movement, resizing, and removal.
 */
@SuppressLint("StaticFieldLeak")
public object FloatingFabOverlayManager {
    /** The root overlay view attached to WindowManager. */
    public var overlayView: View? = null
        private set

    /** Reference to WindowManager used for adding/removing/updating overlay. */
    public var windowManager: WindowManager? = null
        private set

    /** The main MovableExpandedFloatingActionButton view (inside overlayView). */
    private var fab: MovableExpandedFloatingActionButton? = null

    /** Last known FAB center coordinates (used for correct overlay placement). */
    private var centerFabRefX: Int = 0
    private var centerFabRefY: Int = 0

    /** WindowManager.LayoutParams for current overlay view. */
    public var overlayLayoutParams: WindowManager.LayoutParams? = null
        private set

    /**
     * Returns the MovableExpandedFloatingActionButton instance if overlay is visible.
     */
    public fun getFabView(): MovableExpandedFloatingActionButton? = fab

    /**
     * Shows the overlay on the screen, creating the view if not already shown.
     * Safe to call repeatedly.
     */
    @OptIn(MeFabRestricted::class)
    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    public fun showOverlay(context: Context) {
        if (overlayView != null) return // Already displayed

        val appContext = context.applicationContext
        windowManager = appContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager

        // Use AppCompat themed context for proper style inflation
        val themedContext = ContextThemeWrapper(context, androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
        val inflater = LayoutInflater.from(themedContext)
        val root = inflater.inflate(R.layout.floating_button, null) as PassThroughLayout

        fab = root.findViewById(R.id.me_fab)
        val dp = fab!!.resources.displayMetrics.density

        // Initial overlay size and WindowManager params
        val params = WindowManager.LayoutParams(
            (56 * dp).toInt(),
            (56 * dp).toInt(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 0
        }
        overlayLayoutParams = params

        windowManager?.addView(root, params)
        overlayView = root

        val centerFab = fab?.findViewById<CenterFloatingActionButton>(R.id.center_fab)

        // Compute initial center FAB coordinates for later repositioning
        val loc = IntArray(2)
        centerFab?.getLocationOnScreen(loc)
        centerFabRefX = loc[0] + (centerFab?.width ?: 0) / 2
        centerFabRefY = loc[1] + (centerFab?.height ?: 0) / 2

        // Handle drag and tap events for moving/clicking the FAB
        var isDragging = false
        var initialTouchX = 0f
        var initialTouchY = 0f
        var touchOffsetX = 0f
        var touchOffsetY = 0f
        val dragThresholdPx = 10 // Threshold for drag detection

        centerFab?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val p = overlayLayoutParams
                    touchOffsetX = event.rawX - (p?.x ?: 0)
                    touchOffsetY = event.rawY - (p?.y ?: 0)
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - initialTouchX
                    val dy = event.rawY - initialTouchY
                    if (!isDragging && (kotlin.math.abs(dx) > dragThresholdPx || kotlin.math.abs(dy) > dragThresholdPx)) {
                        isDragging = true
                    }
                    if (isDragging) {
                        val p = overlayLayoutParams
                        val displayMetrics = Resources.getSystem().displayMetrics
                        val screenWidth = displayMetrics.widthPixels
                        val screenHeight = displayMetrics.heightPixels

                        val fabWidth = centerFab.width
                        val fabHeight = centerFab.height
                        val overlayW = p?.width ?: overlayView?.width ?: 0
                        val overlayH = p?.height ?: overlayView?.height ?: 0

                        val isExpanded = overlayW > 100 // Change this if you have a state variable

                        var newX = (event.rawX - touchOffsetX).toInt()
                        var newY = (event.rawY - touchOffsetY).toInt()

                        if (isExpanded) {
                            // When expanded, keep the center in screen
                            val centerOffsetX = overlayW / 2
                            val centerOffsetY = overlayH / 2

                            val minX = 0 - centerOffsetX + fabWidth / 2
                            val maxX = screenWidth - centerOffsetX - fabWidth / 2
                            val minY = 0 - centerOffsetY + fabHeight / 2
                            val maxY = screenHeight - centerOffsetY - fabHeight / 2

                            newX = newX.coerceIn(minX, maxX)
                            newY = newY.coerceIn(minY, maxY)
                        } else {
                            // When collapsed, prevent FAB from leaving the screen
                            val minX = 0
                            val maxX = screenWidth - fabWidth
                            val minY = 0
                            val maxY = screenHeight - fabHeight

                            newX = newX.coerceIn(minX, maxX)
                            newY = newY.coerceIn(minY, maxY)
                        }

                        p?.x = newX
                        p?.y = newY
                        windowManager?.updateViewLayout(overlayView, p)

                        // Update FAB center coordinates
                        val centerX = (p?.x ?: 0) + (overlayW / 2)
                        val centerY = (p?.y ?: 0) + (overlayH / 2)
                        centerFabRefX = centerX
                        centerFabRefY = centerY

                        // Optionally: update FAB position state for menu fanning
                        (centerFab as? CenterFloatingActionButton)?.communicator?.onCenterFabPositionChange(
                            centerFab.getCenterFabPositionOnScreen()
                        )
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!isDragging) {
                        // Tap: open menu
                        centerFab.performClick()
                    }
                    isDragging = false
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Resize the overlay for expanded/collapsed state, preserving center position.
     */
    public fun resizeOverlay(isExpanded: Boolean) {
        val params = overlayLayoutParams ?: return
        val dp = fab!!.resources.displayMetrics.density

        val fabCenterX = centerFabRefX
        val fabCenterY = centerFabRefY

        val toWidth = if (isExpanded) (200 * dp).toInt() else (56 * dp).toInt()
        val toHeight = if (isExpanded) (200 * dp).toInt() else (56 * dp).toInt()
        val toX = fabCenterX - toWidth / 2
        val toY = fabCenterY - toHeight / 2

        // Hide overlay while resizing to avoid layout glitches
        overlayView?.visibility = View.INVISIBLE

        // Update WindowManager params
        params.x = toX
        params.y = toY
        params.width = toWidth
        params.height = toHeight
        windowManager?.updateViewLayout(overlayView, params)

        // Restore overlay visibility after layout
        overlayView?.post {
            overlayView?.visibility = View.VISIBLE
        }
    }

    /**
     * Removes the overlay from the window and releases references.
     */
    public fun hideOverlay() {
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
            fab = null
            windowManager = null
        }
    }
}
