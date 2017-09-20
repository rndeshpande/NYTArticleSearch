package com.codepath.news.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.news.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity{

    private final String USER_PREFERENCES = "userprefs";

    private SharedPreferences mSharedpreferences;
    @BindView(R.id.tvBeginDate)
    TextView tvBeginDate;

    @BindView(R.id.etBeginDate)
    EditText etBeginDate;

    @BindView(R.id.spSort)
    Spinner spSort;

    @BindView(R.id.cbArts)
    CheckBox cbArts;

    @BindView(R.id.cbFashion)
    CheckBox cbFashion;

    @BindView(R.id.cbSports)
    CheckBox cbSports;

    @BindView(R.id.btnApply)
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);
        mSharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        etBeginDate.setText(mSharedpreferences.getString("begin_date","18510101"));


        btnApply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedpreferences.edit();
                editor.putString("sort", spSort.getSelectedItem().toString());
                editor.putString("begin_date", etBeginDate.getText().toString());

                String newsDesk = "";

                if (cbArts.isChecked())
                    newsDesk +=  "\"" + cbArts.getText() + "\" ";

                if (cbFashion.isChecked())
                    newsDesk +=  "\"" + cbFashion.getText()+ "\" ";

                if (cbSports.isChecked())
                    newsDesk +=  "\"" + cbSports.getText()+ "\"";


                Log.d("DATA", newsDesk);

                editor.putString("news_desk", newsDesk);
                editor.apply();
            }
        });
    }
}
