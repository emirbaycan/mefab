package io.emirbaycan.mefab

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.doOnPreDraw
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import io.emirbaycan.mefab.enums.Position
import io.emirbaycan.mefab.fabs.CenterFloatingActionButton
import io.emirbaycan.mefab.fabs.EdgeFloatingActionButton
import io.emirbaycan.mefab.fabs.OnEdgeFabClickListener
import io.emirbaycan.mefab.interfaces.Communicator
import io.emirbaycan.mefab.models.Point
import io.emirbaycan.mefab.utils.*
import kotlin.properties.Delegates

/**
 * A custom expandable and movable FloatingActionButton group,
 * featuring a center FAB and up to 4 edge FABs arranged in a fan layout.
 * Handles click and move events, and supports configuration via XML attributes.
 */
@SuppressLint("ClickableViewAccessibility")
@OptIn(MeFabRestricted::class)
public class MovableExpandedFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MotionLayout(context, attrs, defStyleAttr), Communicator {

    // Listener for edge FAB clicks, passed from user
    private var edgeFabClickListener: OnEdgeFabClickListener? = null

    // IDs of edge FABs (derived from provided menu resource)
    private lateinit var edgeFabIds: List<Int>

    // Should the menu close automatically after an edge FAB is clicked
    private var closeAfterEdgeFabClick by Delegates.notNull<Boolean>()

    // The central FloatingActionButton
    private val centerFab: CenterFloatingActionButton =
        CenterFloatingActionButton(context, attrs, defStyleAttr)

    init {
        clipChildren = false
        clipToPadding = false

        // Add the center FAB and center it in the parent
        addView(centerFab)
        centerFab.updateLayoutParams<LayoutParams> {
            startToStart = this@MovableExpandedFloatingActionButton.id
            endToEnd = this@MovableExpandedFloatingActionButton.id
            topToTop = this@MovableExpandedFloatingActionButton.id
            bottomToBottom = this@MovableExpandedFloatingActionButton.id
        }

        // Load XML attributes
        context.withStyledAttributes(attrs, R.styleable.MovableExpandedFloatingActionButton) {
            closeAfterEdgeFabClick = getBoolean(
                R.styleable.MovableExpandedFloatingActionButton_closeAfterEdgeFabClick,
                false
            )
        }

        // Load MotionScene and initialize constraints after view draw
        doOnPreDraw {
            loadLayoutDescription(R.xml.scene)
            initializeStartConstraints()
        }
    }

    /**
     * Sets the click listener for edge FABs.
     */
    public fun setOnEdgeFabClickListener(listener: OnEdgeFabClickListener) {
        this.edgeFabClickListener = listener
    }

    fun setMenu(@MenuRes menuId: Int) {
        setMenu(menuId, null, 0)
    }

    fun setMenu(@MenuRes menuId: Int, attrs: AttributeSet? = null, defStyleAttr: Int = 0) {
        generateEdgeFabs(menuId, attrs, defStyleAttr)
        initializeStartConstraints()
    }
    /**
     * Inflate edge FABs from a menu resource and add them as children.
     */
    private fun generateEdgeFabs(menuId: Int, attrs: AttributeSet?, defStyleAttr: Int) {
        val popupMenu = PopupMenu(context, null)
        popupMenu.inflate(menuId)
        val size = popupMenu.menu.size()
        if (size > 4) throwNumberOfItemsItemsException(size)

        val ids = mutableListOf<Int>()
        popupMenu.menu.forEach { menuItem ->
            val edgeFab = EdgeFloatingActionButton(context, attrs, defStyleAttr).apply {
                setImageDrawable(menuItem.icon)
                id = menuItem.itemId
                ids.add(menuItem.itemId)
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    startToStart = this@MovableExpandedFloatingActionButton.id
                    endToEnd = this@MovableExpandedFloatingActionButton.id
                    topToTop = this@MovableExpandedFloatingActionButton.id
                    bottomToBottom = this@MovableExpandedFloatingActionButton.id
                }
            }
            addView(edgeFab)
        }
        edgeFabIds = ids
    }

    /**
     * Initialize all edge FABs in the center position at start.
     */
    private fun initializeStartConstraints() {
        getConstraintSet(R.id.start)?.let { constraintSet ->
            edgeFabIds.forEach {
                constraintSet.setConstraintsDefault(it, this.id)
                constraintSet.applyTo(this)
            }
        }
    }

    /**
     * Rearrange edge FABs according to the given position of the center FAB.
     */
    override fun onCenterFabPositionChange(newPosition: Position) {
        getConstraintSet(R.id.end)?.let { constraintSet ->
            val containerSize = resources.getDimension(R.dimen.container_size)
            val radius = containerSize * 0.4f
            when (newPosition) {
                Position.TOP_LEFT      -> constraintSet.setFanForViews(edgeFabIds, this.id, 90f, 180f, radius)
                Position.TOP_CENTER    -> constraintSet.setFanForViews(edgeFabIds, this.id, 90f, 270f, radius)
                Position.TOP_RIGHT     -> constraintSet.setFanForViews(edgeFabIds, this.id, 180f, 270f, radius)
                Position.CENTER_LEFT   -> constraintSet.setFanForViews(edgeFabIds, this.id, 360f, 540f, radius)
                Position.CENTER        -> constraintSet.setFanForViews(edgeFabIds, this.id, 0f, 360f, radius)
                Position.CENTER_RIGHT  -> constraintSet.setFanForViews(edgeFabIds, this.id, 180f, 360f, radius)
                Position.BOTTOM_RIGHT  -> constraintSet.setFanForViews(edgeFabIds, this.id, 270f, 360f, radius)
                Position.BOTTOM_CENTER -> constraintSet.setFanForViews(edgeFabIds, this.id, 270f, 450f, radius)
                Position.BOTTOM_LEFT   -> constraintSet.setFanForViews(edgeFabIds, this.id, 360f, 450f, radius)
            }
            constraintSet.applyTo(this)
        }
    }

    /**
     * Move the whole overlay container to a new screen point (for drag operations).
     */
    override fun onContainerMove(newPoint: Point) {
        animate()
            .x(newPoint.x)
            .y(newPoint.y)
            .setDuration(0)
            .start()
    }

    /**
     * Handles edge FAB click events and triggers close if configured.
     */
    override fun onEdgeFabClick(fabId: Int) {
        if (closeAfterEdgeFabClick) {
            centerFab.toggleState()
        }
        edgeFabClickListener?.onClick(fabId)
    }
}
