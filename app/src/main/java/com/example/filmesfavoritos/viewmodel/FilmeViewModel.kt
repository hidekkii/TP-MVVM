package com.example.filmesfavoritos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmesfavoritos.model.Filme
import com.example.filmesfavoritos.repository.FilmeRepository

// VIEWMODEL: toda a lógica da lista fica aqui.
// A Activity só observa o LiveData e repassa as ações do usuário.
class FilmeViewModel : ViewModel() {

    private val repository = FilmeRepository()

    private val _filmes = MutableLiveData<List<Filme>>(repository.listar())
    val filmes: LiveData<List<Filme>> = _filmes

    /** Retorna false se os dados forem inválidos (nome vazio). */
    fun salvar(filmeExistente: Filme?, nome: String, nota: Float, fotoUrl: String): Boolean {
        val nomeLimpo = nome.trim()
        if (nomeLimpo.isEmpty()) return false

        val notaValida = nota.coerceIn(0f, 5f)
        if (filmeExistente == null) {
            repository.adicionar(nomeLimpo, notaValida, fotoUrl.trim())
        } else {
            repository.atualizar(
                filmeExistente.copy(nome = nomeLimpo, nota = notaValida, fotoUrl = fotoUrl.trim())
            )
        }
        atualizarLista()
        return true
    }

    fun remover(filme: Filme) {
        repository.remover(filme.id)
        atualizarLista()
    }

    private fun atualizarLista() {
        _filmes.value = repository.listar()
    }
}
