package ru.iteco.fmhandroid.ui.tests;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.CreateNewsPage;
import ru.iteco.fmhandroid.ui.pages.FilterNewsPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.pages.NewsEditPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;
import ru.iteco.fmhandroid.ui.utils.DateUtils;

@RunWith(AndroidJUnit4.class)
@Epic("Новости")
public class NewsTest {

    private AuthorizationPage authPage;
    private MainPage mainPage;
    private NewsListPage newsListPage;
    private NewsEditPage newsEditPage;
    private CreateNewsPage createNewsPage;
    private FilterNewsPage filterNewsPage;

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
        newsListPage = new NewsListPage();
        newsEditPage = new NewsEditPage();
        createNewsPage = new CreateNewsPage();
        filterNewsPage = new FilterNewsPage();

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

    @Test  //Баг - новость не отображается
    @Story("TC011 – Новость содержит заголовок, дату, категорию, комментарий")
    public void shouldDisplayNewsTitleDateCategoryAndComment() {
        String title = "Праздник";
        String category = "Праздник";
        String description = "Описание";
        mainPage.openNews();
        newsListPage.checkPageDisplayed();

        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
        newsEditPage.clickAddNews();

        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        mainPage.openNews();
        newsListPage.waitForPageLoaded();
        newsListPage.refreshNewsList();
        newsListPage.waitForPageLoaded();

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(category)).check(matches(isDisplayed()));
        onView(withText(DateUtils.getToday())).check(matches(isDisplayed()));

        newsListPage.clickOnNewsTitle(title);
        onView(withText(description)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC012 – Создание новости со всеми заполненными полями")
    public void shouldCreateNewsWithAllFilledFields() {
        String title = "Объявление";
        String category = "Объявление";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();

        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
        newsEditPage.clickAddNews();

        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        onView(withText(title)).check(matches(isDisplayed()));
    }
    @Test // Баг - падает при проверке текста - ошибка логики
    @Story("TC013 – Нажатие кнопки Cancel вместо Save при создании новости")
    public void shouldCancelNewsCreation() {
        String title = "Массаж";
        String category = "Массаж";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();

        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
        newsEditPage.clickAddNews();

        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);

        createNewsPage.clickCancel();

        onView(withText("The changes won't be saved, do you really want to cancel?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText("OK")).perform(click());

        newsEditPage.waitForPageLoaded();

        newsEditPage.clickSort();

        onView(withText(title)).check(doesNotExist());
    }

    @Test
    @Story("TC014 – Нажатие кнопки Ok при удалении новости")
    public void shouldDeleteNewsWithOk() {
        String title = "Зарплата";
        String category = "Зарплата";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();

        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
        newsEditPage.clickAddNews();

        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        onView(withText(title)).check(matches(isDisplayed()));
        newsEditPage.deleteNewsByTitle(title);

        onView(withText("Are you sure you want to permanently delete the document? These changes cannot be reversed in the future."))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText("OK")).perform(click());

        newsEditPage.waitForPageLoaded();

        newsEditPage.clickSort();
        onView(withText(title)).check(doesNotExist());

    }

    @Test
    @Story("TC015 – Нажатие кнопки Cancel при удалении новости")
    public void shouldCancelDeleteNews() {
        String title = "Профсоюз";
        String category = "Профсоюз";
        String description = "Описание";
        mainPage.openNews();
        newsListPage.checkPageDisplayed();

        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
        newsEditPage.clickAddNews();

        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();
        onView(withText(title)).check(matches(isDisplayed()));

        newsEditPage.deleteNewsByTitle(title);

        onView(withText("Are you sure you want to permanently delete the document? These changes cannot be reversed in the future."))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText("Cancel")).perform(click());

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        onView(withText(title)).check(doesNotExist());
    }
    @Test
    @Story("TC016 – Фильтрация новостей за сегодня")
    public void shouldFilterNewsToday() {
        String title = "Праздник";
        String category = "Праздник";
        String description = "Описание";
        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getToday());
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.clickFilterButton();

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(DateUtils.getToday())).check(matches(isDisplayed()));
    }
    @Test
    @Story("TC017 – Фильтрация новостей за неделю")
    public void shouldFilterNewsWeek() {
        String title = "День рождения";
        String category = "День рождения";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate(DateUtils.getDateDaysAgo(7));
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getDateDaysAgo(7));
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.clickFilterButton();

        onView(withText(title)).check(matches(isDisplayed()));
    }
    @Test
    @Story("TC018 – Фильтрация новостей за месяц")
    public void shouldFilterNewsMonth() {
        String title = "Нужна помощь";
        String category = "Нужна помощь";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate(DateUtils.getDateDaysAgo(30));
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getDateDaysAgo(30));
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.clickFilterButton();

        onView(withText(title)).check(matches(isDisplayed()));
    }
    @Test
    @Story("TC019 – Фильтрация новостей по категории 'Объявление'")
    public void shouldFilterNewsByCategoryAnnouncement() {
        String titleAnnouncement = "Объявление";
        String titleMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryAnnouncement);
        createNewsPage.enterTitle(titleAnnouncement);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryMassage);
        createNewsPage.enterTitle(titleMassage);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        onView(withText(titleMassage)).check(matches(isDisplayed()));

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryAnnouncement);
        filterNewsPage.clickFilterButton();

        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));
        onView(withText(titleMassage)).check(doesNotExist());
    }
    @Test
    @Story("TC020 – Фильтрация новостей по категории 'Массаж'")
    public void shouldFilterNewsByCategoryMassage() {
        String titleAnnouncement = "Объявление";
        String titleMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryAnnouncement);
        createNewsPage.enterTitle(titleAnnouncement);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryMassage);
        createNewsPage.enterTitle(titleMassage);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();

        onView(withText(titleMassage)).check(matches(isDisplayed()));

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryMassage);
        filterNewsPage.clickFilterButton();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(doesNotExist());
    }
    @Test
    @Story("TC021 – Сброс фильтра по умолчанию - показываются все новости")
    public void shouldResetFilterShowsAllNews() {
        String titleMassage = "Массаж";
        String titleAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryMassage);
        createNewsPage.enterTitle(titleMassage);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(categoryAnnouncement);
        createNewsPage.enterTitle(titleAnnouncement);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickSort();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryMassage);
        filterNewsPage.clickFilterButton();


        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(doesNotExist());

        newsEditPage.clickFilter();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.clearCategory();
        filterNewsPage.clickFilterButton();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));
    }
    @Test // Баг - даты не совпадают с актуальной и по формату dd.MM.yyyy
    @Story("TC022 – Дата создания новости равна дате публикации")
    public void shouldCreationDateEqualsPublicationDate() {
        String title = "Благодарность";
        String category = "Благодарность";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        String publicationDate = newsEditPage.getPublicationDate(title);
        String creationDate = newsEditPage.getCreationDate(title);

        assertEquals("Дата публикации должна совпадать с датой создания", publicationDate, creationDate);
    }

    @Test
    @Story("TC023 – Дата публикации новости равна завтрашней дате")
    public void shouldPublicationDateEqualsTomorrow() {
        String title = "Зарплата";
        String category = "Зарплата";
        String description = "Описание";

        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();

        newsEditPage.clickAddNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);

        createNewsPage.enterPublicationDate(DateUtils.getTomorrow());
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.clickSort();

        String publicationDate = newsEditPage.getPublicationDate(title);

        assertEquals("Дата публикации должна быть завтрашней", DateUtils.getTomorrow(), publicationDate);
    }
}