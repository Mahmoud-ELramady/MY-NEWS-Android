package com.ramady.mynews.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import com.ramady.mynews.Adapters.DataAdapter
import com.ramady.mynews.Adapters.NewsAdapter
import com.ramady.mynews.ConnectivityUtil
import com.ramady.mynews.DetailsActivity
import com.ramady.mynews.R
import com.ramady.mynews.Repo.NewsRepositary
import com.ramady.mynews.RoomDb.DataBase
import com.ramady.mynews.RoomDb.RoomViewModel
import com.ramady.mynews.ViewModel.DetailsViewModel
import com.ramady.mynews.ViewModel.NewsViewModel
import com.ramady.mynews.api.ApiClient
import com.ramady.mynews.api.ApiInterface
import com.ramady.mynews.databinding.FragmentHomeBinding
import com.ramady.mynews.models.DataCategory
import com.ramady.mynews.models.NewsHeadLines.Article
import com.ramady.mynews.models.NewsHeadLines.Details
import java.io.Serializable


class HomeFragment : Fragment(),AdapterView.OnItemSelectedListener, NewsAdapter.ClickListnerItem{

    private var binding: FragmentHomeBinding?=null
    lateinit var carouselRecyclerview :CarouselRecyclerview
    lateinit var list: ArrayList<DataCategory>
       lateinit var adapter: DataAdapter
    lateinit  var itemSelected:String
    lateinit var spinner:Spinner
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var viewModelD:DetailsViewModel

// var listner: onFragmentClickListner?=null



    lateinit var repo: NewsRepositary
lateinit var apiInterface: ApiInterface
lateinit var viewModel: NewsViewModel
    lateinit var adapterNews: NewsAdapter
    lateinit var rc:RecyclerView

    var country:String="eg"
     var category:String="general"


    lateinit var db: DataBase

    private lateinit var roomViewModel: RoomViewModel



    private var mRewardedAd: RewardedAd? = null
    private final var TAG = "HomeFragment"



    companion object{
        public  var API_KEY:String=""
    }
    public lateinit var pref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        this.list = ArrayList<DataCategory>()
        swipeRefreshLayout=binding!!.sw


//         pref =requireActivity().getSharedPreferences("ApiKey", AppCompatActivity.MODE_PRIVATE)
//         pref.edit().putString("api_key_1","da29f2094590433f9c081707357f56cc5" ).commit().toString()
//         pref.edit().putString("api_key_2","da29f2094590433f9c081707357f56ce" ).commit().toString()
//

//        da29f2094590433f9c081707357f56ce


        adapterNews= NewsAdapter(requireContext(),this)

        addAdMob()


        initSpinner()

        getlistCategory(list)
        initRecyclerCategory()


        initRecyclerNews()


        initViewModel(country,category)


        swipeRefreshLayout.setOnRefreshListener {

            initViewModel(country,category)
            swipeRefreshLayout.isRefreshing = false

        }


        db= DataBase.getInstance(requireContext())
        roomViewModel=getRoomViewModel()

        return binding!!.root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
////        listner = if (context is onFragmentClickListner) {
////            context
////        } else {
////            throw ClassCastException(
////                "Your activity does not implement " +
////                        "OnFragmentClickListner"
////            )
////        }
//    }

//    override fun onDetach() {
//        super.onDetach()
////        listner=null
//    }

    private fun initViewModel( counteryM:String, categoryM:String) {


        Log.e("GGG",counteryM)
        binding!!.rvMain.visibility=View.GONE
        binding!!.tvConnectionHome.visibility=View.GONE

        binding!!.shimmerFrameLayout.startShimmerAnimation()
        binding!!.shimmerFrameLayout.visibility=View.VISIBLE



        if (ConnectivityUtil.isConnected(requireContext())){


                    apiInterface= ApiClient.getClient()

                    repo= NewsRepositary(apiInterface)

                    viewModel=NewsViewModel(repo,counteryM,categoryM)
                    viewModel.newsList.observe(requireActivity(), Observer {

                        if (it.isEmpty()){
                            binding!!.shimmerFrameLayout.stopShimmerAnimation()
                            binding!!.shimmerFrameLayout.visibility=View.GONE
                            binding!!.rvMain.visibility=View.GONE

                            binding!!.tvConnectionHome.text="No Results"
                            binding!!.tvConnectionHome.visibility=View.VISIBLE

                        }else{

                            binding!!.shimmerFrameLayout.stopShimmerAnimation()
                            binding!!.shimmerFrameLayout.visibility=View.GONE
                            binding!!.rvMain.visibility=View.VISIBLE

                            rc.smoothScrollToPosition(0)

                            adapterNews.setList(it)

                        }


                    })

        }else{
            binding!!.shimmerFrameLayout.stopShimmerAnimation()
            binding!!.shimmerFrameLayout.visibility=View.GONE
            binding!!.rvMain.visibility=View.GONE

            binding!!.tvConnectionHome.text="Check Internet Connection \n and try again"
            binding!!.tvConnectionHome.visibility=View.VISIBLE

        }

    }


    fun getlistCategory(list:ArrayList<DataCategory>){

        list.add(DataCategory(R.drawable.general_1, "general"))
        list.add(DataCategory(R.drawable.business_2, "business"))
        list.add(DataCategory(R.drawable.health_3, "health"))
        list.add(DataCategory(R.drawable.sports_4, "sports"))
        list.add(DataCategory(R.drawable.technology_5, "technology"))
        list.add(DataCategory(R.drawable.science_6, "science"))
        list.add(DataCategory(R.drawable.enter_7, "entertainment"))

    }


  fun initRecyclerCategory(){
      carouselRecyclerview=binding!!.recycler
      adapter = DataAdapter(list)
      carouselRecyclerview.adapter = adapter
      carouselRecyclerview.set3DItem(true)
      carouselRecyclerview.setAlpha(true)
      val carouselLayoutManager = carouselRecyclerview.getCarouselLayoutManager()
      val currentlyCenterPosition = carouselRecyclerview.getSelectedPosition()


      carouselRecyclerview.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
          override fun onItemSelected(position: Int) {
              var categoryItem=list[carouselRecyclerview.getSelectedPosition()].nameCategory
              category=categoryItem

              initViewModel(country,category)


          }
      })





  }


//
fun initSpinner(){
    spinner=binding!!.spinnerMain
    spinner.onItemSelectedListener=this
    val countries=resources.getStringArray(R.array.countries)
    val arrayAdapter=ArrayAdapter(requireContext(), R.layout.dropdown_item,countries)
    spinner.adapter=arrayAdapter
}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemSelected= parent?.getItemAtPosition(position).toString()
//        Toast.makeText(context, itemSelected, Toast.LENGTH_LONG).show()

        if (itemSelected=="Egypt"){
            country="eg"
        }else if (itemSelected=="Saudi"){
            country="sa"
        }else if (itemSelected=="Morocco"){
            country="ma"
        }else if (itemSelected=="USA"){
            country="us"
        }else if (itemSelected=="France"){
            country="fr"
        }else if (itemSelected=="Germany"){
            country="de"
        }else if (itemSelected=="China"){
            country="cn"
        }else if (itemSelected=="Philippines"){
            country="ph"
        }else if (itemSelected=="Italy"){
            country="it"
        }else if (itemSelected=="United Kingdom"){
            country="gb"
        }else if (itemSelected=="Brazil"){
            country="br"
        }else if (itemSelected=="Portugal"){
            country="pt"
        }

        initViewModel(country,category)





    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


    fun initRecyclerNews(){
        rc= binding?.rvMain!!
        rc.layoutManager= LinearLayoutManager(requireContext())
        rc.adapter=adapterNews
        rc.setHasFixedSize(true)

    }

    override fun onClickItemNews(d: Details) {



        val action="homeFragment"


        val intent: Intent = Intent(getActivity(), DetailsActivity::class.java)

            intent.putExtra("data",d as Serializable)
            intent.action=action
            startActivity(intent)

        if (mRewardedAd != null) {
            mRewardedAd?.show(requireActivity(), object :OnUserEarnedRewardListener {
                override fun onUserEarnedReward(p0: RewardItem) {
                    Toast.makeText(requireContext(),"rewarded", Toast.LENGTH_SHORT).show()
                    var rewardAmount = p0.amount
                    var rewardType = p0.getType()
                    Log.e("reward","reward")


                }

            })
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }




    }

   fun addAdMob(){
        var adRequest = AdRequest.Builder().build()


        RewardedAd.load(requireActivity(),"ca-app-pub-7021865909664143~2235645203", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
//                Toast.makeText(requireContext(),adError.message.toString(), Toast.LENGTH_SHORT).show()
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
//                Toast.makeText(requireContext(),"success", Toast.LENGTH_SHORT).show()

                mRewardedAd = rewardedAd
            }
        })
    }

    override fun onClickFav(d: Article, position: Int) {
        if (d.favourite==true){
            roomViewModel.insertNews(d)
            Toast.makeText(requireContext(),"added to favourites", Toast.LENGTH_SHORT).show()

        }else if (d.favourite==false){
            roomViewModel.deleteNews(d.title.toString())
            Toast.makeText(requireContext(),"deleted from favourites", Toast.LENGTH_SHORT).show()

        }
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




}