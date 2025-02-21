package com.example.listadecontatos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private Context context;
    private List<Contato> listaContatos;

    public ContatoAdapter(Context context, List<Contato> listaContatos) {
        this.context = context;
        this.listaContatos = listaContatos;
    }

    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contato, parent, false);
        return new ContatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoViewHolder holder, int position) {
        Contato contato = listaContatos.get(position);
        holder.nomeTextView.setText(contato.getName());
        holder.telefoneTextView.setText(contato.getTelemovel());
        holder.chipGrupo.setText(String.valueOf(contato.getGrupoNome()));

        //Ação de clicar e abrir a tela de detalhar do contato
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetalhesContatoActivity.class);
            intent.putExtra("contatoId", contato.getId()); // Envia apenas o ID
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public static class ContatoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView telefoneTextView;
        Chip chipGrupo;


        public ContatoViewHolder(View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nomeContato);
            telefoneTextView = itemView.findViewById(R.id.telefoneContato);
            chipGrupo = itemView.findViewById(R.id.chipGrupo);
        }
    }

}
