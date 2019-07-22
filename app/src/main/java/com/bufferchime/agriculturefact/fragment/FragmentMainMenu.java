package com.bufferchime.agriculturefact.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.activity.BookmarkList;
import com.bufferchime.agriculturefact.activity.InstructionActivity;
import com.bufferchime.agriculturefact.activity.MainActivity;
import com.bufferchime.agriculturefact.activity.PrivacyPolicy;
import com.bufferchime.agriculturefact.helper.SettingsPreferences;
import com.bufferchime.agriculturefact.helper.StaticUtils;


public class FragmentMainMenu extends Fragment implements View.OnClickListener {

    private View mSignIn;
    private View mSignOut;

    private ImageView Bookmark;
    CoordinatorLayout layout;
    private Listener mListener = null;
    private boolean mShowSignInButton = true;


    public interface Listener {
        // called when the user presses the `Easy` or `Okay` button; will pass in which via `hardMode`
        void onStartGameRequested();

        // called when the user presses the `Show Achievements` button
        void onShowAchievementsRequested();

        // called when the user presses the `Show Leaderboards` button
        void onShowLeaderboardsRequested();

        // called when the user presses the `Sign In` button
        void onSignInButtonClicked();

        // called when the user presses the `Sign Out` button
        void onSignOutButtonClicked();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        layout = (CoordinatorLayout) view.findViewById(R.id.layout);
        final int[] clickableIds = new int[]{

                R.id.instruction,

                R.id.start,

                R.id.imgPrivacy
        };

        for (int clickableId : clickableIds) {
            view.findViewById(clickableId).setOnClickListener(this);
        }


        updateUI();

        Bookmark = (ImageView) view.findViewById(R.id.imgBookmark);
        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.btnClick(view, getActivity());
                Intent intent = new Intent(getActivity(), BookmarkList.class);
                startActivity(intent);
            }
        });
        return view;
    }


    public void setListener(Listener listener) {
        mListener = listener;
    }


    private void updateUI() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                MainActivity.btnClick(view, getActivity());
                mListener.onStartGameRequested();
                break;

            case R.id.instruction:

                /**/
                MainActivity.btnClick(view, getActivity());
                SettingsPreferences.setLan(getContext(), true);
                if (SettingsPreferences.getSoundEnableDisable(getContext())) {
                    StaticUtils.backSoundonclick(getContext());
                }
                if (SettingsPreferences.getVibration(getContext())) {
                    StaticUtils.vibrate(getContext(), StaticUtils.VIBRATION_DURATION);
                }
                Intent playQuiz = new Intent(getActivity(), InstructionActivity.class);
                startActivity(playQuiz);


                break;

            case R.id.imgPrivacy:
                MainActivity.btnClick(view, getActivity());
                Intent privacyIntent = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(privacyIntent);
                break;

        }
    }

    public void setShowSignInButton(boolean showSignInButton) {
        mShowSignInButton = showSignInButton;
        updateUI();
    }
}