package com.reimbursement.furgetech.reimbursement.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reimbursement.furgetech.reimbursement.Model.Pengeluaran;
import com.reimbursement.furgetech.reimbursement.R;

import java.util.List;

/**
 * Created by Furgetech on 27/02/2018.
 */

public class PengeluaranList extends ArrayAdapter<Pengeluaran> {

    private Activity context;
    private List<Pengeluaran> pengeluaranList;

    public PengeluaranList(Activity context, List<Pengeluaran> pengeluaranList){
        super(context, R.layout.list_view_reimbursement, pengeluaranList);
        this.context = context;
        this.pengeluaranList = pengeluaranList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_view_reimbursement, null, true);

        TextView textViewTanggal = (TextView) listViewItem.findViewById(R.id.textViewTanggal);
        TextView textViewJenisPengeluaran = (TextView) listViewItem.findViewById(R.id.textViewJenisPengeluaran);
        TextView textViewBiaya = (TextView) listViewItem.findViewById(R.id.textViewBiaya);
        TextView textViewKeterangan = (TextView) listViewItem.findViewById(R.id.textViewKeterangan);
        TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textViewStatus);

        Pengeluaran pengeluaran = pengeluaranList.get(position);

        textViewTanggal.setText(pengeluaran.getPengeluaranTanggal());
        textViewJenisPengeluaran.setText(pengeluaran.getPengeluaranJenisPengualaran());
        textViewBiaya.setText("Pengeluaran : Rp " + pengeluaran.getPengeluaranBiaya());
        textViewKeterangan.setText(pengeluaran.getPengeluaranKeterangan());
        textViewStatus.setText("Status : " + pengeluaran.getPengeluaranStatus());

        return  listViewItem;
    }
}
