package com.pm.carslim


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pm.carslim.data.models.Client
import com.pm.carslim.factory.ClientDetailViewModelFactory
import com.pm.carslim.factory.FactureDetailViewModelFactory
import com.pm.carslim.factory.VoitureDetailViewModelFactory
import com.pm.carslim.ui.client.AddClientScreen
import com.pm.carslim.ui.client.ClientDetailScreen
import com.pm.carslim.ui.client.EditClientScreen
import com.pm.carslim.ui.facture.AddFactureScreen
import com.pm.carslim.ui.facture.FactureDetailScreen

import com.pm.carslim.ui.theme.CarSlimTheme
import com.pm.carslim.ui.voiture.AddVoitureScreen
import com.pm.carslim.ui.voiture.EditVoitureScreen

import com.pm.carslim.ui.voiture.VoitureDetailScreen
import com.pm.carslim.viewmodels.ClientDetailViewModel
import com.pm.carslim.viewmodels.ClientViewModel
import com.pm.carslim.viewmodels.FactureDetailViewModel
import com.pm.carslim.viewmodels.FactureViewModel
import com.pm.carslim.viewmodels.VoitureDetailViewModel

import com.pm.carslim.viewmodels.VoitureViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    private val clientViewModel: ClientViewModel by viewModels()


    private val clientDetailViewModel: ClientDetailViewModel by viewModels {
        ClientDetailViewModelFactory(applicationContext)
    }


    private val voitureViewModel: VoitureViewModel by viewModels()

    private val voitureDetailViewModel: VoitureDetailViewModel by viewModels {
        VoitureDetailViewModelFactory(applicationContext)
    }


    private val factureViewModel: FactureViewModel by viewModels()

    private val factureDetailViewModel: FactureDetailViewModel by viewModels {
        FactureDetailViewModelFactory(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarSlimTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "client_list") {
                    composable("client_list") {
                        ClientListScreen(clientViewModel = clientViewModel,
                            navigateToAddClient = { navController.navigate("add_client") },
                            navigateToClientDetail = { clientId ->
                                navController.navigate("client_detail/$clientId")
                            },
                            navigateToAddCar = { navController.navigate("add_car") },
                            navigateToAddInvoice = { navController.navigate("add_invoice") },
                            navigateToAddRepair = { navController.navigate("add_repair") })
                    }

                    composable("add_client") {
                        AddClientScreen(clientViewModel = clientViewModel, onBackPressed = {
                            clientViewModel.fetchClients() // Rafraîchit la liste des clients
                            navController.popBackStack()
                        })
                    }

                    composable("edit_client/{id}") { backStackEntry ->
                        val clientId = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                        EditClientScreen(clientId, clientDetailViewModel, onBackPressed = {
                            clientViewModel.fetchClients() // Rafraîchit la liste des clients
                            navController.popBackStack()
                        })
                    }

                    composable("client_detail/{id}") { backStackEntry ->
                        val clientId = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                        ClientDetailScreen(clientId,
                            clientDetailViewModel,
                            navController,
                            onBackPressed = { navController.popBackStack() })
                    }


                    composable("add_invoice") {
                        AddFactureScreen(factureViewModel = factureViewModel,
                            onBackPressed = { navController.popBackStack() })
                    }

                    composable("invoice_detail/{invoiceId}") { backStackEntry ->
                        val invoiceId =
                            backStackEntry.arguments?.getString("invoiceId")?.toInt() ?: -1
                        FactureDetailScreen(factureDetailViewModel = factureDetailViewModel,
                            factureId = invoiceId,
                            onBackPressed = { navController.popBackStack() })
                    }

                    composable("add_car") {
                        AddVoitureScreen(voitureViewModel = voitureViewModel,
                            onBackPressed = { navController.popBackStack() })
                    }

                    composable("edit_car/{id}") { backStackEntry ->
                        val voitureId = backStackEntry.arguments?.getString("id")?.toInt() ?: -1
                        EditVoitureScreen(voitureId, voitureDetailViewModel, onBackPressed = {
                            voitureViewModel.fetchClients() // Rafraîchit la liste des clients
                            navController.popBackStack()
                        })
                    }


                    composable("car_detail/{carId}") { backStackEntry ->
                        val carId = backStackEntry.arguments?.getString("carId")?.toInt() ?: -1
                        VoitureDetailScreen(voitureDetailViewModel = voitureDetailViewModel,
                            voitureId = carId,
                            navController,
                            onBackPressed = { navController.popBackStack() })
                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,
    clientViewModel: ClientViewModel,
    navigateToAddClient: () -> Unit,
    navigateToClientDetail: (Int) -> Unit,
    navigateToAddCar: () -> Unit,
    navigateToAddInvoice: () -> Unit,
    navigateToAddRepair: () -> Unit
) {

    val clients by clientViewModel.clients.collectAsState()
    val isLoading by clientViewModel.isLoading.collectAsState()

    clientViewModel.fetchClients()

    Scaffold(topBar = {
        TopAppBar(title = {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.clients_title))
            }
        })
    }, floatingActionButton = {
        FloatingActionButtonWithMenu(
            onAddClient = navigateToAddClient,
            onAddCar = navigateToAddCar,
            onAddInvoice = navigateToAddInvoice,
            onAddRepair = navigateToAddRepair
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                clients.isEmpty() -> EmptyStateMessage()
                else -> ClientList(clients = clients, onClientClick = { client ->
                    // Use the client ID to navigate to details
                    navigateToClientDetail(client.id_client!!)
                })
            }
        }
    }
}


@Composable
fun ClientList(
    clients: List<Client>, onClientClick: (Client) -> Unit
) {
    LazyColumn {
        items(clients) { client ->
            ClientItem(client = client, onClick = { onClientClick(client) })
        }
    }
}


@Composable
fun ClientItem(client: Client, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${client.nom} ${client.prenom}",
                    style = MaterialTheme.typography.titleLarge
                )
                client.telephone?.let { Text(text = "📞 $it") }
                client.mel?.let { Text(text = "✉️ $it") }
                client.ville?.let { Text(text = "📍 $it") }
            }


        }
    }
}

@Composable
fun EmptyStateMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Aucun client trouvé", modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun FloatingActionButtonWithMenu(
    onAddClient: () -> Unit, onAddCar: () -> Unit, onAddInvoice: () -> Unit, onAddRepair: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        FloatingActionButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Add, contentDescription = "Ajouter")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Ajouter un client") }, onClick = {
                onAddClient()
                expanded = false
            }, leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            })
            DropdownMenuItem(text = { Text("Ajouter une voiture") }, onClick = {
                onAddCar()
                expanded = false
            }, leadingIcon = {
                Icon(Icons.Filled.LocationOn, contentDescription = null)
            })
            DropdownMenuItem(text = { Text("Ajouter une facture") }, onClick = {
                onAddInvoice()
                expanded = false
            }, leadingIcon = {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            })
            DropdownMenuItem(text = { Text("Ajouter une réparation") }, onClick = {
                onAddRepair()
                expanded = false
            }, leadingIcon = {
                Icon(Icons.Default.Build, contentDescription = null)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClientListScreen() {
    val fakeClients = listOf(
        Client(
            id_client = 1,
            nom = "Dupont",
            prenom = "Jean",
            telephone = "0123456789",
            mel = "jean.dupont@email.com",
            adresse = "12 rue Véronèse",
            code_postal = "97410",
            ville = "Saint-Pierre"
        ), Client(
            id_client = 2,
            nom = "Durand",
            prenom = "Sophie",
            telephone = "0987654321",
            mel = "sophie.durand@email.com",
            adresse = "12 rue Véronèse",
            code_postal = "97410",
            ville = "Saint-Pierre"
        )
    )

    val fakeViewModel = object : ClientViewModel() {
        override val clients: StateFlow<List<Client>> = MutableStateFlow(fakeClients)
        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)
    }

    CarSlimTheme {
        ClientListScreen(clientViewModel = fakeViewModel,
            navigateToAddClient = {},
            navigateToClientDetail = {},
            navigateToAddCar = {},
            navigateToAddInvoice = {},
            navigateToAddRepair = {})
    }
}