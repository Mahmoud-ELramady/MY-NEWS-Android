package com.ramady.mynews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ramady.mynews.ViewModel.DetailsViewModel
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.ramady.mynews.databinding.ActivityDetailsBinding
import com.ramady.mynews.models.NewsHeadLines.Details

class DetailsActivity : AppCompatActivity() {

lateinit var binding: ActivityDetailsBinding
    lateinit var viewModel: DetailsViewModel
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyNews)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_details)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


//        var adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d(TAG, adError?.message)
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d(TAG, "Ad was loaded")
//                mInterstitialAd = interstitialAd
//            }
//        })





        if (intent.action.equals("homeFragment")) {

            val data = intent.extras?.get("data") as Details
            bindData(data)
            binding.btnDetails.setOnClickListener {

//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this)
//                } else {
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                }

                val uri: Uri = Uri.parse(data.urlLink)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                this.startActivity(intent)
            }
        }

            if (intent.action.equals("searchFragment")) {

                val data = intent.extras?.get("dataSearch") as Details
                bindData(data)
                binding.btnDetails.setOnClickListener {
                    val uri: Uri = Uri.parse(data.urlLink)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    this.startActivity(intent)
                }
            }

        if (intent.action.equals("favFragment")) {

            val data = intent.extras?.get("dataFragment") as Details
            bindData(data)
            binding.btnDetails.setOnClickListener {
                val uri: Uri = Uri.parse(data.urlLink)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                this.startActivity(intent)

            }
        }




    }




     fun bindData(it: Details?) {

        if (it?.urlImage==""){
            binding.tvNameDetails.visibility=View.VISIBLE
        }else{
            val options= RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)

            Glide.with(this).load(it?.urlImage).apply(options).into(binding.ivDetails)
        }

         if (it?.fav==true){
             binding.favouriteDetails.setImageResource(R.drawable.full_favorite_details)
         }



        binding.nameNewDetails.text=it?.name
        binding.dateNewDetails.text=it?.date
        binding.titleNewDetails.text=it?.title
        binding.descNewDetails.text=it?.desc
        binding.contentNewDetails.text=it?.content



    }




}