package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.TestData;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;
import ru.iteco.fmhandroid.ui.utils.ToastMatcher;

public class CreateNewsPage {

    public static final int CATEGORY_AUTO_COMPLETE_TEXT_VIEW = R.id.news_item_category_text_auto_complete_text_view;
    public static final int TITLE_EDIT_TEXT = R.id.news_item_title_text_input_edit_text;
    public static final int PUBLICATION_DATE_EDIT_TEXT = R.id.news_item_publish_date_text_input_edit_text;
    public static final int PUBLICATION_TIME_EDIT_TEXT = R.id.news_item_publish_time_text_input_edit_text;
    public static final int DESCRIPTION_EDIT_TEXT = R.id.news_item_description_text_input_edit_text;
    public static final int SAVE_BUTTON_ID = R.id.save_button;
    public static final int CANCEL_BUTTON_ID = R.id.cancel_button;

    public void waitForPageLoaded() {
        Allure.step("Ожидание загрузки страницы создания новости");
        ViewUtils.waitForView(TITLE_EDIT_TEXT, 10000);
    }

    public void checkPageDisplayed() {
        Allure.step("Проверка отображения страницы создания новости");
        waitForPageLoaded();
        onView(withId(TITLE_EDIT_TEXT)).check(matches(isDisplayed()));
    }

    public void selectCategory(String category) {
        Allure.step("Выбор категории {category}");
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
    }

    public void enterTitle(String title) {
        Allure.step("Ввод заголовка {title}");
        onView(withId(TITLE_EDIT_TEXT))
                .perform(replaceText(title), closeSoftKeyboard());
    }

    public void clearTitle() {
        Allure.step("Очистка поля заголовка");
        onView(withId(TITLE_EDIT_TEXT))
                .perform(clearText(), closeSoftKeyboard());
    }

    public void enterPublicationDate() {
        Allure.step("Ввод даты публикации (текущая дата через DatePicker)");
        onView(withId(PUBLICATION_DATE_EDIT_TEXT)).perform(click());
        ViewUtils.waitForView(withId(android.R.id.button1), 5000);
        onView(withId(android.R.id.button1)).perform(click());
    }

    public void enterPublicationDate(String date) {
        Allure.step("Ввод даты публикации {date}");
        onView(withId(PUBLICATION_DATE_EDIT_TEXT))
                .perform(clearText(), replaceText(date), closeSoftKeyboard());
        onView(withId(PUBLICATION_DATE_EDIT_TEXT))
                .check(matches(withText(date)));
    }

    public void enterTime() {
        Allure.step("Ввод времени (текущее время)");
        onView(withId(PUBLICATION_TIME_EDIT_TEXT)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    public void enterDescription(String description) {
        Allure.step("Ввод описания {description}");
        onView(withId(DESCRIPTION_EDIT_TEXT)).perform(replaceText(description));
    }

    public void clickSave() {
        Allure.step("Нажатие кнопки 'Сохранить'");
        onView(withId(SAVE_BUTTON_ID)).perform(scrollTo(), click());
    }

    public void clickCancel() {
        Allure.step("Нажатие кнопки 'Отмена'");
        onView(withId(CANCEL_BUTTON_ID)).perform(scrollTo(), click());
    }

    public void checkToastMessage(String expectedMessage) {
        Allure.step("Проверка Toast-сообщения: {expectedMessage}");
        onView(withText(expectedMessage))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    public void attemptCreateWithoutCategory(String title, String publicationDate, String description) {
        Allure.step("Попытка создания новости без заполнения поля Category");
        enterTitle(title);
        enterPublicationDate(publicationDate);
        enterTime();
        enterDescription(description);
        clickSave();
    }

    public void attemptCreateWithoutDescription(String category, String title, String publicationDate) {
        Allure.step("Попытка создания новости без заполнения поля Description");
        selectCategory(category);
        enterTitle(title);
        enterPublicationDate(publicationDate);
        enterTime();

        clickSave();
    }

    public void attemptCreateWithoutPublicationDate(String category, String title, String description) {
        Allure.step("Попытка создания новости без заполнения поля Publication date");
        selectCategory(category);
        enterTitle(title);

        enterTime();
        enterDescription(description);
        clickSave();
    }

    public void attemptCreateWithoutTime(String category, String title, String publicationDate, String description) {
        Allure.step("Попытка создания новости без заполнения поля Time");
        selectCategory(category);
        enterTitle(title);
        enterPublicationDate(publicationDate);

        enterDescription(description);
        clickSave();
    }

    public void attemptCreateWithoutTitle(String category, String publicationDate, String description) {
        Allure.step("Попытка создания новости без заполнения поля Title");
        selectCategory(category);
        clearTitle();
        enterPublicationDate(publicationDate);
        enterTime();
        enterDescription(description);
        clickSave();
    }

    public void attemptCreateWithAllEmpty() {
        Allure.step("Попытка создания новости со всеми пустыми полями");
        clickSave();
    }

    public void checkCreationErrorAndPageDisplayed(String expectedErrorMessage) {
        Allure.step("Проверка отображения ошибки и того, что страница создания всё ещё открыта");
        checkToastMessage(expectedErrorMessage);
        checkPageDisplayed();
    }
    public void cancelCreationWithConfirmation() {
        Allure.step("Отмена создания новости с подтверждением");
        clickCancel();
        onView(withText(TestData.CONFIRM_CANCEL_MESSAGE))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        onView(withText(TestData.OK_BUTTON_TEXT))
                .inRoot(isDialog())
                .perform(click());
    }
}