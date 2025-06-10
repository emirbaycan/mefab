package io.emirbaycan.mefab

import android.annotation.SuppressLint

/**
 * Me fab restricted.
 * as [io.emirbaycan.mefab.models.Point] and [io.emirbaycan.mefab.enums.Position] used in [MovableExpandedFloatingActionButton]
 * so they must be public so i need to prevent user from using them
 */
@SuppressLint("ExperimentalAnnotationRetention")
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is internal API for MeFab library, please don't rely on it."
)
public annotation class MeFabRestricted