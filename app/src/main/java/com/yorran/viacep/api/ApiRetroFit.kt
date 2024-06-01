package com.yorran.viacep.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRetroFit {
    @GET("ws/{cep}/json/")
    fun setEndereco(@Path("cep") cep:String): Call<Endereco>


}