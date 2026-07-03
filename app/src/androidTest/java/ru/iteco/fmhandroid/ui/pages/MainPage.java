package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class MainPage {

    public static final int MAIN_MENU_BUTTON_ID = R.id.main_menu_image_button;
    public static final int AUTHORIZATION_BUTTON_ID = R.id.authorization_image_button;
    public static final int QUOTES_BUTTON_ID = R.id.our_mission_image_button;

    @Step("Открытие бокового меню")
    public void openSideMenu() {
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, 10000);
        onView(withId(MAIN_MENU_BUTTON_ID)).perform(click());
    }

    @Step("Открытие страницы 'Цитаты'")
    public void openQuotePage() {
        ViewUtils.waitForView(QUOTES_BUTTON_ID, 10000);
        onView(withId(QUOTES_BUTTON_ID)).perform(click());
    }

    @Step("Открытие раздела 'Новости'")
    public void openNews() {
        openSideMenu();
        ViewUtils.waitForView(withText(TestData.NEWS_MENU_ITEM), 5000);
        onView(withText(TestData.NEWS_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.NEWS_MENU_ITEM)).perform(click());
    }

    @Step("Открытие раздела 'О приложении'")
    public void openAbout() {
        openSideMenu();
        ViewUtils.waitForView(withText(TestData.ABOUT_MENU_ITEM), 5000);
        onView(withText(TestData.ABOUT_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.ABOUT_MENU_ITEM)).perform(click());
    }

    @Step("Открытие меню выхода (кнопка профиля)")
    public void openLogoutMenu() {
        ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, 10000);
        onView(withId(AUTHORIZATION_BUTTON_ID)).perform(click());
    }

    @Step("Нажатие на пункт 'Выйти'")
    public void clickLogout() {
        ViewUtils.waitForView(withText(TestData.LOGOUT_MENU_ITEM), 5000);
        onView(withText(TestData.LOGOUT_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.LOGOUT_MENU_ITEM)).perform(click());
    }

    @Step("Выход из учётной записи")
    public void logout() {
        openLogoutMenu();
        clickLogout();
        AuthorizationPage authPage = new AuthorizationPage();
        authPage.waitForPageLoaded();
    }

    @Step("Проверка отображения главной страницы")
    public void checkMainPageDisplayed() {
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, 10000);
        onView(withId(MAIN_MENU_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка, авторизован ли пользователь")
    public boolean isAuthorized() {
        try {
            ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, 1000);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    @Step("Открытие панели управления новостями")
    public void openNewsControlPanel() {
        openNews();
        NewsListPage newsListPage = new NewsListPage();
        newsListPage.waitForPageLoaded();
        newsListPage.openEditPage();
    }
}