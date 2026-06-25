package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class NewsListPage {

    public static final int EDIT_NEWS_BUTTON_ID = R.id.edit_news_material_button;
    public static final int SWIPE_REFRESH_ID = R.id.news_list_swipe_refresh;

    @Step("Ожидание загрузки страницы списка новостей")
    public void waitForPageLoaded() {
        ViewUtils.waitForView(EDIT_NEWS_BUTTON_ID, 10000);
    }

    @Step("Проверка отображения страницы списка новостей")
    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(EDIT_NEWS_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Step("Переход на страницу редактирования новостей")
    public void openEditPage() {
        onView(withId(EDIT_NEWS_BUTTON_ID)).perform(click());
    }

    @Step("Клик по заголовку новости {title} для раскрытия описания")
    public void clickOnNewsTitle(String title) {
        onView(withText(title)).perform(click());
    }

    @Step("Обновление списка новостей (swipe-to-refresh)")
    public void refreshNewsList() {
        onView(withId(SWIPE_REFRESH_ID)).perform(swipeDown());
    }
}