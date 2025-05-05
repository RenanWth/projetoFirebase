package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Pessoa;

import java.util.List;

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.PessoaViewHolder> {

    private List<Pessoa> pessoas;
    private OnPessoaClickListener listener;

    public interface OnPessoaClickListener {
        void onEditClick(Pessoa pessoa);
        void onDeleteClick(Pessoa pessoa);
    }

    public PessoaAdapter(List<Pessoa> pessoas, OnPessoaClickListener listener) {
        this.pessoas = pessoas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PessoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pessoa, parent, false);
        return new PessoaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PessoaViewHolder holder, int position) {
        Pessoa pessoa = pessoas.get(position);
        holder.textViewNome.setText(pessoa.getNome());
        holder.textViewEmail.setText(pessoa.getEmail());
        holder.textViewTelefone.setText(pessoa.getTelefone());
        holder.textViewEndereco.setText(pessoa.getEndereco());
        holder.textViewIdade.setText("Idade: " + pessoa.getIdade());

        holder.buttonEditar.setOnClickListener(v -> {
            listener.onEditClick(pessoa);
        });

        holder.buttonExcluir.setOnClickListener(v -> {
            listener.onDeleteClick(pessoa);
        });
    }

    @Override
    public int getItemCount() {
        return pessoas.size();
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
        notifyDataSetChanged();
    }

    public static class PessoaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewEmail, textViewTelefone, textViewEndereco, textViewIdade;
        Button buttonEditar, buttonExcluir;

        public PessoaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewTelefone = itemView.findViewById(R.id.textViewTelefone);
            textViewEndereco = itemView.findViewById(R.id.textViewEndereco);
            textViewIdade = itemView.findViewById(R.id.textViewIdade);
            buttonEditar = itemView.findViewById(R.id.buttonEditar);
            buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
        }
    }
}
