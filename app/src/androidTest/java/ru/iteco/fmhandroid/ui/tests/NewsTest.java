package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import static ru.iteco.fmhandroid.ui.data.TestData.SAVING_FAILED_ERROR;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

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

    private void openNewsControlPanel() {
        mainPage.openNews();
        newsListPage.checkPageDisplayed();
        newsListPage.openEditPage();
        newsEditPage.waitForPageLoaded();
    }

    private void createTestNewsToday(String title, String category, String description) {
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();
    }

    private void createTestNews(String title, String category, String description, String date) {
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate(date);
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();
    }

    @Test  // Баг – новость не отображается на главной странице News
    @Story("TC011 – Новость содержит заголовок, дату, категорию, комментарий")
    public void shouldDisplayNewsTitleDateCategoryAndComment() {
        String title = "Праздник" + System.currentTimeMillis();;
        String category = "Праздник";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);

        mainPage.openNews();
        newsListPage.waitForPageLoaded();
        newsListPage.refreshNewsList();

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(category)).check(matches(isDisplayed()));
        onView(withText(DateUtils.getToday())).check(matches(isDisplayed()));

        newsListPage.clickOnNewsTitle(title);
        onView(withText(description)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC012 – Создание новости со всеми заполненными полями")
    public void shouldCreateNewsWithAllFilledFields() {
        String title = "Объявление" + System.currentTimeMillis();;
        String category = "Объявление";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);

        newsEditPage.sortNews();
        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(category)).check(matches(isDisplayed()));
        onView(withText(DateUtils.getToday())).check(matches(isDisplayed()));

    }

    @Test // Баг – текст диалога не соответствует ожидаемому (используется "log out")
    @Story("TC013 – Нажатие кнопки Cancel вместо Save при создании новости")
    public void shouldCancelNewsCreation() {
        String title = "Массаж" + System.currentTimeMillis();;
        String category = "Массаж";
        String description = "Описание";

        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);

        createNewsPage.clickCancel();

        onView(withText(TestData.CONFIRM_CANCEL_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText(TestData.OK_BUTTON_TEXT)).perform(click());

        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews();

        onView(withText(title)).check(doesNotExist());
    }

    @Test
    @Story("TC014 – Нажатие кнопки Ok при удалении новости")
    public void shouldDeleteNewsWithOk() {
        String title = "Зарплата" + System.currentTimeMillis();;
        String category = "Зарплата";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);
        newsEditPage.sortNews();

        onView(withText(title)).check(matches(isDisplayed()));

        newsEditPage.deleteNewsByTitle(title);

        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText(TestData.OK_BUTTON_TEXT)).perform(click());

        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews();

        onView(withText(title)).check(doesNotExist());
    }

    @Test
    @Story("TC015 – Нажатие кнопки Cancel при удалении новости")
    public void shouldCancelDeleteNews() {
        String title = "Профсоюз" + System.currentTimeMillis();;
        String category = "Профсоюз";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);
        newsEditPage.sortNews();

        onView(withText(title)).check(matches(isDisplayed()));

        newsEditPage.deleteNewsByTitle(title);

        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withText(TestData.CANCEL_BUTTON_TEXT)).perform(click());

        newsEditPage.waitForPageLoaded();

        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC016 – Фильтрация новостей за сегодня")
    public void shouldFilterNewsToday() {
        String title = "Праздник" + System.currentTimeMillis();;
        String category = "Праздник";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getToday());
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.applyFilter();

        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC017 – Фильтрация новостей за неделю")
    public void shouldFilterNewsWeek() {
        String title = "День рождения";
        String category = "День рождения";
        String description = "Описание";

        openNewsControlPanel();
        createTestNews(title, category, description, DateUtils.getDateDaysAgo(7));

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getDateDaysAgo(7));
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.applyFilter();

        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC018 – Фильтрация новостей за месяц")
    public void shouldFilterNewsMonth() {
        String title = "Нужна помощь";
        String category = "Нужна помощь";
        String description = "Описание";

        openNewsControlPanel();
        createTestNews(title, category, description, DateUtils.getDateDaysAgo(30));

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();

        filterNewsPage.setStartDate(DateUtils.getDateDaysAgo(30));
        filterNewsPage.setEndDate(DateUtils.getToday());

        filterNewsPage.applyFilter();

        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    @Story("TC019 – Фильтрация новостей по категории 'Объявление'")
    public void shouldFilterNewsByCategoryAnnouncement() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();;
        String titleMassage = "Массаж" + System.currentTimeMillis();;
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        openNewsControlPanel();

        createTestNewsToday(titleAnnouncement, categoryAnnouncement, description);
        createTestNewsToday(titleMassage, categoryMassage, description);

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryAnnouncement);
        filterNewsPage.applyFilter();
        newsEditPage.sortNews();

        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));
        onView(withText(titleMassage)).check(doesNotExist());
    }

    @Test
    @Story("TC020 – Фильтрация новостей по категории 'Массаж'")
    public void shouldFilterNewsByCategoryMassage() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();;
        String titleMassage = "Массаж" + System.currentTimeMillis();;
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        openNewsControlPanel();

        createTestNewsToday(titleAnnouncement, categoryAnnouncement, description);
        createTestNewsToday(titleMassage, categoryMassage, description);

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryMassage);
        filterNewsPage.applyFilter();
        newsEditPage.sortNews();


        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(doesNotExist());
    }

    @Test
    @Story("TC021 – Сброс фильтра по умолчанию - показываются все новости")
    public void shouldResetFilterShowsAllNews() {
        String titleMassage = "Массаж" + System.currentTimeMillis();;
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();;
        String categoryMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String description = "Описание";

        openNewsControlPanel();

        createTestNewsToday(titleMassage, categoryMassage, description);
        createTestNewsToday(titleAnnouncement, categoryAnnouncement, description);

        newsEditPage.sortNews();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.selectCategory(categoryMassage);
        filterNewsPage.applyFilter();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(doesNotExist());

        newsEditPage.openFilterScreen();
        filterNewsPage.waitForPageLoaded();
        filterNewsPage.clearCategory();
        filterNewsPage.applyFilter();

        onView(withText(titleMassage)).check(matches(isDisplayed()));
        onView(withText(titleAnnouncement)).check(matches(isDisplayed()));
    }

    @Test // Баг – дата создания отображается некорректно (год 58448)
    @Story("TC022 – Дата создания новости равна дате публикации")
    public void shouldCreationDateEqualsPublicationDate() {
        String title = "Благодарность" + System.currentTimeMillis();;
        String category = "Благодарность";
        String description = "Описание";

        openNewsControlPanel();
        createTestNewsToday(title, category, description);
        newsEditPage.sortNews();

        String publicationDate = newsEditPage.getPublicationDate(title);
        String creationDate = newsEditPage.getCreationDate(title);

        assertEquals("Дата публикации должна совпадать с датой создания", publicationDate, creationDate);
    }

    @Test
    @Story("TC023 – Дата публикации новости равна завтрашней дате")
    public void shouldPublicationDateEqualsTomorrow() {
        String title = "Зарплата" + System.currentTimeMillis();;
        String category = "Зарплата";
        String description = "Описание";

        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate(DateUtils.getTomorrow());
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews();

        String publicationDate = newsEditPage.getPublicationDate(title);

        assertEquals("Дата публикации должна быть завтрашней", DateUtils.getTomorrow(), publicationDate);
    }

    // === Негативные сценарии создания новости ===

    @Test
    @Story("TC035 – Создание новости с пустым полем Category")
    public void shouldNotCreateNewsWithEmptyCategory() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.enterTitle("Новость");
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription("Описание");
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC036 – Создание новости с пустым полем Description")
    public void shouldNotCreateNewsWithEmptyDescription() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Объявление");
        createNewsPage.enterTitle("Новость");
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC037 – Создание новости с пустым полем Publication date")
    public void shouldNotCreateNewsWithEmptyDate() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Массаж");
        createNewsPage.enterTitle("Новость");
        createNewsPage.enterTime();
        createNewsPage.enterDescription("Описание");
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC038 – Создание новости с пустым полем Time")
    public void shouldNotCreateNewsWithEmptyTime() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Зарплата");
        createNewsPage.enterTitle("Новость");
        createNewsPage.enterPublicationDate();
        createNewsPage.enterDescription("Описание");
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC039 – Создание новости с пустым полем Title")
    public void shouldNotCreateNewsWithEmptyTitle() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Профсоюз");
        createNewsPage.clearTitle("Профсоюз");
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription("Описание");
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC040 – Создание новости со всеми пустыми полями")
    public void shouldNotCreateNewsWithAllEmptyFields() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.FILL_EMPTY_FIELDS_ERROR);
        createNewsPage.checkPageDisplayed();
    }

    @Test
    @Story("TC041 – Создание новости с заголовком длиной 50+ символов")
    public void shouldCreateNewsWithLongTitle() {
        String longTitle = String.join("", Collections.nCopies(51, "A"));
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Объявление");
        createNewsPage.enterTitle(longTitle);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription("Описание");
        createNewsPage.clickSave();

        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews();
        onView(withText(longTitle)).check(matches(isDisplayed()));
    }

    @Test // Баг - новость создается, ошибки сохранения нет (падает, потому что находит новость)
    @Story("TC042 – Создание новости с внедрением HTML-тегов (XSS)")
    public void shouldNotCreateNewsWithXSS() {
        openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.selectCategory("Объявление");
        createNewsPage.enterTitle(TestData.XSS_PAYLOAD);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(TestData.XSS_PAYLOAD);
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(SAVING_FAILED_ERROR);

        newsEditPage.sortNews();
        newsEditPage.checkNewsDoesNotExist(TestData.XSS_PAYLOAD);
    }

    @Test
    @Story("TC043 – Сортировка новостей по дате (от новых к старым)")
    public void shouldSortNewsByDate() {
        String afterTomorrowTitle = "Послезавтрашняя";
        String tomorrowTitle = "Завтрашняя";
        String todayTitle = "Сегодняшняя";

        openNewsControlPanel();

        createTestNews(afterTomorrowTitle, "Объявление", "Описание", DateUtils.getAfterTomorrow());
        createTestNews(tomorrowTitle, "Объявление", "Описание", DateUtils.getTomorrow());
        createTestNews(todayTitle, "Объявление", "Описание", DateUtils.getToday());

        newsEditPage.sortNews();

        assertEquals("Заголовок на позиции 1 должен быть 'Послезавтрашняя'",
                afterTomorrowTitle, newsEditPage.getNewsTitleAtPosition(2));
        assertEquals("Заголовок на позиции 2 должен быть 'Завтрашняя'",
                tomorrowTitle, newsEditPage.getNewsTitleAtPosition(1));
        assertEquals("Заголовок на позиции 3 должен быть 'Сегодняшняя'",
                todayTitle, newsEditPage.getNewsTitleAtPosition(0));
    }
}