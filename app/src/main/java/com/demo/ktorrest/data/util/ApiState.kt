package com.demo.ktorrest.data.util

import com.demo.ktorrest.data.Post

sealed  class ApiState {

    object Empty :ApiState()
    class Failure(val msg:Throwable) :ApiState()
    class Sucess(val data:List<Post>):ApiState()
    object  Loading :ApiState()
}