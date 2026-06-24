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

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@Epic("О приложении")
public class AboutTest {

    private AuthorizationPage authPage;
    private MainPage mainPage;
    private AboutPage aboutPage;

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

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
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();
        aboutPage.checkPrivacyPolicyLinkDisplayed();
        aboutPage.checkTermsOfUseLinkDisplayed();
    }

    @Test
    @Story("TC028 – Переход по ссылке «Политика конфиденциальности»")
    public void shouldOpenPrivacyPolicyLink() {
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
        mainPage.openAbout();
        aboutPage.checkPageDisplayed();

        aboutPage.clickTermsOfUseLink();

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(TestData.TERMS_OF_USE_URL))
        ));
    }
}