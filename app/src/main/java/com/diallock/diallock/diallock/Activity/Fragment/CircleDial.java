package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CircleDial.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CircleDial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleDial extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static View mView;

    private View widget_fragment;

    //private GestureDetectorCompat gestureDetector;
    private static GestureDetector gestureDetector;
    private Boolean mDialInnerCheck;

    public CircleDial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CircleDial.
     */
    // TODO: Rename and change types and number of parameters
    public static CircleDial newInstance() {
        CircleDial fragment = new CircleDial();
        Bundle args = new Bundle();
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

        CommonJava.Loging.i("CircleDial", "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CommonJava.Loging.i("CircleDial", "onCreateView()");
        mView = inflater.inflate(R.layout.fragment_circle_dial, container, false);

        findViewById();
        init();
        setOnTouch();

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
    }

    private void init() {

        mDialInnerCheck = false;
        /*gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                CommonJava.Loging.i(getContext(), "onDown()");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                CommonJava.Loging.i(getContext(), "onShowPress()");

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                CommonJava.Loging.i(getContext(), "onSingleTapUp()");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i(getContext(), "onScroll()");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                CommonJava.Loging.i(getContext(), "onLongPress()");

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                CommonJava.Loging.i(getContext(), "onFling()");
                return false;
            }
        });*/

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (mDialInnerCheck) {
                    CommonJava.Loging.i("CircleDial", "onSingleTapUp");
                    mDialInnerCheck = false;
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float x, float y) {
                if (mDialInnerCheck) {
                    CommonJava.Loging.i("CircleDial", "onScroll motionEvent : " + motionEvent);
                    CommonJava.Loging.i("CircleDial", "onScroll motionEvent1 : " + motionEvent1);
                    CommonJava.Loging.i("CircleDial", "onScroll x : " + x);
                    CommonJava.Loging.i("CircleDial", "onScroll y : " + y);

                    if (y > 0) {//위로

                    } else if (y < 0) { // 아래로

                    }

                    mDialInnerCheck = false;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                if (mDialInnerCheck) {
                    CommonJava.Loging.i("CircleDial", "onFling motionEvent : " + motionEvent);
                    CommonJava.Loging.i("CircleDial", "onFling motionEvent1 : " + motionEvent1);
                    CommonJava.Loging.i("CircleDial", "onFling v : " + v);
                    CommonJava.Loging.i("CircleDial", "onFling v1 : " + v1);
                    mDialInnerCheck = false;
                }
                return false;
            }
        });

    }

    private void findViewById() {
        widget_fragment = mView.findViewById(R.id.widget_fragment);
    }

    private void setOnTouch() {

        widget_fragment.findViewById(R.id.slideImage).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonJava.Loging.i("CircleDial", "onTouch");
                CommonJava.Loging.i("CircleDial", "onTouch");
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDialInnerCheck = true;
                        break;
                }

                return false;
            }
        });
    }

    public static void setOnTouchCircleDial(MotionEvent motionEvent) {/*
        CommonJava.Loging.i("CircleDial","setOnTouchCircleDial motionEvent : "+motionEvent);
        CommonJava.Loging.i("CircleDial","setOnTouchCircleDial gestureDetector : "+gestureDetector);*/
        gestureDetector.onTouchEvent(motionEvent);
    }

    class CircleDialAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

}
