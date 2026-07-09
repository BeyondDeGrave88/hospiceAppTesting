package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class QuotePage {

    private static final int RECYCLER_VIEW = R.id.our_mission_item_list_recycler_view;
    private static final int TITLE_VIEW = R.id.our_mission_item_title_text_view;
    private static final int DESCRIPTION_VIEW = R.id.our_mission_item_description_text_view;

    public String getFirstQuoteTitle() {
        Allure.step("Получение заголовка первой цитаты");
        final String[] text = new String[1];
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get text from first quote title";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View titleView = view.findViewById(TITLE_VIEW);
                        if (titleView != null) {
                            text[0] = ((TextView) titleView).getText().toString();
                        }
                    }
                }));
        return text[0];
    }

    public String getFirstQuoteDescription() {
        Allure.step("Получение описания первой цитаты");
        final String[] text = new String[1];
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get text from first quote description";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View descView = view.findViewById(DESCRIPTION_VIEW);
                        if (descView != null) {
                            text[0] = ((TextView) descView).getText().toString();
                        }
                    }
                }));
        return text[0];
    }

    public void waitForPageLoaded() {
        Allure.step("Ожидание загрузки страницы 'Цитаты'");
        ViewUtils.waitForView(withText("Love is all"), 10000);
    }

    public void checkPageDisplayed() {
        Allure.step("Проверка отображения страницы 'Цитаты'");
        waitForPageLoaded();
        onView(withText("Love is all")).check(matches(isDisplayed()));
    }

    public void clickExpandButton() {
        Allure.step("Нажатие на кнопку раскрытия первой цитаты");
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, click()));
    }

    public void expandFirstQuote() {
        Allure.step("Развернуть первую цитату");
        clickExpandButton();
    }

    public void checkDescriptionVisible() {
        Allure.step("Проверка, что описание первой цитаты видимо");
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Check that first quote description is visible";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View descView = view.findViewById(DESCRIPTION_VIEW);
                        if (descView == null) {
                            throw new PerformException.Builder()
                                    .withActionDescription("Description view not found in first item")
                                    .build();
                        }
                        if (descView.getVisibility() != View.VISIBLE) {
                            throw new PerformException.Builder()
                                    .withActionDescription("Description is not visible")
                                    .build();
                        }
                    }
                }));
    }

    public void checkDescriptionHidden() {
        Allure.step("Проверка, что описание первой цитаты скрыто");
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Check that first quote description is hidden";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View descView = view.findViewById(DESCRIPTION_VIEW);
                        if (descView != null && descView.getVisibility() == View.VISIBLE) {
                            throw new PerformException.Builder()
                                    .withActionDescription("Description is visible, but expected hidden")
                                    .build();
                        }
                    }
                }));
    }

    public void checkTitleVisible() {
        Allure.step("Проверка, что заголовок первой цитаты видим");
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Check that first quote title is visible";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        View titleView = view.findViewById(TITLE_VIEW);
                        if (titleView == null || titleView.getVisibility() != View.VISIBLE) {
                            throw new PerformException.Builder()
                                    .withActionDescription("Title is not visible")
                                    .build();
                        }
                    }
                }));
    }
}