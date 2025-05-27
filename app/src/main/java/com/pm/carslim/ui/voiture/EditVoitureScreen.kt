package com.pm.carslim.ui.voiture

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.viewmodels.VoitureDetailViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVoitureScreen(
    voitureId: Int, voitureDetailViewModel: VoitureDetailViewModel, onBackPressed: () -> Unit
) {

    // Charge la voiture avec l'ID récupéré via les arguments
    LaunchedEffect(voitureId) {
        voitureDetailViewModel.loadVoitureDetail(voitureId)
    }

    val voiture by voitureDetailViewModel.voitureDetail.collectAsState()
    val clients by voitureDetailViewModel.clients.collectAsState()

    var marque by remember { mutableStateOf(voiture?.marque) }
    var modele by remember { mutableStateOf(voiture?.modele) }
    var annee by remember { mutableStateOf(voiture?.annee) }
    var immatriculation by remember { mutableStateOf(voiture?.immatriculation) }
    var kilometrage by remember { mutableStateOf(voiture?.kilometrage) }

    var id_client by remember { mutableStateOf(voiture?.client?.id_client) }
    var nom by remember { mutableStateOf(voiture?.client?.nom) }
    var prenom by remember { mutableStateOf(voiture?.client?.prenom) }

    val message by voitureDetailViewModel.message.collectAsState()
    var showSuccessMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var nomClient: String = nom + " " + prenom  // Pour sauvegarde affichage pour  un client

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 25.dp)
            .fillMaxWidth(),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text("Edit d'une voiture")
                }
            })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // La liste déroulante permet de sélectionner un client.
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxHeight(0.09f)
                        .fillMaxWidth(0.74f), onClick = {
                        expanded = true
                        voitureDetailViewModel.fetchClients()
                    },

                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(
                        text = nomClient, modifier = Modifier.fillMaxWidth()
                    )

                }
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp * 0.92f })
                ) {
                    clients.forEach { client ->
                        DropdownMenuItem(text = {
                            Text(
                                "${client.nom} ${client.prenom}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }, onClick = {
                            id_client = client.id_client
                            nomClient = "${client.nom} ${client.prenom}"
                            expanded = false
                        })
                    }
                }

                // FIN ADD CLIENT

                OutlinedTextField(value = marque.toString(),
                    onValueChange = { marque = it },
                    label = { Text("Marque") })
                OutlinedTextField(value = modele.toString(),
                    onValueChange = { modele = it },
                    label = { Text("Modèle") })
                OutlinedTextField(value = annee.toString(),
                    onValueChange = { annee = it },
                    label = { Text("Année") })
                OutlinedTextField(value = immatriculation.toString(),
                    onValueChange = { immatriculation = it },
                    label = { Text("Immatriculation") })
                OutlinedTextField(value = kilometrage.toString(),
                    onValueChange = { kilometrage = it },
                    label = { Text("Kilométrage") })


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            // Verifier que les variables ne sont pas  vide .
                            if (id_client.toString() == "") id_client = voiture?.client?.id_client
                            if (marque == "") marque = voiture?.marque
                            if (modele == "") modele = voiture?.modele
                            if (annee == "") annee = voiture?.annee
                            if (immatriculation == "") immatriculation = voiture?.immatriculation
                            if (kilometrage == "") kilometrage = voiture?.kilometrage


                            // Instancie  object Voiture
                            val editVoiture = Voiture(
                                id_voiture = voiture?.id_voiture,
                                annee = annee.toString(),
                                marque = marque.toString().uppercase(),
                                modele = modele.toString().uppercase(),
                                immatriculation = immatriculation.toString().uppercase(),
                                kilometrage = kilometrage.toString(),
                                client_id = id_client.toString()
                            )


                            // Appel de la methode editVoiture() pour modifier la voiture
                            voitureDetailViewModel.editVoiture(editVoiture)
                            showSuccessMessage = true
                        },

                        modifier = Modifier.padding(top = 16.dp), shape = RoundedCornerShape(2.dp)

                    ) {
                        Text("Modifier")
                        LaunchedEffect(showSuccessMessage) {
                            if (showSuccessMessage) {
                                delay(500)
                                Toast.makeText(
                                    context, message + " " + nom + " " + prenom, Toast.LENGTH_SHORT
                                ).show()
                                showSuccessMessage = false
                                onBackPressed()
                            }
                        }

                    }
                }
            }
        }
    }
}


