package io.webconnector.housingsupplement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by Alecca on 2015-04-19.
 * Based on information at https://www.forsakringskassan.se/wps/wcm/connect/d03ec639-b52d-4d76-a3f1-55148b07edbe/4126-bostadstillagg-till-aktivitetsersattning-sjukersattning2015.pdf?MOD=AJPERES
 */
public class HousingSupplementApplication {
    public float age;

    public HousingSupplementApplication(Cursor cursor) {
        int columns = cursor.getColumnCount();
        for (int i = 0; i < columns; i++) {
            String column_name = cursor.getColumnName(i);
            try {
                Field field = this.getClass().getField(column_name);
                try {
                    if (cursor.getColumnIndex(field.getName()) != -1)
                        if (column_name == "time") {
                            field.set(this, new Date(cursor.getLong(i)));
                        } else {
                            try {
                                field.set(this, cursor.getFloat(i));
                            } catch (Exception e) {

                            }
                        }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public ContentValues toContentValues(Context context) {
        ContentValues cv = new ContentValues();
        HousingSupplementDatabase database = new HousingSupplementDatabase(context);

        Field[] fields = this.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];

                try {
                    if (field.getName() == "time") {
                        cv.put(field.getName(), ((Date) field.get((Object) this)).getTime());

                    } else {
                        try {
                            if (database.hasColumnInTable("housing_applications", field.getName()))
                                cv.put(field.getName(), field.getFloat(this));
                        } catch (Exception e) {

                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }
        }
        return cv;
    }

    /**
     * Return max incomwe
     * @return
     */
    public float getMaxIncome (){
        if (age < 21) {
            return 93450;
        }
        if (age >= 21 && age < 23) {
            return 95675;
        }
        if (age >= 23 && age < 25 ) {
            return 9700;
        }
        if (age >= 25 && age < 27) {
            return 100125;
        }
        if (age >= 25 && age < 29) {
            return 102350;
        }
        if (age >= 29 && age < 30) {
            return 104575;
        }
        return 106800;
    }
    public Date time;
    public static float pension_rate = 1;
    public static float work_leave_rate = 0.8f;
    public static float work_rate = 0.5f;
    public float rent;
    public float tax_free_incomes; // Skattefria intäkter
    public float pensions; // Sjukersättning, aktivitetsersättning, ålderspension
    public float work_leave; // Sjukskrivning, a-kassa, vårdnadsbidrag, föräldrapenning
    public float work_income; // Arbetsinkomster
    public float study_allowance; // Studiebidrag, CSN
    public float diploma; // Stipendier
    public float communal_care_allowance;
    public float liquid_savings; // Likvida besparingar, räntebärande bankkonton
    public float insurance_incomes; // Inkomster från försäkringar
    public float financial_asset; // Värdet på fondandelar, investeringssparkonton etc
    public float house_supplement_rate = 0.9f;
    public float max_housing_supplement_single = 4650 * 12;
    public float max_housing_supplement_married = 2325 * 12;

    public HousingSupplementApplication () {

    }

    /**
     * Calculate housing supplement
     * @return
     */
    public float calculateYearlyHouseSupplement () {
        float liquid_savings_addon = 0;
        if (liquid_savings > 100000) {
            liquid_savings_addon = liquid_savings * 0.15f;
        }
        float free_amount = getMaxIncome(); // Get max income
        float diploma_income = diploma - 3000 > 0 ? diploma - 3000 : 0;

        float yearly_income = liquid_savings_addon + diploma_income + tax_free_incomes + (pensions * pension_rate) + (work_leave * work_leave_rate) + (work_income * work_rate);
        float reducing_income = yearly_income - free_amount;
        if (reducing_income < 0) {
            return 0;
        }


        float yearly_rent_supplement = (rent > max_housing_supplement_single ? max_housing_supplement_single : rent) * house_supplement_rate;
        float rent_supplement = 0;
        if (reducing_income < 44500) {
            yearly_rent_supplement = yearly_rent_supplement - reducing_income * 0.62f;
        } else {
            yearly_rent_supplement = yearly_rent_supplement - reducing_income * 0.5f;
        }

        float yearly_housing_supplement = yearly_rent_supplement;
        return yearly_housing_supplement;
    }

    public float calculateMonthlyHousingSupplement() {
        return calculateYearlyHouseSupplement() / 12;
    }


}
