package utez.edu.mx.liverpool.ui.screens.liverpool

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Opinion(val autor: String, val comentario: String, val calificacion: Int)

data class Product(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String,
    val descripcion: String,
    val marca: String,
    var isFavorite: Boolean = false,
    val opiniones: List<Opinion> = listOf()
)

class LiverpoolViewModel : ViewModel() {
    val productos = mutableStateListOf<Product>()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        productos.addAll(
            listOf(
                Product(
                    id = 1,
                    nombre = "Dior Homme Sport 200ml",
                    precio = 3850.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Eau de Toilette con notas frescas y amaderadas, ideal para el hombre moderno.",
                    marca = "Dior",
                    opiniones = listOf(Opinion("Juan", "Excelente aroma, dura todo el día.", 5))
                ),
                Product(
                    id = 2,
                    nombre = "Allure Homme Sport Le Blanche",
                    precio = 3400.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Una explosión cítrica con fondo oriental, perfecta para cualquier ocasión.",
                    marca = "Chanel",
                    opiniones = listOf(Opinion("Pedro", "Huele a limón cremoso, muy rico.", 4))
                ),
                Product(
                    id = 3,
                    nombre = "Layton de Parfums de Marly",
                    precio = 5500.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Fragancia nicho con notas de manzana, vainilla y especias.",
                    marca = "Parfums de Marly",
                    opiniones = listOf(Opinion("Luis", "Elegancia embotellada.", 5))
                ),
                Product(
                    id = 4,
                    nombre = "Sony Collection Edition X",
                    precio = 6500.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Cancelación de ruido premium, audio de alta resolución y diseño elegante.",
                    marca = "Sony",
                    opiniones = listOf(Opinion("Ana", "Aíslan el ruido por completo.", 5))
                ),
                Product(
                    id = 5,
                    nombre = "Audífonos de alta fidelidad gama alta Sennheiser",
                    precio = 8900.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "El estándar para audiófilos, sonido natural y balanceado.",
                    marca = "Sennheiser",
                    opiniones = listOf(Opinion("Carlos", "El mejor sonido que he escuchado.", 5))
                ),
                Product(
                    id = 6,
                    nombre = "Teléfono Inteligente",
                    precio = 24999.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Cámara profesional, pantalla OLED a 120Hz y gran batería.",
                    marca = "TechBrand",
                    opiniones = listOf(Opinion("María", "Muy rápido y la cámara es increíble.", 5))
                ),
                Product(
                    id = 7,
                    nombre = "Bocina de Alta Fidelidad",
                    precio = 7200.0,
                    imagenUrl = "https://via.placeholder.com/150",
                    descripcion = "Sonido envolvente 360 y bajos profundos para tus fiestas.",
                    marca = "SoundMax",
                    opiniones = listOf(Opinion("David", "Potencia brutal y no distorsiona.", 4))
                )
            )
        )
    }

    fun toggleFavorite(productId: Int) {
        val index = productos.indexOfFirst { it.id == productId }
        if (index != -1) {
            val p = productos[index]
            productos[index] = p.copy(isFavorite = !p.isFavorite)
        }
    }

    fun agregarOpinion(productId: Int, autor: String, comentario: String, calificacion: Int) {
        val index = productos.indexOfFirst { it.id == productId }
        if (index != -1) {
            val p = productos[index]
            val nuevasOpiniones = p.opiniones + Opinion(autor, comentario, calificacion)
            productos[index] = p.copy(opiniones = nuevasOpiniones)
        }
    }
}
