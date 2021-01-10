package com.example.mynote.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.MainActivity;
import com.example.mynote.R;
import com.example.mynote.models.Note;
import com.example.mynote.noteEditor;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Note> Notes;


    public RecyclerViewAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.Notes = notes;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Note nv = Notes.get(position);
        holder.noteTitle.setText(nv.getNoteTitle());
        holder.dateCreated.setText(nv.getNoteDateTime());
        String sb[];
        String info = nv.getNotecontent();
        sb=info.split("\n");
        if(sb.length>3) {
            info = "\"" + sb[0] + "\n " + sb[1] + "\n " + sb[2] + "...\"";
            }
            else {
            if(info.length()>20) {
                info = info.substring(0,20);
                info+="...";
            }
            info = "\"" + info + "\"";
        }
            holder.contentinfo.setText(info);

    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView noteTitle;
        public TextView dateCreated;
        public TextView contentinfo;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            dateCreated = itemView.findViewById(R.id.dateTime);
            contentinfo = itemView.findViewById(R.id.contentinfo);

        }

        @Override
        public void onClick(View v) {
            Note nv = Notes.get(this.getAdapterPosition());
            Intent intent = new Intent(context, noteEditor.class);
            intent.putExtra("title", nv.getNoteTitle());
            intent.putExtra("datetime", nv.getNoteDateTime());
            intent.putExtra("content",nv.getNotecontent());
            intent.putExtra("id",nv.getNoteId());

            context.startActivity(intent);

        }


    }

}
