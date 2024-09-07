package com.example.prtica_retrofit_cep.network

import android.telecom.Call
import com.example.prtica_retrofit_cep.models.Endereco
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EnderecoServiceIF {

    @GET("{cep}/json/")
    suspend fun getDetailsByCep(
        @Path("cep") cep: String,
    ): Endereco

}