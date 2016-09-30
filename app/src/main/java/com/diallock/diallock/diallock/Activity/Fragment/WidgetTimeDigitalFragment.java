package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WidgetTimeDigitalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WidgetTimeDigitalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WidgetTimeDigitalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mView;
    private TextView txt_time;
    private TextView txt_am_pm;
    private Timer mTimer;
    private final String LOG_NAME = "WidgetTimeDigitalFragment";

    public WidgetTimeDigitalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WidgetTimeDigitalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WidgetTimeDigitalFragment newInstance(String param1, String param2) {
        WidgetTimeDigitalFragment fragment = new WidgetTimeDigitalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_widget_time_digital, container, false);

        setFindById();
        init();

        return mView;
    }

    private void setFindById() {
        txt_time = (TextView) mView.findViewById(R.id.txt_time);
        txt_am_pm = (TextView) mView.findViewById(R.id.txt_am_pm);
    }

    private void init() {

        MainTimerTask timerTask = new MainTimerTask();

        mTimer = new Timer();

        mTimer.schedule(timerTask, 500, 1000);
    }

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {

            Date nowDate = new Date();

            String strTxtAmPm =
                    CommonJava.getAmPm(nowDate);
            String strTxtTime = CommonJava.getHour(nowDate) + "시 " + CommonJava.getMinute(nowDate) + "분";
            txt_am_pm.setText(strTxtAmPm);
            txt_time.setText(strTxtTime);
            CommonJava.Loging.i(LOG_NAME, "strTxtAmPm : " + strTxtAmPm);
            CommonJava.Loging.i(LOG_NAME, "strTxtTime : " + strTxtTime);
        }

    };


    class MainTimerTask extends TimerTask {

        public void run() {

            mHandler.post(mUpdateTimeTask);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
