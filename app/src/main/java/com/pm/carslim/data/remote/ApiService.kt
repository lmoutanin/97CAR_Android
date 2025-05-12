package com.pm.carslim.data.remote


import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Facture
import com.pm.carslim.data.models.FactureComplete
import com.pm.carslim.data.models.Reparation
import com.pm.carslim.data.models.Voiture
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // clients
    @GET("clients")
    suspend fun getClients(): List<Client>

    @GET("clients/{clientId}")
    suspend fun getClientById(@Path("clientId") clientId: Int): Client?

    @POST("clients")
    suspend fun addClient(@Body client: Client): Response<ResponseBody>

    @PUT("putclients/{clientId}")
    suspend fun editClient(@Path("clientId") clientId: String, @Body client: Client): Response<ResponseBody>

    @DELETE("dropclients/{clientId}")
    suspend fun deleteClientById(@Path("clientId") clientId: String): Response<ResponseBody>


    // facture détaillée
    @GET("factures/{factureId}")
    suspend fun getFactureById(@Path("factureId") factureId: Int): FactureComplete

    // liste des factures pour un client
    @GET("factures")
    suspend fun getFacturesByClientId(@Query("client_id") clientId: Int): List<Facture>

    @POST("factures")
    suspend fun addFacture(@Body facture: Facture): Response<ResponseBody>

    // liste des voitures pour un client
    @GET("voitures")
    suspend fun getVoitures(@Query("client_id") clientId: Int): List<Voiture>

    @POST("voitures")
    suspend fun addVoiture(@Body voiture: Voiture): Response<ResponseBody>

    @GET("reparations")
    suspend fun getReparation(): List<Reparation>

    @POST("reparations")
    suspend fun addReparation(@Body reparation: Reparation): Response<ResponseBody>

}