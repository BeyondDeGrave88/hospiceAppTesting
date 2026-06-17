package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class MainPage {

    public static final int MAIN_MENU_BUTTON_ID = R.id.main_menu_image_button;
    public static final int AUTHORIZATION_BUTTON_ID = R.id.authorization_image_button;

    public void openSideMenu() {
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, 10000);
        onView(withId(MAIN_MENU_BUTTON_ID)).perform(click());
    }

    public void openNews() {
        openSideMenu();
        ViewUtils.waitForView(withText("News"), 5000);
        onView(withText("News")).check(matches(isEnabled()));
        onView(withText("News")).perform(click());
    }

    public void openAbout() {
        openSideMenu();
        ViewUtils.waitForView(withText("About"), 5000);
        onView(withText("About")).check(matches(isEnabled()));
        onView(withText("About")).perform(click());
    }

    public void openLogoutMenu() {
        ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, 10000);
        onView(withId(AUTHORIZATION_BUTTON_ID)).perform(click());
        // Задержка удалена, так как clickLogout() сам ждёт появления меню
    }

    public void clickLogout() {
        ViewUtils.waitForView(withText("Log out"), 5000);
        onView(withText("Log out")).check(matches(isEnabled()));
        onView(withText("Log out")).perform(click());
    }

    public void logout() {
        openLogoutMenu();
        clickLogout();
        AuthorizationPage authPage = new AuthorizationPage();
        authPage.waitForPageLoaded();
    }

    public void checkMainPageDisplayed() {
        ViewUtils.waitForView(MAIN_MENU_BUTTON_ID, 10000);
        onView(withId(MAIN_MENU_BUTTON_ID)).check(matches(isDisplayed()));
    }
}