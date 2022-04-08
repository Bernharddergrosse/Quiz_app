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
import com.example.myapplication.services.UserService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
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
        const val host = "http://172.25.45.176:8080/api/v1/"
    }

    object API {
        var BASE_URL = MainActivity.host

        private val retrofit: Retrofit get() {
            val json = Gson();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(json))
                .build()
        }

        val users get() = retrofit.create(UserService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        performUserCrudServices();
    }

    fun logIn(view: View) {
        var username: String = findViewById<TextView>(R.id.txtUsername).text.toString();
        var password: String = findViewById<TextView>(R.id.txtPassword).text.toString();

        val user: UserToLogin = UserToLogin(username,password);
        GlobalScope.launch {
            try {
                var callLogin = API.users.login(user);
                runOnUiThread {
                    val toast = Toast.makeText(applicationContext, "Hello " + username + " and welcome to Quizy", Toast.LENGTH_LONG);

                    Log.i("Call login", callLogin.toString());
                    var loggedInUser: User = User(callLogin, user.username, user.password);
                    //loggedInUser = user;
                    //loggedInUser.setId(callLogin);
                    var gson: Gson = Gson();
                    var userToLogin = gson.toJson(loggedInUser);
                    Log.i("user to login", userToLogin.toString())
                    val intent: Intent = Intent(this@MainActivity, UserPageActivity::class.java).apply { putExtra("user",  userToLogin) };
                    startActivity(intent);
                }
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

    fun performUserCrudServices() {
        GlobalScope.launch {
            try {
                var allUsers = API.users.listAll().toString();
                //var user = API.users.get(1).toString();
                //var create = API.users.create(User(2,"crud created", "crud password created")).toString();
                //var update = API.users.update(2, User(2,"User Updated", "user password updated")).toString();
                //var delete = API.users.delete(2).toString();
                //var msg = "AllUsers: " + allUsers + "\n\n" + "SingleUser: " + user + "\n\n" + "Created user: " + create + "\n\n" + "updated: " + update + "\n\n" + "Deleted: " + delete
                Log.i("GetAll", allUsers)
                //Log.i("GetSingle", user)
                //Log.i("create", create)
                //Log.i("update", update)
                //Log.i("delete", delete)
                //runOnUiThread {
                  //  binding.txtUsers.text = msg;
                //}
            } catch (ex: Throwable) {
                Log.e("Error", ex.toString())
            }
        }
    }
}