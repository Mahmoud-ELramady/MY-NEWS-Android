package com.ramady.mynews.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ramady.mynews.ViewModel.DetailsViewModel
import com.ramady.mynews.R
import com.ramady.mynews.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {
    private var binding: FragmentDetailsBinding?=null

    lateinit var viewModel:DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

//        viewModel= DetailsViewModel()
//
//        viewModel.detailsData.observe(requireActivity(), Observer {
//            bind(it)
//        })


        return binding!!.root
    }

//    private fun bind(it: Details?) {
//
//        val options= RequestOptions()
//            .placeholder(R.drawable.logo)
//            .error(R.drawable.logo)
//
//        Glide.with(requireActivity()).load(it?.urlImage).apply(options).into(binding!!.ivDetails)
//
//        binding!!.nameNewDetails.text=it?.name
//        binding!!.dateNewDetails.text=it?.date
//        binding!!.titleNewDetails.text=it?.title
//        binding!!.descNewDetails.text=it?.desc
//
//        if (it?.content==null){
//            it?.content=""
//        }
//        binding!!.contentNewDetails.text=it?.content
//
//    }


}