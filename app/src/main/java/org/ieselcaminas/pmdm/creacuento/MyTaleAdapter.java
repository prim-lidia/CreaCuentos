package org.ieselcaminas.pmdm.creacuento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyTaleAdapter extends RecyclerView.Adapter<MyTaleAdapter.MyViewHolder> implements View.OnClickListener  {

    private static ArrayList<Tale> items;
    private View.OnClickListener listener;

    public MyTaleAdapter(ArrayList<Tale> items) {

        this.items = items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ImageViewIcon;
        private TextView titleTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ImageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.item_tale_title);

        }


        public void bindTitular(final Tale tale) {
            int icon = -1;

            if(tale.getEditing()){
                icon = R.mipmap.ic_editing;
            }else{
                icon = R.mipmap.ic_checked;

            }
            ImageViewIcon.setImageResource(icon);
            titleTextView.setText(tale.getTitle());
            ImageView iconDelete = (ImageView) itemView.findViewById(R.id.delete);
            iconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDeleteDialog(v , tale);
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_tale_item, viewGroup, false);
        itemView.setOnClickListener(this);
        MyViewHolder tvh = new MyViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int pos) {
        Tale tale = items.get(pos);
        viewHolder.bindTitular(tale);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void addItem(Tale tale) {
        items.add(tale);
        notifyItemInserted(items.size());
    }

    public void removeItem(Tale tale) {
        //Borrar imagenes
        for(int i= 0; i<items.size(); i++) {
            Log.d("Cuentos", String.valueOf(items.get(i).getId().equals(tale.getId())));
            if (items.get(i).getId().equals(tale.getId())) {
                items.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, items.size());
                //taleRef.removeValue();
                break;
            }
        }
    }

    public void updateItem(Tale tale){
        for(int i= 0; i<items.size(); i++) {
            Log.d("Cuentos", String.valueOf(items.get(i).equals(tale)));
            if (items.get(i).getId().equals(tale.getId())) {
                items.set(i, tale);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void openDeleteDialog(final View view, final Tale tale){
        Context context = view.getContext();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setIcon(context.getResources().getDrawable(R.drawable.ic_help_blue_24dp))
                .setTitle(context.getString(R.string.delete_confirmation_title))
                .setMessage(context.getResources().getString(R.string.delete_confirmation_desc))
                .setPositiveButton(context.getResources().getString(R.string.delete_btn_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final DatabaseReference taleRef = FirebaseDatabase.getInstance().getReference("tales/"+tale.getId());
                                taleRef.removeValue();
                                Toast.makeText(view.getContext(),
                                        "OK clicked!", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton(context.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(),
                                        "Cancel clicked!", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .create();
        dialog.show();
    }
}
