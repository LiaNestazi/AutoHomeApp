package com.example.autohomeapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.autohomeapp.ModelsActivity
import com.example.autohomeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.models.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, ModelsActivity::class.java)
            intent.putExtra("ref", "Models")
            startActivity(intent)
        })
        binding.categories.setOnClickListener(View.OnClickListener {
            var intent = Intent(context, ModelsActivity::class.java)
            intent.putExtra("ref", "Categories")
            startActivity(intent)
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}