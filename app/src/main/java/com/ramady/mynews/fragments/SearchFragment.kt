package com.ramady.mynews.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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
import com.ramady.mynews.Adapters.NewsAdapter
import com.ramady.mynews.ConnectivityUtil
import com.ramady.mynews.DetailsActivity
import com.ramady.mynews.R
import com.ramady.mynews.Repo.RepositarySearch
import com.ramady.mynews.RoomDb.DataBase
import com.ramady.mynews.RoomDb.RoomViewModel
import com.ramady.mynews.ViewModel.SearchViewModelProvider
import com.ramady.mynews.ViewModel.ViewModelSearch
import com.ramady.mynews.databinding.FragmentSearchBinding
import com.ramady.mynews.models.NewsHeadLines.Article
import com.ramady.mynews.models.NewsHeadLines.Details
import java.io.Serializable

class SearchFragment : Fragment(), NewsAdapter.ClickListnerItem {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel:ViewModelSearch
    lateinit var rv:RecyclerView
    var searchName:String=""
    lateinit var adapter: NewsAdapter

    lateinit var searchEd:EditText
    lateinit var tvConnection:TextView
    lateinit var prBar:ProgressBar



    lateinit var db: DataBase

    private lateinit var roomViewModel: RoomViewModel

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "SearchFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
         adapter= NewsAdapter(requireContext(),this)

        searchEd=binding.edSearch
        rv=binding.rvSearch
        tvConnection=binding.tvConnection





        MobileAds.initialize(requireContext())
        val request= AdRequest.Builder().build()
        binding.adViewSearch.loadAd(request)

        addAdMob()




//        binding.adViewSearch.adListener=object : AdListener(){
//
//            override fun onAdLoaded() {
//                Toast.makeText(requireContext(),"success",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                Toast.makeText(requireContext(),p0.message.toString(),Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAdOpened() {
//                super.onAdOpened()
//            }
//
//            override fun onAdClicked() {
//                super.onAdClicked()
//            }
//
//            override fun onAdClosed() {
//                super.onAdClosed()
//            }
//
//        }






        val repo= RepositarySearch()

        val factory=SearchViewModelProvider(repo)

        viewModel=ViewModelProvider(this,factory).get(ViewModelSearch::class.java)


        initRecyclerSearch()



        searchEd.setOnKeyListener(object :View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_DOWN) {
                    request()
                }
                return true

            }
        })


        binding.btnSearch.setOnClickListener {

            request()

        }
        binding.swSearch.setOnRefreshListener {
            request()
            binding.swSearch.isRefreshing = false
        }



        db= DataBase.getInstance(requireContext())
        roomViewModel=getRoomViewModel()

        return binding.root


    }


  fun  addAdMob(){
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

    override fun onClickItemNews(d: Details) {
        val action="searchFragment"


        val intent: Intent = Intent(getActivity(), DetailsActivity::class.java)

        intent.putExtra("dataSearch",d as Serializable)
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

        }else if (d.favourite==false){
            roomViewModel.deleteNews(d.title.toString())
            Toast.makeText(requireContext(),"deleted to favourites",Toast.LENGTH_SHORT).show()

        }
    }

    fun initRecyclerSearch(){
        rv.layoutManager=LinearLayoutManager(requireContext())
        rv.adapter=adapter
        rv.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        request2()
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


    fun request(){

        searchName=searchEd.text.toString()


        if (TextUtils.isEmpty(searchName)){
            Toast.makeText(requireContext(),"the field is empty",Toast.LENGTH_SHORT).show()

        }else{
            rv.visibility=View.GONE
            tvConnection.visibility=View.GONE
            try {
                binding.prSearch.visibility=View.VISIBLE


            }catch (e:Exception){
              Log.e("prError",e.message.toString())
            }




            if (ConnectivityUtil.isConnected(requireContext())){

                viewModel.getListSearch(searchName)
                viewModel.searchList.observe(requireActivity(), Observer {



                    if (it.articles.isEmpty()){

                        requireActivity().runOnUiThread(Runnable {
                            tvConnection.text="no results"

                            binding.prSearch.visibility=View.GONE
                            rv.visibility=View.GONE

                            tvConnection.visibility=View.VISIBLE
                        })



                    }else{
                        requireActivity().runOnUiThread(Runnable {
                            tvConnection.visibility=View.GONE
//                        binding.prSearch.visibility=View.GONE

                            rv.visibility=View.VISIBLE

                            rv.smoothScrollToPosition(0)
                            adapter.setList(it.articles)
                            binding.prSearch.visibility=View.GONE

                        })



                    }
                })

            }else{
                requireActivity().runOnUiThread(Runnable {
                    binding.prSearch.visibility=View.GONE
                    rv.visibility=View.GONE

                    tvConnection.text="Check Internet Connection \n and try again "
                    tvConnection.visibility=View.VISIBLE

                })






            }



        }


    }


    fun request2(){

        searchName=searchEd.text.toString()

        if (TextUtils.isEmpty(searchName)){

        }else{
            if (ConnectivityUtil.isConnected(requireContext())){

                binding.prSearch.visibility=View.VISIBLE
                rv.visibility=View.GONE



                viewModel.getListSearch(searchName)
                viewModel.searchList.observe(requireActivity(), Observer {



                    if (it.articles.isEmpty()){
                        tvConnection.text="no results"

                        binding.prSearch.visibility=View.GONE
                        rv.visibility=View.GONE

                        tvConnection.visibility=View.VISIBLE

                    }else{
                        tvConnection.visibility=View.GONE
                        binding.prSearch.visibility=View.GONE

                        rv.visibility=View.VISIBLE

                        rv.smoothScrollToPosition(0)
                        adapter.setList(it.articles)

                    }






                })

            }else{
                binding.prSearch.visibility=View.GONE
                rv.visibility=View.GONE

                tvConnection.text="Check Internet Connection \n and try again "
                tvConnection.visibility=View.VISIBLE



            }



        }


    }




}