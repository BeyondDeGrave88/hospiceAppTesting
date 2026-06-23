package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class QuotePage {

    private static final int RECYCLER_VIEW = R.id.our_mission_item_list_recycler_view;
    private static final int TITLE_VIEW = R.id.our_mission_item_title_text_view;
    private static final int DESCRIPTION_VIEW = R.id.our_mission_item_description_text_view;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(withText("Love is all"), 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withText("Love is all")).check(matches(isDisplayed()));
    }

    public void clickExpandButton() {
        onView(withId(RECYCLER_VIEW))
                .perform(actionOnItemAtPosition(0, click()));
    }

    public void expandFirstQuote() {
        clickExpandButton();
    }

    // Проверка, что описание первой цитаты видимо
    public void checkDescriptionVisible() {
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

    // Проверка, что описание первой цитаты скрыто (GONE или отсутствует)
    public void checkDescriptionHidden() {
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
                        // Если descView == null или его visibility != VISIBLE, считаем скрытым
                        if (descView != null && descView.getVisibility() == View.VISIBLE) {
                            throw new PerformException.Builder()
                                    .withActionDescription("Description is visible, but expected hidden")
                                    .build();
                        }
                    }
                }));
    }

    // Проверка, что заголовок первой цитаты виден
    public void checkTitleVisible() {
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

    // Получение текста заголовка первой цитаты (без Ambiguous ошибок)
    public String getFirstQuoteTitle() {
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

    // Получение текста описания первой цитаты (после раскрытия)
    public String getFirstQuoteDescription() {
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
}