package com.pm.carslim.ui.client


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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pm.carslim.viewmodels.ClientDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    clientId: Int,
    viewModelClient: ClientDetailViewModel,
    navController: NavController,
    onBackPressed: () -> Unit
) {

    // Charge le client avec l'ID récupéré via les arguments
    LaunchedEffect(clientId) {
        viewModelClient.loadClient(clientId)
    }

    val client by viewModelClient.client.collectAsState()
    val factures by viewModelClient.factures.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Détails du client")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            client?.let { clientData ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                            Text(
                                text = "${clientData.nom} ${clientData.prenom}",
                                style = MaterialTheme.typography.headlineMedium
                            )

                            HorizontalDivider()

                            clientData.telephone?.let { telephone ->
                                DetailRow(icon = "📞", label = "Téléphone", value = telephone)
                            }

                            clientData.mel?.let { email ->
                                DetailRow(icon = "✉️", label = "Email", value = email)
                            }

                            clientData.adresse?.let { adresse ->
                                DetailRow(icon = "", label = "Adresse", value = adresse)
                            }

                            clientData.code_postal?.let { codePostal ->
                                DetailRow(icon = "", label = "Code Postal", value = codePostal)
                            }

                            clientData.ville?.let { ville ->
                                DetailRow(icon = "📍", label = "Ville", value = ville)
                            }
                        }
                    }

                    Text(
                        text = "Factures",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    if (factures.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                count = factures.size,
                                itemContent = { index ->
                                    val facture = factures[index]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
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
                            )
                        }
                    } else {
                        Text(
                            text = "Aucune facture trouvée",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$icon $label :")
        Text(text = value)
    }
}