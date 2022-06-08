package com.example.autohomeapp.ui.notifications

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.autohomeapp.ChangePassActivity
import com.example.autohomeapp.FavoritesActivity
import com.example.autohomeapp.R
import com.example.autohomeapp.RegisterActivity
import com.example.autohomeapp.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var sharedPref: SharedPreferences? = null
    private var viewModel: NotificationsViewModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mAuth = FirebaseAuth.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sharedPref = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        viewModel = NotificationsViewModel(context, container)
        var email = String()
        var password = String()
        if (sharedPref != null) {
            email = sharedPref!!.getString("email", "").toString()
            password = sharedPref!!.getString("password", "").toString()
        }
        Log.d("newtag", email+" "+password)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        if (!email.equals("") &&!password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.signInLayout.visibility = View.GONE
                    binding.signLabel.visibility = View.GONE
                    binding.register.visibility = View.GONE
                    binding.signedLabel.text =
                        "Добро пожаловать, ".plus(mAuth.currentUser?.displayName)
                    binding.signedLabel.visibility = View.VISIBLE
                    binding.signedInLayout.visibility = View.VISIBLE
                }
            }
        }
        val root: View = binding.root

        binding.register.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        })
        binding.signIn.setOnClickListener(View.OnClickListener {
            signIn()
        })
        binding.signOut.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            binding.emailEdit.text.clear()
            binding.passEdit.text.clear()
            binding.signedLabel.visibility = View.GONE
            binding.signedInLayout.visibility = View.GONE
            binding.signInLayout.visibility = View.VISIBLE
            binding.signLabel.visibility = View.VISIBLE
            binding.register.visibility = View.VISIBLE
            if (sharedPref != null) {
                with(sharedPref!!.edit()) {
                    clear()
                    apply()
                }
            }
        })
        binding.favorites.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, FavoritesActivity::class.java))
        })

        binding.changePass.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ChangePassActivity::class.java))
        })
        return root
    }

    private fun signIn(){
        var email = binding.emailEdit.text.toString().trim()
        var password = binding.passEdit.text.toString().trim()
        if (email.isEmpty()) {
            binding.emailEdit.error = "Введите email!"
            binding.emailEdit.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEdit.error = "Пожалуйста, введите корректный email!"
            binding.emailEdit.requestFocus()
            return
        }
        if (password.isEmpty()) {
            binding.passEdit.error = "Введите пароль!"
            binding.passEdit.requestFocus()
            return
        }
        if (password.length < 6) {
            binding.passEdit.error = "Минимальная длина пароля - 6 символов!"
            binding.passEdit.requestFocus()
            return
        }
        if (!isOnline(context)) {
            Toast.makeText(context, "Нет подключения к интернету", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                binding.signInLayout.visibility = View.GONE
                binding.signLabel.visibility = View.GONE
                binding.register.visibility = View.GONE
                binding.signedLabel.text = "Добро пожаловать, ".plus(mAuth.currentUser?.displayName)
                binding.signedLabel.visibility = View.VISIBLE
                binding.signedInLayout.visibility = View.VISIBLE

                if (sharedPref != null) {
                    with(sharedPref!!.edit()) {
                        Log.d("newtag", email + " " + password)
                        putString("email", email)
                        putString("password", password)
                        apply()
                    }
                } else{
                    Log.d("newtag", "sharedpref is null")
                }

            } else{
                Toast.makeText(context, "Неверная почта или пароль.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun isOnline(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting) {
            return true
        }
        return false
    }

}