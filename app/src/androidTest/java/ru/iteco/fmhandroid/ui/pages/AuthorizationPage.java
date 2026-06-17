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
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class AuthorizationPage {

    public static final int ENTER_BUTTON_ID = R.id.enter_button;
    public static final String LOGIN_FIELD_HINT = "Login";
    public static final String PASSWORD_FIELD_HINT = "Password";

    public ViewInteraction getLoginField() {
        return onView(withHint(LOGIN_FIELD_HINT));
    }

    public ViewInteraction getPasswordField() {
        return onView(withHint(PASSWORD_FIELD_HINT));
    }

    public ViewInteraction getSignInButton() {
        return onView(withId(ENTER_BUTTON_ID));
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

    public void waitForPageLoaded() {
        ViewUtils.waitForView(ENTER_BUTTON_ID, 15000);
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }

    public void checkWrongLoginAndPasswordError() {
        onView(withText(TestData.WRONG_LOGIN_AND_PASSWORD_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }

    public void checkEmptyFieldsError() {
        onView(withText(TestData.EMPTY_FIELDS_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }
}