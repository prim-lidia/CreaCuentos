package org.ieselcaminas.pmdm.creacuento;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StagesAdapter extends RecyclerView.Adapter<StagesAdapter.MyViewHolder> implements View.OnClickListener  {

    private static ArrayList<Stage> stages;
    private View.OnClickListener listener;

    public StagesAdapter(ArrayList<Stage> stages) {

        this.stages = stages;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ImageViewIcon;
        private TextView titleTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.item_stage_title);

        }


        public void bindTitular(final Stage stage) {
            int icon = -1;

            //ImageViewIcon.setImageResource(icon);
            titleTextView.setText(stage.getNumber()+": "+stage.getTitle());
            /*ImageView iconDelete = (ImageView) itemView.findViewById(R.id.delete);
            iconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

        }
    }

    @Override
    public StagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stage_item_list, viewGroup, false);
        itemView.setOnClickListener(this);
        StagesAdapter.MyViewHolder tvh = new StagesAdapter.MyViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(StagesAdapter.MyViewHolder viewHolder, int pos) {
        Stage stage = stages.get(pos);
        viewHolder.bindTitular(stage);
    }

    @Override
    public int getItemCount() {
        return stages.size();
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

    public void addItem(Stage stage) {
        stages.add(stage);
        notifyItemInserted(stages.size());
    }

    public void removeItem(Stage stage) {
        //Borrar imagenes
        for(int i = 0; i< stages.size(); i++) {
            if (stages.get(i).getIdStage().equals(stage.getIdStage())) {
                stages.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, stages.size());
                //taleRef.removeValue();
                break;
            }
        }
    }

    public void updateItem(Stage stage){
        for(int i = 0; i< stages.size(); i++) {
            if (stages.get(i).getIdStage().equals(stage.getIdStage())){
                stages.set(i, stage);
                notifyItemChanged(i);
                break;
            }
        }
    }

}