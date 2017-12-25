package com.vocabtrainer.project.vocabtrainer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.vocabtrainer.project.vocabtrainer.ListWordActivity;
import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.VocabAppWidgetConfigureActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link VocabAppWidgetConfigureActivity VocabAppWidgetConfigureActivity}
 */
public class VocabAppWidget extends AppWidgetProvider {

    public static final String CATEGORY_ID = "extra_category_id";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {


        String categoryId = VocabAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);

        Intent intent = new Intent(context, VocabWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        if(categoryId != null) intent.setData(Uri.parse(categoryId));

        String name = "";
        String categories[] = context.getResources().getStringArray(R.array.categories);
        if(categoryId == null) name = categories[0];
        else name = categories[Integer.valueOf(categoryId) - 1];

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.vocab_app_widget);
        rv.setTextViewText(R.id.title, name);
        rv.setRemoteAdapter(R.id.list_view, intent);
        rv.setEmptyView(R.id.list_view, R.id.empty_view);


        Intent detailIntent = new Intent(context, ListWordActivity.class);
        if(categoryId != null)  detailIntent.putExtra(ListWordActivity.EXTRA_CATEGORY_ID, Long.parseLong(categoryId));

        detailIntent.putExtra(ListWordActivity.EXTRA_CATEGORY_NAME, name);

       PendingIntent pendingIntent =  TaskStackBuilder.create(context)
                // add all of DetailsActivity's parents to the stack,
                // followed by DetailsActivity itself
                .addNextIntentWithParentStack(detailIntent)
                .getPendingIntent(appWidgetId, PendingIntent.FLAG_UPDATE_CURRENT);
       rv.setOnClickPendingIntent(R.id.container, pendingIntent);

        PendingIntent pendingFillIntent =  TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(detailIntent)
                .getPendingIntent(appWidgetId, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.list_view, pendingFillIntent);

        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            VocabAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

