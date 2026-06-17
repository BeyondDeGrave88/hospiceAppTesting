package ru.iteco.fmhandroid.ui.tests;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@RunWith(AndroidJUnit4.class)
@Epic("Авторизация")
public class AuthorizationTest {
    private AuthorizationPage authPage;
    private MainPage mainPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private boolean isLoggedIn() {
        if (authPage.isAuthPageDisplayed()) {
            return false;
        }
        return mainPage.isAuthorized();
    }

    @Before
    public void setUp() {
        authPage = new AuthorizationPage();
        mainPage = new MainPage();

        if (isLoggedIn()) {
            mainPage.logout();
        }

        if (!authPage.isAuthPageDisplayed()) {
            activityScenarioRule.getScenario().recreate();
            if (mainPage.isAuthorized()) {
                mainPage.logout();
            }
        }

        authPage.waitForPageLoaded();
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
        mainPage.checkMainPageDisplayed();
    }

    @Test
    @Story("Авторизация с неверным паролем")
    public void shouldNotLoginWithInvalidPassword() {
        authPage.login(TestData.VALID_LOGIN, TestData.WRONG_PASSWORD);
        authPage.checkWrongLoginAndPasswordError();
    }

    @Test
    @Story("Авторизация с неверным логином")
    public void shouldNotLoginWithInvalidLogin() {
        authPage.login(TestData.WRONG_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkWrongLoginAndPasswordError();
    }

    @Test
    @Story("Авторизация с пустыми полями")
    public void shouldNotLoginWithEmptyFields() {
        authPage.loginEmpty();
        authPage.checkEmptyFieldsError();
    }

    @Test
    @Story("Выход из учетной записи")
    public void shouldLogout() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainPageDisplayed();
        mainPage.logout();
        authPage.waitForPageLoaded();
    }
}