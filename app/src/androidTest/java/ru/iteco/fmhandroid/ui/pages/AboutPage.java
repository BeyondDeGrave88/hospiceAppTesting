package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class AboutPage {

    private final int versionTextViewId = R.id.about_version_title_text_view;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(versionTextViewId, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(versionTextViewId)).check(matches(isDisplayed()));
    }
}