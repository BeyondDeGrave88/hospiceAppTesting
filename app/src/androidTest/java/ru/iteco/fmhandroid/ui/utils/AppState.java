package ru.iteco.fmhandroid.ui.utils;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

import io.qameta.allure.kotlin.Allure;

public class AppState {

    private final AuthorizationPage authPage = new AuthorizationPage();
    private final MainPage mainPage = new MainPage();

    public void goToAuthorization(ActivityScenarioRule<AppActivity> rule) {
        Allure.step("Переход на страницу 'Авторизация'");
        rule.getScenario().recreate();
        ViewUtils.sleep(3000);

        if (authPage.isAuthPageDisplayed()) {
            return;
        }

        if (mainPage.isOnMainPage()) {
            mainPage.logout();
            authPage.waitForPageLoaded();
            return;
        }

        rule.getScenario().recreate();
        ViewUtils.sleep(3000);

        if (authPage.isAuthPageDisplayed()) {
            return;
        }

        if (mainPage.isOnMainPage()) {
            mainPage.logout();
            authPage.waitForPageLoaded();
            return;
        }

        throw new AssertionError("Не удалось перейти на страницу авторизации");
    }

    public void goToMainPage(ActivityScenarioRule<AppActivity> rule) {
        Allure.step("Переход на главную страницу");
        rule.getScenario().recreate();
        ViewUtils.sleep(3000);

        if (authPage.isAuthPageDisplayed()) {
            authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
            mainPage.checkMainPageDisplayed();
            return;
        }

        if (mainPage.isOnMainPage()) {
            return;
        }

        rule.getScenario().recreate();
        ViewUtils.sleep(3000);

        if (authPage.isAuthPageDisplayed()) {
            authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
            mainPage.checkMainPageDisplayed();
            return;
        }

        if (mainPage.isOnMainPage()) {
            return;
        }

        throw new AssertionError("Не удалось перейти на главную страницу");
    }
}