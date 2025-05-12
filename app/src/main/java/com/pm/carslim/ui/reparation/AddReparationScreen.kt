package com.pm.carslim.ui.reparation




import android.util.Log
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
import com.pm.carslim.data.models.Reparation
import com.pm.carslim.ui.theme.CarSlimTheme
import com.pm.carslim.viewmodels.ClientViewModel
import com.pm.carslim.viewmodels.ReparationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReparationScreen(
    reparationViewModel: ReparationViewModel, onBackPressed: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var cout by remember { mutableStateOf("") }

    var showSuccessMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            Toast.makeText(context, "Réparation ajouté avec succès", Toast.LENGTH_SHORT).show()
            showSuccessMessage = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 25.dp)
            .fillMaxWidth(),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text("Ajout d'une réparation")
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
                OutlinedTextField(value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
                OutlinedTextField(value = cout,
                    onValueChange = { cout = it },
                    label = { Text("Cout") })


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            val reparation = Reparation(

                                description = description.toString() ,
                                cout = cout.toString()
                            )
                           reparationViewModel.addReparation(reparation)
                            showSuccessMessage = true
                            onBackPressed()
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = RoundedCornerShape(2.dp)

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
fun PreviewAddClientScreen() {
    val fakeViewModel = object :ReparationViewModel() {

    }

    CarSlimTheme {
        AddReparationScreen(fakeViewModel, {})
    }
}