package com.pm.carslim.ui.facture

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.pm.carslim.viewmodels.FactureDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactureDetailScreen(
    viewModel: FactureDetailViewModel, factureId: Int, onBackPressed: () -> Unit

) {
    val facture by viewModel.factureDetail.collectAsState()

    Log.d("FactureScreen", "factureId : $factureId")

    // Charge la facture avec l'ID récupéré via les arguments
    LaunchedEffect(factureId) {
        viewModel.loadFactureDetail(factureId)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text("Détails de la facture")
            }
        }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        })
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            facture?.let { dataFacture ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
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
                    }
                    item {
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
                                Text("Modele : ${dataFacture.voiture.modele}")
                                Text("Année : ${dataFacture.voiture.annee}")
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Réparations :", style = MaterialTheme.typography.headlineSmall)

                        if (dataFacture.reparations.isNotEmpty()) {
                            dataFacture.reparations.forEach { reparation ->
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
                }
            } ?: CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}