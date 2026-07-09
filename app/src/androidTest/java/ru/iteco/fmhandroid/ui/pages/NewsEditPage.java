package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Allure;
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
    public static final int SWIPE_REFRESH_ID = R.id.news_control_panel_swipe_to_refresh;


    public String getPublicationDate(String title) {
        Allure.step("Получение даты публикации новости с заголовком {title}");
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

    public String getCreationDate(String title) {
        Allure.step("Получение даты создания новости с заголовком {title}");
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

    public int getNewsPosition(String title) {
        Allure.step("Получение позиции новости с заголовком {title} в списке");
        final int[] position = new int[1];
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get position of news with title: " + title;
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        RecyclerView recyclerView = (RecyclerView) view;
                        RecyclerView.Adapter adapter = recyclerView.getAdapter();
                        if (adapter != null) {
                            for (int i = 0; i < adapter.getItemCount(); i++) {
                                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
                                if (holder != null) {
                                    View itemView = holder.itemView;
                                    TextView titleView = itemView.findViewById(NEWS_ITEM_TITLE_TEXT_VIEW_ID);
                                    if (titleView != null && titleView.getText().toString().equals(title)) {
                                        position[0] = i;
                                        return;
                                    }
                                }
                            }
                        }
                        throw new RuntimeException("News with title '" + title + "' not found in RecyclerView");
                    }
                });
        return position[0];
    }

    public void waitForPageLoaded() {
        Allure.step("Ожидание загрузки страницы редактирования новостей");
        ViewUtils.waitForView(ADD_NEWS_BUTTON_ID, 10000);
    }

    public void addNews() {
        Allure.step("Нажатие кнопки 'Добавить новость'");
        onView(withId(ADD_NEWS_BUTTON_ID)).perform(click());
    }

    public void openFilterScreen() {
        Allure.step("Открытие экрана фильтра");
        onView(withId(FILTER_ICON_ID)).perform(click());
    }

    public void sortNews(String title) {
        Allure.step("Сортировка новостей и ожидание появления новости с заголовком {title}");
        onView(withId(SORT_BUTTON_ID)).perform(click());

        Matcher<View> newsCardMatcher = allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)));
        ViewUtils.waitForView(newsCardMatcher, 15000);

        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(RecyclerViewActions.scrollTo(withText(title)));

        onView(newsCardMatcher).check(matches(isDisplayed()));
    }

    public void deleteNewsByTitle(String title) {
        Allure.step("Клик по иконке удаления новости с заголовком {title} (открывает диалог)");
        onView(allOf(
                withId(DELETE_ICON_ID),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID),
                                hasDescendant(withText(title)))
                )
        )).perform(click());
    }

    public void confirmDelete() {
        Allure.step("Подтверждение удаления новости (нажатие OK)");
        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(TestData.OK_BUTTON_TEXT))
                .inRoot(isDialog())
                .perform(click());
        ViewUtils.sleep(500);
    }

    public void cancelDelete() {
        Allure.step("Отмена удаления новости (нажатие Cancel)");
        onView(withText(TestData.CONFIRM_DELETE_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(TestData.CANCEL_BUTTON_TEXT))
                .inRoot(isDialog())
                .perform(click());
        ViewUtils.sleep(500);
    }
    public void deleteNewsIfExists(String title) {
        Allure.step("Удаление новости с заголовком {title}, если она существует");
        try {
            onView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))))
                    .check(matches(isDisplayed()));
            deleteNewsByTitle(title);
            confirmDelete();
        } catch (Exception e) {
        }
    }

    public void checkNewsDoesNotExist(String title) {
        Allure.step("Проверка, что новость с заголовком {title} не существует");
        onView(allOf(
                withId(NEWS_CARD_ID),
                hasDescendant(withText(title))
        )).check(doesNotExist());
    }

    public void createNewsToday(String title, String category, String description) {
        Allure.step("Создание новости с сегодняшней датой");
        waitForPageLoaded();
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
        refreshNewsList();
        printNewsCount();
        ViewUtils.waitForView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))), 15000);
    }

    public void createNewsWithDate(String title, String category, String description, String date) {
        Allure.step("Создание новости с указанной датой");
        waitForPageLoaded();
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
        refreshNewsList();
        printNewsCount();
        ViewUtils.waitForView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))), 15000);
    }
    public void printNewsCount() {
        Allure.step("Подсчет новостей");
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(new ViewAction() {

                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Print adapter size";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        RecyclerView rv = (RecyclerView) view;
                        System.out.println("COUNT = " + rv.getAdapter().getItemCount());
                    }
                });
    }
    public void editNewsByTitle(String oldTitle, String newTitle, String newDescription) {
        Allure.step("Редактирование новости с заголовком {oldTitle}. Новый заголовок: {newTitle}, новое описание: {newDescription}");
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(oldTitle))));

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

    public void checkNewsExists(String title) {
        Allure.step("Проверка, что новость с заголовком {title} существует и видна");
        Matcher<View> newsCardMatcher = allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)));
        ViewUtils.waitForView(newsCardMatcher, 15000);
        onView(withId(NEWS_LIST_RECYCLER_VIEW_ID))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        onView(newsCardMatcher).check(matches(isDisplayed()));
    }

    public void checkNewsDescription(String title, String description) {
        Allure.step("Проверка описания новости с заголовком {title} после раскрытия");
        onView(allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title))))
                .perform(click());
        onView(allOf(
                withId(R.id.news_item_description_text_view),
                isDescendantOfA(
                        allOf(withId(NEWS_CARD_ID), hasDescendant(withText(title)))
                )
        )).check(matches(withText(description)));
    }

    public void refreshNewsList() {
        Allure.step("Обновление списка новостей в панели управления");
        onView(withId(SWIPE_REFRESH_ID))
                .perform(swipeDown());

        ViewUtils.waitForView(ADD_NEWS_BUTTON_ID, 10000);

        ViewUtils.sleep(2000);
    }
}
