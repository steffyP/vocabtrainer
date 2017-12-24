package com.vocabtrainer.project.vocabtrainer;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vocabtrainer.project.vocabtrainer.widget.VocabAppWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link VocabAppWidget VocabAppWidget} AppWidget.
 */
public class VocabAppWidgetConfigureActivity extends AppCompatActivity {

    @BindView(R.id.drop_down_widget)
    Spinner spinner;

    @BindView(R.id.add_button)
    View btnAdd;

    private static final String PREFS_NAME = "com.vocabtrainer.project.vocabtrainer.widget.VocabAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.vocab_app_widget_configure);
        ButterKnife.bind(this);

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_item, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                final Context context = VocabAppWidgetConfigureActivity.this;

                // When the button is clicked, store the string locally
                int category = spinner.getSelectedItemPosition() + 1;
                saveTitlePref(context, mAppWidgetId, String.valueOf(category));

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                VocabAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        };

        btnAdd.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);

        return titleValue;

    }

    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

}

