package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

@RunWith(AndroidJUnit4.class)
@Epic("Авторизация")
public class AuthorizationTest {
    private AuthorizationPage authPage;
    private MainPage mainPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private boolean isLoggedIn() {
        if (ViewUtils.isViewDisplayed(R.id.enter_button)) {
            return false;
        }
        return ViewUtils.isViewDisplayed(R.id.authorization_image_button);
    }

    @Before
    public void setUp() {
        authPage = new AuthorizationPage();
        mainPage = new MainPage();

        if (isLoggedIn()) {
            mainPage.logout();
        }

        if (!ViewUtils.isViewDisplayed(R.id.enter_button)) {
            activityScenarioRule.getScenario().recreate();
            if (ViewUtils.isViewDisplayed(R.id.authorization_image_button)) {
                mainPage.logout();
            }
        }

        authPage.waitForAuthorizationScreen();
    }

    @After
    public void tearDown() {
        if (isLoggedIn()) {
            mainPage.logout();
        }
    }

    @Test
    @Story("Авторизация с верными данными")
    public void shouldLoginWithValidCredentials() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        ViewUtils.waitForView(R.id.main_menu_image_button, 10000);
        onView(withId(R.id.main_menu_image_button)).check(matches(isDisplayed()));
    }

    @Test
    @Story("Авторизация с неверным паролем")
    public void shouldNotLoginWithInvalidPassword() {
        authPage.login(TestData.VALID_LOGIN, TestData.WRONG_PASSWORD);
        onView(withText(TestData.WRONG_LOGIN_AND_PASSWORD_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(R.id.enter_button)).check(matches(isDisplayed()));
    }

    @Test
    @Story("Авторизация с неверным логином")
    public void shouldNotLoginWithInvalidLogin() {
        authPage.login(TestData.WRONG_LOGIN, TestData.VALID_PASSWORD);
        onView(withText(TestData.WRONG_LOGIN_AND_PASSWORD_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
        onView(withId(R.id.enter_button)).check(matches(isDisplayed()));
    }

    @Test
    @Story("Авторизация с пустыми полями")
    public void shouldNotLoginWithEmptyFields() {
        authPage.loginEmpty();

        onView(withText(TestData.EMPTY_FIELDS_ERROR))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        onView(withId(R.id.enter_button)).check(matches(isDisplayed()));
    }


    @Test
    @Story("Выход из учетной записи")
    public void shouldLogout() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        ViewUtils.waitForView(R.id.main_menu_image_button, 10000);
        onView(withId(R.id.main_menu_image_button)).check(matches(isDisplayed()));

        mainPage.logout();

        authPage.waitForAuthorizationScreen();
        onView(withId(R.id.enter_button)).check(matches(isDisplayed()));
    }
}