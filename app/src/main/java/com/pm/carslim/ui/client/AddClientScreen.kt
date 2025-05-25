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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.carslim.data.models.Client
import com.pm.carslim.ui.theme.CarSlimTheme
import com.pm.carslim.viewmodels.ClientViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    clientViewModel: ClientViewModel, onBackPressed: () -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var mel by remember { mutableStateOf("") }
    var adresse by remember { mutableStateOf("") }
    var codePostal by remember { mutableStateOf("") }
    var ville by remember { mutableStateOf("") }

    val message by clientViewModel.message.collectAsState()

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
                    Text("Ajout d'un client")
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
                OutlinedTextField(value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") })
                OutlinedTextField(value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("Prénom") })
                OutlinedTextField(value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Téléphone") })
                OutlinedTextField(value = mel,
                    onValueChange = { mel = it },
                    label = { Text("Email") })
                OutlinedTextField(value = adresse,
                    onValueChange = { adresse = it },
                    label = { Text("Adresse") })
                OutlinedTextField(value = codePostal,
                    onValueChange = { codePostal = it },
                    label = { Text("Code Postal") })
                OutlinedTextField(value = ville,
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
                            // Verifier que les champs ne sont pas vide .
                            if (nom.isNotEmpty() && prenom.isNotEmpty() && telephone.isNotEmpty() && mel.isNotEmpty() && adresse.isNotEmpty() && codePostal.isNotEmpty() && ville.isNotEmpty()) {

                                val client = Client(nom = nom,
                                    prenom = prenom,
                                    telephone = telephone.takeIf { it.isNotEmpty() },
                                    mel = mel.takeIf { it.isNotEmpty() },
                                    adresse = adresse.takeIf { it.isNotEmpty() },
                                    code_postal = codePostal.takeIf { it.isNotEmpty() },
                                    ville = ville.takeIf { it.isNotEmpty() })
                                clientViewModel.addClient(client)
                                showSuccessMessage = true

                            }
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = RoundedCornerShape(2.dp)

                    ) {
                        Text("Ajouter")
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

@Preview(showBackground = true)
@Composable
fun PreviewAddClientScreen() {
    val fakeViewModel = object : ClientViewModel() {

    }

    CarSlimTheme {
        AddClientScreen(clientViewModel = fakeViewModel, onBackPressed = {})
    }
}