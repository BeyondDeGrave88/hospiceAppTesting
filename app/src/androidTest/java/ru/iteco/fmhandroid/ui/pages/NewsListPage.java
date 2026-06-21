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

    private final int editButtonId = R.id.edit_news_material_button;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(editButtonId, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(editButtonId)).check(matches(isDisplayed()));
    }

    public void openEditPage() {
        onView(withId(R.id.edit_news_material_button)).perform(click());
    }

    public void expandNews() {
        onView(withId(R.id.expand_material_button)).perform(click());
    }

    public void checkNewsListExists() {
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
    }

    public void clickOnNewsTitle(String title) {
        onView(withText(title)).perform(click());
    }

    public void refreshNewsList() {
        onView(withId(R.id.news_list_swipe_refresh)).perform(swipeDown());
    }
}