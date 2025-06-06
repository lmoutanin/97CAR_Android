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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.carslim.data.models.Voiture
import com.pm.carslim.ui.theme.CarSlimTheme
import com.pm.carslim.viewmodels.VoitureViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVoitureScreen(
    voitureViewModel: VoitureViewModel, onBackPressed: () -> Unit
) {
    var annee by remember { mutableStateOf("") }
    var marque by remember { mutableStateOf("") }
    var modele by remember { mutableStateOf("") }
    var immatriculation by remember { mutableStateOf("") }
    var kilometrage by remember { mutableStateOf("") }
    var client_id by remember { mutableStateOf("") }

    var nomClient: String = ""  // Pour sauvegarde un client

    var expanded by remember { mutableStateOf(false) }
    val clients by voitureViewModel.clients.collectAsState()

    val message by voitureViewModel.message.collectAsState() // Pour message retourne par addVoiture
    var showSuccessMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Scaffold(topBar = {
        TopAppBar(modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 25.dp)
            .fillMaxWidth(),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text("Ajout d'une voiture")
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
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp), onClick = {
                        expanded = true
                        voitureViewModel.fetchClients()
                    },


                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(
                        text = if (client_id.isEmpty()) "Sélectionner un client" else nomClient,
                        modifier = Modifier.fillMaxWidth(),
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
                            client_id = client.id_client.toString()
                            nomClient = "${client.nom} ${client.prenom}"
                            expanded = false
                        })
                    }
                }

                // FIN ADD CLIENT


                OutlinedTextField(value = marque,
                    onValueChange = { marque = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    label = { Text("Marque") })
                OutlinedTextField(value = modele,
                    onValueChange = { modele = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    label = { Text("Modèle") })
                OutlinedTextField(value = annee,
                    onValueChange = {
                        if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                            annee = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    label = { Text("Année") })
                OutlinedTextField(value = immatriculation,
                    onValueChange = { immatriculation = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    label = { Text("Immatriculation") })
                OutlinedTextField(value = kilometrage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    onValueChange = {
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                            kilometrage = it
                        }
                    },
                    label = { Text("Kilométrage") })



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {

                            if (annee.isNotEmpty() && marque.isNotEmpty() && modele.isNotEmpty() && immatriculation.isNotEmpty() && kilometrage.isNotEmpty() && client_id.isNotEmpty()) {

                                // Instancie  object Voiture
                                val voiture = Voiture(
                                    annee = annee,
                                    marque = marque.uppercase(),
                                    modele = modele.uppercase(),
                                    immatriculation = immatriculation.uppercase(),
                                    kilometrage = kilometrage,
                                    client_id = client_id
                                )

                                voitureViewModel.addVoiture(voiture) // Add une voiture
                                showSuccessMessage = true
                            }

                        }, shape = RoundedCornerShape(2.dp)
                    ) {
                        Text("Ajouter")

                        // Affiche un message  et retourne a la page precedente
                        LaunchedEffect(showSuccessMessage) {
                            if (showSuccessMessage) {
                                delay(500) // Un delai avant affiche le message et de retourne a l'accueil
                                Toast.makeText(
                                    context,
                                    message + " " + marque + " " + modele,
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                                showSuccessMessage = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAddVoitureScreen() {
    CarSlimTheme {
        AddVoitureScreen(voitureViewModel = VoitureViewModel(), onBackPressed = {})
    }
}