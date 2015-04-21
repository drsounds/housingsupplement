package io.webconnector.housingsupplement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.webconnector.housingsupplement.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class HousingSupplementApplicationFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_YEAR = "param1";

    // TODO: Rename and change types of parameters
    private int mYear;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static HousingSupplementApplicationFragment newInstance(int year) {
        HousingSupplementApplicationFragment fragment = new HousingSupplementApplicationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HousingSupplementApplicationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mYear = getArguments().getInt(ARG_YEAR);
        }
        HousingSupplementDatabase db = new HousingSupplementDatabase(this.getActivity());
        // TODO: Change Adapter to display your content
        Cursor cursor =  db.getHousingApplicationsForYear(mYear);
        mAdapter = new CursorAdapter(this.getActivity(),cursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = View.inflate(context, R.layout.item_housingsupplementapplication, null);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tvAmount = (TextView)view.findViewById(R.id.tv_amount);
                TextView tvDate = (TextView)view.findViewById(R.id.tv_date);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                String period = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex("time"))));
                tvDate.setText(period);
                HousingSupplementApplication application = new HousingSupplementApplication(cursor);
                tvAmount.setText(NumberFormat.getInstance().format(application.calculateMonthlyHousingSupplement()));
                view.setTag( cursor.getLong(cursor.getColumnIndex("_id")));

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_housingsupplementapplication, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), HousingSupplementApplicationActivity.class);
                i.putExtra("id", (long) view.getTag());
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new
                        Intent(getActivity(), HousingSupplementApplicationActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
