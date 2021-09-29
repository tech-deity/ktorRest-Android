package com.demo.ktorrest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.ktorrest.Adapter.PostAdapter
import com.demo.ktorrest.data.util.ApiState
import com.demo.ktorrest.databinding.ActivityMainBinding
import com.demo.ktorrest.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private  lateinit var  binding :ActivityMainBinding
    private  val mainViewModel :MainViewModel by viewModels()

    @Inject
     lateinit var postAdapter :PostAdapter
     override fun onCreate(savedInstanceState: Bundle?) {

         super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)
         initRecylcerView()
         mainViewModel.getPost()
         handleResponse()
     }

    private  fun handleResponse(){
        lifecycleScope.launchWhenStarted {
            mainViewModel.apiStateFlow.collect {
                when(it){
                    is ApiState.Sucess->{
                        recycler_view.isVisible = true
                        progress_bar.isVisible = false
                        error.isVisible = false
                        postAdapter.submitList(it.data)
                    }
                    is ApiState.Failure ->
                    {
                        recycler_view.isVisible = false
                        progress_bar.isVisible = false
                        error.isVisible = true
                        error.text = it.msg.toString()

                    }
                    is ApiState.Loading ->{
                        recycler_view.isVisible = false
                        progress_bar.isVisible = true
                        error.isVisible = false


                    }
                    is ApiState.Empty ->{

                    }
                }
            }
        }
    }

    private fun initRecylcerView(){

        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = postAdapter

            }
        }
    }
}