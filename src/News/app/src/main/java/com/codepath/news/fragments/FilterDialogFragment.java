package com.codepath.news.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.codepath.news.R;
import com.codepath.news.models.FilterSettings;

import org.parceler.Parcels;

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

    public static FilterDialogFragment newInstance(Parcelable settings) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filter_settings", settings);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    public static FilterDialogFragment newInstance(FilterSettings settings) {
        FilterDialogFragment fragment = new FilterDialogFragment();

        Bundle args = new Bundle();

        args.putString("beginYear", settings.getBeginYear());
        args.putString("beginMonth", settings.getBeginMonth());
        args.putString("beginDay", settings.getBeginDay());
        args.putInt("sortSelectedIndex", settings.getSortSelectedIndex());
        args.putBoolean("isCheckedArts", settings.isCheckedArts());
        args.putBoolean("isCheckedFashion", settings.isCheckedFashion());
        args.putBoolean("isCheckedSports", settings.isCheckedSports());

        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            FilterSettings settings = (FilterSettings) Parcels.unwrap(getArguments().getParcelable("filter_settings"));
            setFields(settings);
        }

        btnApply.setOnClickListener(v -> {
            onButtonPressed();
            dismiss();
        });

        return view;
    }

    private void setFields(FilterSettings settings) {
        dpBeginDate.updateDate(Integer.parseInt(settings.getBeginYear()), Integer.parseInt(settings.getBeginMonth()), Integer.parseInt(settings.getBeginDay()));

        spSort.setSelection(settings.getSortSelectedIndex());
        cbArts.setChecked(settings.isCheckedArts());
        cbFashion.setChecked(settings.isCheckedFashion());
        cbSports.setChecked(settings.isCheckedSports());
    }

    public void onButtonPressed() {
        mListener = (OnFragmentInteractionListener) getActivity();
        if (mListener != null) {
            FilterSettings settings = new FilterSettings(Integer.toString(dpBeginDate.getYear()),
                    Integer.toString(dpBeginDate.getMonth()),
                    Integer.toString(dpBeginDate.getDayOfMonth()),
                    spSort.getSelectedItemPosition(),
                    spSort.getSelectedItem().toString(),
                    cbArts.isChecked(),
                    cbFashion.isChecked(),
                    cbSports.isChecked());

            mListener.onFragmentInteraction(settings);
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
        void onFragmentInteraction(FilterSettings settings);
    }
}
