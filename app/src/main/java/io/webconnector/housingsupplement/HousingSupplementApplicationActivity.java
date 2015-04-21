package io.webconnector.housingsupplement;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HousingSupplementApplicationActivity extends ActionBarActivity {
    public Map<String, String> elements = new HashMap<String, String>();


    private long id;
    private View findViewById(String resId) {
        int resID = getResources().getIdentifier(resId,
                "id", getPackageName());
        View view = findViewById(resID);
        return view;
    }
    HousingSupplementDatabase database = new HousingSupplementDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_supplement_application);

        id = getIntent().getLongExtra("id", -1);
        HousingSupplementApplication application = null;
        if (id > -1) {
            application = database.getApplicationById(id);
        } else {

            application = database.getLastApplication();
        }
        if (application == null) {
            application = new HousingSupplementApplication();
        }
        // Get values
        Field[] fields = application.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                EditText view = (EditText)findViewById("et_" + field.getName());
                view.setText(String.valueOf(field.getFloat(application) / 12));
            } catch (Exception e) {

            }
        }
        TextView tv_id = (TextView)findViewById(R.id.tv_id);
        tv_id.setText(String.valueOf(id));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            HousingSupplementApplication application = new HousingSupplementApplication();
            HousingSupplementDatabase database = new HousingSupplementDatabase(this);
            Field[] fields = application.getClass().getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                try {
                    EditText view = (EditText)findViewById("et_" + field.getName());
                    if (field.getName().equals("time") && id < 0) {
                        field.set(application, new Date());
                    } else {
                        field.setFloat(application, Float.valueOf(view.getText().toString()) * 12);
                    }
                } catch (Exception e) {

                }
            }
            if (this.id > -1) {
                database.updateApplicationWithId(this, application, this.id);
            }
            database.addPeriod(application, this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
