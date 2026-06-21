package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class CreateNewsPage {

    public void waitForPageLoaded() {
        ViewUtils.waitForView(R.id.news_item_title_text_input_edit_text, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(R.id.news_item_title_text_input_edit_text)).check(matches(isDisplayed()));
    }

    public void selectCategory(String category) {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
    }

    public void enterTitle(String title) {
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(replaceText(title), closeSoftKeyboard());
    }

    public void enterPublicationDate() {
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }
    public void enterPublicationDate(String date) {
        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    public void enterTime() {
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    public void enterDescription(String description) {
        onView(withId(R.id.news_item_description_text_input_edit_text)).perform(replaceText(description));
    }

    public void clickSave() {
        onView(withId(R.id.save_button)).perform(scrollTo(), click());
    }

    public void clickCancel() {
        onView(withId(R.id.cancel_button)).perform(scrollTo(), click());
    }
}