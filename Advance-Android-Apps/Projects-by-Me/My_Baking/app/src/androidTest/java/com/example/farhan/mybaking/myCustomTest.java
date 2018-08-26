package com.example.farhan.mybaking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.farhan.mybaking.model.Recipe;
import com.example.farhan.mybaking.utils.Prefs;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class myCustomTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void clickMainRecycleViewItem() {
        //Magic happening
        onView(withId(R.id.rc_view_main))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));

        onView(withId(R.id.btn_details_menu_add_to_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.rc_view_details_steps))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));

        Recipe recipe = Prefs.getRecipePref(mActivityTestRule.getActivity());

        assertNotNull(recipe);
    }


    // unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
