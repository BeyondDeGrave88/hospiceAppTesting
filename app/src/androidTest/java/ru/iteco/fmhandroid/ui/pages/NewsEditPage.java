package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class NewsEditPage {

    public static final int ADD_NEWS_BUTTON_ID = R.id.add_news_image_view;
    public static final int FILTER_ICON_ID = R.id.filter_news_material_button;
    public static final int SORT_BUTTON_ID = R.id.sort_news_material_button;
    public static final int DELETE_ICON_ID = R.id.delete_news_item_image_view;
    public static final int NEWS_CARD_ID = R.id.news_item_material_card_view;
    public static final int PUBLICATION_DATE_TEXT_VIEW_ID = R.id.news_item_publication_date_text_view;
    public static final int CREATION_DATE_TEXT_VIEW_ID = R.id.news_item_create_date_text_view;
    public static final int NEWS_LIST_RECYCLER_VIEW_ID = R.id.news_list_recycler_view;
    public static final int NEWS_ITEM_TITLE_TEXT_VIEW_ID = R.id.news_item_title_text_view;
    public static final int EDIT_ICON_ID = R.id.edit_news_item_image_view;



    @Step("Ожидание загрузки страницы редактирования новостей")
    public void waitForPageLoaded() {
        ViewUtils.waitForView(ADD_NEWS_BUTTON_ID, 10000);
    }

    @Step("Нажатие кнопки 'Добавить новость'")
    public void addNews() {
        onView(withId(ADD_NEWS_BUTTON_ID)).perform(click());
    }

    @Step("Открытие экрана фильтра")
    public void openFilterScreen() {
        onView(withId(FILTER_ICON_ID)).perform(click());
    }

    @Step("Сортировка новостей и ожидание появления новости с заголовком {title}")
    public void sortNews(String title) {
        onView(withId(SORT_BUTTON_ID)).perform(click());

        ViewUtils.waitForView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))), 10000);

        onView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))))
                .check(matches(isDisplayed()));
    }

    @Step("Удаление новости с заголовком {title}")
    public void deleteNewsByTitle(String title) {
        onView(allOf(
                withId(DELETE_ICON_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID),
                                hasDescendant(withText(title)))
                )
        )).perform(click());
    }

    @Step("Получение даты публикации новости с заголовком {title}")
    public String getPublicationDate(String title) {
        final String[] text = new String[1];
        onView(allOf(
                withId(PUBLICATION_DATE_TEXT_VIEW_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID),
                                hasDescendant(withText(title)))
                )
        )).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Get text from Publication date TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                text[0] = ((TextView) view).getText().toString();
            }
        });
        return text[0];
    }

    @Step("Получение даты создания новости с заголовком {title}")
    public String getCreationDate(String title) {
        final String[] text = new String[1];
        onView(allOf(
                withId(CREATION_DATE_TEXT_VIEW_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID),
                                hasDescendant(withText(title)))
                )
        )).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Get text from Creation date TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                text[0] = ((TextView) view).getText().toString();
            }
        });
        return text[0];
    }

    @Step("Получение заголовка новости на позиции {position}")
    public String getNewsTitleAtPosition(int position) {
        final String[] text = new String[1];
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(actionOnItemAtPosition(position, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get news title at position " + position;
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View titleView = view.findViewById(NEWS_ITEM_TITLE_TEXT_VIEW_ID);
                        if (titleView != null) {
                            text[0] = ((TextView) titleView).getText().toString();
                        }
                    }
                }));
        return text[0];
    }

    @Step("Проверка, что новость с заголовком {title} не существует")
    public void checkNewsDoesNotExist(String title) {
        onView(allOf(
                withId(NEWS_CARD_ID),
                hasDescendant(withText(title))
        )).check(doesNotExist());
    }

    @Step("Создание новости с сегодняшней датой")
    public void createNewsToday(String title, String category, String description) {
        addNews();
        CreateNewsPage createPage = new CreateNewsPage();
        createPage.waitForPageLoaded();
        createPage.selectCategory(category);
        createPage.enterTitle(title);
        createPage.enterPublicationDate();
        createPage.enterTime();
        createPage.enterDescription(description);
        createPage.clickSave();
        waitForPageLoaded();
    }

    @Step("Создание новости с указанной датой")
    public void createNewsWithDate(String title, String category, String description, String date) {
        addNews();
        CreateNewsPage createPage = new CreateNewsPage();
        createPage.waitForPageLoaded();
        createPage.selectCategory(category);
        createPage.enterTitle(title);
        createPage.enterPublicationDate(date);
        createPage.enterTime();
        createPage.enterDescription(description);
        createPage.clickSave();
        waitForPageLoaded();
    }

    @Step("Редактирование новости с заголовком {oldTitle}. Новый заголовок: {newTitle}, новое описание: {newDescription}")
    public void editNewsByTitle(String oldTitle, String newTitle, String newDescription) {
        onView(allOf(
                withId(EDIT_ICON_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID),
                                hasDescendant(withText(oldTitle)))
                )
        )).perform(click());

        CreateNewsPage createPage = new CreateNewsPage();
        createPage.waitForPageLoaded();
        createPage.enterTitle(newTitle);
        createPage.enterDescription(newDescription);
        createPage.clickSave();
        waitForPageLoaded();

        ViewUtils.waitForView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(newTitle))), 10000);
    }

    @Step("Проверка, что новость с заголовком {title} существует и видна")
    public void checkNewsExists(String title) {
        onView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))))
                .check(matches(isDisplayed()));
    }

    @Step("Проверка описания новости с заголовком {title} после раскрытия")
    public void checkNewsDescription(String title, String description) {
        onView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))))
                .perform(click());
        onView(allOf(
                withId(R.id.news_item_description_text_view),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)))
                )
        )).check(matches(withText(description)));
    }
    @Step("Подтверждение удаления новости (нажатие OK)")
    public void confirmDelete() {
        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(TestData.OK_BUTTON_TEXT)).perform(click());
    }

    @Step("Отмена удаления новости (нажатие Cancel)")
    public void cancelDelete() {
        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(TestData.CANCEL_BUTTON_TEXT))
                .inRoot(isDialog())
                .perform(click());
    }
}
