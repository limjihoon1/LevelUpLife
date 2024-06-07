package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.FragmentSettingBinding




class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    var fild1: String? = "char1"
    var fild2: String? = "char2"
    var fild3: String? = "char3"
    var fild4: String? = "char4"
    var fild5: String? = "char5"
    var fild6: String? = "char6"
    var fild7: String? = "char7"
    var fild8: String? = "char8"
    var fild9: String? = "char9"
    var fild10: String? = "char10"
    var fild11: String? = "char11"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        binding.changeImage.setOnClickListener { select() }

        // Firebase 초기화
        FirebaseApp.initializeApp(requireContext())
        val db = FirebaseFirestore.getInstance()

        // Firestore에서 필드 값 가져오기
        db.collection("users").document("1").get().addOnSuccessListener { doc ->
            if (doc != null && doc.exists()) {
                fild1 = getFieldAsString(doc, fild1)
                fild2 = getFieldAsString(doc, fild2)
                fild3 = getFieldAsString(doc, fild3)
                fild4 = getFieldAsString(doc, fild4)
                fild5 = getFieldAsString(doc, fild5)
                fild6 = getFieldAsString(doc, fild6)
                fild7 = getFieldAsString(doc, fild7)
                fild8 = getFieldAsString(doc, fild8)
                fild9 = getFieldAsString(doc, fild9)
                fild10 = getFieldAsString(doc, fild10)
                fild11 = getFieldAsString(doc, fild11)

                updateUI()
            }
        }

        return binding.root
    }

    private fun getFieldAsString(doc: DocumentSnapshot, field: String?): String {
        return when (val value = doc.get(field!!)) {
            is String -> value
            else -> value.toString()
        }
    }

    private fun updateUI() {
        if (fild1 == "1") {
            binding.char1.setImageResource(R.drawable.level_up_char1)
        }
        if (fild2 == "1") {
            binding.char2.setImageResource(R.drawable.level_up_char2)
        }
        if (fild3 == "1") {
            binding.char3.setImageResource(R.drawable.level_up_char3)
        }
        if (fild4 == "1") {
            binding.char4.setImageResource(R.drawable.level_up_char4)
        }
        if (fild5 == "1") {
            binding.char5.setImageResource(R.drawable.level_up_char5)
        }
        if (fild6 == "1") {
            binding.char6.setImageResource(R.drawable.level_up_char6)
        }
        if (fild7 == "1") {
            binding.char7.setImageResource(R.drawable.level_up_char7)
        }
        if (fild8 == "1") {
            binding.char8.setImageResource(R.drawable.level_up_char8)
        }
        if (fild9 == "1") {
            binding.char9.setImageResource(R.drawable.level_up_char9)
        }
        if (fild10 == "1") {
            binding.char10.setImageResource(R.drawable.level_up_char10)
        }
        if (fild11 == "1") {
            binding.char11.setImageResource(R.drawable.level_up_char11)
        }
        if (fild1 == "1" && fild2 == "1" && fild3 == "1" && fild4 == "1" && fild5 == "1" && fild6 == "1" && fild7 == "1" && fild8 == "1" && fild9 == "1" && fild10 == "1" && fild11 == "1") {
            binding.charhiden.setImageResource(R.drawable.level_up_char_hiden2)
        }
    }

    private fun select() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_select_char, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}