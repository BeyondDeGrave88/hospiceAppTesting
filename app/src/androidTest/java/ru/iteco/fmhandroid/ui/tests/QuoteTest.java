package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.QuotePage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Цитаты")
public class QuoteTest extends BaseTest {

    private QuotePage quotePage;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        quotePage = new QuotePage();
    }


    @Test // Баг - разные кавычки в начале и конце текста заголовка
    @Story("TC024 – Переход на страницу 'Цитаты'")
    public void shouldOpenQuotePage() {
        mainPage.openQuotePage();
        quotePage.checkPageDisplayed();
        String actualTitle = quotePage.getFirstQuoteTitle();
        assertEquals("Заголовок первой цитаты не совпадает",
                TestData.QUOTE_TITLE, actualTitle);
    }

    @Test // Баг - разные кавычки в начале и конце текста заголовка и описания
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

    @Test // Баг - разные кавычки в начале и конце текста заголовка и описания
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