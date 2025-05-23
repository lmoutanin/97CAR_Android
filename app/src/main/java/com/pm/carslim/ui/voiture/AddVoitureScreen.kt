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
import com.pm.carslim.viewmodels.ClientViewModel
import com.pm.carslim.viewmodels.VoitureViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVoitureScreen(
    clientViewModel: ClientViewModel, voitureViewModel: VoitureViewModel, onBackPressed: () -> Unit
) {
    var annee by remember { mutableStateOf("") }
    var marque by remember { mutableStateOf("") }
    var modele by remember { mutableStateOf("") }
    var immatriculation by remember { mutableStateOf("") }
    var kilometrage by remember { mutableStateOf("") }
    var client_id by remember { mutableStateOf("") }


    var expanded by remember { mutableStateOf(false) }
    val clients by clientViewModel.clients.collectAsState()
    var nomClient: String = ""

    val message by voitureViewModel.message.collectAsState()
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

                // ADD CLIENT
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier

                        .padding(top = 7.dp)
                        .align(Alignment.Start)
                        .fillMaxHeight(0.08f),
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

                OutlinedTextField(value = annee,
                    onValueChange = { annee = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Année") })
                OutlinedTextField(value = marque,
                    onValueChange = { marque = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Marque") })
                OutlinedTextField(value = modele,
                    onValueChange = { modele = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Modèle") })
                OutlinedTextField(value = immatriculation,
                    onValueChange = { immatriculation = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Immatriculation") })
                OutlinedTextField(value = kilometrage,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { kilometrage = it },
                    label = { Text("Kilométrage") })



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {

                            val voiture = Voiture(
                                annee = annee,
                                marque = marque,
                                modele = modele,
                                immatriculation = immatriculation,
                                kilometrage = kilometrage,
                                client_id = client_id
                            )

                            voitureViewModel.addVoiture(voiture)
                            showSuccessMessage = true

                        }, shape = RoundedCornerShape(2.dp)
                    ) {
                        Text("Ajouter")
                        LaunchedEffect(showSuccessMessage) {
                            if (showSuccessMessage) {
                                delay(200)
                                Toast.makeText(context, message+" "+marque+" "+modele, Toast.LENGTH_SHORT).show()
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
        AddVoitureScreen(clientViewModel = ClientViewModel(),
            voitureViewModel = VoitureViewModel(),
            onBackPressed = {})
    }
}