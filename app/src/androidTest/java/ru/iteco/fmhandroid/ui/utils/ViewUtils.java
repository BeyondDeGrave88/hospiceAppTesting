package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

public class ViewUtils {

    public static void waitForView(int viewId, long timeoutMillis) {
        waitForView(withId(viewId), timeoutMillis);
    }

    public static void waitForView(Matcher<View> matcher, long timeoutMillis) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMillis) {
            try {
                onView(matcher).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionFailedError e) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new AssertionError("View matching " + matcher + " not displayed within " + timeoutMillis + " ms");
    }
}