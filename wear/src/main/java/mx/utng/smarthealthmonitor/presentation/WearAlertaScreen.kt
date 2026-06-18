package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun WearAlertaScreen(
    fc: Int,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {

    var alertaEnviada by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterVertically
        )
    ) {

        Text(
            text = "FC: 72 bpm",
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colors.error
        )

        if (!alertaEnviada) {

            Text(
                text = "¿Enviar alerta?",
                style = MaterialTheme.typography.body2
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        alertaEnviada = true
                    },

                    modifier = Modifier.size(52.dp),

                    colors = ButtonDefaults.primaryButtonColors(
                        backgroundColor = MaterialTheme.colors.error
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirmar alerta"
                    )
                }

                Button(
                    onClick = onCancelar,

                    modifier = Modifier.size(52.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar"
                    )
                }
            }

        } else {

            Text(
                text = "✅ Alerta enviada",
                style = MaterialTheme.typography.title3
            )

            Button(
                onClick = onConfirmar,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Text(
                    text = "Aceptar"
                )
            }
        }
    }
}