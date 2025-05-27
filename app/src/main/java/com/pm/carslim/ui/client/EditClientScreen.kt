package com.pm.carslim.ui.client

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pm.carslim.data.models.Client
import com.pm.carslim.viewmodels.ClientDetailViewModel

import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: Int, clientDetailViewModel: ClientDetailViewModel, onBackPressed: () -> Unit
) {

    // Charge le client avec l'ID récupéré via les arguments
    LaunchedEffect(clientId) {
        clientDetailViewModel.loadClient(clientId)
    }

    val client by clientDetailViewModel.client.collectAsState()

    var nom by remember { mutableStateOf(client?.nom?.toString()) }
    var prenom by remember { mutableStateOf(client?.prenom?.toString()) }
    var telephone by remember { mutableStateOf(client?.telephone) }
    var mel by remember { mutableStateOf(client?.mel) }
    var adresse by remember { mutableStateOf(client?.adresse) }
    var codePostal by remember { mutableStateOf(client?.code_postal) }
    var ville by remember { mutableStateOf(client?.ville) }

    val message by clientDetailViewModel.message.collectAsState()
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
                    Text("Edit d'un client")
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

                OutlinedTextField(value = nom.toString(),
                    onValueChange = { nom = it },
                    label = { Text("Nom") })
                OutlinedTextField(value = prenom.toString(),
                    onValueChange = { prenom = it },
                    label = { Text("Prénom") })
                OutlinedTextField(value = telephone.toString(),
                    onValueChange = { telephone = it },
                    label = { Text("Téléphone") })
                OutlinedTextField(value = mel.toString(),
                    onValueChange = { mel = it },
                    label = { Text("Email") })
                OutlinedTextField(value = adresse.toString(),
                    onValueChange = { adresse = it },
                    label = { Text("Adresse") })
                OutlinedTextField(value = codePostal.toString(),
                    onValueChange = { codePostal = it },
                    label = { Text("Code Postal") })
                OutlinedTextField(value = ville.toString(),
                    onValueChange = { ville = it },
                    label = { Text("Ville") })

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
                            if (nom == "") nom = client?.nom
                            if (prenom == "") prenom = client?.prenom
                            if (telephone == "") telephone = client?.telephone
                            if (mel == "") mel = client?.mel
                            if (adresse == "") adresse = client?.adresse
                            if (codePostal == "") codePostal = client?.code_postal
                            if (ville == "") ville = client?.ville

                            var indice = 0
                            var newPrenom = ""

                            // Mettre la premier lettre du Prénom en Majuscule
                            for (i in prenom!!.indices) {
                                if(indice == 0) newPrenom = prenom!![i].uppercase() else newPrenom = newPrenom + prenom!![i]
                                indice += 1
                            }
                            prenom = newPrenom


                            // Instancie  object Client
                            val editclient = Client(
                                id_client = client?.id_client,
                                nom = nom.toString().uppercase(),
                                prenom = prenom.toString(),
                                telephone = telephone ,
                                mel = mel.toString().lowercase(),
                                adresse = adresse.toString().lowercase(),
                                code_postal = codePostal,
                                ville = ville.toString().uppercase()
                            )

                            // Appel de la methode editClient() pour modifier le client
                            clientDetailViewModel.editClient(editclient)
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

