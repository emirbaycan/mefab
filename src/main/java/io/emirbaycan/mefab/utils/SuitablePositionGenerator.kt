package io.emirbaycan.mefab.utils

import io.emirbaycan.mefab.MeFabRestricted
import io.emirbaycan.mefab.enums.Position

/**
 * File supplies the suitable positions for the [io.emirbaycan.mefab.fabs.EdgeFloatingActionButton] based on the size of menu
 *  to make them fit nicely in the screen
 */

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInTopLeft(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.BOTTOM_RIGHT)
        2 -> listOf(Position.CENTER_RIGHT, Position.BOTTOM_CENTER)
        3 -> listOf(Position.CENTER_RIGHT, Position.BOTTOM_RIGHT, Position.BOTTOM_CENTER)
        4 -> listOf(Position.TOP_CENTER, Position.CENTER_RIGHT, Position.BOTTOM_CENTER, Position.BOTTOM_RIGHT)
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInTopCenter(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.BOTTOM_CENTER)
        2 -> listOf(Position.BOTTOM_RIGHT, Position.BOTTOM_LEFT)
        3 -> listOf(Position.CENTER_RIGHT, Position.BOTTOM_CENTER, Position.CENTER_LEFT)
        4 -> listOf(Position.TOP_LEFT, Position.CENTER_LEFT, Position.CENTER_RIGHT, Position.TOP_RIGHT)
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInTopRight(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.BOTTOM_LEFT)
        2 -> listOf(Position.BOTTOM_CENTER, Position.CENTER_LEFT)
        3 -> listOf(Position.BOTTOM_CENTER, Position.BOTTOM_LEFT, Position.CENTER_LEFT)
        4 -> listOf(Position.TOP_CENTER, Position.CENTER_LEFT, Position.BOTTOM_CENTER, Position.BOTTOM_LEFT) // Sol aşağı yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInCenterLeft(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.CENTER_RIGHT)
        2 -> listOf(Position.TOP_RIGHT, Position.BOTTOM_RIGHT)
        3 -> listOf(Position.TOP_CENTER, Position.CENTER_RIGHT, Position.BOTTOM_CENTER)
        4 -> listOf(Position.TOP_LEFT, Position.TOP_CENTER, Position.BOTTOM_CENTER, Position.BOTTOM_LEFT) // Soldan içeri yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInCenter(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.TOP_CENTER)
        2 -> listOf(Position.TOP_LEFT, Position.TOP_RIGHT)
        3 -> listOf(Position.TOP_CENTER, Position.BOTTOM_RIGHT, Position.BOTTOM_LEFT)
        4 -> listOf(Position.TOP_LEFT, Position.TOP_CENTER, Position.TOP_RIGHT, Position.CENTER_RIGHT) // Üste ve sağa açık yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInCenterRight(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.CENTER_LEFT)
        2 -> listOf(Position.BOTTOM_LEFT, Position.TOP_LEFT)
        3 -> listOf(Position.BOTTOM_CENTER, Position.CENTER_LEFT, Position.TOP_CENTER)
        4 -> listOf(Position.TOP_RIGHT, Position.TOP_CENTER, Position.BOTTOM_CENTER, Position.BOTTOM_RIGHT) // Sağdan içeri yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInBottomLeft(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.TOP_RIGHT)
        2 -> listOf(Position.TOP_CENTER, Position.CENTER_RIGHT)
        3 -> listOf(Position.TOP_CENTER, Position.TOP_RIGHT, Position.CENTER_RIGHT)
        4 -> listOf(Position.BOTTOM_CENTER, Position.CENTER_RIGHT, Position.TOP_CENTER, Position.TOP_RIGHT) // Yukarı-sağa yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInBottomCenter(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.TOP_CENTER)
        2 -> listOf(Position.TOP_LEFT, Position.TOP_RIGHT)
        3 -> listOf(Position.CENTER_LEFT, Position.TOP_CENTER, Position.CENTER_RIGHT)
        4 -> listOf(Position.BOTTOM_LEFT, Position.CENTER_LEFT, Position.CENTER_RIGHT, Position.BOTTOM_RIGHT) // Alta doğru yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

@OptIn(MeFabRestricted::class)
internal fun suitablePositionsForChildrenInBottomRight(fabSize: Int): List<Position> {
    return when (fabSize) {
        1 -> listOf(Position.TOP_LEFT)
        2 -> listOf(Position.CENTER_LEFT, Position.TOP_CENTER)
        3 -> listOf(Position.CENTER_LEFT, Position.TOP_LEFT, Position.TOP_CENTER)
        4 -> listOf(Position.BOTTOM_CENTER, Position.CENTER_LEFT, Position.TOP_CENTER, Position.TOP_LEFT) // Yukarı-sola yelpaze
        else -> throwNumberOfItemsItemsException(fabSize)
    }
}

internal fun throwNumberOfItemsItemsException(size: Int): Nothing {
    throw IllegalStateException("Max items is 4 and Min is 1, the current is $size")
}