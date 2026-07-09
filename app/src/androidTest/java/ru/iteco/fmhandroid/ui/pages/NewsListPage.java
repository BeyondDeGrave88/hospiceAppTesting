package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;
import static org.hamcrest.Matchers.allOf;

public class NewsListPage {

    public static final int EDIT_NEWS_BUTTON_ID = R.id.edit_news_material_button;
    public static final int SWIPE_REFRESH_ID = R.id.news_list_swipe_refresh;
    public static final int NEWS_CARD_ID = R.id.news_item_material_card_view;
    public static final int NEWS_ITEM_DATE_TEXT_VIEW_ID = R.id.news_item_date_text_view;
    public static final int VIEW_NEWS_ICON_ID = R.id.view_news_item_image_view;
    public static final int NEWS_ITEM_DESCRIPTION_TEXT_VIEW_ID = R.id.news_item_description_text_view;

    public void waitForPageLoaded() {
        Allure.step("Ожидание загрузки страницы списка новостей");
        ViewUtils.waitForView(EDIT_NEWS_BUTTON_ID, 10000);
    }

    public void checkPageDisplayed() {
        Allure.step("Проверка отображения страницы списка новостей");
        waitForPageLoaded();
        onView(withId(EDIT_NEWS_BUTTON_ID)).check(matches(isDisplayed()));
    }

    public void openEditPage() {
        Allure.step("Переход на страницу редактирования новостей");
        onView(withId(EDIT_NEWS_BUTTON_ID)).perform(click());
    }

    public void refreshNewsList() {
        Allure.step("Обновление списка новостей (swipe-to-refresh)");
        onView(withId(SWIPE_REFRESH_ID)).perform(swipeDown());
    }

    public void checkNewsDetails(String title, String date) {
        Allure.step("Проверка заголовка и даты новости");
        onView(allOf(
                withId(NEWS_CARD_ID),
                hasDescendant(withText(title))
        )).check(matches(isDisplayed()));

        onView(allOf(
                withId(NEWS_ITEM_DATE_TEXT_VIEW_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)))
                )
        )).check(matches(withText(date)));
    }

    public void checkNewsDescription(String title, String description) {
        Allure.step("Проверка описания новости с заголовком {title} после раскрытия");
        onView(allOf(
                withId(VIEW_NEWS_ICON_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)))
                )
        )).perform(click());

        onView(allOf(
                withId(NEWS_ITEM_DESCRIPTION_TEXT_VIEW_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)))
                )
        )).check(matches(withText(description)));
    }
}

