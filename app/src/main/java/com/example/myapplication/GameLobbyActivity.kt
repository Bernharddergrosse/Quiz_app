package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.GameLobbyBinding
import com.example.myapplication.model.Score.Score
import com.example.myapplication.model.Score.ScoreToCreate
import com.example.myapplication.model.User
import com.example.myapplication.model.question.Question
import com.example.myapplication.services.GameService
import com.example.myapplication.services.QuestionService
import com.example.myapplication.services.ScoreService
import com.example.myapplication.services.UserService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class GameLobbyActivity: AppCompatActivity() {
    private lateinit var binding: GameLobbyBinding;

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GameLobbyBinding.inflate(layoutInflater);
        setContentView(binding.root);

        var gson: Gson = Gson();
        var strUserObj = intent.getStringExtra("user");
        var user = gson.fromJson(strUserObj, User::class.java)

        var timerBar: ProgressBar = findViewById(R.id.timerBar);
        timerBar.setProgress(0);

        var txtQuestion = findViewById<TextView>(R.id.txtQuestion);
        var btnAnswer1 = findViewById<Button>(R.id.btnAnswer1);
        var btnAnswer2 = findViewById<Button>(R.id.btnAnswer2);
        var btnAnswer3 = findViewById<Button>(R.id.btnAnswer3);
        var btnAnswer4 = findViewById<Button>(R.id.btnAnswer4);

        var txtQuestionNr = findViewById<TextView>(R.id.txtViewNrOfQuestion)

        GlobalScope.launch {
            try {
                /**
                 * Get all Questions
                 */
                var questions = MainActivity.API.questions.listAll().toMutableList();
                var questionsAsked: MutableList<Question> = mutableListOf();
                questions.shuffle();
                /**
                 * Populate questionsAsked with random questions
                 */
                for (i in 0..4) {
                  //  val questionId: Int = (0..questions.size).random();
                    questionsAsked.add(questions.get(i));
                }

                /**
                 * Main question loop, asks the number of questions in questionsAsked
                 * Change the question every 10 seconds
                 */
                var questionNr = 1;
                var points = 0;
                for (question: Question in questionsAsked) {
                    txtQuestionNr.text = "Question ${questionNr}/${questionsAsked.size}"
                    Log.i("loop", "next question")
                    Log.i("question", question.toString())
                    txtQuestion.text = question.questionText;
                    var answers: MutableList<String> = mutableListOf(question.answer1, question.answer2, question.answer3, question.answer4)
                    answers.shuffle()

                    btnAnswer1.text = answers.get(0)
                    btnAnswer2.text = answers.get(1);
                    btnAnswer3.text = answers.get(2);
                    btnAnswer4.text = answers.get(3);

                    /**
                     * Enable all buttons when new question is asked
                     */
                    runOnUiThread {
                        btnAnswer1.isEnabled = true;
                        btnAnswer2.isEnabled = true;
                        btnAnswer3.isEnabled = true;
                        btnAnswer4.isEnabled = true;

                        btnAnswer1.setBackgroundColor(Color.parseColor("#FF3700B3"))
                        btnAnswer2.setBackgroundColor(Color.parseColor("#FF3700B3"))
                        btnAnswer3.setBackgroundColor(Color.parseColor("#FF3700B3"))
                        btnAnswer4.setBackgroundColor(Color.parseColor("#FF3700B3"))

                        btnAnswer1.setTextColor(Color.parseColor("#FFFFFFFF"));
                        btnAnswer2.setTextColor(Color.parseColor("#FFFFFFFF"));
                        btnAnswer3.setTextColor(Color.parseColor("#FFFFFFFF"));
                        btnAnswer4.setTextColor(Color.parseColor("#FFFFFFFF"));
                    }

                    btnAnswer1.setOnClickListener {
                        if (btnAnswer1.text.toString() == question.correctAnswer) {
                            points += 20;
                            btnAnswer1.setBackgroundColor(Color.GREEN)
                            btnAnswer1.setTextColor(Color.parseColor("#FF000000"));
                            Log.i("Correct", "correct")
                        } else {
                            btnAnswer1.setBackgroundColor(Color.RED)
                            btnAnswer1.setTextColor(Color.parseColor("#FFFFFFFF"));
                            Log.i("Wrong", "rong")
                        }
                        btnAnswer1.isEnabled = false;
                        btnAnswer2.isEnabled = false;
                        btnAnswer3.isEnabled = false;
                        btnAnswer4.isEnabled = false;

                        Log.i("a1", btnAnswer1.text.toString())
                    }

                    btnAnswer2.setOnClickListener {
                        if (btnAnswer2.text.toString() == question.correctAnswer) {
                            points += 20;
                            btnAnswer2.setBackgroundColor(Color.GREEN)
                            btnAnswer2.setTextColor(Color.parseColor("#FF000000"));
                            Log.i("Correct", "correct")
                        } else {
                            btnAnswer2.setBackgroundColor(Color.RED)
                            btnAnswer2.setTextColor(Color.parseColor("#FFFFFFFF"));
                            Log.i("Wrong", "rong")
                        }
                        btnAnswer1.isEnabled = false;
                        btnAnswer2.isEnabled = false;
                        btnAnswer3.isEnabled = false;
                        btnAnswer4.isEnabled = false;


                        Log.i("a2", btnAnswer2.text.toString())
                    }

                    btnAnswer3.setOnClickListener {
                        points += 20
                        if (btnAnswer3.text.toString() == question.correctAnswer) {
                            btnAnswer3.setBackgroundColor(Color.GREEN)
                            btnAnswer3.setTextColor(Color.parseColor("#FF000000"));
                            Log.i("Correct", "correct")
                        } else {
                            btnAnswer3.setBackgroundColor(Color.RED)
                            btnAnswer3.setTextColor(Color.parseColor("#FFFFFFFF"));
                            Log.i("Wrong", "rong")
                        }
                        btnAnswer1.isEnabled = false;
                        btnAnswer2.isEnabled = false;
                        btnAnswer3.isEnabled = false;
                        btnAnswer4.isEnabled = false;

                        Log.i("a3", btnAnswer3.text.toString())
                    }

                    btnAnswer4.setOnClickListener {
                        if (btnAnswer4.text.toString() == question.correctAnswer) {
                            points += 20;
                            btnAnswer4.setBackgroundColor(Color.GREEN)
                            btnAnswer4.setTextColor(Color.parseColor("#FF000000"));
                            Log.i("Correct", "correct")
                        } else {
                            btnAnswer4.setBackgroundColor(Color.RED)
                            btnAnswer4.setTextColor(Color.parseColor("#FFFFFFFF"));
                            Log.i("Wrong", "rong")
                        }
                        btnAnswer1.isEnabled = false;
                        btnAnswer2.isEnabled = false;
                        btnAnswer3.isEnabled = false;
                        btnAnswer4.isEnabled = false;

                        Log.i("a4", btnAnswer4.text.toString())
                    }
                    /**
                     * Update loading bar
                     */
                    for (i in 0..99/2) {
                        runOnUiThread {
                            timerBar.setProgress((i + 1) * 2, true);
                        }
                        delay(100);
                    }
                    questionNr++;
                }


                /**
                 * Post score and end game
                 */
                MainActivity.API.scores.create(ScoreToCreate(points, LocalDate.now().toString(), LocalTime.now(ZoneId.of("Europe/Berlin")).toString(), user));
                var x = 3;
                for(i in 0..3) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Your final score was ${points}, going home in ${x}", Toast.LENGTH_SHORT).show();
                        x--;
                    }
                    delay(1000);
                }
                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent);
                Log.i("all questions asked", "loop done")
            } catch (ex: Throwable) {
                Log.e("Error gameLobby", ex.toString());
            }
        }
    }
}