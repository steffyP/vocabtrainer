package com.vocabtrainer.project.vocabtrainer.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

/**
 * Created by stefanie on 24.12.17.
 */

public class VocabWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Cursor cursor;
        private Context context;
        @SuppressWarnings("unused")
        private int appWidgetId;
        private int categoryId;


        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String uri = intent.getDataString();
            if(uri != null) {
                categoryId = Integer.valueOf(intent.getDataString());
            } else categoryId = -1;
        }

        private void loadData() {
            if(categoryId == -1) return;

            if (cursor != null) {
                cursor.close();
                cursor = null;
            }

            // required to load data without exporting the content provider
            final long token = Binder.clearCallingIdentity();
            try {

                cursor = context.getContentResolver().query(VocabContract.Category.buildItemUri(categoryId),
                        null,
                        null,
                        null,
                        null);
            } finally {
                Binder.restoreCallingIdentity(token);
            }


        }

        @Override
        public void onCreate() {
            loadData();
        }



        @Override
        public void onDataSetChanged() {
            loadData();
        }

        @Override
        public void onDestroy() {
            if (cursor == null) {
                cursor.close();
                cursor = null;
            }
        }

        @Override
        public int getCount() {
            if (cursor == null) return 0;
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d("widget", "view at position " + position);

            if (position == AdapterView.INVALID_POSITION ||
                    cursor == null || !cursor.moveToPosition(position)) {
                return null;
            }


            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget);

            String german = cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_GERMAN));
            String english = cursor.getString(cursor.getColumnIndex(VocabContract.Word.COLUMN_ENGLISH));


            rv.setTextViewText(R.id.german, german);
            rv.setTextViewText(R.id.english, english);

            if(position % 2 != 0){
                rv.setInt(R.id.line, "setBackgroundResource", android.R.color.white);
            } else {
                rv.setInt(R.id.line, "setBackgroundResource", R.color.white_transparent);
            }

            Intent intent = new Intent();
            rv.setOnClickFillInIntent(R.id.line, intent);


            return rv;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


    }

}
