package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.espresso.NoActivityResumedException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.rules.LogcatRule;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;


import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Навигация")
public class NavigationTest {
    private AuthorizationPage authPage;
    private MainPage mainPage;
    private AboutPage aboutPage;
    private NewsListPage newsListPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    @Rule
    public LogcatRule logcatRule = new LogcatRule();

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
        aboutPage = new AboutPage();
        newsListPage = new NewsListPage();

        if (isLoggedIn()) {
            mainPage.logout();
        }

        if (!authPage.isAuthPageDisplayed()) {
            activityScenarioRule.getScenario().recreate();
            if (!authPage.isAuthPageDisplayed()) {
                if (mainPage.isAuthorized()) {
                    mainPage.logout();
                }
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

    @Test
    @Story("TC006 – Переход со страницы Main на About")
    public void shouldNavigateFromMainToAbout() {
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();
    }

    @Test //Баг - неактивная кнопка About
    @Story("TC007 – Переход со страницы Main на News, потом с News на About")
    public void shouldNavigateFromMainToNewsThenToAbout() {
        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();
    }

    @Test
    @Story("TC008 – Переход со страницы News на страницу Main при нажатии системной кнопки 'Назад'")
    public void shouldNavigateBackFromNewsToMain() {
        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        pressBack();
        mainPage.checkMainPageDisplayed();
    }

    @Test
    @Story("TC009 – Однократное нажатие системной кнопки 'Назад' на главной странице Main")
    public void shouldMinimizeAppOnBackPressFromMain() {
        mainPage.checkMainPageDisplayed();
        try {
            pressBack();
        } catch (NoActivityResumedException e) {
            throw new AssertionError("Приложение было закрыто вместо сворачивания. Это баг!", e);
        }
    }
}