package com.project.simoneconigliaro.bakingapp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.project.simoneconigliaro.bakingapp.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Simone on 07/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class BakingAppTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    private final static String FIRST_RECIPE = "Nutella Pie";
    private final static String SECOND_RECIPE = "Brownies";
    private final static String THIRD_RECIPE = "Yellow Cake";
    private final static String FOURTH_RECIPE = "Cheesecake";
    private final static String FIRST_STEP = "Recipe Introduction";
    private final static String SECOND_STEP = "Starting prep";

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void appIuTest() {

        onView(nthChildOf(withId(R.id.rv_list_recipes), 0)).check(matches(hasDescendant(withText(FIRST_RECIPE))));
        onView(nthChildOf(withId(R.id.rv_list_recipes), 1)).check(matches(hasDescendant(withText(SECOND_RECIPE))));
        onView(nthChildOf(withId(R.id.rv_list_recipes), 2)).check(matches(hasDescendant(withText(THIRD_RECIPE))));
        onView(withId(R.id.rv_list_recipes)).perform(RecyclerViewActions.scrollToPosition(3)).check(matches(hasDescendant(withText(FOURTH_RECIPE))));

        onView(withId(R.id.rv_list_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        matchToolbarTitle(FIRST_RECIPE);

        onView(nthChildOf(withId(R.id.rv_list_steps), 0)).check(matches(hasDescendant(withText(FIRST_STEP))));
        onView(withId(R.id.rv_list_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView((withId(R.id.tv_short_description_step))).check(matches(withText(FIRST_STEP)));

        onView(withId(R.id.button_next_step)).perform(click());
        onView((withId(R.id.tv_short_description_step))).check(matches(withText(SECOND_STEP)));

    }

    private static ViewInteraction matchToolbarTitle(
            CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));

    }

    private static Matcher<Object> withToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && view.equals(group.getChildAt(childPosition));
            }
        };
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
