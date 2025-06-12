package io.emirbaycan.mefab.utils

import androidx.constraintlayout.widget.ConstraintSet
import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.enums.Position
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Utility extension functions for setting and updating ConstraintSet positions for FAB overlays.
 */

/**
 * Clears all constraints for the specified view and sets its size to WRAP_CONTENT.
 */
internal fun ConstraintSet.clearConstraints(viewId: Int) {
    clear(viewId)
    constrainWidth(viewId, ConstraintSet.WRAP_CONTENT)
    constrainHeight(viewId, ConstraintSet.WRAP_CONTENT)
}

/**
 * Centers the view within its parent and resets scale to 0.5.
 */
internal fun ConstraintSet.setConstraintsDefault(viewId: Int, parentId: Int) {
    clearConstraints(viewId)
    constraintCenter(viewId, parentId)
    setScaleX(viewId, 0.5f)
    setScaleY(viewId, 0.5f)
}

/**
 * Moves the view to a screen position according to [Position] (e.g., top-left, center, etc).
 * Scales view to 1.0 (full size).
 */
@OptIn(MeFabRestricted::class)
internal fun ConstraintSet.setNewConstraints(viewId: Int, parentId: Int, position: Position) {
    setScaleX(viewId, 1f)
    setScaleY(viewId, 1f)
    when (position) {
        Position.TOP_LEFT      -> constraintTopLeft(viewId, parentId)
        Position.TOP_CENTER    -> constraintTopCenter(viewId, parentId)
        Position.TOP_RIGHT     -> constraintTopRight(viewId, parentId)
        Position.CENTER_LEFT   -> constraintCenterLeft(viewId, parentId)
        Position.CENTER        -> constraintCenter(viewId, parentId)
        Position.CENTER_RIGHT  -> constraintCenterRight(viewId, parentId)
        Position.BOTTOM_LEFT   -> constraintBottomLeft(viewId, parentId)
        Position.BOTTOM_CENTER -> constraintBottomCenter(viewId, parentId)
        Position.BOTTOM_RIGHT  -> constraintBottomRight(viewId, parentId)
        Position.UNDEFINED -> false
    }
}

// -- Private constraint helper methods --

private fun ConstraintSet.constraintTopLeft(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
}

private fun ConstraintSet.constraintTopCenter(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
}

private fun ConstraintSet.constraintTopRight(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
}

private fun ConstraintSet.constraintCenterLeft(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
}

private fun ConstraintSet.constraintCenter(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
}

private fun ConstraintSet.constraintCenterRight(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
    connect(viewId, ConstraintSet.TOP, parentId, ConstraintSet.TOP)
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
}

private fun ConstraintSet.constraintBottomLeft(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
}

private fun ConstraintSet.constraintBottomCenter(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
    connect(viewId, ConstraintSet.START, parentId, ConstraintSet.START)
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
}

private fun ConstraintSet.constraintBottomRight(viewId: Int, parentId: Int) {
    connect(viewId, ConstraintSet.END, parentId, ConstraintSet.END)
    connect(viewId, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM)
}

/**
 * Arrange multiple views in a circular "fan" around the parent center.
 *
 * @param viewIds      List of view IDs to arrange.
 * @param parentId     Parent view ID.
 * @param fanStartAngle Starting angle (degrees).
 * @param fanEndAngle   Ending angle (degrees).
 * @param radius        Distance from center.
 */
public fun ConstraintSet.setFanForViews(
    viewIds: List<Int>,
    parentId: Int,
    fanStartAngle: Float,
    fanEndAngle: Float,
    radius: Float
) {
    val count = viewIds.size
    val sweep = if (fanEndAngle > fanStartAngle)
        fanEndAngle - fanStartAngle
    else
        360f - fanStartAngle + fanEndAngle

    val realEnd = if (count > 1 && sweep == 360f) fanEndAngle - (360f / count) else fanEndAngle
    val angleStep = if (count > 1) (realEnd - fanStartAngle) / (count - 1) else 0f

    viewIds.forEachIndexed { i, viewId ->
        val angle = fanStartAngle + i * angleStep
        constrainCircle(viewId, parentId, radius.roundToInt(), angle % 360)
    }
}

/**
 * Alternative: Set a view's position in a fan/circle using direct center offset (not used in core).
 */
public fun ConstraintSet.setFanConstraint(
    viewId: Int,
    parentId: Int,
    centerX: Float,
    centerY: Float,
    angleDeg: Float,
    radius: Float
) {
    val angleRad = Math.toRadians(angleDeg.toDouble())
    val xOffset = (cos(angleRad) * radius).toFloat()
    val yOffset = (sin(angleRad) * radius).toFloat()

    // Note: Can be used for advanced custom layouts if needed.
    constrainCircle(viewId, parentId, radius.roundToInt(), angleDeg.roundToInt().toFloat())
}
