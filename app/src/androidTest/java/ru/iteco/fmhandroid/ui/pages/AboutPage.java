package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewUtils;

public class AboutPage {

    public static final int VERSION_TEXT_VIEW_ID = R.id.about_version_title_text_view;
    public static final int PRIVACY_POLICY_LINK_ID = R.id.about_privacy_policy_value_text_view;
    public static final int TERMS_OF_USE_LINK_ID = R.id.about_terms_of_use_value_text_view;

    public void waitForPageLoaded() {
        ViewUtils.waitForView(VERSION_TEXT_VIEW_ID, 10000);
    }

    public void checkPageDisplayed() {
        waitForPageLoaded();
        onView(withId(VERSION_TEXT_VIEW_ID)).check(matches(isDisplayed()));
    }
    public void checkPrivacyPolicyLinkDisplayed() {
        onView(withId(PRIVACY_POLICY_LINK_ID)).check(matches(isDisplayed()));
    }

    public void checkTermsOfUseLinkDisplayed() {
        onView(withId(TERMS_OF_USE_LINK_ID)).check(matches(isDisplayed()));
    }

    public void clickPrivacyPolicyLink() {
        onView(withId(PRIVACY_POLICY_LINK_ID)).perform(click());
    }

    public void clickTermsOfUseLink() {
        onView(withId(TERMS_OF_USE_LINK_ID)).perform(click());
    }
}