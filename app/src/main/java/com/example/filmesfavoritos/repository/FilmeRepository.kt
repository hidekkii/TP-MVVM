package com.example.filmesfavoritos.repository

import com.example.filmesfavoritos.model.Filme

// REPOSITORY: fonte de dados (aqui, em memória).
// Poderia ser trocado por Room ou Firestore sem mexer na View.
class FilmeRepository {

    private val filmes = mutableListOf(
        Filme(1, "Interestelar", 5f, "https://picsum.photos/seed/interestelar/300/450"),
        Filme(2, "Matrix", 4.5f, "https://picsum.photos/seed/matrix/300/450"),
        Filme(3, "Cidade de Deus", 5f, "https://picsum.photos/seed/cidadededeus/300/450"),
        Filme(4, "Divertida Mente", 4f, "https://picsum.photos/seed/divertidamente/300/450")
    )
    private var proximoId = 5L

    fun listar(): List<Filme> = filmes.toList()

    fun adicionar(nome: String, nota: Float, fotoUrl: String) {
        filmes.add(Filme(proximoId++, nome, nota, fotoUrl))
    }

    fun atualizar(filme: Filme) {
        val i = filmes.indexOfFirst { it.id == filme.id }
        if (i != -1) filmes[i] = filme
    }

    fun remover(id: Long) {
        filmes.removeAll { it.id == id }
    }
}
