package io.emirbaycan.mefab.enums

/**
 * State.
 * representing the state of the whole view
 * when [EXPANDED] the [io.emirbaycan.mefab.fabs.CenterFloatingActionButton] shows the close icon
 * and the [io.emirbaycan.mefab.fabs.EdgeFloatingActionButton] shows on edges
 *
 * when [CLOSED] the [io.emirbaycan.mefab.fabs.CenterFloatingActionButton] shows the Menu icon
 * and the [io.emirbaycan.mefab.fabs.EdgeFloatingActionButton] shows on center
 */
internal enum class State {
    EXPANDED,
    CLOSED;

    fun inverse(): State = when (this) {
        EXPANDED -> CLOSED
        CLOSED -> EXPANDED
    }
}