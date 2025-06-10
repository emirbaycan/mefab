package io.emirbaycan.mefab.interfaces

import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.enums.Position
import io.emirbaycan.mefab.models.Point

/**
 * Communicator interface enables event-driven communication between core
 * MeFab components, including the MovableExpandedFloatingActionButton,
 * CenterFloatingActionButton, and EdgeFloatingActionButton.
 *
 * Implement this interface in your container or overlay to receive FAB movement and click events.
 */
@MeFabRestricted
internal interface Communicator {

    /**
     * Called when the center FAB moves to a new logical screen position.
     *
     * @param newPosition New [Position] of the center FAB (e.g., TOP_LEFT, CENTER, etc.)
     */
    fun onCenterFabPositionChange(newPosition: Position)

    /**
     * Called when the FAB menu container moves to a new screen coordinate.
     *
     * @param newPoint The new [Point] (x, y) position of the container.
     */
    fun onContainerMove(newPoint: Point)

    /**
     * Called when an edge FAB is clicked.
     *
     * @param fabId Resource ID of the clicked [EdgeFloatingActionButton].
     */
    fun onEdgeFabClick(fabId: Int)
}
