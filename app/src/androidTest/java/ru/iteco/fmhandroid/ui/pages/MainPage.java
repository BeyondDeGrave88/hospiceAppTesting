package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class MainPage {

    public void openLogoutMenu() {
        ViewUtils.waitForView(R.id.authorization_image_button, 10000);
        onView(withId(R.id.authorization_image_button)).perform(click());
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public void clickLogout() {
        ViewUtils.waitForView(withText("Log out"), 5000);
        onView(withText("Log out")).perform(click());
    }

    public void logout() {
        openLogoutMenu();
        clickLogout();
        ViewUtils.waitForView(R.id.enter_button, 15000);
    }
}