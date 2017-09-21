package com.codepath.news;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterDialogFragment extends DialogFragment {

    @BindView(R.id.dpBeginDate)
    DatePicker dpBeginDate;

    @BindView(R.id.spSort)
    Spinner spSort;

    @BindView(R.id.cbArts)
    CheckBox cbArts;

    @BindView(R.id.cbSports)
    CheckBox cbSports;

    @BindView(R.id.cbFashion)
    CheckBox cbFashion;

    @BindView(R.id.btnApply)
    Button btnApply;


    private OnFragmentInteractionListener mListener;

    public FilterDialogFragment() {
        // Required empty public constructor
    }

    public static FilterDialogFragment newInstance(String beginYear, String beginMonth, String beginDay, int sortSelectedIndex, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
        FilterDialogFragment fragment = new FilterDialogFragment();

        Bundle args = new Bundle();
        args.putString("beginYear", beginYear);
        args.putString("beginMonth", beginMonth);
        args.putString("beginDay", beginDay);
        args.putInt("sortSelectedIndex", sortSelectedIndex);
        args.putBoolean("isCheckedArts", isCheckedArts);
        args.putBoolean("isCheckedFashion", isCheckedFashion);
        args.putBoolean("isCheckedSports", isCheckedSports);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {

            String year = getArguments().getString("beginYear");
            String month = getArguments().getString("beginMonth");
            String day = getArguments().getString("beginDay");
            dpBeginDate.updateDate(Integer.parseInt(year),Integer.parseInt(month), Integer.parseInt(day));

            spSort.setSelection(getArguments().getInt("sortSelectedIndex"));
            cbArts.setChecked(getArguments().getBoolean("isCheckedArts"));
            cbFashion.setChecked(getArguments().getBoolean("isCheckedFashion"));
            cbSports.setChecked(getArguments().getBoolean("isCheckedSports"));
        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonPressed();
                dismiss();
            }
        });

        return view;
    }

    public void onButtonPressed() {
        mListener = (OnFragmentInteractionListener) getActivity();
        if (mListener != null) {

            mListener.onFragmentInteraction(Integer.toString(dpBeginDate.getYear()), Integer.toString(dpBeginDate.getMonth()), Integer.toString(dpBeginDate.getDayOfMonth()), spSort.getSelectedItemPosition(),spSort.getSelectedItem().toString(), cbArts.isChecked(), cbFashion.isChecked(), cbSports.isChecked());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String beginYear, String beginMonth, String beginDay, int sortItemIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports);
    }
}
