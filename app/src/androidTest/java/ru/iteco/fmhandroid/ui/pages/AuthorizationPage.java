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

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class AuthorizationPage {

    public static final int ENTER_BUTTON_ID = R.id.enter_button;
    public static final String LOGIN_FIELD_HINT = "Login";
    public static final String PASSWORD_FIELD_HINT = "Password";

    @Step("Получение поля ввода логина")
    public ViewInteraction getLoginField() {
        return onView(withHint(LOGIN_FIELD_HINT));
    }

    @Step("Получение поля ввода пароля")
    public ViewInteraction getPasswordField() {
        return onView(withHint(PASSWORD_FIELD_HINT));
    }

    @Step("Получение кнопки 'Войти'")
    public ViewInteraction getSignInButton() {
        return onView(withId(ENTER_BUTTON_ID));
    }

    @Step("Выполнение входа с логином {login} и паролем {password}")
    public void login(String login, String password) {
        getLoginField().perform(clearText(), typeText(login), closeSoftKeyboard());
        getPasswordField().perform(clearText(), typeText(password), closeSoftKeyboard());
        getSignInButton().perform(click());
    }

    @Step("Выполнение входа с пустыми полями")
    public void loginEmpty() {
        getLoginField().perform(clearText(), closeSoftKeyboard());
        getPasswordField().perform(clearText(), closeSoftKeyboard());
        getSignInButton().perform(click());
    }

    @Step("Ожидание загрузки страницы авторизации")
    public void waitForPageLoaded() {
        ViewUtils.waitForView(ENTER_BUTTON_ID, 15000);
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка ошибки 'Неверный логин или пароль'")
    public void checkWrongLoginAndPasswordError() {
        onView(withText(TestData.WRONG_LOGIN_AND_PASSWORD_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка ошибки 'Поля не заполнены'")
    public void checkEmptyFieldsError() {
        onView(withText(TestData.EMPTY_FIELDS_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(ENTER_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка, отображается ли страница авторизации")
    public boolean isAuthPageDisplayed() {
        try {
            ViewUtils.waitForView(ENTER_BUTTON_ID, 1000);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}