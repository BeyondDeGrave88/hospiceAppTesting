package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.allOf;


import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class NewsEditPage {

    public void waitForPageLoaded() {
        ViewUtils.waitForView(R.id.add_news_image_view, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
    }

    public void clickAddNews() {
        onView(withId(R.id.add_news_image_view)).perform(click());
    }

    public void clickFilter() {
        onView(withId(R.id.filter_news_material_button)).perform(click());
    }

    public void clickSort() {
        onView(withId(R.id.sort_news_material_button)).perform(click());
    }

    public void deleteNews() {
        onView(withContentDescription("News delete button")).perform(click());
    }
    public void deleteNewsByTitle(String title) {
        onView(allOf(
                withId(R.id.delete_news_item_image_view),
                isDescendantOfA(
                        allOf(withId(R.id.news_item_material_card_view),
                                hasDescendant(withText(title)))
                )
        )).perform(click());
    }
    public String getPublicationDate(String title) {
        final String[] text = new String[1];
        onView(allOf(
                withId(R.id.news_item_publication_date_text_view),
                isDescendantOfA(
                        allOf(withId(R.id.news_item_material_card_view),
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
        final String[] text = new String[1];
        onView(allOf(
                withId(R.id.news_item_create_date_text_view),
                isDescendantOfA(
                        allOf(withId(R.id.news_item_material_card_view),
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
}
