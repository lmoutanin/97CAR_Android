package com.pm.carslim.data.remote


import com.pm.carslim.data.models.Client
import com.pm.carslim.data.models.Facture
import com.pm.carslim.data.models.FactureComplete
import com.pm.carslim.data.models.Reparation
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.data.models.VoitureComplete
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

    // CLIENT

    // Afficher les Clients
    @GET("clients")
    suspend fun getClients(): List<Client>

    // Afficher un Client
    @GET("clients/{clientId}")
    suspend fun getClientById(@Path("clientId") clientId: Int): Client?

    // Ajouter un Client
    @POST("clients")
    suspend fun addClient(@Body client: Client): Response<ResponseBody>

    // Modifier un Client
    @PUT("putclients/{clientId}")
    suspend fun editClientById(@Path("clientId") clientId: Int, @Body client: Client): Response<ResponseBody>

    // Supprimer un Client
    @DELETE("dropclients/{clientId}")
    suspend fun deleteClientById(@Path("clientId") clientId: Int): Response<ResponseBody>


    //FACTURE

    // Afficher des factures pour un client
    @GET("factures")
    suspend fun getFacturesByClientId(@Query("client_id") clientId: Int): List<Facture>

    // Afficher détail une facture
    @GET("factures/{factureId}")
    suspend fun getFactureById(@Path("factureId") factureId: Int): FactureComplete

    // Ajouter une facture
    @POST("factures")
    suspend fun addFacture(@Body facture: Facture): Response<ResponseBody>



    //VOITURE

    // Afficher des voitures pour un client
    @GET("voitures")
    suspend fun getVoituresByClientId(@Query("client_id") clientId: Int): List<Voiture>

    // Afficher détail une voiture
    @GET("voitures/{voitureId}")
    suspend fun getVoitureById(@Path("voitureId") voitureId: Int): VoitureComplete

    // Ajouter une voiture
    @POST("voitures")
    suspend fun addVoiture(@Body voiture: Voiture): Response<ResponseBody>

    // Modifier une voiture
    @PUT("putvoitures/{voitureId}")
    suspend fun editVoitureById(@Path("voitureId") voitureId: Int, @Body voiture: Voiture): Response<ResponseBody>

    // Supprimer une voiture
    @DELETE("dropvoitures/{voitureId}")
    suspend fun deleteVoitureById(@Path("voitureId") voitureId: Int): Response<ResponseBody>





}