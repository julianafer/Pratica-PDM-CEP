package com.example.prtica_retrofit_cep

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prtica_retrofit_cep.models.Endereco
import com.example.prtica_retrofit_cep.network.RetrofitClient
import com.example.prtica_retrofit_cep.ui.theme.PráticaRetrofitCEPTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PráticaRetrofitCEPTheme {
                CepLookupScreen()
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CepLookupScreen() {
    var cep by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf<Endereco?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "BUSCA CEP",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = cep,
                onValueChange = { cep = it },
                label = { Text("Digite o CEP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            endereco = RetrofitClient.enderecoService.getDetailsByCep(cep)
                            error = null
                        } catch (e: HttpException) {
                            error = "Erro ao buscar dados. Verifique o CEP e tente novamente."
                            endereco = null
                        } catch (e: Exception) {
                            error = "Erro desconhecido."
                            endereco = null
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            if (error != null) {
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            } else if (endereco != null) {
                // Informações do endereço
                Text("CEP: ${endereco!!.cep}")
                Text("Logradouro: ${endereco!!.logradouro}")
                Text("Complemento: ${endereco!!.complemento}")
                Text("Unidade: ${endereco!!.unidade}")
                Text("Bairro: ${endereco!!.bairro}")
                Text("Localidade: ${endereco!!.localidade}")
                Text("UF: ${endereco!!.uf}")
                Text("Estado: ${endereco!!.estado}")
                Text("Região: ${endereco!!.regiao}")
                Text("IBGE: ${endereco!!.ibge}")
                Text("GIA: ${endereco!!.gia}")
                Text("DDD: ${endereco!!.ddd}")
                Text("SIAFI: ${endereco!!.siafi}")
            }
        }
    }
}