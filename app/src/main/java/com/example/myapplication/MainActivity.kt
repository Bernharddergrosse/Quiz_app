package com.example.myapplication

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64InputStream
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UserToLogin
import com.example.myapplication.services.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var loggedInUser: User;

    companion object {
        const val host = "http://10.0.0.57:8080/api/v1/"
    }

    object API {
        var BASE_URL = MainActivity.host

        fun setHost(host: String) {
            BASE_URL = host;
        }

        private val retrofit: Retrofit
            get() {
                val json = Gson();

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(json))
                    .build()
            }

        val users get() = retrofit.create(UserService::class.java)
        val games get() = retrofit.create(GameService::class.java)
        val scores get() = retrofit.create(ScoreService::class.java)
        val questions get() = retrofit.create(QuestionService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun connectToBackend(view: View) {
        Log.i("connect", "connect")
        API.setHost(binding.txtHost.text.toString());
        GlobalScope.launch {
            try {
                var connectionTest = API.users.listAll();
                Log.i("connection", connectionTest.toString())
            } catch (ex: Throwable) {
                Log.e("Connection Error", ex.toString());
            }
        }
        binding.btnRegisterNewUser.isEnabled = true;
        binding.loginButton.isEnabled = true;
        Log.i("API host", API.BASE_URL);
    }

    fun openRegisterUser(view: View) {
        val intent: Intent = Intent(
            this@MainActivity,
            RegisterActivity::class.java
        )
        startActivity(intent);
    }

    fun logIn(view: View) {
        var username: String = findViewById<TextView>(R.id.txtUsername).text.toString();
        var password: String = findViewById<TextView>(R.id.txtPassword).text.toString();

        val user: UserToLogin = UserToLogin(username, password);
        GlobalScope.launch {
            try {
                var callLogin = API.users.login(user);
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Hello " + username + " and welcome to Quizer",
                        Toast.LENGTH_LONG
                    ).show();
                }

                Log.i("Call login", callLogin.toString());
                var loggedInUser: User = User(callLogin, user.username, user.password);

                var gson: Gson = Gson();
                var userToLogin = gson.toJson(loggedInUser);
                Log.i("user to login", userToLogin.toString())
                val intent: Intent = Intent(
                    this@MainActivity,
                    UserPageActivity::class.java
                ).apply { putExtra("user", userToLogin) };
                startActivity(intent);
            } catch (ex: HttpException) {
                runOnUiThread {
                    if (ex.code().equals(404)) {
                        Toast.makeText(applicationContext, "User not found", Toast.LENGTH_LONG)
                            .show();
                    } else if (ex.code().equals(400)) {
                        Toast.makeText(applicationContext, "Password Incorrect", Toast.LENGTH_LONG)
                            .show();
                    }
                }
                Log.e("HTTP error", ex.toString());
            }
        }
    }
}