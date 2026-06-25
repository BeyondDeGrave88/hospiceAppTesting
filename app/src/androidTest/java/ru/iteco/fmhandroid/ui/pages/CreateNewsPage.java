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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
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

    @Step("Ожидание загрузки страницы создания новости")
    public void waitForPageLoaded() {
        ViewUtils.waitForView(TITLE_EDIT_TEXT, 10000);
    }

    @Step("Проверка отображения страницы создания новости")
    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(TITLE_EDIT_TEXT)).check(matches(isDisplayed()));
    }

    @Step("Выбор категории {category}")
    public void selectCategory(String category) {
        onView(withId(CATEGORY_AUTO_COMPLETE_TEXT_VIEW)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category)))
                .inRoot(isPlatformPopup())
                .perform(click());
    }

    @Step("Ввод заголовка {title}")
    public void enterTitle(String title) {
        onView(withId(TITLE_EDIT_TEXT))
                .perform(replaceText(title), closeSoftKeyboard());
    }

    @Step("Очистка поля заголовка")
    public void clearTitle() {
        onView(withId(TITLE_EDIT_TEXT))
                .perform(clearText(), closeSoftKeyboard());
    }

    @Step("Ввод даты публикации (текущая дата через DatePicker)")
    public void enterPublicationDate() {
        onView(withId(PUBLICATION_DATE_EDIT_TEXT)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Step("Ввод даты публикации {date}")
    public void enterPublicationDate(String date) {
        onView(withId(PUBLICATION_DATE_EDIT_TEXT))
                .perform(replaceText(date), closeSoftKeyboard());
    }

    @Step("Ввод времени (текущее время)")
    public void enterTime() {
        onView(withId(PUBLICATION_TIME_EDIT_TEXT)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Step("Ввод описания {description}")
    public void enterDescription(String description) {
        onView(withId(DESCRIPTION_EDIT_TEXT)).perform(replaceText(description));
    }

    @Step("Нажатие кнопки 'Сохранить'")
    public void clickSave() {
        onView(withId(SAVE_BUTTON_ID)).perform(scrollTo(), click());
    }

    @Step("Нажатие кнопки 'Отмена'")
    public void clickCancel() {
        onView(withId(CANCEL_BUTTON_ID)).perform(scrollTo(), click());
    }

    @Step("Проверка Toast-сообщения: {expectedMessage}")
    public void checkToastMessage(String expectedMessage) {
        onView(withText(expectedMessage))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}