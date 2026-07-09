package ru.iteco.fmhandroid.ui.tests;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;

import io.qameta.allure.android.rules.LogcatRule;
import io.qameta.allure.android.rules.ScreenshotRule;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.AppState;

public abstract class BaseTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    @Rule
    public LogcatRule logcatRule = new LogcatRule();

    protected MainPage mainPage;
    protected AppState appState;

    @Before
    public void setUp() {
        mainPage = new MainPage();
        appState = new AppState();
        appState.goToMainPage(activityScenarioRule);
    }
}