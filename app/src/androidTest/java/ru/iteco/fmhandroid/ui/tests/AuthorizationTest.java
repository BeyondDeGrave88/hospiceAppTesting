package ru.iteco.fmhandroid.ui.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;

import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.AppState;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Авторизация")
public class AuthorizationTest extends BaseTest {

    private AuthorizationPage authPage;

    @Override
    @Before
    public void setUp() {
        authPage = new AuthorizationPage();
        mainPage = new MainPage();
        appState = new AppState();
        appState.goToAuthorization(activityScenarioRule);
    }

    @Test
    @Story("TC001 – Авторизация с верными данными")
    public void shouldLoginWithValidCredentials() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainPageDisplayed();
    }

    @Test//Баг - несоответствие сообщения
    @Story("TC002 – Авторизация с неверным паролем")
    public void shouldNotLoginWithInvalidPassword() {
        authPage.login(TestData.VALID_LOGIN, TestData.WRONG_PASSWORD);
        authPage.checkWrongLoginAndPasswordError();
    }

    @Test //Баг - несоответствие сообщения
    @Story("TC003 – Авторизация с неверным логином")
    public void shouldNotLoginWithInvalidLogin() {
        authPage.login(TestData.WRONG_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkWrongLoginAndPasswordError();
    }

    @Test
    @Story("TC004 – Авторизация с пустыми полями")
    public void shouldNotLoginWithEmptyFields() {
        authPage.loginEmpty();
        authPage.checkEmptyFieldsError();
    }

    @Test
    @Story("TC005 – Выход из учетной записи")
    public void shouldLogout() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainPageDisplayed();
        mainPage.logout();
        authPage.waitForPageLoaded();
    }
}