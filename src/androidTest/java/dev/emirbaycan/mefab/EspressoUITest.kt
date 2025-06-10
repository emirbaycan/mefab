package dev.emirbaycan.mefab

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import io.emirbaycan.mefab.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class FabOverlayInstrumentedTest {

    @get:Rule
    public val activityRule: ActivityScenarioRule<FabTestActivity> = ActivityScenarioRule(FabTestActivity::class.java)

    @Test
    public fun testFabOverlayVisible() {
        // Verify the FAB overlay root is displayed
        onView(withId(R.id.me_fab)).check(matches(isDisplayed()))
    }

    @Test
    public fun testFabOpensMenu() {
        onView(withId(R.id.me_fab)).perform(click())
        // Ör: İlk edge FAB id'si action_camera ise:
        onView(withId(R.id.fab_live)).check(matches(isDisplayed()))
    }

}
