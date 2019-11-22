package com.example.assignmenta2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuestionAnswerActivity extends AppCompatActivity {

    private TextView tv_question, tvCountDown, earnings;
    private EditText tv_answer;
    private Button btn_go;
    private String JSON_URL = "http://jservice.io/api/clues?&";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private String categoryID = "1";
    private String value = "100";
    private ArrayList<QuestionItem> arrayList = new ArrayList<>();
    private int questionCounter = 0;
    private int questionCountTotal;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean answered;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private double score = 0;
    private long backPressedTime;
    MediaPlayer mMediaPlayer;
    private QuestionItem currentQuestion;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    static boolean active = false;
    boolean isBackFromGoogle = false;

    //score is saved through sharedPreferences and maintained as user plays game
    //https://stackoverflow.com/questions/35069461/how-to-save-score-to-sharedpreferences-then-update-it
    //https://www.youtube.com/watch?v=mdKoebwwwtM
    @Override
    protected void onResume() {
        super.onResume();
        earnings.setText("$" + prefs.getLong("earnings", 0));
        if (isBackFromGoogle) {
            showNextQuestion();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_ans);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Game Play");

        earnings = findViewById(R.id.earnings);
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        score = prefs.getLong("earnings", 0);
        editor = prefs.edit();
        tv_question = findViewById(R.id.tv_question);
        tv_question.setText("Fetching questions...");

        tvCountDown = findViewById(R.id.tvCountDown);
        tv_answer = findViewById(R.id.tv_answer);
        btn_go = findViewById(R.id.btn_go);
        categoryID = getIntent().getExtras().getString("categoryID");
        value = getIntent().getExtras().getString("value");
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_answer.getText().toString().isEmpty()) {
                    Toast.makeText(QuestionAnswerActivity.this, "Please provide your answer.", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                }
            }
        });

        JSON_URL = JSON_URL + "category=" + categoryID + "&value=" + value;

        Log.e("URL =>", "URL=>" + JSON_URL);
        getQuestionsAPI();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.game_sound);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    private void getQuestionsAPI() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            QuestionItem questionItem = new QuestionItem();
                            questionItem.id = object.getString("id");
                            questionItem.question = object.getString("question");
                            questionItem.airdate = object.getString("airdate");
                            questionItem.value = object.getString("value");
                            questionItem.answer = object.getString("answer");
                            arrayList.add(questionItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (arrayList.size() > 0) {
                        Collections.shuffle(arrayList);
                        tv_answer.setVisibility(View.VISIBLE);
                        btn_go.setVisibility(View.VISIBLE);
                        questionCountTotal = arrayList.size();
                        questionCounter = 0;
                        showNextQuestion();
                        startCountDown();
                    }
                } else {
                    Toast.makeText(QuestionAnswerActivity.this, "No questions found. Please choose another option.", Toast.LENGTH_SHORT).show();
                    finishQuiz();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue = Volley.newRequestQueue(QuestionAnswerActivity.this);
        requestQueue.add(request);

    }

    //countdown timer
    //timer also indicates in red text when coming close to end of time period

    //https://stackoverflow.com/questions/14393423/how-to-make-a-countdown-timer-in-java
    //https://www.youtube.com/watch?v=bLUXfWkZMD8

    private void showNextQuestion() {
        tv_answer.setText("");
        if (questionCounter < questionCountTotal) {
            currentQuestion = arrayList.get(questionCounter);
            tv_question.setText(currentQuestion.question);
            questionCounter++;
            answered = false;
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tvCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            tvCountDown.setTextColor(Color.RED);
        } else {
            tvCountDown.setTextColor(Color.WHITE);
        }
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
//        if(txtanswer.getText().toString().isEmpty()){
//        }else{
        if (tv_answer.getText().toString().trim().equalsIgnoreCase(currentQuestion.answer)) {
            score = score + Double.parseDouble(currentQuestion.value);
            editor.putLong("earnings", (long) score);
            editor.commit();
            earnings.setText("$" + score);
            showSolution(true);
        } else {
            showSolution(false);
        }
    }

    private void showSolution(boolean isUserCorrect) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (isUserCorrect)
            builder.setMessage("You are correct! Nice job :)");
        else
            builder.setMessage("Oops! The correct answer is actually " + currentQuestion.answer);
        builder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showNextQuestion();
                    }
                }).setNegativeButton("View more!", new DialogInterface.OnClickListener() {

            //implicit intent to allow users to search what the answer means (not! to cheat)
            public void onClick(DialogInterface dialog, int id) {
                isBackFromGoogle = true;
                Intent implicit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + currentQuestion.question));
                startActivity(implicit);
            }
        });
        AlertDialog alert = builder.create();
        if (active)
            alert.show();
    }


    private void finishQuiz() {
        if (mMediaPlayer != null)
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishQuiz();
                break;
        }
        return true;
    }
}