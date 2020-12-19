package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "https://cat-fact.herokuapp.com"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.gen_new_fact)
        getData()
        button.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        val text = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        text.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = api.getFacts().awaitResponse()
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        Log.d(TAG, data.text)

                        withContext(Dispatchers.Main) {
                            text.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            text.text = data.text
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Whoops, something bad happened", Toast.LENGTH_SHORT).show()
                        text.text = "You're not connected to the Internet"
                        text.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }

            }

    }
}