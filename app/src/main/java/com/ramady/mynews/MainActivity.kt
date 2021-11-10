package com.ramady.mynews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.ramady.mynews.Adapters.MenuBottomAdapter
import com.ramady.mynews.fragments.FavouriteFragment
import com.ramady.mynews.fragments.HomeFragment
import com.ramady.mynews.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
     lateinit var mbn:MeowBottomNavigation
     lateinit var viewPager:ViewPager
//     lateinit var adView:AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MyNews)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)



        mbn=findViewById(R.id.mbn_main)
        viewPager=findViewById(R.id.view_pager)

        val menuAdapter= MenuBottomAdapter(initFragments(),supportFragmentManager)
        viewPager.adapter=menuAdapter

        mbn.add(MeowBottomNavigation.Model(1,R.drawable.icon_home))
        mbn.add(MeowBottomNavigation.Model(2,R.drawable.icon_search))
        mbn.add(MeowBottomNavigation.Model(3,R.drawable.icon_favorite))


        mbn.show(1,true)

        mbn.setOnClickMenuListener{
            model ->
            when(model.id){


                1-> viewPager.currentItem=0
                2-> viewPager.currentItem=1
                3-> viewPager.currentItem=2
//

            }
        }


        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mbn.show(position+1,true)
            }


            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }





    private fun initFragments():ArrayList<Fragment>{
        return arrayListOf(
            HomeFragment(),
            SearchFragment(),
            FavouriteFragment()
        )
    }





}