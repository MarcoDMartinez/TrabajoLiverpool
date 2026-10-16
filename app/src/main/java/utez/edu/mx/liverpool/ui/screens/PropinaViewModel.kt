package mx.edu.mx.ConversorGerardob.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PropinaViewModel: ViewModel() {

    val p1 = 0.05;

    var total_cuenta by mutableStateOf("")
    var resultado_n by mutableStateOf(0.0);


    //Funcion de boton
    fun calcularPropina(){
        resultado_n = total_cuenta.toDouble()*p1
        "La propina es del ${p1*100}% y equivale al :$resultado_n "
    }

    fun inputOnChange(total: String){
        val numerico =  total.toDoubleOrNull() ?: 0.0

        total_cuenta = numerico.toString()
    }

}