package com.example.personalitytest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalitytest.models.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QuizLove extends AppCompatActivity {
    private Button buttonyes;
    private Button buttonno;
    private ImageView homebutton;
    private TextView questionTV, indexView, questionIndexView;
    private int qCounter = 1, quizSize;
    private ArrayList<Question> loveQuizVar = new ArrayList<Question>();
    private ArrayList<Question> loveQuizAns = new ArrayList<Question>();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizlove);

        buttonyes = (Button) findViewById(R.id.buttonyes);
        buttonyes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                for(int z=0; z<loveQuizVar.size(); z++){
                    if(loveQuizVar.get(z).getQnId() == qCounter && loveQuizVar.get(z).getAnswer()){
                        loveQuizAns.add(loveQuizVar.get(z));
                    }
                }
                qCounter=qCounter+1;
                if(qCounter>quizSize){
                    toresultloveactivity();
                }else{
                    toNextQuestion();
                }
            }
        });
        buttonno = (Button) findViewById(R.id.buttonno);
        buttonno.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                {
                    for(int z=0; z<loveQuizVar.size(); z++){
                        if(loveQuizVar.get(z).getQnId() == qCounter && loveQuizVar.get(z).getAnswer()){
                            loveQuizAns.add(loveQuizVar.get(z));
                        }
                    }
                    qCounter=qCounter+1;
                    if(qCounter>quizSize){
                        toresultloveactivity();
                    }else{
                        toNextQuestion();
                    }
                }
            }
        });
        homebutton = (ImageView) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tohomeactivity();
            }
        });

        questionTV = (TextView) findViewById(R.id.textView41);
        questionTV.setText("Question "+qCounter);
        //Log.d("Total Num of Qns: ", String.valueOf(totalQuestions));

        questionIndexView = (TextView) findViewById(R.id.textView49);

        indexView = (TextView) findViewById(R.id.textView52);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Log.d("Intent_check", String.valueOf(intent.getExtras()));
        dialog = ProgressDialog.show(QuizLove.this, "",
                "Loading. Please wait...", true);
        if(intent.getExtras().containsKey("Ans_list")){
            loveQuizVar= intent.getParcelableArrayListExtra("Question_list");
            loveQuizAns= intent.getParcelableArrayListExtra("Ans_list");
            qCounter = intent.getIntExtra("Question_Counter", 1);
            quizSize = intent.getIntExtra("Quiz_size", 1);
            setUpPage();
        }else{
            Services.getAllQuestions(QuizLove.this, new Services.QuestionCallback() {
                @Override
                public void onSuccess(ArrayList<Question> result) {
                    qCounter = 1;
                    for(int i=0;i<result.size();i++){
                        if(result.get(i).getQnCategory().equals("Love")){
                            loveQuizVar.add(result.get(i));
                        }
                    }
                    quizSize = loveQuizVar.size() / 2;
                    setUpPage();
                }
            });
        }

        for(int i=0;i<loveQuizAns.size();i++){
            Log.d("Love_Answer", String.valueOf(loveQuizAns.get(i).getAnswer()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void toresultloveactivity() {
        Intent intent = new Intent(this, ResultLove.class);
        intent.putExtra("Result", calculateResult());
        startActivity(intent);
    }
    public void tohomeactivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    public void toNextQuestion(){
        Intent intent = new Intent(this, QuizLove.class);
        intent.putExtra("Question_list", loveQuizVar);
        intent.putExtra("Ans_list", loveQuizAns);
        intent.putExtra("Question_Counter", qCounter);
        intent.putExtra("Quiz_size", quizSize);
        Log.d("Quiz Size", String.valueOf(quizSize));
        startActivity(intent);
        finish();
    }
    public void setUpPage(){
        for(int x=0;x<loveQuizVar.size();x++){
            Log.d("Question_List","QID:"+loveQuizVar.get(x).getQnId()+" Question:"+loveQuizVar.get(x).getQns()+" Ans:"+loveQuizVar.get(x).getAnswer());
            if(loveQuizVar.get(x).getQnId()==qCounter){
                questionTV.setText(loveQuizVar.get(x).getQns());
            }
        }
        questionIndexView.setText("Question "+qCounter);
        indexView.setText(qCounter+"/"+quizSize);
        dialog.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calculateResult(){
        HashMap<String, Integer> resultMap = new HashMap<>();
        for(int i=0; i<loveQuizAns.size(); i++){
            if(!resultMap.containsKey(loveQuizAns.get(i).getTraits())){
                resultMap.put(loveQuizAns.get(i).getTraits(), 0);
            }else{
                resultMap.merge(loveQuizAns.get(i).getTraits(), 1, Integer::sum);
            }
        }

        int maxValueInMap=(Collections.max(resultMap.values()));  // This will return max value in the HashMap
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {  // Iterate through HashMap
            if (entry.getValue()==maxValueInMap) {
                return entry.getKey();
            }
        }

        return null;
    }


}
