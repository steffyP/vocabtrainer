package com.vocabtrainer.project.vocabtrainer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.ListWordActivity;
import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

/**
 * Created by stefanie on 20.12.17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final Context context;
    private Cursor data;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void swapData(Cursor c) {
        this.data = c;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.getCount();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_category, parent, false);

        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        data.moveToPosition(position);

        String title = data.getString(data.getColumnIndex(VocabContract.Category.COLUMN_NAME));
        final String localizedTitle = findTitleForString(title);
        holder.title.setText(localizedTitle);
        holder.imageView.setImageDrawable(findDrawableForTitle(title));

        holder.buttonStartTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final long categoryId = data.getLong(data.getColumnIndex(VocabContract.Category._ID));

        View.OnClickListener showWordsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListWordActivity.class);
                intent.putExtra(ListWordActivity.EXTRA_CATEGORY_ID, categoryId);
                intent.putExtra(ListWordActivity.EXTRA_CATEGORY_NAME, localizedTitle);
                context.startActivity(intent);
            }

            ;
        };
        holder.buttonShowWords.setOnClickListener(showWordsClickListener);
        holder.imageView.setOnClickListener(showWordsClickListener);
    }

    private String findTitleForString(String title) {
        switch (title) {
            case "breakfast":
                return context.getString(R.string.breakfast);
            case "simple conversation":
                return context.getString(R.string.conversation);
            case "shopping":
                return context.getString(R.string.shopping);
            case "traveling":
                return context.getString(R.string.travel);
            case "other":
                return "Other";
            default:
                return title;
        }
    }

    private Drawable findDrawableForTitle(String title) {
        /**
         *  "('breakfast')," +
         "('simple conversation')," +
         "('shopping')," +
         "('traveling');"
         */
        switch (title) {
            case "breakfast":
                return context.getResources().getDrawable(R.drawable.img_breakfast);
            case "simple conversation":
                return context.getResources().getDrawable(R.drawable.img_conversation);
            case "shopping":
                return context.getResources().getDrawable(R.drawable.img_shopping);
            case "traveling":
                return context.getResources().getDrawable(R.drawable.img_travel);
            default:
                return context.getResources().getDrawable(R.drawable.img_default);

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView buttonShowWords;
        TextView buttonStartTraining;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_category);
            title = itemView.findViewById(R.id.tv_cat_title);
            buttonShowWords = itemView.findViewById(R.id.show_words);
            buttonStartTraining = itemView.findViewById(R.id.start_training);
        }
    }
}
