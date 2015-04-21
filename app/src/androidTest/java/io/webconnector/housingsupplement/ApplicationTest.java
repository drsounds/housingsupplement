package io.webconnector.housingsupplement;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    protected HousingSupplementApplication application;
    protected void setUp() {
        application = new HousingSupplementApplication();
    }
    protected void testApplyForHousing() {
        float monthly_work_income = 11000;
        float monthly_music_income = 3014 / 12;
        float monthly_insurance_income = 2856 / 0.67f;
        application.age = 24;
        application.yearly_rent = 3303 * 12;
        application.work_income = monthly_work_income * 12 + monthly_music_income * 12;
        application.pensions = monthly_insurance_income * 12;

    }
}