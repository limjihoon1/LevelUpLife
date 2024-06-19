package com.limjihoon.myhero.fragment
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.limjihoon.myhero.G
import com.limjihoon.myhero.activitis.MainActivity
import com.limjihoon.myhero.databinding.FragmentNotificationsBinding
import com.limjihoon.myhero.model.DataManager
import org.json.JSONObject

class ListFragment : Fragment(){
    lateinit var binding: FragmentNotificationsBinding
    private lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wv.loadUrl("http://myhero.dothome.co.kr/levelUpLife")

        binding.wv.settings.javaScriptEnabled = true
        binding.wv.settings.builtInZoomControls = true
        binding.wv.settings.displayZoomControls = false
        binding.wv.settings.allowFileAccess = true
        binding.wv.settings.domStorageEnabled= true
        binding.wv.settings.allowFileAccess = true

        binding.wv.webViewClient= WebViewClient()

        binding.wv.webChromeClient= MyWebChromeClient(requireContext())
        binding.wv.addJavascriptInterface(MyWebViewConnector(), "Droid")

        //1) native app 에서 web js를 제어하기
        //웹뷰에 보낼 메세지
        val ma = activity as MainActivity
        ma.dataManager.memberFlow.value ?: return
        dataManager = ma.dataManager
        val member = dataManager.memberFlow.value

        if(member != null){
            val gson = Gson()
            val userSet = gson.toJson(member)
            val escapedUserSet = userSet.replace("\\", "\\\\").replace("'", "\\'")

            binding.wv.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (url == "http://myhero.dothome.co.kr/levelUpLife/") {
                        binding.wv.evaluateJavascript("javascript:sendToWeb('${escapedUserSet}')",
                            { result->Log.d("web","${result}")})
                        Log.d("계정","uid${member.level},히어로${member.hero},닉네임${member.nickname}")
                    }
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                binding.wv.loadUrl("http://myhero.dothome.co.kr/levelUpLife/")
            }, 2000)

        }



    //뒤로가기 눌렀을때 페이지 전환
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if ( binding.wv.canGoBack()) {
                    binding.wv.goBack()
                } else {
                    requireActivity().finish()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.wv.addJavascriptInterface(MyWebViewConnector(),"Droid")

    }// onViewCreated

    override fun onResume() {
        super.onResume()
//        val ma = activity as MainActivity
//        ma.dataManager.memberFlow.value ?: return
//        dataManager = ma.dataManager
//        val member = dataManager.memberFlow.value
//
//        if(member != null){
//            val gson = Gson()
//            val userSet = gson.toJson(member)
//            val escapedUserSet = userSet.replace("\\", "\\\\").replace("'", "\\'")
//            binding.wv.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    if (url == "http://myhero.dothome.co.kr/levelUpLife/") {
//                        binding.wv.evaluateJavascript("javascript:sendToWeb('${escapedUserSet}')",
//                            { result->Log.d("web","${result}")})
//                        Log.d("계정","uid${member.level},히어로${member.hero},닉네임${member.nickname}")
//                    }
//                }
//            }
//
//            binding.wv.loadUrl("http://myhero.dothome.co.kr/levelUpLife/")
//        }

    }


    inner class MyWebViewConnector{

        @JavascriptInterface
        fun user(msg:String){

        }

    }

    var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    inner class MyWebChromeClient(val context: Context) : WebChromeClient(){
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            Toast.makeText(context, "파일선택 클릭", Toast.LENGTH_SHORT).show()

            val intent= if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
            else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")

            resultLauncher.launch((intent))
            mFilePathCallback= filePathCallback

            return true
        }

        val resultLauncher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if ( it.resultCode == AppCompatActivity.RESULT_OK ){
                //선택했을때
                val imgs: MutableList<Uri> = mutableListOf()
                if ( it.data?.data != null ) imgs.add(it.data!!.data!!)

                val uris:Array<Uri> = imgs.toTypedArray()
                mFilePathCallback!!.onReceiveValue(uris)

            }else Toast.makeText(context, "파일선택을 취소하였습니다.", Toast.LENGTH_SHORT).show()

        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            Toast.makeText(context, "다이얼로그~", Toast.LENGTH_SHORT).show()

            val dialog= AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton("확인", { dialog, which -> result!!.confirm() })
                .setNegativeButton("취소", { dialog, which -> result!!.cancel() })
                .create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()

            return true
        }

    }//inner class

}
