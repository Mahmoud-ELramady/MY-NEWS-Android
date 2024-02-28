package com.ramady.mynews.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.ramady.mynews.Adapters.NewsAdapter
import com.ramady.mynews.DetailsActivity
import com.ramady.mynews.R
import com.ramady.mynews.RoomDb.DataBase
import com.ramady.mynews.RoomDb.RoomViewModel
import com.ramady.mynews.databinding.FragmentFavouriteBinding
import com.ramady.mynews.models.NewsHeadLines.Article
import com.ramady.mynews.models.NewsHeadLines.Details
import java.io.Serializable


class FavouriteFragment : Fragment(), NewsAdapter.ClickListnerItem {

    lateinit var binding: FragmentFavouriteBinding


    lateinit var db: DataBase

    private lateinit var roomViewModel: RoomViewModel

    lateinit var rv: RecyclerView
    lateinit var adapter: NewsAdapter
private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "FavFragment"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false)

        rv=binding.rvFav
        adapter= NewsAdapter(requireContext(),this)



        MobileAds.initialize(requireContext())
        val request= AdRequest.Builder().build()
        binding.adViewFav.loadAd(request)


        addAdMob()





        db= DataBase.getInstance(requireContext())
        roomViewModel=getRoomViewModel()

        initRecyclerSearch()


      requestGetData()


        binding.facebook.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.facebook.com/mahmoudelrmady")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }
        binding.twitter.setOnClickListener {
            val uri: Uri = Uri.parse("https://twitter.com/melramady84")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }

        binding.linkedin.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/mahmoud-el-ramady-05b79518a/")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }


        return binding.root
    }






    fun initRecyclerSearch(){

        val linear=LinearLayoutManager(requireContext())
        linear.reverseLayout=true
        linear.stackFromEnd=true
        rv.layoutManager= linear
        rv.adapter=adapter
        rv.setHasFixedSize(true)

    }

    fun addAdMob(){
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),"ca-app-pub-7021865909664143~2235645203", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded")
                mInterstitialAd = interstitialAd
            }
        })
    }


    override fun onStart() {
        super.onStart()



    }


    fun getRoomViewModel(): RoomViewModel {
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)){
                    return RoomViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[RoomViewModel::class.java]


    }

    override fun onClickItemNews(d: Details) {
        val action="favFragment"


        val intent: Intent = Intent(getActivity(), DetailsActivity::class.java)

        intent.putExtra("dataFragment",d as Serializable)
        intent.action=action
        startActivity(intent)

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }

    }

    override fun onClickFav(d: Article, position: Int) {
        if (d.favourite==true){
            roomViewModel.insertNews(d)

            Toast.makeText(requireContext(),"added to favourites",Toast.LENGTH_SHORT).show()
        }else if (d.favourite==false) {
            roomViewModel.deleteNews(d.title.toString())
            Snackbar.make(binding.root, "article is deleted", Snackbar.LENGTH_SHORT).apply {
                setAction("Undo") {
                    roomViewModel.insertNews(d)
                }
                show()

                //   Toast.makeText(requireContext(),"deleted from favourites",Toast.LENGTH_SHORT).show()
                //  requestGetData()

            }
        }
    }

    private fun requestGetData(){
        roomViewModel.getNews().observe(requireActivity(), Observer {
            Log.e("dataRoom",it.toString())
//            listNews=it
            adapter.setList(it)
        })
    }

//
//    override fun onStart() {
//        super.onStart()
//        Log.e("onStart","onStart")
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Log.e("onAttach","onAttach")
//
//    }

//    override fun onDetach() {
//        super.onDetach()
//        Log.e("onDetach","onDetach")
//
//    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume","onResume")
       // requestGetData()

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.e("onDestroy","onDestroy")
//
//    }

}