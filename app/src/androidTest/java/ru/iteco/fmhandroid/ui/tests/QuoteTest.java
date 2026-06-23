package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertEquals;

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
import ru.iteco.fmhandroid.ui.pages.QuotePage;

@Epic("Цитаты")
public class QuoteTest {

    private AuthorizationPage authPage;
    private MainPage mainPage;
    private QuotePage quotePage;

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
        quotePage = new QuotePage();

        if (isLoggedIn()) {
            mainPage.logout();
        }

        if (!authPage.isAuthPageDisplayed()) {
            activityScenarioRule.getScenario().recreate();
            if (!authPage.isAuthPageDisplayed() && mainPage.isAuthorized()) {
                mainPage.logout();
            }
        }

        authPage.waitForPageLoaded();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainPageDisplayed();
    }

    @After
    public void tearDown() {
        if (isLoggedIn()) {
            mainPage.logout();
            authPage.waitForPageLoaded();
        }
    }

    @Test// Баг - разные кавычки в начале и конце текста заголовка
    @Story("TC024 – Переход на страницу 'Цитаты'")
    public void shouldOpenQuotePage() {
        mainPage.openQuotePage();
        quotePage.checkPageDisplayed();
        String actualTitle = quotePage.getFirstQuoteTitle();
        assertEquals("Заголовок первой цитаты не совпадает",
                TestData.QUOTE_TITLE, actualTitle);
    }

    @Test// Баг - разные кавычки в начале и конце текста заголовка и описания
    @Story("TC025 – Развернуть первую цитату")
    public void shouldExpandFirstQuote() {
        mainPage.openQuotePage();
        quotePage.checkPageDisplayed();

        String actualTitle = quotePage.getFirstQuoteTitle();
        assertEquals("Заголовок первой цитаты не совпадает",
                TestData.QUOTE_TITLE, actualTitle);

        quotePage.checkDescriptionHidden();

        quotePage.expandFirstQuote();

        quotePage.checkDescriptionVisible();
        String actualDescription = quotePage.getFirstQuoteDescription();
        assertEquals("Текст описания не совпадает",
                TestData.QUOTE_DESCRIPTION, actualDescription);
    }

    @Test// Баг - разные кавычки в начале и конце текста заголовка и описания
    @Story("TC026 – Свернуть первую цитату")
    public void shouldCollapseFirstQuote() {
        mainPage.openQuotePage();
        quotePage.checkPageDisplayed();
        quotePage.expandFirstQuote();
        quotePage.checkDescriptionVisible();

        String actualDescription = quotePage.getFirstQuoteDescription();
        assertEquals("Текст описания не совпадает",
                TestData.QUOTE_DESCRIPTION, actualDescription);

        quotePage.expandFirstQuote();
        quotePage.checkDescriptionHidden();
        quotePage.checkTitleVisible();

        String actualTitle = quotePage.getFirstQuoteTitle();
        assertEquals("Заголовок первой цитаты не совпадает",
                TestData.QUOTE_TITLE, actualTitle);
    }
}