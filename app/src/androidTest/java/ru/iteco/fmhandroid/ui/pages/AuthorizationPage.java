package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class AuthorizationPage {

    public ViewInteraction getLoginField() {
        return onView(withHint("Login"));
    }

    public ViewInteraction getPasswordField() {
        return onView(withHint("Password"));
    }

    public ViewInteraction getSignInButton() {
        return onView(withId(R.id.enter_button));
    }

    public void login(String login, String password) {
        getLoginField().perform(clearText(), typeText(login), closeSoftKeyboard());
        getPasswordField().perform(clearText(), typeText(password), closeSoftKeyboard());
        getSignInButton().perform(click());
    }
    public void loginEmpty() {
        getLoginField().perform(clearText(), closeSoftKeyboard());
        getPasswordField().perform(clearText(), closeSoftKeyboard());
        getSignInButton().perform(click());
    }


    public void checkErrorMessage(String expectedText) {
        ViewUtils.waitForView(withText(expectedText), 5000);
        onView(withText(expectedText)).check(matches(isDisplayed()));
    }

    public void waitForAuthorizationScreen() {
        ViewUtils.waitForView(R.id.enter_button, 15000);
    }

    public boolean isAuthorizationScreenShown() {
        return ViewUtils.isViewDisplayed(R.id.enter_button);
    }
}