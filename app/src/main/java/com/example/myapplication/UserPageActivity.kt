package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.UserPageBinding
import com.example.myapplication.model.Game.Game
import com.example.myapplication.model.Game.GameToCreate
import com.example.myapplication.model.Score.Score
import com.example.myapplication.model.User
import com.example.myapplication.services.GameService
import com.example.myapplication.services.ScoreService
import com.example.myapplication.services.UserService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class UserPageActivity: AppCompatActivity() {
    private lateinit var binding: UserPageBinding;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        binding = UserPageBinding.inflate(layoutInflater);
        setContentView(binding.root);
        var scoreList = findViewById<ListView>(R.id.lvUsersScores);
        var gamesToJoinList = findViewById<ListView>(R.id.lvGamesToJoin);
        var btnCreateGameLobby = findViewById<Button>(R.id.btnCreateLobby);

        var gson: Gson = Gson();
        var strUserObj = intent.getStringExtra("user");
        var user: User = gson.fromJson(strUserObj, User::class.java);

        val txtBox = findViewById<TextView>(R.id.txtUserPageUsername).apply {
            text = user.username;
        }

        Log.i("user", user.toString());
        GlobalScope.launch {
            try {
                /**
                 * Populate listView with Scores from the Previous games
                 * for the logged in user
                 */
                var usersScores: List<Score> = MainActivity.API.scores.getForUser(user.id);
                val formattedScores: MutableList<String> = mutableListOf();
                var cnt = 1;
                for(userScore: Score in usersScores) {
                    formattedScores.add("" + cnt + ": Game on the ${userScore.date} at ${userScore.time} result: " + userScore.points);
                    cnt++;
                }
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this@UserPageActivity.baseContext, android.R.layout.simple_list_item_1, formattedScores);
                scoreList.adapter = arrayAdapter;
                Log.i("usersScores", usersScores.toString());


                /**
                 * Populate GamesToJoin list with games that only have 1 user
                 */
                var allUsers: List<User> = MainActivity.API.users.listAll();
                Log.i("All users", allUsers.toString());
                var gamesToJoin: List<Game> = MainActivity.API.games.getGamesOneUser();
                Log.i("Games one user", gamesToJoin.toString());
                val formattedGamesToJoin: MutableList<String> = mutableListOf();
                var cntGamesToJoin = 1;

                for(game: Game in gamesToJoin) {
                    Log.i("game", game.toString());
                    Log.i("user", user.toString());
                    if(game.user1.id != user.id) {
                        formattedGamesToJoin.add(
                            "" + cntGamesToJoin + ": Lobby von " + game.user1.username
                            + " erstellt am " + game.date + " um " + game.time + ""
                        );
                        cntGamesToJoin++;
                    }
                }
                Log.i("games to join", formattedGamesToJoin.toString());
                runOnUiThread {
                    val gamesToJoinAdapter: ArrayAdapter<String> = ArrayAdapter(
                        this@UserPageActivity.baseContext,
                        android.R.layout.simple_list_item_1,
                        formattedGamesToJoin
                    );
                    gamesToJoinList.adapter = gamesToJoinAdapter;
                }


                btnCreateGameLobby.setOnClickListener {
                    GlobalScope.launch {
                        MainActivity.API.games.create(
                            GameToCreate(
                                LocalDate.now().toString(),
                                LocalTime.now().toString(),
                                user,
                                null
                            )
                        )
                    }
                    createGameLobby(user);
                }
            } catch (ex: Throwable) {
                    Log.e("Error", ex.toString())
            }
        }
    }

    fun createGameLobby(user: User) {
        GlobalScope.launch {
            var gson: Gson = Gson();
            var user = gson.toJson(user);

            val intent: Intent = Intent(this@UserPageActivity, GameLobbyActivity::class.java).apply {
                putExtra("user", user)
            };
            startActivity(intent);
        }
    }
}
