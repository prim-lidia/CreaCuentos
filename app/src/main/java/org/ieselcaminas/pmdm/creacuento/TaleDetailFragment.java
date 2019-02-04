package org.ieselcaminas.pmdm.creacuento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaleDetailFragment extends Fragment {
    private Tale tale = null;
    public TaleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_tale_detail, container, false);

        return viewRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            tale = (Tale) savedInstanceState.get("tale");
            setTaleInfo(tale);
        }

    }

    public void setTaleInfo(Tale tale2){
        tale = tale2;
        String[] categories = getActivity().getResources().getStringArray(R.array.categories);
        ImageView detailImage = (ImageView) getView().findViewById(R.id.detailImageView);
        TextView detailTitle = (TextView) getView().findViewById(R.id.detailTitle);
        TextView detailCategory = (TextView) getView().findViewById(R.id.detailCategory);
        TextView detailAuthor = (TextView) getView().findViewById(R.id.detailAuthor);
        TextView detailIllustrator = (TextView) getView().findViewById(R.id.detailIllustrator);
        TextView detailDescription = (TextView) getView().findViewById(R.id.detailText);

        detailTitle.setText(tale.getTitle());
        detailAuthor.setText(tale.getAuthor());
        detailCategory.setText(categories[tale.getCategory()]);
        detailIllustrator.setText(tale.getIllustrationAuthor());
        detailDescription.setText(tale.getDescription());

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable("tale", tale);
        super.onSaveInstanceState(bundle);

    }
}
