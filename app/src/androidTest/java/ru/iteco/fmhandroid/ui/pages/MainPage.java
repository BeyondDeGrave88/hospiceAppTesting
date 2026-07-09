package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class MainPage {

    private static final long TIMEOUT_DEFAULT = 10000;
    private static final long TIMEOUT_SHORT = 5000;

    public static final int MAIN_MENU_BUTTON_ID = R.id.main_menu_image_button;
    public static final int AUTHORIZATION_BUTTON_ID = R.id.authorization_image_button;
    public static final int QUOTES_BUTTON_ID = R.id.our_mission_image_button;

    public void openSideMenu() {
        Allure.step("Открытие бокового меню");
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, TIMEOUT_DEFAULT);
        onView(withId(MAIN_MENU_BUTTON_ID)).perform(click());
    }

    public void openQuotePage() {
        Allure.step("Открытие страницы 'Цитаты'");
        ViewUtils.waitForView(QUOTES_BUTTON_ID, TIMEOUT_DEFAULT);
        onView(withId(QUOTES_BUTTON_ID)).perform(click());
    }

    public void openNews() {
        Allure.step("Открытие раздела 'Новости'");
        openSideMenu();
        ViewUtils.waitForView(withText(TestData.NEWS_MENU_ITEM), TIMEOUT_DEFAULT);
        onView(withText(TestData.NEWS_MENU_ITEM))
                .check(matches(isEnabled()))
                .perform(click());
    }

    public void openAbout() {
        Allure.step("Открытие раздела 'О приложении'");
        openSideMenu();
        ViewUtils.waitForView(withText(TestData.ABOUT_MENU_ITEM), TIMEOUT_DEFAULT);
        onView(withText(TestData.ABOUT_MENU_ITEM))
                .check(matches(isEnabled()))
                .perform(click());
    }

    public void openLogoutMenu() {
        Allure.step("Открытие меню выхода (кнопка профиля)");
        ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, TIMEOUT_DEFAULT);
        onView(withId(AUTHORIZATION_BUTTON_ID)).perform(click());
    }

    public void clickLogout() {
        Allure.step("Нажатие на пункт 'Выйти'");
        ViewUtils.waitForView(withText(TestData.LOGOUT_MENU_ITEM), TIMEOUT_DEFAULT);
        onView(withText(TestData.LOGOUT_MENU_ITEM))
                .check(matches(isEnabled()))
                .perform(click());
    }

    public void logout() {
        Allure.step("Выход из учётной записи");
        openLogoutMenu();
        clickLogout();
        AuthorizationPage authPage = new AuthorizationPage();
        authPage.waitForPageLoaded();
    }

    public void checkMainPageDisplayed() {
        Allure.step("Проверка отображения главной страницы");
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, TIMEOUT_DEFAULT);
        onView(withId(MAIN_MENU_BUTTON_ID)).check(matches(isDisplayed()));
    }

    public void openNewsControlPanel() {
        Allure.step("Открытие панели управления новостями");
        openNews();
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.waitForPageLoaded();
        newsListPage.openEditPage();
    }

    public boolean isOnMainPage() {
        Allure.step("Проверка, находимся ли мы на главной странице");
        return ViewUtils.isViewDisplayed(MAIN_MENU_BUTTON_ID);
    }
}