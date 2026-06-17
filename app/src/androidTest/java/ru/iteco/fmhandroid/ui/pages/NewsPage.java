package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class NewsPage {

    private final int newsListId = R.id.news_list_recycler_view;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(newsListId, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(newsListId)).check(matches(isDisplayed()));
    }
}