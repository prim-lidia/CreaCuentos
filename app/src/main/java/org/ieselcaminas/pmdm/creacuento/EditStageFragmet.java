package org.ieselcaminas.pmdm.creacuento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class EditStageFragmet extends Fragment {
    private StageOptionAdapter stageAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_edit_stage, container, false);

        //Stages
        ArrayList<StageOption> nextStages = new ArrayList<StageOption>();
        ListView listStage = viewRoot.findViewById(R.id.stageList);
        stageAdapter = new StageOptionAdapter(getContext(),nextStages);
        listStage.setAdapter(stageAdapter);
        return viewRoot;
    }
}
