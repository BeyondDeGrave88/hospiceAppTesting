package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertEquals;

import static ru.iteco.fmhandroid.ui.data.TestData.SAVING_FAILED_ERROR;
import static ru.iteco.fmhandroid.ui.data.TestData.XSS_PAYLOAD;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import io.qameta.allure.android.rules.LogcatRule;
import io.qameta.allure.android.rules.ScreenshotRule;
import io.qameta.allure.android.runners.AllureAndroidJUnit4;
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

@RunWith(AllureAndroidJUnit4.class)
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
    }

    @Test  // Баг – новость не отображается на главной странице News
    @Story("TC011 – Новость содержит заголовок, дату, категорию, комментарий")
    public void shouldDisplayNewsTitleDateCategoryAndComment() {
        String title = "Праздник" + System.currentTimeMillis();
        String category = "Праздник";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);

        mainPage.openNews();
        newsListPage.waitForPageLoaded();
        newsListPage.refreshNewsList();

        newsListPage.checkNewsDetails(title, category, DateUtils.getToday());
        newsListPage.checkNewsDescription(title, description);
    }

    @Test
    @Story("TC012 – Создание новости со всеми заполненными полями")
    public void shouldCreateNewsWithAllFilledFields() {
        String title = "Объявление" + System.currentTimeMillis();
        String category = "Объявление";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.sortNews(title);
    }

    @Test // Баг – текст диалога не соответствует ожидаемому (используется "log out")
    @Story("TC013 – Нажатие кнопки Cancel вместо Save при создании новости")
    public void shouldCancelNewsCreation() {
        String title = "Массаж" + System.currentTimeMillis();
        String category = "Массаж";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);

        createNewsPage.cancelCreationWithConfirmation();

        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews(title);
        newsEditPage.checkNewsDoesNotExist(title);
    }

    @Test
    @Story("TC014 – Нажатие кнопки Ok при удалении новости")
    public void shouldDeleteNewsWithOk() {
        String title = "Зарплата" + System.currentTimeMillis();
        String category = "Зарплата";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.sortNews(title);
        newsEditPage.deleteNewsByTitle(title);
        newsEditPage.confirmDelete();

        newsEditPage.waitForPageLoaded();
        newsEditPage.checkNewsDoesNotExist(title);
    }

    @Test
    @Story("TC015 – Нажатие кнопки Cancel при удалении новости")
    public void shouldCancelDeleteNews() {
        String title = "Профсоюз" + System.currentTimeMillis();
        String category = "Профсоюз";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.sortNews(title);
        newsEditPage.deleteNewsByTitle(title);
        newsEditPage.cancelDelete();

        newsEditPage.waitForPageLoaded();
        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC016 – Фильтрация новостей за сегодня")
    public void shouldFilterNewsToday() {
        String title = "Праздник" + System.currentTimeMillis();
        String category = "Праздник";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getToday(), DateUtils.getToday());

        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC017 – Фильтрация новостей за неделю")
    public void shouldFilterNewsWeek() {
        String title = "День рождения";
        String category = "День рождения";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(title, category, description, DateUtils.getDateDaysAgo(7));
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getDateDaysAgo(7), DateUtils.getToday());

        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC018 – Фильтрация новостей за месяц")
    public void shouldFilterNewsMonth() {
        String title = "Нужна помощь";
        String category = "Нужна помощь";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(title, category, description, DateUtils.getDateDaysAgo(30));
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getDateDaysAgo(30), DateUtils.getToday());

        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC019 – Фильтрация новостей по категории 'Объявление'")
    public void shouldFilterNewsByCategoryAnnouncement() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        String titleMassage = "Массаж" + System.currentTimeMillis();
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryAnnouncement);
        newsEditPage.sortNews(titleAnnouncement);

        newsEditPage.checkNewsDoesNotExist(titleMassage);
    }

    @Test
    @Story("TC020 – Фильтрация новостей по категории 'Массаж'")
    public void shouldFilterNewsByCategoryMassage() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        String titleMassage = "Массаж" + System.currentTimeMillis();
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryMassage);
        newsEditPage.sortNews(titleMassage);

        newsEditPage.checkNewsDoesNotExist(titleAnnouncement);
    }

    @Test
    @Story("TC021 – Сброс фильтра по умолчанию - показываются все новости")
    public void shouldResetFilterShowsAllNews() {
        String titleMassage = "Массаж" + System.currentTimeMillis();
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        String categoryMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.sortNews(titleAnnouncement);

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsExists(titleAnnouncement);

        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryMassage);

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsDoesNotExist(titleAnnouncement);

        newsEditPage.openFilterScreen();
        filterNewsPage.resetFilter();

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsExists(titleAnnouncement);
    }

    @Test // Баг – дата создания отображается некорректно (год 58448)
    @Story("TC022 – Дата создания новости равна дате публикации")
    public void shouldCreationDateEqualsPublicationDate() {
        String title = "Благодарность" + System.currentTimeMillis();
        String category = "Благодарность";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.sortNews(title);

        String publicationDate = newsEditPage.getPublicationDate(title);
        String creationDate = newsEditPage.getCreationDate(title);

        assertEquals("Дата публикации должна совпадать с датой создания", publicationDate, creationDate);
    }

    @Test
    @Story("TC023 – Дата публикации новости равна завтрашней дате")
    public void shouldPublicationDateEqualsTomorrow() {
        String title = "Зарплата" + System.currentTimeMillis();
        String category = "Зарплата";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory(category);
        createNewsPage.enterTitle(title);
        createNewsPage.enterPublicationDate(DateUtils.getTomorrow());
        createNewsPage.enterTime();
        createNewsPage.enterDescription(description);
        createNewsPage.clickSave();
        newsEditPage.waitForPageLoaded();
        newsEditPage.sortNews(title);

        String publicationDate = newsEditPage.getPublicationDate(title);

        assertEquals("Дата публикации должна быть завтрашней", DateUtils.getTomorrow(), publicationDate);
    }

    // === Негативные сценарии создания новости  ===

    @Test
    @Story("TC035 – Создание новости с пустым полем Category")
    public void shouldNotCreateNewsWithEmptyCategory() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.attemptCreateWithoutCategory(
                "Новость",
                DateUtils.getToday(),
                "Описание"
        );

        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC036 – Создание новости с пустым полем Description")
    public void shouldNotCreateNewsWithEmptyDescription() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.attemptCreateWithoutDescription(
                "Объявление",
                "Новость",
                DateUtils.getToday()
        );

        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC037 – Создание новости с пустым полем Publication date")
    public void shouldNotCreateNewsWithEmptyDate() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.attemptCreateWithoutPublicationDate(
                "Массаж",
                "Новость",
                "Описание"
        );

        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC038 – Создание новости с пустым полем Time")
    public void shouldNotCreateNewsWithEmptyTime() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.attemptCreateWithoutTime(
                "Зарплата",
                "Новость",
                DateUtils.getToday(),
                "Описание"
        );

        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC039 – Создание новости с пустым полем Title")
    public void shouldNotCreateNewsWithEmptyTitle() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();

        createNewsPage.attemptCreateWithoutTitle(
                "Профсоюз",
                DateUtils.getToday(),
                "Описание"
        );

        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC040 – Создание новости со всеми пустыми полями")
    public void shouldNotCreateNewsWithAllEmptyFields() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.attemptCreateWithAllEmpty();
        createNewsPage.checkCreationErrorAndPageDisplayed(TestData.FILL_EMPTY_FIELDS_ERROR);
    }

    @Test
    @Story("TC041 – Создание новости с заголовком длиной 50+ символов")
    public void shouldCreateNewsWithLongTitle() {
        String longTitle = String.join("", Collections.nCopies(51, "A"));

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(longTitle, "Объявление", "Описание");
        newsEditPage.sortNews(longTitle);
    }

    @Test // Баг - новость создается, ошибки сохранения нет (падает, потому что находит новость)
    @Story("TC042 – Создание новости с внедрением HTML-тегов (XSS)")
    public void shouldNotCreateNewsWithXSS() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory("Объявление");
        createNewsPage.enterTitle(TestData.XSS_PAYLOAD);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(TestData.XSS_PAYLOAD);
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(SAVING_FAILED_ERROR);

        newsEditPage.sortNews(XSS_PAYLOAD);
        newsEditPage.checkNewsDoesNotExist(TestData.XSS_PAYLOAD);
    }

    @Test
    @Story("TC043 – Сортировка новостей по дате (от новых к старым)")
    public void shouldSortNewsByDate() {
        String afterTomorrowTitle = "Послезавтрашняя";
        String tomorrowTitle = "Завтрашняя";
        String todayTitle = "Сегодняшняя";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(afterTomorrowTitle, "Объявление", "Описание", DateUtils.getAfterTomorrow());
        newsEditPage.createNewsWithDate(tomorrowTitle, "Объявление", "Описание", DateUtils.getTomorrow());
        newsEditPage.createNewsWithDate(todayTitle, "Объявление", "Описание", DateUtils.getToday());

        newsEditPage.sortNews(afterTomorrowTitle);

        assertEquals("Заголовок на позиции 1 должен быть 'Послезавтрашняя'",
                afterTomorrowTitle, newsEditPage.getNewsTitleAtPosition(2));
        assertEquals("Заголовок на позиции 2 должен быть 'Завтрашняя'",
                tomorrowTitle, newsEditPage.getNewsTitleAtPosition(1));
        assertEquals("Заголовок на позиции 3 должен быть 'Сегодняшняя'",
                todayTitle, newsEditPage.getNewsTitleAtPosition(0));
    }

     //=== Новый тест: редактирование новости ===

    @Test
    @Story("TC044 – Редактирование существующей новости")
    public void shouldEditNews() {
        String oldTitle = "Старый заголовок" + System.currentTimeMillis();
        String newTitle = "Новый заголовок" + System.currentTimeMillis();
        String newDescription = "Обновлённое описание";
        String category = "Объявление";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(oldTitle, category, "Старое описание");
        newsEditPage.sortNews(oldTitle);

        newsEditPage.editNewsByTitle(oldTitle, newTitle, newDescription);

        newsEditPage.checkNewsDoesNotExist(oldTitle);
        newsEditPage.checkNewsExists(newTitle);
        newsEditPage.checkNewsDescription(newTitle, newDescription);
    }
}