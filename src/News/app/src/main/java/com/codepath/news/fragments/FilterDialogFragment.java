package com.codepath.news.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.codepath.news.databinding.FragmentFilterDialogBinding;
import com.codepath.news.models.FilterSettings;

import org.parceler.Parcels;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterDialogFragment extends DialogFragment {

    DatePicker dpBeginDate;

    Spinner spSort;

    CheckBox cbArts;

    CheckBox cbSports;

    CheckBox cbFashion;

    Button btnApply;

    FragmentFilterDialogBinding binding;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_dialog, container, false);

        View view = binding.getRoot();

        if (getArguments() != null) {
            FilterSettings settings = (FilterSettings) Parcels.unwrap(getArguments().getParcelable("filter_settings"));
            binding.setSettings(settings);
        }

        btnApply = binding.btnApply;
        btnApply.setOnClickListener(v -> {
            onButtonPressed();
            dismiss();
        });

        return view;
    }

    public void onButtonPressed() {
        mListener = (OnFragmentInteractionListener) getActivity();
        if (mListener != null) {
            dpBeginDate = binding.dpBeginDate;
            cbArts = binding.cbArts;
            cbFashion = binding.cbFashion;
            cbSports = binding.cbSports;
            spSort = binding.spSort;

            FilterSettings settings = new FilterSettings(dpBeginDate.getYear(),
                    dpBeginDate.getMonth(),
                    dpBeginDate.getDayOfMonth(),
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
