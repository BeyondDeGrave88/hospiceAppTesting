package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class NewsListPage {

    public static final int EDIT_NEWS_BUTTON_ID = R.id.edit_news_material_button;
    public static final int EXPAND_BUTTON_ID = R.id.expand_material_button;
    public static final int NEWS_LIST_RECYCLER_VIEW_ID = R.id.news_list_recycler_view;
    public static final int SWIPE_REFRESH_ID = R.id.news_list_swipe_refresh;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(EDIT_NEWS_BUTTON_ID, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(EDIT_NEWS_BUTTON_ID)).check(matches(isDisplayed()));
    }

    public void openEditPage() {
        onView(withId(EDIT_NEWS_BUTTON_ID)).perform(click());
    }

    public void expandNews() {
        onView(withId(EXPAND_BUTTON_ID)).perform(click());
    }

    public void checkNewsListExists() {
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID)).check(matches(isDisplayed()));
    }

    public void clickOnNewsTitle(String title) {
        onView(withText(title)).perform(click());
    }

    public void refreshNewsList() {
        onView(withId(SWIPE_REFRESH_ID)).perform(swipeDown());
    }
}