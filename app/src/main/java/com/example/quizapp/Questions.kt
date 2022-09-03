package com.example.quizapp

import com.QuestonAnswers
import retrofit2.Call
import retrofit2.http.GET


interface Questions {

      @GET("https://b4e7d359-c58f-4aa3-a314-726b3baa3852.mock.pstmn.io/?quiz=true")
      fun getQuestion():Call<List<QuestonAnswers>>
      {

      }
}