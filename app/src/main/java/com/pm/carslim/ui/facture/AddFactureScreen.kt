package com.pm.carslim.ui.facture

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.carslim.data.models.Facture
import com.pm.carslim.ui.theme.CarSlimTheme
import com.pm.carslim.viewmodels.FactureViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFactureScreen(
    factureViewModel: FactureViewModel, onBackPressed: () -> Unit
) {
    var date by remember { mutableStateOf("") }
    var client_id by remember { mutableStateOf("") }
    var voiture_id by remember { mutableStateOf("") }
    var montant by remember { mutableStateOf("") }


    var showSuccessMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            Toast.makeText(context, "Facture ajouté avec succès", Toast.LENGTH_SHORT).show()
            showSuccessMessage = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text("Ajout d'une facture")
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
                OutlinedTextField(value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") })
                OutlinedTextField(value = client_id,
                    onValueChange = { client_id = it },
                    label = { Text("Client") })
                OutlinedTextField(value = voiture_id,
                    onValueChange = { voiture_id = it },
                    label = { Text("Voiture") })
                OutlinedTextField(value = montant,
                    onValueChange = { montant = it },
                    label = { Text("Montant") })


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            val facture = Facture(
                                date = date,
                                client_id = client_id,
                                voiture_id = voiture_id,
                                montant = montant

                            )

                            factureViewModel.addFacture(facture)
                            showSuccessMessage = true
                            onBackPressed()
                        }, modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Ajouter")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddFactureScreen() {
    val fakeViewModel = object : FactureViewModel() {
        override fun addFacture(facture: Facture) {
            Log.d("FakeViewModel", "Facture ajouté: $facture")
        }
    }

    CarSlimTheme {
        AddFactureScreen(factureViewModel = fakeViewModel, onBackPressed = {})
    }
}