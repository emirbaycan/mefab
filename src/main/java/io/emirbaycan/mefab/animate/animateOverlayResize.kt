package io.emirbaycan.mefab.animate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import io.emirbaycan.mefab.R
import io.emirbaycan.mefab.fabs.CenterFloatingActionButton
import io.emirbaycan.mefab.utils.FloatingFabOverlayManager.overlayLayoutParams
import io.emirbaycan.mefab.utils.FloatingFabOverlayManager.overlayView
import io.emirbaycan.mefab.utils.FloatingFabOverlayManager.windowManager

public fun animateOverlayResize(
    fromWidth: Int, toWidth: Int,
    fromHeight: Int, toHeight: Int,
    fromX: Int, toX: Int,
    fromY: Int, toY: Int,
    duration: Long = 220L
) {
    // --- 1. ANİMASYON BAŞLAMADAN ÖNCE CHILDLARI GİZLE ---
    val centerFab = overlayView?.findViewById<CenterFloatingActionButton>(R.id.center_fab)
    centerFab?.visibility = View.INVISIBLE

    // Eğer edgeFab'ları da gizlemek istersen burada benzer şekilde ekle:
    // val edgeFab1 = overlayView?.findViewById<EdgeFloatingActionButton>(R.id.edge_fab1)
    // edgeFab1?.visibility = View.INVISIBLE
    // (Tüm edge fab'lar için loop veya ayrı ayrı...)

    val animator = ValueAnimator.ofFloat(0f, 1f)
    animator.duration = duration
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.addUpdateListener { valueAnimator ->

        val fraction = valueAnimator.animatedFraction

        val currWidth = (fromWidth + (toWidth - fromWidth) * fraction).toInt()
        val currHeight = (fromHeight + (toHeight - fromHeight) * fraction).toInt()
        val currX = (fromX + (toX - fromX) * fraction).toInt()
        val currY = (fromY + (toY - fromY) * fraction).toInt()

        Log.v("FAB_ANIM", "fraction: $fraction width: $currWidth height: $currHeight x: $currX y: $currY")

        val params = overlayLayoutParams ?: return@addUpdateListener
        params.width = currWidth
        params.height = currHeight
        params.x = currX
        params.y = currY
        windowManager?.updateViewLayout(overlayView, params)
    }
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            Log.d("FAB_ANIM", "Animasyon bitti, width:$toWidth height:$toHeight x:$toX y:$toY")
            // --- 2. ANİMASYON BİTİNCE CHILDLARI GÖSTER ---
            centerFab?.visibility = View.VISIBLE
            // EdgeFab'ları da tekrar görünür yapmak istersen burada ekle
            // edgeFab1?.visibility = View.VISIBLE
        }
    })
    animator.start()
}
