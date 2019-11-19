package com.example.assignmenta2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.TriviaViewHolder> {

    public static List<Trivia> triviaToAdapt;

    public void setData(List<Trivia> triviaToAdapt) {
        this.triviaToAdapt = triviaToAdapt;
    }

    @NonNull
    @Override
    public TriviaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trivia, parent, false);

        TriviaViewHolder triviaViewHolder = new TriviaViewHolder(view);
        return triviaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TriviaViewHolder holder, int position) {
        final Trivia triviaAtPosition = triviaToAdapt.get(position);
        final Context context = holder.view.getContext();
        holder.bind(triviaAtPosition);
    }

    @Override
    public int getItemCount() {
        return triviaToAdapt.size();
    }

    public static class TriviaViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView questionText;
        public TextView answert;
        public Button answerb;

        public TriviaViewHolder(View v){
            super(v);
            view = v;
            questionText = v.findViewById(R.id.questionText);
            answert = v.findViewById(R.id.answertext);
            answerb = v.findViewById(R.id.answerbutton);
        }

        public void bind(final Trivia trivia) {

            questionText.setText(trivia.getQuestion());

            answerb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s1 = trivia.getAnswer();
                    String replaceString = s1.replace("<i>","");
                    String s2 = replaceString;
                    String replaceString3 = s2.replace("</i>", "");
                    answert.setText(replaceString3);
                }
            });
        }

    }
}
