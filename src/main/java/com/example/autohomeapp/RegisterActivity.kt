package com.example.autohomeapp

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.autohomeapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log


class RegisterActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var register: Button
    private lateinit var back: ImageButton
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
        supportActionBar?.hide()
        register.setOnClickListener(View.OnClickListener {
            register()
        })
        back.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
    private fun register(){
        var email = this.email.text.toString().trim()
        var login = this.login.text.toString().trim()
        var password = this.password.text.toString().trim()
        var passwordConfirm = this.passwordConfirm.text.toString().trim()

        if (email.isEmpty()){
            this.email.error = "Введите email!"
            this.email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.error = "Пожалуйста, введите корректный email!"
            this.email.requestFocus()
            return
        }
        if (login.isEmpty()) {
            this.login.error = "Введите логин!"
            this.login.requestFocus()
            return
        }
        if (password.isEmpty()) {
            this.password.error = "Введите пароль!"
            this.password.requestFocus()
            return
        }
        if (password.length < 6) {
            this.password.error = "Минимальная длина пароля - 6 символов!"
            this.password.requestFocus()
            return
        }
        if (passwordConfirm.isEmpty()) {
            this.passwordConfirm.error = "Введите подтверждение пароля!"
            this.passwordConfirm.requestFocus()
            return
        }
        if (!passwordConfirm.equals(password)) {
            this.passwordConfirm.error = "Пароли не совпадают"
            this.passwordConfirm.requestFocus()
            return
        }



        progressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                var user = User(email, login)
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(login).build()
                mAuth.currentUser?.updateProfile(profileUpdates)
                mAuth.currentUser?.let { it1 ->
                    database.getReference("Users")
                        .child(it1.uid)
                        .setValue(user).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(this, "Регистрация прошла успешно!", Toast.LENGTH_LONG).show()
                                progressBar.visibility = View.GONE
                                finish()
                            } else{
                                Toast.makeText(this, "Ошибка регистрации!", Toast.LENGTH_LONG).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                }
            } else{
                Toast.makeText(this, "Ошибка регистрации!", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun init(){
        mAuth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email_edit)
        login = findViewById(R.id.login_edit)
        password = findViewById(R.id.pass_edit)
        passwordConfirm = findViewById(R.id.pass_confirm_edit)
        progressBar = findViewById(R.id.progress_bar)
        register = findViewById(R.id.register)
        back = findViewById(R.id.back_button)

        database = FirebaseDatabase.getInstance()
    }
}