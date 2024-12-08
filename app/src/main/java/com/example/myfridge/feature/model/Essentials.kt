package com.example.myfridge.feature.model

data class Essentials(
    val id: String = "",
    val userEmail: String = "",
    val name: String = "", //eName -> firebase에서 ename으로 저장됨
    val place: String = "",
    val price: String = ""
)
