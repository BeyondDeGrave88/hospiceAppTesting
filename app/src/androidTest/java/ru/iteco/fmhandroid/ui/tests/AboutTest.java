package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.Matchers.allOf;

import android.content.Intent;
import android.net.Uri;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AboutPage;


@RunWith(AllureAndroidJUnit4.class)
@Epic("О приложении")
public class AboutTest extends BaseTest {

    private AboutPage aboutPage;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        aboutPage = new AboutPage();
        Intents.init();
    }

    @After
    public void tearDown() {
        try {
            Intents.release();
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