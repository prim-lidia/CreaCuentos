package org.ieselcaminas.pmdm.creacuento;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StageOptionAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<StageOption> options;
    private Context context;
    public StageOptionAdapter(Context context, ArrayList<StageOption> option) {
        super();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.options = option;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public StageOption getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.stage_info, null);
            holder =  new ViewHolder();
            holder.hTitle = (TextView) convertView.findViewById(R.id.titleStage);
            holder.textOption = (EditText) convertView.findViewById(R.id.optionText);
            convertView.setTag(holder);

            holder.textOption.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    StageOption option = getItem(position);
                    option.setOption(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StageOption option = getItem(position);
        String stageTitle = context.getResources().getString(R.string.stage);
        holder.hTitle.setText(stageTitle+" "+ option.getNumber());
        holder.textOption.setText(option.getOption());
        return convertView;
    }

    class ViewHolder {
        TextView hTitle;
        EditText textOption;
    }
}