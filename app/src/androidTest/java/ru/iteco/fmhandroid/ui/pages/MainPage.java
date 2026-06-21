package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
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
        ViewUtils.waitForView(withText(TestData.NEWS_MENU_ITEM), 5000);
        onView(withText(TestData.NEWS_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.NEWS_MENU_ITEM)).perform(click());
    }

    public void openAbout() {
        openSideMenu();
        ViewUtils.waitForView(withText(TestData.ABOUT_MENU_ITEM), 5000);
        onView(withText(TestData.ABOUT_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.ABOUT_MENU_ITEM)).perform(click());
    }

    public void openLogoutMenu() {
        ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, 10000);
        onView(withId(AUTHORIZATION_BUTTON_ID)).perform(click());
    }

    public void clickLogout() {
        ViewUtils.waitForView(withText(TestData.LOGOUT_MENU_ITEM), 5000);
        onView(withText(TestData.LOGOUT_MENU_ITEM)).check(matches(isEnabled()));
        onView(withText(TestData.LOGOUT_MENU_ITEM)).perform(click());
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

    public boolean isAuthorized() {
        try {
            ViewUtils.waitForView(AUTHORIZATION_BUTTON_ID, 1000);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}