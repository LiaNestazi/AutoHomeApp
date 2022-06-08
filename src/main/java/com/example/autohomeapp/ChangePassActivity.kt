package com.example.autohomeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChangePassActivity : AppCompatActivity() {
    private lateinit var oldPass: EditText
    private lateinit var newPass: EditText
    private lateinit var confirmPass: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var change: Button
    private lateinit var back: ImageButton
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)
        supportActionBar?.hide()
        init()
        change.setOnClickListener(View.OnClickListener {
            changePass()
        })
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
    private fun changePass(){
        var old_pass = this.oldPass.text.toString().trim()
        var new_pass = this.newPass.text.toString().trim()
        var confirm_pass = this.confirmPass.text.toString().trim()

        if (old_pass.isEmpty()){
            this.oldPass.error = "Введите email!"
            this.oldPass.requestFocus()
            return
        }
        if (new_pass.isEmpty()) {
            this.newPass.error = "Введите логин!"
            this.newPass.requestFocus()
            return
        }
        if (confirm_pass.isEmpty()) {
            this.confirmPass.error = "Введите пароль!"
            this.confirmPass.requestFocus()
            return
        }
        if (new_pass.length < 6) {
            this.newPass.error = "Минимальная длина пароля - 6 символов!"
            this.newPass.requestFocus()
            return
        }
        if (new_pass.equals(old_pass)){
            this.newPass.error = "Пароли не могут совпадать!"
            this.newPass.requestFocus()
            return
        }
        if (confirm_pass.isEmpty()) {
            this.confirmPass.error = "Введите подтверждение пароля!"
            this.confirmPass.requestFocus()
            return
        }
        if (!confirm_pass.equals(new_pass)) {
            this.confirmPass.error = "Пароли не совпадают"
            this.confirmPass.requestFocus()
            return
        }
        progressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(mAuth.currentUser?.email.toString(), old_pass).addOnCompleteListener {
            if (it.isSuccessful) {
                mAuth.currentUser?.updatePassword(new_pass)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Пароль изменен успешно!", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        finish()
                    } else{
                        Toast.makeText(this, "Ошибка при смене пароля. Попробуйте еще раз!", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }
            } else{
                Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }

    }
    private fun init(){
        mAuth = FirebaseAuth.getInstance()
        oldPass = findViewById(R.id.old_pass_edit)
        newPass = findViewById(R.id.new_pass_edit)
        confirmPass = findViewById(R.id.confirm_pass_edit)
        progressBar = findViewById(R.id.progress_bar)
        change = findViewById(R.id.change)
        back = findViewById(R.id.back_button)

        database = FirebaseDatabase.getInstance()
    }
}