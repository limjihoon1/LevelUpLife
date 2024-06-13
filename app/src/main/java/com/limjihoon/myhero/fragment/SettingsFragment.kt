package com.limjihoon.myhero.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.changeImage.setOnClickListener { select() }
        binding.settingBtn.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.END) }
    }

    private fun getFieldAsString(doc: DocumentSnapshot, field: String?): String {
        return when (val value = doc.get(field!!)) {
            is String -> value
            else -> value.toString()
        }
    }

    private fun updateUI() {
        var progress: Double = 0.0
        if (fild1 == "1") {
            binding.char1.setImageResource(R.drawable.level_up_char1)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild2 == "1") {
            binding.char2.setImageResource(R.drawable.level_up_char2)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild3 == "1") {
            binding.char3.setImageResource(R.drawable.level_up_char3)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild4 == "1") {
            binding.char4.setImageResource(R.drawable.level_up_char4)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild5 == "1") {
            binding.char5.setImageResource(R.drawable.level_up_char5)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild6 == "1") {
            binding.char6.setImageResource(R.drawable.level_up_char6)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild7 == "1") {
            binding.char7.setImageResource(R.drawable.level_up_char7)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild8 == "1") {
            binding.char8.setImageResource(R.drawable.level_up_char8)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild9 == "1") {
            binding.char9.setImageResource(R.drawable.level_up_char9)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild10 == "1") {
            binding.char10.setImageResource(R.drawable.level_up_char10)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild11 == "1") {
            binding.char11.setImageResource(R.drawable.level_up_char11)
            progress = progress + 91
            binding.progressText.text = "" + (progress / 10)
        }
        if (fild1 == "1" && fild2 == "1" && fild3 == "1" && fild4 == "1" && fild5 == "1" && fild6 == "1" && fild7 == "1" && fild8 == "1" && fild9 == "1" && fild10 == "1" && fild11 == "1") {
            binding.charhiden.setImageResource(R.drawable.level_up_char_hiden2)
            progress++
            binding.progressText.text = "100"
        }
        var ppp = (progress / 10).toInt()
        binding.progressBar.progress = ppp

    }

    fun select() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_select_char, null)
        val image1: ImageView = dialogView.findViewById(R.id.select_char1)
        val image2: ImageView = dialogView.findViewById(R.id.select_char2)
        val image3: ImageView = dialogView.findViewById(R.id.select_char3)
        val image4: ImageView = dialogView.findViewById(R.id.select_char4)
        val image5: ImageView = dialogView.findViewById(R.id.select_char5)
        val image6: ImageView = dialogView.findViewById(R.id.select_char6)
        val image7: ImageView = dialogView.findViewById(R.id.select_char7)
        val image8: ImageView = dialogView.findViewById(R.id.select_char8)
        val image9: ImageView = dialogView.findViewById(R.id.select_char9)
        val image10: ImageView = dialogView.findViewById(R.id.select_char10)
        val image11: ImageView = dialogView.findViewById(R.id.select_char11)
        val imagehiden: ImageView = dialogView.findViewById(R.id.select_charhiden)

        builder.setView(dialogView)

        val dialog = builder.create()
        if (fild1 == "1") {
            image1.visibility = View.VISIBLE
        } else {
            image1.visibility = View.GONE
        }
        if (fild2 == "1") {
            image2.visibility = View.VISIBLE
        } else {
            image2.visibility = View.GONE
        }
        if (fild3 == "1") {
            image3.visibility = View.VISIBLE
        } else {
            image3.visibility = View.GONE
        }
        if (fild4 == "1") {
            image4.visibility = View.VISIBLE
        } else {
            image4.visibility = View.GONE
        }
        if (fild5 == "1") {
            image5.visibility = View.VISIBLE
        } else {
            image5.visibility = View.GONE
        }
        if (fild6 == "1") {
            image6.visibility = View.VISIBLE
        } else {
            image6.visibility = View.GONE
        }
        if (fild7 == "1") {
            image7.visibility = View.VISIBLE
        } else {
            image7.visibility = View.GONE
        }
        if (fild8 == "1") {
            image8.visibility = View.VISIBLE
        } else {
            image8.visibility = View.GONE
        }
        if (fild9 == "1") {
            image9.visibility = View.VISIBLE
        } else {
            image9.visibility = View.GONE
        }
        if (fild10 == "1") {
            image10.visibility = View.VISIBLE
        } else {
            image10.visibility = View.GONE
        }
        if (fild11 == "1") {
            image11.visibility = View.VISIBLE
        } else {
            image11.visibility = View.GONE
        }
        if (fild1 == "1" && fild2 == "1" && fild3 == "1" && fild4 == "1" && fild5 == "1" && fild6 == "1" && fild7 == "1" && fild8 == "1" && fild9 == "1" && fild10 == "1" && fild11 == "1") {
            imagehiden.visibility = View.VISIBLE
        } else {
            imagehiden.visibility = View.GONE
        }
        image1.setOnClickListener {
            image1.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char1)
        }
        image2.setOnClickListener {
            image2.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char2)
        }
        image3.setOnClickListener {
            image3.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char3)
        }
        image4.setOnClickListener {
            image4.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char4)
        }
        image5.setOnClickListener {
            image5.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char5)
        }
        image6.setOnClickListener {
            image6.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char6)
        }
        image7.setOnClickListener {
            image7.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char7)
        }
        image8.setOnClickListener {
            image8.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char8)
        }
        image9.setOnClickListener {
            image9.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)

            image10.setBackgroundResource(R.drawable.char_bg)

            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char9)
        }
        image10.setOnClickListener {
            image10.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))


            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)


            image11.setBackgroundResource(R.drawable.char_bg)
            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char10)
        }
        image11.setOnClickListener {
            image11.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)

            imagehiden.setBackgroundResource(R.drawable.char_bg)
            binding.myChar.setImageResource(R.drawable.level_up_char11)
        }
        imagehiden.setOnClickListener {
            imagehiden.setBackgroundColor(resources.getColor(R.color.ypgbtn, null))

            image1.setBackgroundResource(R.drawable.char_bg)
            image2.setBackgroundResource(R.drawable.char_bg)
            image3.setBackgroundResource(R.drawable.char_bg)
            image4.setBackgroundResource(R.drawable.char_bg)
            image5.setBackgroundResource(R.drawable.char_bg)
            image6.setBackgroundResource(R.drawable.char_bg)
            image7.setBackgroundResource(R.drawable.char_bg)
            image8.setBackgroundResource(R.drawable.char_bg)
            image9.setBackgroundResource(R.drawable.char_bg)
            image10.setBackgroundResource(R.drawable.char_bg)
            image11.setBackgroundResource(R.drawable.char_bg)

            binding.myChar.setImageResource(R.drawable.level_up_char_hiden2)

        }

        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.confirmButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}
