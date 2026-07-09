package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.pressBack;

import androidx.test.espresso.NoActivityResumedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;

import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Навигация")
public class NavigationTest extends BaseTest {

    private AboutPage aboutPage;
    private NewsListPage newsListPage;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        aboutPage = new AboutPage();
        newsListPage = new NewsListPage();
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

    @Test//Баг - приложение закрывается
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