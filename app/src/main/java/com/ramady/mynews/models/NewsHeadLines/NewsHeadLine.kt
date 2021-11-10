package com.ramady.mynews.models.NewsHeadLines


data class NewsHeadLine(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)