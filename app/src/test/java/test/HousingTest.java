package io.webconnector.housingsupplement.test;

import junit.framework.TestCase;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import io.webconnector.housingsupplement.HousingSupplementApplication;

/**
 * Created by Alecca on 2015-04-19.
 */
public class HousingTest extends TestCase {
    protected HousingSupplementApplication application;
    @Override
    public void setUp() {
        application = new HousingSupplementApplication();
    }
    public void testApplyForHousing() {
        float monthly_work_income = 11000;
        float monthly_music_income = 3014 / 12;
        float monthly_insurance_income = 2856 / 0.67f;
        application.age = 24;
        application.yearly_rent = 3303 * 12;
        application.work_income = monthly_work_income * 12 + monthly_music_income * 12;
        application.pensions = monthly_insurance_income * 12;
        float yearly_housing_supplement = application.calculateYearlyHouseSupplement();
        float monthly_housing_supplement = application.calculateMonthlyHousingSupplement();

        BufferedWriter osw = new BufferedWriter(new OutputStreamWriter(System.out));


        System.out.println(String.format("Yearly housing supplement is %2f", yearly_housing_supplement));
        System.out.println(String.format("Monthly housing supplement is %2f", monthly_housing_supplement));

    }
}
