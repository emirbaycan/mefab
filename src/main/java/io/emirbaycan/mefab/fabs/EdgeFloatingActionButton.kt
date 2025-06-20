package io.emirbaycan.mefab.fabs

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.interfaces.Communicator

/**
 * An edge floating action button used in the expandable FAB menu.
 * Forwards click events to the parent Communicator.
 */

@SuppressLint("ClickableViewAccessibility")
@OptIn(MeFabRestricted::class)
public class EdgeFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val communicator: Communicator
        get() = parent as Communicator

    init {
        isClickable = true
        size = SIZE_MINI
        compatElevation = 6f

        setOnClickListener {
            communicator.onEdgeFabClick(id)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        communicator.onEdgeFabClick(id)
        return true
    }
}


/**
 * Callback wrapper for edge FAB clicks.
 */
public class OnEdgeFabClickListener(private val listener: (fabId: Int) -> Unit) {
    public fun onClick(fabId: Int): Any {
        return listener(fabId)
    }
}