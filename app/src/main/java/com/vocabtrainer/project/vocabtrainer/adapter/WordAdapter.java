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

import com.vocabtrainer.project.vocabtrainer.AddWordActivity;
import com.vocabtrainer.project.vocabtrainer.ListWordActivity;
import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

/**
 * Created by stefanie on 20.12.17.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    private final Context context;
    private Cursor data;

    public WordAdapter(Context context) {
        this.context = context;
    }

    public void swapData(Cursor c){
        this.data = c;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount(){
        if(data == null) return 0;
        return data.getCount();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_word, parent, false);

        WordAdapter.ViewHolder viewHolder = new WordAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0){
            return TYPE_ONE;
        }
        return TYPE_TWO;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        data.moveToPosition(position);

        if(getItemViewType(position) == TYPE_ONE){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        final String german = data.getString(data.getColumnIndex(VocabContract.Word.COLUMN_GERMAN));
        final String english = data.getString(data.getColumnIndex(VocabContract.Word.COLUMN_ENGLISH));
        final long id = data.getLong(data.getColumnIndex(VocabContract.Word._ID));
        final long categoryId = data.getLong(data.getColumnIndex("categoryId"));

        holder.german.setText(german);
        holder.english.setText(english);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddWordActivity.class);
                intent.putExtra(AddWordActivity.EXTRA_ITEM_ID, id);
                intent.putExtra(AddWordActivity.EXTRA_ITEM_GERMAN, german);
                intent.putExtra(AddWordActivity.EXTRA_ITEM_ENGLSH, english);
                intent.putExtra(AddWordActivity.EXTRA_ITEM_CATEGORY, categoryId);

                context.startActivity(intent);
            }
        });
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView german;
        TextView english;

        public ViewHolder(View itemView) {
            super(itemView);
            german = itemView.findViewById(R.id.german);
            english = itemView.findViewById(R.id.english);
        }
    }
}
