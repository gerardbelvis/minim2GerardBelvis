package com.example.minim2_gerardbelvis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.minim2_gerardbelvis.models.*;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<Repositories> reposList;
    private Context context;

    public RecyclerViewAdapter(Context ct, List<Repositories> repos){
        context = ct;
        reposList = repos;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.repo.setText(reposList.get(position).getRepoName());
        holder.code.setText(reposList.get(position).getLanguage());


    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView code;
        TextView repo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.codeText);
            repo = itemView.findViewById(R.id.userText);

        }
    }
}
