package com.pm.carslim.ui.voiture

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.pm.carslim.viewmodels.VoitureDetailViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoitureDetailScreen(
    voitureDetailViewModel: VoitureDetailViewModel,
    voitureId: Int,
    navController: NavController,
    onBackPressed: () -> Unit

) {
    val voiture by voitureDetailViewModel.voitureDetail.collectAsState()

    // Affiche un message de suppresion , edit
    var showSuccessMessage by remember { mutableStateOf(false) }
    val message by voitureDetailViewModel.message.collectAsState()
    val context = LocalContext.current


    // Charge la facture avec l'ID récupéré via les arguments
    LaunchedEffect(voitureId) {
        voitureDetailViewModel.loadVoitureDetail(voitureId)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Détails de la voiture",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp, end = 25.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                )
            }
        }, navigationIcon = {
            IconButton(onClick = { navController.navigate("client_detail/${voiture?.client?.id_client}") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            voiture?.let { dataVoiture ->
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
                                    text = "${dataVoiture.marque} ${dataVoiture.modele}",
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
                                        // Informations de la voiture
                                        Text("Propriétaire : ${dataVoiture.client.nom} ${dataVoiture.client.prenom}")
                                        Text("Année : ${dataVoiture.annee}")
                                        Text("Immatriculation : ${dataVoiture.immatriculation}")
                                        Text("Kilometrage : ${dataVoiture.kilometrage}")
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
                                                        navController.navigate("edit_car/${dataVoiture.id_voiture}")
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            Icons.Outlined.Edit,
                                                            contentDescription = null
                                                        )
                                                    })
                                                DropdownMenuItem(text = { Text("Delete") },
                                                    onClick = {

                                                        voitureDetailViewModel.deleteVoiture(
                                                            dataVoiture.id_voiture
                                                        )
                                                        expanded = false
                                                        showSuccessMessage = true


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

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Réparations :",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(5.dp)
                        )

                        if (dataVoiture.reparations.isNotEmpty()) {
                            dataVoiture.reparations.forEach { reparation ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("Description : ${reparation.description}")
                                        Text("Coût : ${reparation.cout} €")
                                        Text("Quantité : ${reparation.quantite}")
                                    }
                                }
                            }
                        } else {
                            Text("Aucune réparation associée")
                        }
                    }


                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Factures :",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(5.dp)
                        )

                        if (dataVoiture.factures.isNotEmpty()) {
                            dataVoiture.factures.forEach { facture ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("Facture # ${facture.id_facture}")
                                        Text("Date : ${facture.date} €")
                                        Text("Montant Total : ${facture.montant}")
                                    }
                                }
                            }
                        } else {
                            Text("Aucune facture associée")
                        }
                    }
                }
            } ?: CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(500)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onBackPressed()
            showSuccessMessage = false
        }
    }

}