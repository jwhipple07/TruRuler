package com.truruler.truruler;


import android.os.SystemClock;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddToDatabaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addToDatabaseTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.appBarMain),
                                withParent(withId(R.id.drawer_layout)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.measure_edit_width), withText("0.0"),
                        childAtPosition(
                                allOf(withId(R.id.LinearLayout02),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("0.0")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.measure_edit_height), withText("0.0"),
                        childAtPosition(
                                allOf(withId(R.id.LinearLayout02),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                3),
                        isDisplayed()));
        editText2.check(matches(withText("0.0")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.measure_edit_width), withText("0.0"),
                        withParent(withId(R.id.LinearLayout02)),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("4"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.measure_edit_height), withText("0.0"),
                        withParent(withId(R.id.LinearLayout02)),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("4"));

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.measure_type),
                        withParent(withId(R.id.LinearLayout02)),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Inches"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.measure_edit_summary),
                        withParent(withId(R.id.LinearLayout01)),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("This is an automated test."));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.save_measurement), withContentDescription("Save"), isDisplayed()));
        actionMenuItemView.perform(click());

        onView(withId(R.id.DragView)).perform(swipeRight());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("View Saved Measurements"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.measure_width_row), withText("4"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("4")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.measure_height_row), withText("4"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("4")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.measure_type_row), withText("Inches"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("Inches")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.measure_descr_row), withText("This is an automated test."),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("This is an automated test.")));

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        0),
                        isDisplayed()));
        linearLayout.perform(longClick());

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.title), withText("Delete Measurement"), isDisplayed()));
        textView5.perform(click());

        SystemClock.sleep(2000);
        ViewInteraction textView6 = onView(
                allOf(withId(android.R.id.empty), withText("Currently there are measurements saved."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public static ViewAction swipeRight() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }
}
