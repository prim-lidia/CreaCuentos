package org.ieselcaminas.pmdm.creacuento;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTaleAdapter extends RecyclerView.Adapter<MyTaleAdapter.MyViewHolder> implements View.OnClickListener  {

    private static ArrayList<Tale> items;
    private View.OnClickListener listener;

    public MyTaleAdapter(ArrayList<Tale> items) {

        this.items = items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.item_tale_title);
        }


        public void bindTitular(final Tale tale) {
            int icon = -1;
            if(tale.getEditing()){
                icon = R.mipmap.ic_checked;
            }else{
                icon = R.mipmap.ic_editing;
            }
            imageView.setImageResource(icon);
            titleTextView.setText(tale.getTitle());

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
}
