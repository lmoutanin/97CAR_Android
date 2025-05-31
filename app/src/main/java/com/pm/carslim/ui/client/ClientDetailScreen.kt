package com.pm.carslim.ui.client


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pm.carslim.viewmodels.ClientDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    clientId: Int,
    clientDetailViewModel: ClientDetailViewModel,
    navController: NavController,
    onBackPressed: () -> Unit
) {

    // Charge le client avec l'ID récupéré via les arguments
    LaunchedEffect(clientId) {
        clientDetailViewModel.loadClient(clientId)
    }

    // Affiche un message de suppresion , edit
    var showSuccessMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            showSuccessMessage = false
        }
    }

    val client by clientDetailViewModel.client.collectAsState()
    val voitures by clientDetailViewModel.voitures.collectAsState()
    val factures by clientDetailViewModel.factures.collectAsState()

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Box {
                Text(
                    text = "Détails du client",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, end = 25.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { navController.navigate("client_list") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            client?.let { clientData ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // En-tête avec le nom et le menu

                                Text(
                                    text = "${clientData.nom} ${clientData.prenom}",
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                HorizontalDivider(thickness = 2.dp)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        // Informations du client
                                        clientData.telephone?.let { telephone ->
                                            Text(text = "📞 Téléphone: $telephone")
                                        }

                                        clientData.mel?.let { email ->
                                            Text(text = "✉️ Email: $email")
                                        }

                                        clientData.adresse?.let { adresse ->
                                            Text(text = "🏠 Adresse: $adresse")
                                        }

                                        clientData.code_postal?.let { codePostal ->
                                            Text(text = "📪 Code Postal: $codePostal")
                                        }

                                        clientData.ville?.let { ville ->
                                            Text(text = "📍 Ville: $ville")
                                        }
                                    }
                                    Column {
                                        // Menu
                                        var expanded by remember { mutableStateOf(false) }
                                        Box {
                                            IconButton(onClick = { expanded = true }) {
                                                Icon(
                                                    Icons.Default.MoreVert,
                                                    contentDescription = "Options"
                                                )
                                            }
                                            DropdownMenu(expanded = expanded,
                                                onDismissRequest = { expanded = false }) {
                                                DropdownMenuItem(text = { Text("Edit") },
                                                    onClick = {
                                                        expanded = false
                                                        navController.navigate("edit_client/${clientData.id_client}")
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Outlined.Edit,
                                                            contentDescription = null
                                                        )
                                                    })
                                                DropdownMenuItem(text = { Text("Delete") },
                                                    onClick = {

                                                        clientDetailViewModel.deleteClient(
                                                            clientData.id_client!!.toInt()
                                                        )
                                                        expanded = false
                                                        showSuccessMessage = true
                                                        message = "Client supprimer avec succès"
                                                        navController.navigate("client_list")
                                                    },

                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Outlined.Delete,
                                                            contentDescription = null
                                                        )
                                                    })
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    //Détail Voiture du Client


                    item {
                        Text(
                            "Voitures :",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(5.dp)
                        )
                        if (voitures.isNotEmpty()) {
                            voitures.forEach { voiture ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable {
                                            navController.navigate("car_detail/${voiture.id_voiture}")
                                        },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier

                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "Marque :${voiture.marque}",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = "Modele : ${voiture.modele}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                            Text(
                                                text = "Immatriculation : ${voiture.immatriculation} ",
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                        }
                                    }
                                }
                            }

                        } else {
                            Text(
                                text = "Aucune voiture trouvée",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    //FIN Détail Voiture du Client


                    // Détail Facture du Client

                    item {
                        Text(
                            "Factures :",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(5.dp)
                        )
                        if (factures.isNotEmpty()) {
                            factures.forEach { facture ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable {
                                            navController.navigate("invoice_detail/${facture.id_facture}")
                                        },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Facture #${facture.id_facture}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Date : ${facture.date}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Montant total : ${facture.montant} €",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                        } else {
                            Text(
                                text = "Aucune facture trouvée",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    //FIN Détail Facture du Client
                }
            } ?: run {
                CircularProgressIndicator(  // effet de chargement
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$icon $label :")
        Text(text = value)
    }
}

