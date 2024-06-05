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
import com.limjihoon.myhero.R
import com.limjihoon.myhero.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(){
    lateinit var binding: FragmentProfileBinding
    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)
        binding.rendumBtn.setOnClickListener { dilrendum() }
        return binding.root
    }
    private fun dilrendum() {
        Handler(Looper.getMainLooper()).postDelayed({
//            showLoadingDialog()
            rendum()
        }, 3000) // 3000 milliseconds delay
    }


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


    private fun rendum(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custum_dialog_rendum, null)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.show()

        val dialogButton: Button = dialogView.findViewById(R.id.dialogButton)
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

    }

}