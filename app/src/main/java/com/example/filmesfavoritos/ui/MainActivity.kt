package com.example.filmesfavoritos.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.filmesfavoritos.R
import com.example.filmesfavoritos.databinding.ActivityMainBinding
import com.example.filmesfavoritos.databinding.DialogFilmeBinding
import com.example.filmesfavoritos.model.Filme
import com.example.filmesfavoritos.viewmodel.FilmeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// VIEW: só cuida da tela. Não guarda nem altera dados diretamente.
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FilmeViewModel by viewModels()
    private val adapter = FilmeAdapter { filme -> abrirDialogo(filme) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFilmes.adapter = adapter
        binding.fabAdicionar.setOnClickListener { abrirDialogo(null) }

        // Observa a lista do ViewModel: toda mudança atualiza a tela sozinha
        viewModel.filmes.observe(this) { lista ->
            adapter.submitList(lista)
            binding.tvVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    /** filme == null -> adicionar; filme != null -> editar */
    private fun abrirDialogo(filme: Filme?) {
        val dialogBinding = DialogFilmeBinding.inflate(layoutInflater)

        // Preenche os campos quando for edição
        filme?.let {
            dialogBinding.etNome.setText(it.nome)
            dialogBinding.etUrl.setText(it.fotoUrl)
            dialogBinding.ratingNota.rating = it.nota
            carregarPreview(dialogBinding, it.fotoUrl)
        }

        // Pré-visualização da foto enquanto digita a URL
        dialogBinding.etUrl.doAfterTextChanged { texto ->
            carregarPreview(dialogBinding, texto.toString())
        }

        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(if (filme == null) R.string.novo_filme else R.string.editar_filme)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.salvar, null) // tratado abaixo p/ validar sem fechar
            .setNegativeButton(R.string.cancelar, null)

        if (filme != null) {
            builder.setNeutralButton(R.string.excluir) { _, _ -> viewModel.remover(filme) }
        }

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val ok = viewModel.salvar(
                    filmeExistente = filme,
                    nome = dialogBinding.etNome.text.toString(),
                    nota = dialogBinding.ratingNota.rating,
                    fotoUrl = dialogBinding.etUrl.text.toString()
                )
                if (ok) dialog.dismiss()
                else dialogBinding.tilNome.error = getString(R.string.erro_nome)
            }
        }
        dialog.show()
    }

    private fun carregarPreview(dialogBinding: DialogFilmeBinding, url: String) {
        dialogBinding.imgPreview.load(url) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_report_image)
        }
    }
}
