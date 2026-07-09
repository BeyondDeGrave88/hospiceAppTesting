package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

public final class ViewUtils {

    private static final long DEFAULT_TIMEOUT = 15000;
    private static final long POLLING_INTERVAL = 300;

    private ViewUtils() {
    }

    public static void waitForView(int viewId, long timeoutMillis) {
        waitForView(withId(viewId), timeoutMillis);
    }

    public static void waitForView(Matcher<View> matcher, long timeoutMillis) {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(matcher).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionFailedError ignored) {
                SystemClock.sleep(POLLING_INTERVAL);
            }
        }
        throw new AssertionError(
                "Объект " + matcher + " не отобразился в течение "
                        + timeoutMillis + " мс.");
    }

    public static boolean isViewDisplayed(int viewId) {
        return isViewDisplayed(withId(viewId));
    }

    public static boolean isViewDisplayed(Matcher<View> matcher) {
        try {
            onView(matcher).check(matches(isDisplayed()));
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}