package org.ieselcaminas.pmdm.creacuento;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TaleListFragment extends ListFragment {
    public interface Communicator {
        public void setTale(Tale tale);
    }

    private Communicator communicator;
    public TaleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tale_list, container, false);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Tale> taleList = new ArrayList<Tale>();
        initList(taleList);

        final TaleAdapter adapter = new TaleAdapter(getActivity(),R.layout.tale_info,taleList);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Communicator) {
            communicator = (Communicator) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ListFragmentVersions.AndroidVersionReceiver");
        }
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        //Toast.makeText(getActivity(),
        //        "You have selected " + presidents[position], Toast.LENGTH_SHORT).show();
        Tale tale = (Tale)   parent.getItemAtPosition(position);
        Log.d("taleCom", tale.getTitle());
        communicator.setTale(tale);
    }

    private void initList(ArrayList<Tale> androidList) {
        //Recoger datos de firebase
        androidList.add(new Tale("Titulo1", "Autor Autor", "Autor ilustracion", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "NombreFichero", 2));
        androidList.add(new Tale("Titulo2", "Autor Autor", "Autor ilustracion", "Este libro NUM 2 trata de bla bla bla....", "NombreFichero", 1));
    }

}
