package com.pm.carslim.ui.facture

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pm.carslim.viewmodels.FactureDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactureDetailScreen(
    viewModel: FactureDetailViewModel,
    factureId: Int,
    onBackPressed: () -> Unit

) {
    val facture by viewModel.factureDetail.collectAsState()

    Log.d("FactureScreen","factureId : $factureId")

    // Charge la facture avec l'ID récupéré via les arguments
    LaunchedEffect(factureId) {
        viewModel.loadFactureDetail(factureId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Détails de la facture")
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
            facture?.let { dataFacture ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Facture #$factureId",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text("Date : ${dataFacture.date_facture}")
                            Text("Montant : ${dataFacture.montant} €")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Voiture :", style = MaterialTheme.typography.headlineSmall)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Marque : ${dataFacture.voiture.marque}")
                            Text("Marque : ${dataFacture.voiture.modele}")
                            Text("Marque : ${dataFacture.voiture.annee}")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Réparations :", style = MaterialTheme.typography.headlineSmall)

                    if (dataFacture.reparations.isNotEmpty()) {
                        dataFacture.reparations.forEach { reparation ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
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
            } ?: CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}