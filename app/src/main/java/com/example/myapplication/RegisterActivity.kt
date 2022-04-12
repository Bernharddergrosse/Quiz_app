package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.RegisterUserBinding
import com.example.myapplication.model.User
import com.example.myapplication.model.UserToLogin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterUserBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterUserBinding.inflate(layoutInflater);
        setContentView(binding.root);
    }


    fun registerUser(view: View) {
        GlobalScope.launch {
            try {
                var allUsers: List<User> = MainActivity.API.users.listAll();
                var userToRegister: UserToLogin = UserToLogin(
                    binding.txtUserNameRegister.text.toString(),
                    binding.txtPasswordRegister.text.toString()
                )
                var duplicateUserFound = false;
                for (user: User in allUsers) {
                    if (user.username == userToRegister.username) {
                        duplicateUserFound = true;
                    }
                }
                if (!duplicateUserFound) {
                    MainActivity.API.users.create(userToRegister)
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "User ${userToRegister.username} registered",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    delay(1000);
                    val intent: Intent = Intent(
                        this@RegisterActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent);
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "User already exists",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            } catch (ex: Throwable) {
                Log.e("Register Error", ex.toString())
            }
        }
    }
}