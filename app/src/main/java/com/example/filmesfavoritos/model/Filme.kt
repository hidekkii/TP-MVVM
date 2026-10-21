package com.example.filmesfavoritos.model

// MODEL: representa os dados de um filme favorito
data class Filme(
    val id: Long,
    val nome: String,
    val nota: Float,     // de 0 a 5 (meia estrela permitida)
    val fotoUrl: String  // URL da imagem carregada com Coil
)
