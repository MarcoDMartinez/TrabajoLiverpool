package mx.edu.mx.ConversorGerardob.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier



@Composable
fun Boton(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
){
    Button(
        onClick = { onClick(texto) },
        modifier = modifier,

        ) {
        Text(text = texto, style = MaterialTheme.typography.bodyLarge)
    }
}

