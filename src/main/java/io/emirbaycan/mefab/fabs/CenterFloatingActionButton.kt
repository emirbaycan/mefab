package io.emirbaycan.mefab.fabs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.R
import io.emirbaycan.mefab.enums.Position
import io.emirbaycan.mefab.enums.State
import io.emirbaycan.mefab.interfaces.Communicator
import io.emirbaycan.mefab.utils.FloatingFabOverlayManager
import io.emirbaycan.mefab.utils.getSeparators

/**
 * The central floating action button of the expandable FAB menu.
 * Handles icon animation, state transitions, and center position calculation.
 */
@SuppressLint("ClickableViewAccessibility")
@OptIn(MeFabRestricted::class)
internal class CenterFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FloatingActionButton(context, attrs, defStyleAttr) {

    /** Layout separators for dividing the screen into position zones. */
    private val separators = resources.displayMetrics.getSeparators()

    /** Current open/close state of the FAB menu. */
    private var state = State.CLOSED

    /** Accessor for the Communicator interface implemented by parent. */
    val communicator: Communicator
        get() = parent as Communicator

    /** Parent MotionLayout for position/animation context. */
    private val motionLayout: MotionLayout
        get() = this.parent as MotionLayout

    init {
        id = R.id.center_fab
        isClickable = true
        compatElevation = 0f

        val sizePx = (56 * context.resources.displayMetrics.density).toInt()
        layoutParams = ConstraintLayout.LayoutParams(sizePx, sizePx)

        // Initial icon is "menu to close" animated drawable
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.menu_to_close_anim))
    }

    /**
     * Ignore direct touch events—overlay handles drag/tap externally.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean = true

    /**
     * Handle clicks by toggling state and updating overlay.
     */
    override fun performClick(): Boolean {
        super.performClick()
        toggleState()
        return true
    }

    /**
     * Toggles between open/closed state, resizes overlay, animates icon,
     * and notifies Communicator of new position.
     */
    public fun toggleState() {
        state = state.inverse()
        FloatingFabOverlayManager.resizeOverlay(state == State.EXPANDED)
        updateIconAnimation()
        communicator.onCenterFabPositionChange(getCenterFabPositionOnScreen())
    }

    /**
     * Animate icon depending on state.
     */
    private fun updateIconAnimation() {
        when (state) {
            State.EXPANDED -> animateIconToClose()
            State.CLOSED   -> animateIconToMenu()
        }
    }

    /**
     * Switch icon animation to "menu" state.
     */
    private fun animateIconToMenu() {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.close_to_menu_anim))
        (drawable as? AnimatedVectorDrawable)?.start()
    }

    /**
     * Switch icon animation to "close" state.
     */
    private fun animateIconToClose() {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.menu_to_close_anim))
        (drawable as? AnimatedVectorDrawable)?.start()
    }

    /**
     * Calculate which part of the screen the center FAB currently occupies.
     * Returns the appropriate Position enum.
     * Throws if position is ambiguous (should never happen).
     */
    fun getCenterFabPositionOnScreen(): Position {
        val location = IntArray(2)
        getLocationOnScreen(location)
        val x = location[0] + motionLayout.width / 2
        val y = location[1] + motionLayout.height / 2

        return when {
            y in separators.borderToY1Rang && x in separators.borderToX1Rang     -> Position.TOP_LEFT
            y in separators.borderToY1Rang && x in separators.x1ToX2Range        -> Position.TOP_CENTER
            y in separators.borderToY1Rang && x in separators.x2ToBorderRange    -> Position.TOP_RIGHT

            y in separators.y1ToY2Range && x in separators.borderToX1Rang        -> Position.CENTER_LEFT
            y in separators.y1ToY2Range && x in separators.x1ToX2Range           -> Position.CENTER
            y in separators.y1ToY2Range && x in separators.x2ToBorderRange       -> Position.CENTER_RIGHT

            y in separators.y2ToBorderRange && x in separators.borderToX1Rang    -> Position.BOTTOM_LEFT
            y in separators.y2ToBorderRange && x in separators.x1ToX2Range       -> Position.BOTTOM_CENTER
            y in separators.y2ToBorderRange && x in separators.x2ToBorderRange   -> Position.BOTTOM_RIGHT
            else -> throw IllegalStateException("Cannot determine center FAB position for (x=$x, y=$y)")
        }
    }

    /**
     * Return current FAB state (useful for persistence).
     */
    fun getCurrentState(): State = state

    /**
     * Ensure correct icon animation on reattach (e.g., fragment switch).
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateIconAnimation()
    }
}
