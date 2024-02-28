package com.ramady.mynews.Adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ramady.mynews.R
import com.ramady.mynews.RoomDb.DataBase
import com.ramady.mynews.RoomDb.RoomViewModel
import com.ramady.mynews.databinding.ItemNewsMainBinding
import com.ramady.mynews.models.NewsHeadLines.Article
import com.ramady.mynews.models.NewsHeadLines.Details
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsAdapter(val context: Context , val listner: ClickListnerItem): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    var newsHeadLinesList= ArrayList<Article>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : ItemNewsMainBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_news_main,parent,false)
        return NewsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news: Article =newsHeadLinesList[position]
        holder.bind(news,context)
    }


    override fun getItemCount(): Int {
        return newsHeadLinesList.size
    }

    fun setList(surahsNamesList: List<Article>) {
        this.newsHeadLinesList= surahsNamesList as ArrayList<Article>
        notifyDataSetChanged()

    }



    inner class NewsViewHolder(val binding: ItemNewsMainBinding) : RecyclerView.ViewHolder(binding.root) {


        var name = ""
        var content: String = ""
        var urlImage: String = ""
        var dateNew = ""
        var desc: String = ""
        var title: String = ""
        var urlLink: String = ""

        lateinit var db: DataBase

        private lateinit var roomViewModel: RoomViewModel

        fun bind(data: Article, context: Context) {


            db = DataBase.getInstance(context)
            roomViewModel = RoomViewModel(db)

            // reCreate Name
            name = data.source!!.name.toString()
            if (name.contains(".com")) {
                name = name.replace(".com", "")
            } else if (name.contains(".net")) {
                name = name.replace(".net", "")
            } else if (name == null) {
                name = ""
            }

            // reCreate date
            dateNew = data.publishedAt.toString()
            dateNew = dateNew.replaceAfter("T", "")
            dateNew = dateNew.replace("T", "")


            binding.nameNewsMain.text = name
            binding.titleNewsMain.text = data.title
            binding.dateNewsMain.text = dateNew


            val options = RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
            Glide.with(context).load(data.urlToImage).apply(options).into(binding.imageNewsMain)


            if (data.content == null) {
                data.content = ""
                content = data?.content!!
            } else {
                for (i in 0 until data.content!!.length) {
//            print(str[i])
                    if (data.content!![i].toString() == " ") {
                        content = content + data.content!![i]

                    }

                    if (data.content!![i].toInt() > 64 && data.content!![i].toInt() <= 122) //returns true if both conditions returns true
                    {
                        content = content + data.content!![i]
                    }
                }

                if (content.contains("[ chars]")) {
                    content = content.replace("[ chars]", "")
                }
            }




            desc = data.description.toString()
            if (desc == null) {
                desc = ""
            }

            title = data.title.toString()
            if (title == null) {
                title = ""
            }

            urlImage = data.urlToImage.toString()
            if (urlImage == null) {
                urlImage = ""
            }


            urlLink = data.url.toString()
            if (urlLink == null) {
                urlLink = ""
            }


            db.newsDao.isRowIsExist(data.title.toString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it) {
                            data.favourite = true
                            binding.favNewsMain.setImageResource(R.drawable.ic_favorite_full)
                        } else {
                            data.favourite = false
                            binding.favNewsMain.setImageResource(R.drawable.icon_favorite)

                        }
                        Log.e("done", it.toString())
                    }, {

                    }
                ).let {

                }


//            if (data.favourite==true){
//                binding.favNewsMain.setImageResource(R.drawable.ic_favorite_full)
//            }else{
//                data.favourite=false
//                binding.favNewsMain.setImageResource(R.drawable.icon_favorite)
//
//            }


            if (data.author == null) {
                data.author = ""
            }





            binding.itemContainer.setOnClickListener {
                val d = Details(urlImage, name, dateNew, title, desc, content, urlLink,
                    data.favourite
                )
                listner.onClickItemNews(d)
            }

            binding.favNewsMain.setOnClickListener {


                if (data.favourite == false) {
                    data.favourite = true
                    binding.favNewsMain.setImageResource(R.drawable.ic_favorite_full)
                } else if (data.favourite == true) {
                    data.favourite = false
                    binding.favNewsMain.setImageResource(R.drawable.icon_favorite)
                }


//                val d=Article(position,data.author,content,desc,dateNew,data.source,title,urlLink,urlImage,data.favourite)

                listner.onClickFav(data, position)

            }


        }



    }
    interface ClickListnerItem {

        fun onClickItemNews(d: Details)

        fun onClickFav(d: Article, position: Int)
    }



}