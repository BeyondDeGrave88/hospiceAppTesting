package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class FilterNewsPage {

    public void waitForPageLoaded() {
        ViewUtils.waitForView(R.id.filter_button, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
    }

    public void selectCategory(String category) {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        onView(withText(category)).inRoot(isPlatformPopup()).perform(click());
    }

    public void setStartDate(String date) {
        onView(withId(R.id.news_item_publish_date_start_text_input_edit_text))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    public void setEndDate(String date) {
        onView(withId(R.id.news_item_publish_date_end_text_input_edit_text))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    public void clickFilter() {
        onView(withId(R.id.filter_news_material_button)).perform(click());
    }
    public void clickFilterButton() {
        onView(withId(R.id.filter_button)).perform(click());
    }
    public void clearCategory() {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(clearText(), closeSoftKeyboard());
    }
}