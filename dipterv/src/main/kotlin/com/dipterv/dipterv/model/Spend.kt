package com.dipterv.dipterv.model

import com.dipterv.dipterv.model.documentModel.User

class Spend (
    val id: Long,
    val user: User,
    val cost: Int,
    val currency: String
){
}