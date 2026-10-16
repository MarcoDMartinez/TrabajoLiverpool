package utez.edu.mx.liverpool.ui.screens.liverpool

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiverpoolScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { LiverpoolViewModel() }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(if (selectedProduct == null) "Mi Bolsa" else "Detalles") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedProduct != null) {
                            selectedProduct = null
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE10098),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (selectedProduct == null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.productos) { product ->
                        ProductCard(
                            product = product,
                            onClick = { selectedProduct = product },
                            onFavoriteClick = { viewModel.toggleFavorite(product.id) }
                        )
                    }
                }
            } else {
                val currentProduct = viewModel.productos.find { it.id == selectedProduct?.id }
                if (currentProduct != null) {
                    ProductDetailScreen(
                        product = currentProduct,
                        onAddOpinion = { autor, comentario, cal ->
                            viewModel.agregarOpinion(currentProduct.id, autor, comentario, cal)
                        },
                        onFavoriteClick = { viewModel.toggleFavorite(currentProduct.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                ) {}
                
                FavoriteButton(
                    isFavorite = product.isFavorite,
                    onClick = onFavoriteClick
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.marca,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(40.dp)
            )
            Text(
                text = "$${product.precio}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFE10098)
            )

            if (product.opiniones.isNotEmpty()) {
                val promedio = product.opiniones.map { it.calificacion }.average()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format(Locale.US, "%.1f (%d)", promedio, product.opiniones.size),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "favorite_animation"
    )

    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorito",
            tint = if (isFavorite) Color.Red else Color.Gray,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun ProductDetailScreen(
    product: Product,
    onAddOpinion: (String, String, Int) -> Unit,
    onFavoriteClick: () -> Unit
) {
    var newOpinionText by remember { mutableStateOf("") }
    var newOpinionAuthor by remember { mutableStateOf("") }
    var newOpinionRating by remember { mutableIntStateOf(5) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Column {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ) {}
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = product.marca, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Text(text = product.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(text = "$${product.precio}", style = MaterialTheme.typography.headlineSmall, color = Color(0xFFE10098))
                    
                    if (product.opiniones.isNotEmpty()) {
                        val promedio = product.opiniones.map { it.calificacion }.average()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = if (index < promedio.toInt()) Color(0xFFFFC107) else Color.LightGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format(Locale.US, "%.1f (%d %s)", promedio, product.opiniones.size, if (product.opiniones.size == 1) "reseña" else "reseñas"),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(text = product.descripcion, style = MaterialTheme.typography.bodyLarge)
                }
                FavoriteButton(isFavorite = product.isFavorite, onClick = onFavoriteClick)
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(text = "Opiniones y Reseñas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (product.opiniones.isEmpty()) {
                Text(
                    text = "Aún no hay opiniones para este producto. ¡Sé el primero en publicar una!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        items(product.opiniones) { opinion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = opinion.autor, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Row {
                            repeat(opinion.calificacion) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = opinion.comentario, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Dejar una opinión", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Calificación:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (star in 1..5) {
                    IconButton(
                        onClick = { newOpinionRating = star },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "$star estrellas",
                            tint = if (star <= newOpinionRating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$newOpinionRating / 5 estrellas",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE10098)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newOpinionAuthor,
                onValueChange = { newOpinionAuthor = it },
                label = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newOpinionText,
                onValueChange = { newOpinionText = it },
                label = { Text("Tu comentario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (newOpinionAuthor.isNotBlank() && newOpinionText.isNotBlank()) {
                        onAddOpinion(newOpinionAuthor.trim(), newOpinionText.trim(), newOpinionRating)
                        newOpinionAuthor = ""
                        newOpinionText = ""
                        newOpinionRating = 5
                    }
                },
                enabled = newOpinionAuthor.isNotBlank() && newOpinionText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE10098)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar opinión")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
