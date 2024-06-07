package com.limjihoon.myhero.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.limjihoon.myhero.G
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.FragmentProfileBinding
import kotlin.random.Random


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding


    private var loadingDialog: AlertDialog? = null
    val updatedField1: Map<String, Any> = hashMapOf("char1" to 1)
    val updatedField2: Map<String, Any> = hashMapOf("char2" to 1)
    val updatedField3: Map<String, Any> = hashMapOf("char3" to 1)
    val updatedField4: Map<String, Any> = hashMapOf("char4" to 1)
    val updatedField5: Map<String, Any> = hashMapOf("char5" to 1)
    val updatedField6: Map<String, Any> = hashMapOf("char6" to 1)
    val updatedField7: Map<String, Any> = hashMapOf("char7" to 1)
    val updatedField8: Map<String, Any> = hashMapOf("char8" to 1)
    val updatedField9: Map<String, Any> = hashMapOf("char9" to 1)
    val updatedField10: Map<String, Any> = hashMapOf("char10" to 1)
    val updatedField11: Map<String, Any> = hashMapOf("char11" to 1)
    val updatedFieldhiden: Map<String, Any> = hashMapOf("charhiden" to 1)
    var fb = Firebase.firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)


        binding.rendumBtn.setOnClickListener { rendum() }

        return binding.root
    }
//    private fun dilrendum() {
//        Handler(Looper.getMainLooper()).postDelayed({
////            showLoadingDialog()
//            rendum()
//        }, 3000) // 3000 milliseconds delay
//    }


//    private fun showLoadingDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        val inflater = layoutInflater
//        val dialogView = inflater.inflate(R.layout.loding_layout, null)
//        builder.setView(dialogView)
//        builder.setCancelable(false)
//
//        val loadingGif: ImageView = dialogView.findViewById(R.id.loadingGif)
//        Glide.with(this).asGif().load(R.drawable.loding).into(loadingGif)
//
//        loadingDialog = builder.create()
//        loadingDialog?.show()
//    }
//
//    private fun hideLoadingDialog() {
//        loadingDialog?.dismiss()
//    }
//


    private fun rendum() {
        val randomNumber = Random.nextInt(1, 12)


        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_rendum, null)
        var img: ImageView = dialogView.findViewById(R.id.dialogImage)
        if (randomNumber == 1) {
            img.setImageResource(R.drawable.level_up_char1)
            fb.document("1").update(updatedField1)
        } else if (randomNumber == 2) {
            img.setImageResource(R.drawable.level_up_char2)
            fb.document("1").update(updatedField2)
        } else if (randomNumber == 3) {
            img.setImageResource(R.drawable.level_up_char3)
            fb.document("1").update(updatedField3)
        } else if (randomNumber == 4) {
            img.setImageResource(R.drawable.level_up_char4)
            fb.document("1").update(updatedField4)
        } else if (randomNumber == 5) {
            img.setImageResource(R.drawable.level_up_char5)
            fb.document("1").update(updatedField5)
        } else if (randomNumber == 6) {
            img.setImageResource(R.drawable.level_up_char6)
            fb.document("1").update(updatedField6)
        } else if (randomNumber == 7) {
            img.setImageResource(R.drawable.level_up_char7)
            fb.document("1").update(updatedField7)
        } else if (randomNumber == 8) {
            img.setImageResource(R.drawable.level_up_char8)
            fb.document("1").update(updatedField8)
        } else if (randomNumber == 9) {
            img.setImageResource(R.drawable.level_up_char9)
            fb.document("1").update(updatedField9)
        } else if (randomNumber == 10) {
            img.setImageResource(R.drawable.level_up_char10)
            fb.document("1").update(updatedField10)
        } else if (randomNumber == 11) {
            img.setImageResource(R.drawable.level_up_char11)
            fb.document("1").update(updatedField11)
        }



        builder.setView(dialogView)


        val dialog = builder.create()
        dialog.show()


        val dialogButton: Button = dialogView.findViewById(R.id.dialogButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}