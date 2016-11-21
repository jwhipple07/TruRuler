package com.truruler.truruler;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class GuiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void testMethod1() {
        onView(withId(R.id.DragView)).check(matches(isDisplayed()));        //check if dragbars are shown
        onView(withId(R.id.DrawView)).perform(longClick()); //hide them
        onView(withId(R.id.DragView)).check(matches(not(isDisplayed())));   //check they are hidden
        onView(withId(R.id.DrawView)).perform(longClick());                         //redisplay them
        onView(withId(R.id.DragView)).check(matches(isDisplayed()));        //check they are there
        onView(withId(R.id.DragViewVertical)).perform(swipeDown());        //move the draggable object
        onView(withId(R.id.DragView)).perform(swipeRight());
    }
    public static ViewAction swipeDown() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_CENTER,
                GeneralLocation.CENTER, Press.FINGER);
    }
    public static ViewAction swipeRight() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }
}