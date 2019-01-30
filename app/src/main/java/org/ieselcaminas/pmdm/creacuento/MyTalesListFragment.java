package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class MyTalesListFragment extends Fragment {
    public interface Communicator {
        public void setTale(Tale tale);
    }

    private Communicator communicator;

    private ArrayList<Tale> taleList;
    public MyTalesListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        taleList = new ArrayList<>();
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_my_tale_list, container, false);
        // Inflate the layout for this fragment
        initList();
        Button buttonNewTale = viewRoot.findViewById(R.id.buttonNewTale);
        buttonNewTale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.setTale(null);
            }
        });
        final RecyclerView recView = (RecyclerView) viewRoot.findViewById(R.id.my_tales_recyclerView);

        //el RecyclerView tendrá tamaño fijo
        recView.setHasFixedSize(true);
        recView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        final MyTaleAdapter adaptador = new MyTaleAdapter(taleList);
        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(viewRoot.getContext(), LinearLayoutManager.VERTICAL, false));
        recView.setItemAnimator(new DefaultItemAnimator());
        adaptador.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Tale tale = taleList.get(recView.getChildAdapterPosition(v));
                communicator.setTale(tale);
                /*MyTalesListFragment fragment2 = new Fragment2();
                Bundle args = new Bundle();
                args.putInt("Imagen", img);
                fragment2.setArguments(args);
                Transition slide = TransitionInflater.from(getActivity()).inflateTransition(R.transition.slide);
                Transition fade = TransitionInflater.from(getActivity()).inflateTransition(R.transition.fade);
                fragment2.setSharedElementEnterTransition(slide);
                fragment2.setSharedElementReturnTransition(fade);
                final View idlogo = v.findViewById(R.id.imageView);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentReceiver, fragment2)
                        .addToBackStack(null)
                        .addSharedElement(idlogo, idlogo.getTransitionName())
                        .commit();*/
            }
        });

        return viewRoot;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Communicator) {
            communicator = (Communicator) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyTalesListFragment.Communicator");
        }
    }

    private void initList() {
        //Recoger datos de firebase
        Tale tale1 = new Tale("Titulo1", "Autor Autor", "Autor ilustracion", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "NombreFichero", "De 0-3 años");
        tale1.setEditing(true);
        taleList.add(tale1);
        Tale tale2 = new Tale("Titulo2", "Autor Autor", "Autor ilustracion", "Este libro NUM 2 trata de bla bla bla....", "NombreFichero", "De 4-8 años");
        tale2.setEditing(false);
        taleList.add(tale2);
    }
}
