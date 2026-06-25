package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.Matchers.allOf;

import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.rules.LogcatRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("О приложении")
public class AboutTest {

    private AuthorizationPage authPage;
    private MainPage mainPage;
    private AboutPage aboutPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    @Rule
    public LogcatRule logcatRule = new LogcatRule();

    private boolean isLoggedIn() {
        try {
            if (authPage.isAuthPageDisplayed()) {
                return false;
            }
            return mainPage.isAuthorized();
        } catch (Exception e) {
            return false;
        }
    }

    @Before
    public void setUp() {
        Allure.label("epic", "О приложении");

        authPage = new AuthorizationPage();
        mainPage = new MainPage();
        aboutPage = new AboutPage();

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

        Intents.init();
    }

    @After
    public void tearDown() {
        try {
            Intents.release();
        } catch (Exception ignored) {
        }

        try {
            if (isLoggedIn()) {
                mainPage.logout();
                authPage.waitForPageLoaded();
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    @Story("TC027 – Переход на страницу About (проверка элементов)")
    public void shouldNavigateToAboutAndCheckElements() {
        Allure.label("story", "TC027 – Переход на страницу About (проверка элементов)");
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();
        aboutPage.checkPrivacyPolicyLinkDisplayed();
        aboutPage.checkTermsOfUseLinkDisplayed();
    }

    @Test
    @Story("TC028 – Переход по ссылке «Политика конфиденциальности»")
    public void shouldOpenPrivacyPolicyLink() {
        Allure.label("story", "TC028 – Переход по ссылке «Политика конфиденциальности»");
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();

        aboutPage.clickPrivacyPolicyLink();

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(TestData.PRIVACY_POLICY_URL))
        ));
    }

    @Test
    @Story("TC029 – Переход по ссылке «Пользовательское соглашение»")
    public void shouldOpenTermsOfUseLink() {
        Allure.label("story", "TC029 – Переход по ссылке «Пользовательское соглашение»");
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();

        aboutPage.clickTermsOfUseLink();

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(TestData.TERMS_OF_USE_URL))
        ));
    }
}