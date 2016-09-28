package com.diallock.diallock.diallock.Activity.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diallock.diallock.diallock.Activity.Common.CommonJava;
import com.diallock.diallock.diallock.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String LOG_NAME = "ImageFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mView;
    private LinearLayout image_layout;
    private ImageView sildeImage;

    private Context mContext;

    private Bitmap mCenterBitmapImg;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
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

        CommonJava.Loging.i(LOG_NAME, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_image, container, false);

        findViewById();
        init();

        return mView;
    }

    private void findViewById() {
        image_layout = (LinearLayout) mView.findViewById(R.id.image_layout);
        sildeImage = (ImageView) mView.findViewById(R.id.slideImage);
    }

    private void init() {
        mContext = getContext();

        String centerStrImgUrl = CommonJava.loadSharedPreferences(mContext, "imgUrl");

        if (centerStrImgUrl != null && !centerStrImgUrl.isEmpty()) {
            Uri centerImgUrl = Uri.parse(centerStrImgUrl);
            CommonJava.Loging.i(mContext.getClass().getName(), "centerStrImgUrl : " + centerStrImgUrl);
            try {
                mCenterBitmapImg = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), centerImgUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CommonJava.Loging.e(mContext.getClass().getName(), "centerStrImgUrl Null ");
            //TODO: 기본 이미지 로고 넣어야 함
            mCenterBitmapImg = BitmapFactory.decodeResource(getResources(), R.drawable.dialcenter);
        }

        int radius = mCenterBitmapImg.getHeight() >= mCenterBitmapImg.getWidth() ? mCenterBitmapImg.getHeight() / 2 : mCenterBitmapImg.getWidth() / 2;

        mCenterBitmapImg = getCircleBitmap(mCenterBitmapImg, radius);

        sildeImage.setImageBitmap(mCenterBitmapImg);
    }

    /**
     * 비트맵 이미지 리사이징 하기
     * 비트맵 이미지 원으로 만들기
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int innerRadius) {
        Bitmap reSize = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
        Bitmap output = Bitmap.createBitmap(reSize.getWidth(), reSize.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, reSize.getWidth(), reSize.getHeight());

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        int sizeX = (reSize.getWidth() / 2);
        int sizeY = (reSize.getHeight() / 2);

        canvas.drawCircle(sizeX, sizeY, innerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(reSize, rect, rect, paint);

        return output;

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
       /* if (context instanceof OnFragmentInteractionListener) {
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
