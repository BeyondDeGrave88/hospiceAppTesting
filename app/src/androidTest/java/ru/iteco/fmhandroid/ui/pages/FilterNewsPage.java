package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class FilterNewsPage {

    public static final int APPLY_FILTER_BUTTON_ID = R.id.filter_button;
    public static final int CATEGORY_AUTO_COMPLETE_TEXT_VIEW = R.id.news_item_category_text_auto_complete_text_view;
    public static final int START_DATE_EDIT_TEXT = R.id.news_item_publish_date_start_text_input_edit_text;
    public static final int END_DATE_EDIT_TEXT = R.id.news_item_publish_date_end_text_input_edit_text;

    @Step("Ожидание загрузки страницы фильтра")
    public void waitForPageLoaded() {
        ViewUtils.waitForView(APPLY_FILTER_BUTTON_ID, 10000);
    }

    @Step("Выбор категории {category} в фильтре")
    public void selectCategory(String category) {
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW)).perform(click());
        onView(withText(category)).inRoot(isPlatformPopup()).perform(click());
    }

    @Step("Очистка категории в фильтре")
    public void clearCategory() {
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW))
                .perform(clearText(), closeSoftKeyboard());
    }

    @Step("Установка даты начала {date}")
    public void setStartDate(String date) {
        onView(withId(START_DATE_EDIT_TEXT))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    @Step("Установка даты окончания {date}")
    public void setEndDate(String date) {
        onView(withId(END_DATE_EDIT_TEXT))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    @Step("Применение фильтра")
    public void applyFilter() {
        onView(withId(APPLY_FILTER_BUTTON_ID)).perform(click());
    }
}