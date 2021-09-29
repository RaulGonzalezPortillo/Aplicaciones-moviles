package com.example.unittesting

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)

class MainActivityTest {
    @get:Rule
    var intentScenarioRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun dataEntry() {
        onView(withId(R.id.nameEditText))
            .perform(typeText("raul"), closeSoftKeyboard())

        onView(withId(R.id.femaleRB))
            .perform(click())

        onView(withId(R.id.maleRB))
            .perform(click())

        intending(hasExtraWithKey("NAME"))

        val dataResponse: Intent =
            Iterables.getOnlyElement(Intents.getIntents())

        assertEquals(dataResponse.extras !! .getString("NAME"), "raul")
    }
}