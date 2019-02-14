package com.whoami.gcxhzz.base.fragment;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoami.gcxhzz.R;


/**
 * Created by algorithm on 2017/11/14.
 */

public abstract class BaseTitleFragment extends BaseFragment {

    protected TextView mTitle;
    protected ImageView mLeftImage, mRightImage;
    protected TextView mLeftText, mRightText;

    /**
     * @param idResLeft
     * @param title
     * @param idResRight
     */
    protected void setTitle(@DrawableRes int idResLeft, String title, @DrawableRes int idResRight) {

        mTitle = (TextView) mRootView.findViewById(R.id.title);
        mLeftImage = (ImageView) mRootView.findViewById(R.id.iv_left);
        mRightImage = (ImageView) mRootView.findViewById(R.id.iv_right);

        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
        }

        if (idResLeft != 0) {
            mLeftImage.setImageResource(idResLeft);
            mLeftImage.setVisibility(View.VISIBLE);

            mLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }



        if (idResRight != 0) {
            mRightImage.setImageResource(idResRight);
            mRightImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param idResLeft
     * @param title
     * @param rightText
     */
    protected void setTitle(@DrawableRes int idResLeft, String title, String rightText) {
        mTitle = (TextView) mRootView.findViewById(R.id.title);
        mLeftImage = (ImageView) mRootView.findViewById(R.id.iv_left);
        mRightText = (TextView) mRootView.findViewById(R.id.tv_right);

        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
        }

        if (idResLeft != 0) {
            mLeftImage.setImageResource(idResLeft);
            mLeftImage.setVisibility(View.VISIBLE);

            mLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        if (!TextUtils.isEmpty(rightText)) {
            mRightText.setText(rightText);
            mRightText.setVisibility(View.VISIBLE);
        }
    }

    protected void setTitle(String leftText, String title, String rightText) {
        mLeftText = (TextView) mRootView.findViewById(R.id.tv_left);
        mTitle = (TextView) mRootView.findViewById(R.id.title);
        mRightText = (TextView) mRootView.findViewById(R.id.tv_right);

        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(leftText)) {
            mLeftText.setText(leftText);
            mLeftText.setVisibility(View.VISIBLE);

            mLeftText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        if (!TextUtils.isEmpty(rightText)) {
            mRightText.setText(rightText);
            mRightText.setVisibility(View.VISIBLE);
        }

    }


}
