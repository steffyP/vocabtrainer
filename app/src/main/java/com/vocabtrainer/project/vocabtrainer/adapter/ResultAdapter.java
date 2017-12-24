package com.vocabtrainer.project.vocabtrainer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.api.model.Entry;
import com.vocabtrainer.project.vocabtrainer.api.model.LexicalEntry;
import com.vocabtrainer.project.vocabtrainer.api.model.Sense;

import java.util.List;

/**
 * Created by stefanie on 23.12.17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Context context;
    private List<LexicalEntry> resultList;

    public ResultAdapter(Context context, List<LexicalEntry> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_result, parent, false);

        ResultAdapter.ViewHolder viewHolder = new ResultAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LexicalEntry result = resultList.get(position);

        String text = result.getText() + " (" + result.getLanguage() + "), " + result.getLexicalCategory();

        holder.text.setText(text);

        String translation = "";
        for (Entry entry : result.getEntries()) {
            for (Sense sense : entry.getSenses()) {
                translation += sense.toHtmlString();
            }
        }
        holder.translation.setText(Html.fromHtml(translation));

    }

    @Override
    public int getItemCount() {
        if (resultList == null) return 0;
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        TextView translation;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_text);
            translation = itemView.findViewById(R.id.translation);
        }
    }
}
