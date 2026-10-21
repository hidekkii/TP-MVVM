package com.example.filmesfavoritos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.filmesfavoritos.databinding.ItemFilmeBinding
import com.example.filmesfavoritos.model.Filme

class FilmeAdapter(
    private val aoClicar: (Filme) -> Unit
) : ListAdapter<Filme, FilmeAdapter.FilmeViewHolder>(Diff) {

    // ViewHolder usando View Binding (sem findViewById)
    inner class FilmeViewHolder(private val binding: ItemFilmeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filme: Filme) {
            binding.tvNome.text = filme.nome
            binding.ratingNota.rating = filme.nota
            binding.tvNota.text = String.format("%.1f", filme.nota)

            // Coil carregando a foto pela URL
            binding.imgFoto.load(filme.fotoUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }

            binding.root.setOnClickListener { aoClicar(filme) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<Filme>() {
        override fun areItemsTheSame(a: Filme, b: Filme) = a.id == b.id
        override fun areContentsTheSame(a: Filme, b: Filme) = a == b
    }
}
