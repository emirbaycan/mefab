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
import io.emirbaycan.mefab.utils.Separators
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
        compatElevation = 7f

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
    fun toggleState() {
        state = state.inverse()
        FloatingFabOverlayManager.resizeOverlay(state == State.EXPANDED)
        updateIconAnimation()
        communicator.onCenterFabPositionChange(getFabPositionOnScreen())
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
    public fun getFabPositionOnScreen(): Position {
        val location = IntArray(2)
        getLocationOnScreen(location)

        val fabLeft = location[0]
        val fabTop = location[1]
        val fabRight = fabLeft + width
        val fabBottom = fabTop + height

        // X ve Y ekseni için her 3 alanı kontrol et: sol, orta, sağ ve üst, orta, alt
        fun inRange(range: IntRange, vararg values: Int) = values.any { it in range }

        val xZone = when {
            inRange(separators.borderToX1Rang, fabLeft, fabRight) -> "LEFT"
            inRange(separators.x1ToX2Range, fabLeft, fabRight)    -> "CENTER"
            inRange(separators.x2ToBorderRange, fabLeft, fabRight) -> "RIGHT"
            else -> "UNDEFINED"
        }

        val yZone = when {
            inRange(separators.borderToY1Rang, fabTop, fabBottom) -> "TOP"
            inRange(separators.y1ToY2Range, fabTop, fabBottom)    -> "CENTER"
            inRange(separators.y2ToBorderRange, fabTop, fabBottom) -> "BOTTOM"
            else -> "UNDEFINED"
        }

        return when {
            xZone == "LEFT" && yZone == "TOP" -> Position.TOP_LEFT
            xZone == "CENTER" && yZone == "TOP" -> Position.TOP_CENTER
            xZone == "RIGHT" && yZone == "TOP" -> Position.TOP_RIGHT

            xZone == "LEFT" && yZone == "CENTER" -> Position.CENTER_LEFT
            xZone == "CENTER" && yZone == "CENTER" -> Position.CENTER
            xZone == "RIGHT" && yZone == "CENTER" -> Position.CENTER_RIGHT

            xZone == "LEFT" && yZone == "BOTTOM" -> Position.BOTTOM_LEFT
            xZone == "CENTER" && yZone == "BOTTOM" -> Position.BOTTOM_CENTER
            xZone == "RIGHT" && yZone == "BOTTOM" -> Position.BOTTOM_RIGHT

            else -> Position.UNDEFINED
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
