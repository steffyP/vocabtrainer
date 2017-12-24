package com.vocabtrainer.project.vocabtrainer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vocabtrainer.project.vocabtrainer.AddWordActivity;
import com.vocabtrainer.project.vocabtrainer.R;
import com.vocabtrainer.project.vocabtrainer.database.VocabContract;

import java.util.HashSet;
import java.util.List;

/**
 * Created by stefanie on 20.12.17.
 */

public class ScanWordAdapter extends RecyclerView.Adapter<ScanWordAdapter.ViewHolder>{

    private final Context context;
    private List<String> data;

    public ScanWordAdapter(Context context) {
        this.context = context;
    }

    public void swapData(List<String> words){
        this.data = words;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount(){
        if(data == null) return 0;
        return data.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_scan, parent, false);

        ScanWordAdapter.ViewHolder viewHolder = new ScanWordAdapter.ViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String detectedText = data.get(position);
        holder.text.setText(detectedText);



        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddWordActivity.class);
                intent.putExtra(AddWordActivity.EXTRA_ITEM_GERMAN, detectedText);

                context.startActivity(intent);
            }
        });
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
