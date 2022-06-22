package com.yashjain.javaquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yashjain.javaquiz.data.MainAPI;
import com.yashjain.javaquiz.databinding.ActivityMainBinding;
import com.yashjain.javaquiz.model.Question;
import com.yashjain.javaquiz.model.QuestionsList;

import java.util.List;

import io.requestly.android.core.Requestly;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Call<QuestionsList> list;
    private List<Question> data_list;
    ActivityMainBinding binding;
    private String selectedOption="";
    private String correctOption="";
    private int index=0;
    private int score=0;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();

        showDialog();


        binding.option1.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.option2.setChecked(false);
                binding.option3.setChecked(false);
                binding.option4.setChecked(false);
                selectedOption="A";
            }else{
                selectedOption="";
            }
        });

        binding.option2.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.option1.setChecked(false);
                binding.option3.setChecked(false);
                binding.option4.setChecked(false);
                selectedOption="B";
            }else{
                selectedOption="";
            }
        });

        binding.option3.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.option1.setChecked(false);
                binding.option2.setChecked(false);
                binding.option4.setChecked(false);
                selectedOption="C";
            }else{
                selectedOption="";
            }
        });

        binding.option4.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.option1.setChecked(false);
                binding.option2.setChecked(false);
                binding.option3.setChecked(false);
                selectedOption="D";
            }else{
                selectedOption="";
            }
        });

        binding.nextBtn.setOnClickListener(view -> {
              if(selectedOption.isEmpty()){
                  Toast.makeText(this, "Please Select Option", Toast.LENGTH_SHORT).show();
              }else{
                  Log.d("option", "Selected: "+selectedOption+"\nCorrect: "+correctOption);
                  if(selectedOption.equals(correctOption)){
                      ++score;
                      Toast.makeText(this, "Great work!", Toast.LENGTH_SHORT).show();
                  }else{
                      Toast.makeText(this, "Oops! Incorrect choice", Toast.LENGTH_SHORT).show();

                  }
                  selectedOption="";
                  nextQuestion();
              }
        });

        binding.resetBtn.setOnClickListener(view -> reset());

    }

    public void getData(){
        list= MainAPI.get(getApplicationContext()).getQuestionsList();
        list.enqueue(new Callback<QuestionsList>() {
            @Override
            public void onResponse(Call<QuestionsList> call, Response<QuestionsList> response) {
                dialog.dismiss();
                QuestionsList list=response.body();
                data_list= list.getQuestions();
                if(!data_list.isEmpty()){
                    binding.questionNumber.setText("Question 1");
                    binding.question.setText(data_list.get(0).getQuestion());
                    binding.option1.setText(data_list.get(0).getA());
                    binding.option2.setText(data_list.get(0).getB());
                    binding.option3.setText(data_list.get(0).getC());
                    binding.option4.setText(data_list.get(0).getD());
                    correctOption=data_list.get(0).getCorrectOption();
                }
                //Setting up adapter
                Log.d("MainApiCall", "onResponse: "+data_list.get(0).getA());
            }
            @Override
            public void onFailure(Call<QuestionsList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                Log.d("MainApiCall", ""+t);

            }
        });

    }

    private void nextQuestion(){
        ++index;
        if(index!=data_list.size()){
            uncheckAllOption();
            int ind=index+1;
            binding.questionNumber.setText("Question "+ind);
            binding.question.setText(data_list.get(index).getQuestion());
            binding.option1.setText(data_list.get(index).getA());
            binding.option2.setText(data_list.get(index).getB());
            binding.option3.setText(data_list.get(index).getC());
            binding.option4.setText(data_list.get(index).getD());
            correctOption=data_list.get(index).getCorrectOption();
        }else{
            binding.score.setVisibility(View.VISIBLE);
            binding.score.setText("Score: "+score+"/10");
            binding.resetBtn.setVisibility(View.VISIBLE);
            binding.nextBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void uncheckAllOption(){
        binding.option1.setChecked(false);
        binding.option2.setChecked(false);
        binding.option3.setChecked(false);
        binding.option4.setChecked(false);
    }

    private void reset() {
        binding.questionNumber.setText("Question 1");
        binding.question.setText(data_list.get(0).getQuestion());
        binding.option1.setText(data_list.get(0).getA());
        binding.option2.setText(data_list.get(0).getB());
        binding.option3.setText(data_list.get(0).getC());
        binding.option4.setText(data_list.get(0).getD());
        correctOption=data_list.get(0).getCorrectOption();
        selectedOption="";
        uncheckAllOption();
        binding.nextBtn.setVisibility(View.VISIBLE);
        binding.score.setVisibility(View.GONE);
        score=0;
        binding.resetBtn.setVisibility(View.GONE);
        index=0;
    }

    private void showDialog() {
        dialog=new ProgressDialog(MainActivity.this);
        dialog.setCancelable(false);//you cannot cancel it by pressing back button
        dialog.setMessage("Please Wait ...");
        dialog.setProgress(0);//initially progress is 0
        dialog.setMax(100);//sets the maximum value 100
        dialog.show();
    }
}