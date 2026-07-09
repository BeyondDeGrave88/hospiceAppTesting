package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Story;

import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.pages.CreateNewsPage;
import ru.iteco.fmhandroid.ui.pages.FilterNewsPage;
import ru.iteco.fmhandroid.ui.pages.NewsEditPage;
import ru.iteco.fmhandroid.ui.pages.NewsListPage;
import ru.iteco.fmhandroid.ui.utils.DateUtils;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Новости")
public class NewsTest extends BaseTest {

    private NewsListPage newsListPage;
    private NewsEditPage newsEditPage;
    private CreateNewsPage createNewsPage;
    private FilterNewsPage filterNewsPage;
    private List<String> createdTitles = new ArrayList<>();

    @Override
    @Before
    public void setUp() {
        super.setUp();
        newsListPage = new NewsListPage();
        newsEditPage = new NewsEditPage();
        createNewsPage = new CreateNewsPage();
        filterNewsPage = new FilterNewsPage();
        createdTitles.clear();
    }
    @After
    public void tearDown() {
        // Если нет созданных новостей, ничего не делаем
        if (createdTitles.isEmpty()) {
            createdTitles.clear();
            return;
        }

        try {
            mainPage.openNewsControlPanel();
            newsEditPage.waitForPageLoaded();
            ViewUtils.sleep(1000);
        } catch (Exception e) {
            System.err.println("Не удалось перейти на панель управления: " + e.getMessage());
            try {
                activityScenarioRule.getScenario().recreate();
                ViewUtils.sleep(2000);
                mainPage.openNewsControlPanel();
                newsEditPage.waitForPageLoaded();
            } catch (Exception ex) {
                System.err.println("Не удалось перейти на панель управления после перезапуска: " + ex.getMessage());
                createdTitles.clear();
                return;
            }
        }

        for (String title : createdTitles) {
            try {
                newsEditPage.deleteNewsIfExists(title);
            } catch (Exception e) {
                System.err.println("Не удалось удалить новость: " + title + " | " + e.getMessage());
            }
        }
        createdTitles.clear();
    }

    @Test
    @Story("TC011 – Новость содержит заголовок, дату, категорию, комментарий")
    public void shouldDisplayNewsTitleDateCategoryAndComment() {
        String title = "Праздник" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Праздник";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);

        mainPage.openNews();
        newsListPage.waitForPageLoaded();
        newsListPage.refreshNewsList();

        newsListPage.checkNewsDetails(title, DateUtils.getToday());
        newsListPage.checkNewsDescription(title, description);
    }

    @Test
    @Story("TC012 – Создание новости со всеми заполненными полями")
    public void shouldCreateNewsWithAllFilledFields() {
        String title = "Объявление" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Объявление";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.checkNewsExists(title);
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
        newsEditPage.checkNewsDoesNotExist(title);
    }

    @Test
    @Story("TC014 – Нажатие кнопки Ok при удалении новости")
    public void shouldDeleteNewsWithOk() {
        String title = "Зарплата" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Зарплата";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.deleteNewsByTitle(title);
        newsEditPage.confirmDelete();

        newsEditPage.waitForPageLoaded();
        newsEditPage.checkNewsDoesNotExist(title);
    }

    @Test
    @Story("TC015 – Нажатие кнопки Cancel при удалении новости")
    public void shouldCancelDeleteNews() {
        String title = "Профсоюз" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Профсоюз";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);
        newsEditPage.deleteNewsByTitle(title);
        newsEditPage.cancelDelete();

        newsEditPage.waitForPageLoaded();
        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC016 – Фильтрация новостей за сегодня")
    public void shouldFilterNewsToday() {
        String title = "Праздник" + System.currentTimeMillis();
        createdTitles.add(title);
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
        String title = "День рождения" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "День рождения";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(title, category, description, DateUtils.getDateDaysAfter(7));
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getToday(), DateUtils.getDateDaysAfter(7));

        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC018 – Фильтрация новостей за месяц")
    public void shouldFilterNewsMonth() {
        String title = "Нужна помощь" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Нужна помощь";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(title, category, description, DateUtils.getDateDaysAfter(30));
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getToday(), DateUtils.getDateDaysAfter(30));

        newsEditPage.checkNewsExists(title);
    }

    @Test
    @Story("TC019 – Фильтрация новостей по категории 'Объявление'")
    public void shouldFilterNewsByCategoryAnnouncement() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        createdTitles.add(titleAnnouncement);
        String titleMassage = "Массаж" + System.currentTimeMillis();
        createdTitles.add(titleMassage);
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryAnnouncement);

        newsEditPage.checkNewsDoesNotExist(titleMassage);
    }

    @Test
    @Story("TC020 – Фильтрация новостей по категории 'Массаж'")
    public void shouldFilterNewsByCategoryMassage() {
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        createdTitles.add(titleAnnouncement);
        String titleMassage = "Массаж" + System.currentTimeMillis();
        createdTitles.add(titleMassage);
        String categoryAnnouncement = "Объявление";
        String categoryMassage = "Массаж";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryMassage);

        newsEditPage.checkNewsDoesNotExist(titleAnnouncement);
    }

    @Test
    @Story("TC021 – Сброс фильтра по умолчанию - показываются все новости")
    public void shouldResetFilterShowsAllNews() {
        String titleMassage = "Массаж" + System.currentTimeMillis();
        createdTitles.add(titleMassage);
        String titleAnnouncement = "Объявление" + System.currentTimeMillis();
        createdTitles.add(titleAnnouncement);
        String categoryMassage = "Массаж";
        String categoryAnnouncement = "Объявление";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(titleMassage, categoryMassage, description);
        newsEditPage.createNewsToday(titleAnnouncement, categoryAnnouncement, description);
        newsEditPage.refreshNewsList();

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsExists(titleAnnouncement);

        newsEditPage.openFilterScreen();
        filterNewsPage.filterByCategory(categoryMassage);
        newsEditPage.refreshNewsList();

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsDoesNotExist(titleAnnouncement);

        newsEditPage.openFilterScreen();
        filterNewsPage.resetFilter();
        newsEditPage.refreshNewsList();

        newsEditPage.checkNewsExists(titleMassage);
        newsEditPage.checkNewsExists(titleAnnouncement);
    }

    @Test // Баг – дата создания отображается некорректно (год 58448)
    @Story("TC022 – Дата создания новости равна дате публикации")
    public void shouldCreationDateEqualsPublicationDate() {
        String title = "Благодарность" + System.currentTimeMillis();
        createdTitles.add(title);
        String category = "Благодарность";
        String description = "Описание";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(title, category, description);

        String publicationDate = newsEditPage.getPublicationDate(title);
        String creationDate = newsEditPage.getCreationDate(title);

        assertEquals("Дата публикации должна совпадать с датой создания", publicationDate, creationDate);
    }

    @Test
    @Story("TC023 – Дата публикации новости равна завтрашней дате")
    public void shouldPublicationDateEqualsTomorrow() {
        String title = "Зарплата" + System.currentTimeMillis();
        createdTitles.add(title);
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
        createdTitles.add(longTitle);

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(longTitle, "Объявление", "Описание");
    }

    @Test // Баг - новость создается, ошибки сохранения нет (падает, потому что нет toast)
    @Story("TC042 – Создание новости с внедрением HTML-тегов (XSS)")
    public void shouldNotCreateNewsWithXSS() {
        mainPage.openNewsControlPanel();
        newsEditPage.addNews();
        createNewsPage.waitForPageLoaded();
        createNewsPage.selectCategory("Объявление");
        createNewsPage.enterTitle(TestData.XSS_PAYLOAD);
        createdTitles.add(TestData.XSS_PAYLOAD);
        createNewsPage.enterPublicationDate();
        createNewsPage.enterTime();
        createNewsPage.enterDescription(TestData.XSS_PAYLOAD);
        createNewsPage.clickSave();

        createNewsPage.checkToastMessage(TestData.SAVING_FAILED_ERROR);

        newsEditPage.checkNewsDoesNotExist(TestData.XSS_PAYLOAD);
    }


    @Test
    @Story("TC043 – Сортировка новостей по дате (от новых к старым)")
    public void shouldSortNewsByDate() {
        String todayTitle = "Сегодняшняя" + System.currentTimeMillis();
        createdTitles.add(todayTitle);
        String tomorrowTitle = "Завтрашняя" + System.currentTimeMillis();
        createdTitles.add(tomorrowTitle);
        String afterTomorrowTitle = "Послезавтрашняя" + System.currentTimeMillis();
        createdTitles.add(afterTomorrowTitle);

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsWithDate(todayTitle, "Объявление", "Описание", DateUtils.getToday());
        newsEditPage.createNewsWithDate(tomorrowTitle, "Объявление", "Описание", DateUtils.getTomorrow());
        newsEditPage.createNewsWithDate(afterTomorrowTitle, "Объявление", "Описание", DateUtils.getAfterTomorrow());

        newsEditPage.openFilterScreen();
        filterNewsPage.filterByDateRange(DateUtils.getToday(), DateUtils.getAfterTomorrow());
        newsEditPage.refreshNewsList();

        newsEditPage.checkNewsExists(todayTitle);
        newsEditPage.checkNewsExists(tomorrowTitle);
        newsEditPage.checkNewsExists(afterTomorrowTitle);

        int posToday = newsEditPage.getNewsPosition(todayTitle);
        int posTomorrow = newsEditPage.getNewsPosition(tomorrowTitle);
        int posAfter = newsEditPage.getNewsPosition(afterTomorrowTitle);

        assertTrue("Послезавтрашняя должна быть перед завтрашней", posAfter < posTomorrow);
        assertTrue("Завтрашняя должна быть перед сегодняшней", posTomorrow < posToday);
    }

     //=== Новый тест: редактирование новости ===

    @Test
    @Story("TC044 – Редактирование существующей новости")
    public void shouldEditNews() {
        String oldTitle = "Старый заголовок" + System.currentTimeMillis();
        createdTitles.add(oldTitle);
        String newTitle = "Новый заголовок" + System.currentTimeMillis();
        createdTitles.add(newTitle);
        String newDescription = "Обновлённое описание";
        String category = "Объявление";

        mainPage.openNewsControlPanel();
        newsEditPage.createNewsToday(oldTitle, category, "Старое описание");

        newsEditPage.editNewsByTitle(oldTitle, newTitle, newDescription);

        newsEditPage.checkNewsDoesNotExist(oldTitle);
        newsEditPage.checkNewsExists(newTitle);
        newsEditPage.checkNewsDescription(newTitle, newDescription);
    }
}