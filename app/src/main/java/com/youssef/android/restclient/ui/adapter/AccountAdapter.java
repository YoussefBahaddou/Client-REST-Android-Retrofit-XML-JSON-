package com.youssef.android.restclient.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youssef.android.restclient.R;
import com.youssef.android.restclient.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    public interface OnDeleteClickListener {
        void onDeleteClick(Account Account);
    }
    public interface OnUpdateClickListener {
        void onUpdateClick(Account Account);
    }

    private List<Account> Accounts;
    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public AccountAdapter(OnDeleteClickListener onDeleteClickListener, OnUpdateClickListener onUpdateClickListener) {
        this.Accounts = new ArrayList<>();
        this.onDeleteClickListener = onDeleteClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_Account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account Account = Accounts.get(position);
        holder.bind(Account);
    }

    @Override
    public int getItemCount() {
        return Accounts.size();
    }

    public void updateData(List<Account> newAccounts) {
        this.Accounts.clear();
        this.Accounts.addAll(newAccounts);
        notifyDataSetChanged();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvSolde, tvType, tvDate;
        private View btnDelete, btnUpdate;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Account Account) {
            tvId.setText("ID: " + Account.getId());
            tvSolde.setText(String.format("Solde: %.2f", Account.getSolde()));
            tvType.setText("Type: " + Account.getType());
            tvDate.setText("Date: " + Account.getDateCreation());

            btnDelete.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(Account);
                }
            });
            btnUpdate.setOnClickListener(v -> {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(Account);
                }
            });
        }
    }
}
