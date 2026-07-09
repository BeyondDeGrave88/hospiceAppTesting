package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class FilterNewsPage {

    public static final int APPLY_FILTER_BUTTON_ID = R.id.filter_button;
    public static final int CATEGORY_AUTO_COMPLETE_TEXT_VIEW = R.id.news_item_category_text_auto_complete_text_view;
    public static final int START_DATE_EDIT_TEXT = R.id.news_item_publish_date_start_text_input_edit_text;
    public static final int END_DATE_EDIT_TEXT = R.id.news_item_publish_date_end_text_input_edit_text;


    public void setStartDate(String date) {
        Allure.step("Установка даты начала {date}");
        onView(withId(START_DATE_EDIT_TEXT))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    public void setEndDate(String date) {
        Allure.step("Установка даты окончания {date}");
        onView(withId(END_DATE_EDIT_TEXT))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    public void waitForPageLoaded() {
        Allure.step("Ожидание загрузки страницы фильтра");
        ViewUtils.waitForView(APPLY_FILTER_BUTTON_ID, 10000);
    }

    public void selectCategory(String category) {
        Allure.step("Выбор категории {category} в фильтре");
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW)).perform(click());
        onView(withText(category)).inRoot(isPlatformPopup()).perform(click());
    }

    public void clearCategory() {
        Allure.step("Очистка категории в фильтре");
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW))
                .perform(clearText(), closeSoftKeyboard());
    }

    public void applyFilter() {
        Allure.step("Применение фильтра");
        onView(withId(APPLY_FILTER_BUTTON_ID)).perform(click());
    }

    public void filterByDateRange(String startDate, String endDate) {
        Allure.step("Применить фильтр по диапазону дат: {start} - {end}");
        waitForPageLoaded();
        setStartDate(startDate);
        setEndDate(endDate);
        applyFilter();
    }

    public void filterByCategory(String category) {
        Allure.step("Применить фильтр по категории: {category}");
        waitForPageLoaded();
        selectCategory(category);
        applyFilter();
    }
    public void resetFilter() {
        Allure.step("Сбросить фильтр (очистить категорию)");
        waitForPageLoaded();
        clearCategory();
        applyFilter();
    }
}
