package com.bufferchime.agriculturefact.game.quiz.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bufferchime.agriculturefact.game.quiz.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.game.quiz.activity.MainActivity;
import com.bufferchime.agriculturefact.game.quiz.activity.SettingActivity;
import com.bufferchime.agriculturefact.game.quiz.helper.AppController;
import com.bufferchime.agriculturefact.game.quiz.helper.CircularProgressIndicator;
import com.bufferchime.agriculturefact.game.quiz.helper.CircularProgressIndicator2;
import com.bufferchime.agriculturefact.game.quiz.helper.SettingsPreferences;
import com.bufferchime.agriculturefact.game.quiz.helper.StaticUtils;
import com.bufferchime.agriculturefact.game.quiz.helper.TouchImageView;
import com.bufferchime.agriculturefact.game.quiz.helper.Utils;
import com.bufferchime.agriculturefact.game.quiz.model.Question;
import com.bufferchime.agriculturefact.game.quiz.model.Review;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.bufferchime.agriculturefact.game.quiz.activity.MainActivity.bookmarkDBHelper;
import static com.bufferchime.agriculturefact.game.quiz.activity.MainActivity.context;
import static com.bufferchime.agriculturefact.game.quiz.activity.MainActivity.rewardedVideoAd;

public class FragmentPlay extends Fragment implements OnClickListener {


    public static AdRequest adRequest;
    private static int levelNo = 1;
    public Question question;
    public int questionIndex = 0,
            btnPosition = 0,
            totalScore = 0,
            count_question_completed = 0,
            score = 0,
            coin = 6,
            level_coin = 6,
            correctQuestion = 0,
            inCorrectQuestion = 0,
            rightAns;
    public TextView txtQuestionIndex,
            tvLevel,
            option_a, option_b, option_c, option_d,
            txtScore, txtTrueQuestion, txtFalseQuestion, coin_count;

    public static TextView btnOpt1, btnOpt2, btnOpt3, btnOpt4, txtQuestion, txtQuestion1;
    public FragmentComplete fragmentComplete;

    public ImageView fifty_fifty, skip_question, resetTimer, audience_poll, back, setting;

    public static SharedPreferences settings;
    public RelativeLayout playLayout, checkLayout;
    public Button btnTry;
    CardView layout_A, layout_B, layout_C, layout_D;
    private Animation animation;
    private final Handler mHandler = new Handler();
    public static SharedPreferences.Editor editor;
    private View v;
    public Animation RightSwipe_A, RightSwipe_B, RightSwipe_C, RightSwipe_D, Fade_in, fifty_fifty_anim;
    private CircularProgressIndicator2 progressBarTwo_A, progressBarTwo_B, progressBarTwo_C, progressBarTwo_D;
    public static CircularProgressIndicator progressTimer;
    public static MyCountDownTimer myCountDownTimer;
    public static Context mContext;
    public static InterstitialAd interstitial;
    public static Callback mCallback = null;
    public static ArrayList<String> options;
    public static ArrayList<Review> reviews = new ArrayList<>();
    public MyCountDownTimer myCountDownTimer1;
    public static long leftTime = 0;
    public boolean isDialogOpen = false;
    ArrayList<Question> questionList;
    public ImageView imgBookmark;

    //public View rightView;
    ///// for image question //////
    TouchImageView imgQuestion;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressBar imgProgress, rightProgress, wrongProgress;
    ImageView imgZoom;
    int click = 0;
    int textSize;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onEnteredScore(int score);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_play, container, false);
        fragmentComplete = new FragmentComplete();
        final int[] CLICKABLE = new int[]{R.id.a_layout, R.id.b_layout, R.id.c_layout, R.id.d_layout};

        for (int i : CLICKABLE) {
            v.findViewById(i).setOnClickListener(this);
        }
        mContext = getActivity().getBaseContext();

        textSize = Integer.valueOf(SettingsPreferences.getSavedTextSize(getActivity()));
        SettingsPreferences.removeSharedPreferencesData(getActivity());
        getActivity().setTitle(getString(R.string.level_pre_text) + StaticUtils.RequestlevelNo);
        RightSwipe_A = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_right_a);
        RightSwipe_B = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_right_b);
        RightSwipe_C = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_right_c);
        RightSwipe_D = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_right_d);
        Fade_in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        fifty_fifty_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fifty_fifty);
        settings = getActivity().getSharedPreferences(SettingsPreferences.SETTING_Quiz_PREF, 0);

        playLayout = (RelativeLayout) v.findViewById(R.id.innerLayout);
        playLayout.setVisibility(View.GONE);

        resetAllValue();
        reviews.clear();
        rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        progressTimer = (CircularProgressIndicator) v.findViewById(R.id.progressBarTwo);
        progressTimer.setMaxProgress(Constant.CIRCULAR_MAX_PROGRESS);
        progressTimer.setCurrentProgress(Constant.CIRCULAR_MAX_PROGRESS);
        try {
            interstitial = new InterstitialAd(getActivity());
            interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
            adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    //when ads show , we have to stop timer
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                   /* if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();

                    }*/
                }

                @Override
                public void onAdClosed() {
                    //after ad close we restart timer
                    // myCountDownTimer1= new MyCountDownTimer(Constant.TIME_PER_QUESTION, Constant.COUNT_DOWN_TIMER);
                    myCountDownTimer.start();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public static void ChangeTextSize(int size) {

        System.out.println("+++++ test size  " + size);
        if (btnOpt1 != null)
            btnOpt1.setTextSize(size);
        if (btnOpt2 != null)
            btnOpt2.setTextSize(size);
        if (btnOpt3 != null)
            btnOpt3.setTextSize(size);
        if (btnOpt4 != null)
            btnOpt4.setTextSize(size);
        if (txtQuestion != null)
            txtQuestion.setTextSize(size);
        if (txtQuestion1 != null)
            txtQuestion1.setTextSize(size);
    }

    public void resetAllValue() {
        levelNo = MainActivity.dbHelper.GetLevelById(Constant.CATE_ID, Constant.SUB_CAT_ID);
        txtQuestionIndex = (TextView) v.findViewById(R.id.txt_question);
        coin_count = (TextView) v.findViewById(R.id.coin_count);
        imgProgress = (ProgressBar) v.findViewById(R.id.imgProgress);
        rightProgress = (ProgressBar) v.findViewById(R.id.rightProgress);
        wrongProgress = (ProgressBar) v.findViewById(R.id.wrongProgress);
        imgQuestion = (TouchImageView) v.findViewById(R.id.imgQuestion);

        checkLayout = (RelativeLayout) v.findViewById(R.id.checkLayout);
        btnTry = (Button) v.findViewById(R.id.btnTry);

        btnOpt1 = (TextView) v.findViewById(R.id.btnOpt1);
        btnOpt2 = (TextView) v.findViewById(R.id.btnOpt2);
        btnOpt3 = (TextView) v.findViewById(R.id.btnOpt3);
        btnOpt4 = (TextView) v.findViewById(R.id.btnOpt4);

        option_a = (TextView) v.findViewById(R.id.option_a);
        option_b = (TextView) v.findViewById(R.id.option_b);
        option_c = (TextView) v.findViewById(R.id.option_c);
        option_d = (TextView) v.findViewById(R.id.option_d);
        back = (ImageView) v.findViewById(R.id.back);
        setting = (ImageView) v.findViewById(R.id.setting);
        tvLevel = (TextView) v.findViewById(R.id.tvLevel);

        imgZoom = (ImageView) v.findViewById(R.id.imgZoom);
        imgBookmark = (ImageView) v.findViewById(R.id.imgBookmark);
        imgBookmark.setTag("unmark");
        coin_count.setText(String.valueOf(coin));


        txtTrueQuestion = (TextView) v.findViewById(R.id.txtTrueQuestion);
        txtTrueQuestion.setText("0");
        txtFalseQuestion = (TextView) v.findViewById(R.id.txtFalseQuestion);
        txtFalseQuestion.setText("0");
        txtQuestion = (TextView) v.findViewById(R.id.txtQuestion);
        txtQuestion1 = (TextView) v.findViewById(R.id.txtQuestion1);
        layout_A = (CardView) v.findViewById(R.id.a_layout);
        layout_B = (CardView) v.findViewById(R.id.b_layout);
        layout_C = (CardView) v.findViewById(R.id.c_layout);
        layout_D = (CardView) v.findViewById(R.id.d_layout);

        ChangeTextSize(textSize);
        progressBarTwo_A = (CircularProgressIndicator2) v.findViewById(R.id.progress_A);
        progressBarTwo_B = (CircularProgressIndicator2) v.findViewById(R.id.progress_B);
        progressBarTwo_C = (CircularProgressIndicator2) v.findViewById(R.id.progress_C);
        progressBarTwo_D = (CircularProgressIndicator2) v.findViewById(R.id.progress_D);
        progressBarTwo_A.SetAttributes(Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_BG_COLOR), Color.WHITE, Constant.AUD_PROGRESS_TEXT_SIZE);
        progressBarTwo_B.SetAttributes(Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_BG_COLOR), Color.WHITE, Constant.AUD_PROGRESS_TEXT_SIZE);
        progressBarTwo_C.SetAttributes(Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_BG_COLOR), Color.WHITE, Constant.AUD_PROGRESS_TEXT_SIZE);
        progressBarTwo_D.SetAttributes(Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_COLOR), Color.parseColor(Constant.AUD_PROGRESS_BG_COLOR), Color.WHITE, Constant.AUD_PROGRESS_TEXT_SIZE);

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.right_ans_anim); // Change alpha from fully visible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
        totalScore = SettingsPreferences.getScore(mContext);
        count_question_completed = SettingsPreferences.getCountQuestionCompleted(mContext);
        coin = SettingsPreferences.getPoint(mContext);
        txtScore = (TextView) v.findViewById(R.id.txtScore);
        txtScore.setText(String.valueOf(score));
        coin_count.setText(String.valueOf(coin));

        rightProgress.setMax(Constant.MAX_QUESTION_PER_LEVEL);
        wrongProgress.setMax(Constant.MAX_QUESTION_PER_LEVEL);

        if (Utils.isNetworkAvailable(getActivity())) {
            questionList = filter(FragmentLock.questionList, String.valueOf(StaticUtils.RequestlevelNo));
            Collections.shuffle(questionList);
            playLayout.setVisibility(View.VISIBLE);
            nextQuizQuestion();
            checkLayout.setVisibility(View.GONE);
        } else {
            playLayout.setVisibility(View.GONE);
            checkLayout.setVisibility(View.VISIBLE);

        }
        tvLevel.setText("Level" + " : " + StaticUtils.RequestlevelNo);
        //myCountDownTimer = new MyCountDownTimer(Constant.TIME_PER_QUESTION, Constant.COUNT_DOWN_TIMER);



        imgBookmark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgBookmark.getTag().equals("unmark")) {
                    String solution = question.getNote();
                    MainActivity.bookmarkDBHelper.insertIntoDB(question.getId(),
                            question.getQuestion(),
                            question.getTrueAns(),
                            solution,
                            question.getImage(),
                            options.get(0).trim(),
                            options.get(1).trim(),
                            options.get(2).trim(),
                            options.get(3).trim());
                    imgBookmark.setImageResource(R.drawable.ic_mark);
                    imgBookmark.setTag("mark");
                } else {
                    MainActivity.bookmarkDBHelper.delete_id(question.getId());
                    imgBookmark.setImageResource(R.drawable.ic_unmark);
                    imgBookmark.setTag("unmark");
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                BackButtonMethod();
            }
        });
        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingButtonMethod();
            }
        });

    }


    private void nextQuizQuestion() {
        myCountDownTimer = new MyCountDownTimer(Constant.TIME_PER_QUESTION, Constant.COUNT_DOWN_TIMER);
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer.start();
        } else {
            myCountDownTimer.start();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        Constant.LeftTime = 0;
        leftTime = 0;
        setAgain();
        if (questionIndex >= Constant.MAX_QUESTION_PER_LEVEL) {
            levelCompleted();
        }
        layout_A.setBackgroundResource(R.drawable.answer_bg);
        layout_B.setBackgroundResource(R.drawable.answer_bg);
        layout_C.setBackgroundResource(R.drawable.answer_bg);
        layout_D.setBackgroundResource(R.drawable.answer_bg);
        layout_A.clearAnimation();
        layout_B.clearAnimation();
        layout_C.clearAnimation();
        layout_D.clearAnimation();


        layout_A.setClickable(true);
        layout_B.setClickable(true);
        layout_C.setClickable(true);
        layout_D.setClickable(true);
        btnOpt1.startAnimation(RightSwipe_A);
        btnOpt2.startAnimation(RightSwipe_B);
        btnOpt3.startAnimation(RightSwipe_C);
        btnOpt4.startAnimation(RightSwipe_D);
        txtQuestion1.startAnimation(Fade_in);
        if (questionIndex < questionList.size()) {
            question = questionList.get(questionIndex);
            int temp = questionIndex;
            imgQuestion.resetZoom();
            txtQuestionIndex.setText(++temp + "/" + Constant.MAX_QUESTION_PER_LEVEL);
            if (!question.getImage().isEmpty()) {
                imgZoom.setVisibility(View.VISIBLE);
                txtQuestion1.setVisibility(View.VISIBLE);
                txtQuestion.setVisibility(View.GONE);
                imgQuestion.setImageUrl(question.getImage(), imageLoader);
                imgQuestion.setVisibility(View.VISIBLE);
                imgProgress.setVisibility(View.GONE);
                imgZoom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click++;
                        if (click == 1)
                            imgQuestion.setZoom(1.25f);
                        else if (click == 2)
                            imgQuestion.setZoom(1.50f);
                        else if (click == 3)
                            imgQuestion.setZoom(1.75f);
                        else if (click == 4) {
                            imgQuestion.setZoom(2.00f);
                            click = 0;
                        }
                    }
                });
            } else {
                imgZoom.setVisibility(View.GONE);
                imgQuestion.setVisibility(View.GONE);
                txtQuestion1.setVisibility(View.GONE);
                txtQuestion.setVisibility(View.VISIBLE);
            }


            System.out.println("img Question " + question.getImage());
            txtQuestion.setText(Html.fromHtml(question.getQuestion()));
            txtQuestion1.setText(Html.fromHtml(question.getQuestion()));
            options = new ArrayList<String>();
            options.addAll(question.getOptions());
            Collections.shuffle(options);

            btnOpt1.setText(Html.fromHtml(options.get(0).trim()));
            btnOpt2.setText(Html.fromHtml(options.get(1).trim()));
            btnOpt3.setText(Html.fromHtml(options.get(2).trim()));
            btnOpt4.setText(Html.fromHtml(options.get(3).trim()));

            int isfav = bookmarkDBHelper.getBookmarks(question.getId());

            if (isfav == question.getId()) {
                imgBookmark.setImageResource(R.drawable.ic_mark);
                imgBookmark.setTag("mark");
            } else {
                imgBookmark.setImageResource(R.drawable.ic_unmark);
                imgBookmark.setTag("unmark");
            }
        }
    }

    public void levelCompleted() {
        StaticUtils.TotalQuestion = Constant.MAX_QUESTION_PER_LEVEL;
        StaticUtils.CoreectQuetion = correctQuestion;
        StaticUtils.WrongQuation = inCorrectQuestion;
        myCountDownTimer.cancel();
        editor = settings.edit();
        if (correctQuestion >= Constant.correctAnswer && levelNo == StaticUtils.RequestlevelNo) {
            levelNo = levelNo + 1;
            if (MainActivity.dbHelper.isExist(Constant.CATE_ID, Constant.SUB_CAT_ID)) {
                MainActivity.dbHelper.UpdateLevel(Constant.CATE_ID, Constant.SUB_CAT_ID, levelNo);
            } else {
                MainActivity.dbHelper.insertIntoDB(Constant.CATE_ID, Constant.SUB_CAT_ID, levelNo);
            }
            SettingsPreferences.setNoCompletedLevel(mContext, levelNo);
        }
        if (correctQuestion >= Constant.correctAnswer) {
            editor.putBoolean(SettingsPreferences.IS_LAST_LEVEL_COMPLETED, true);
        } else {
            editor.putBoolean(SettingsPreferences.IS_LAST_LEVEL_COMPLETED, false);
        }
        if (correctQuestion >= 3 && correctQuestion <= 4) {
            coin = coin + Constant.giveOneCoin;
            level_coin = Constant.giveOneCoin;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 4 && correctQuestion <= 5) {
            coin = coin + Constant.giveTwoCoins;
            level_coin = Constant.giveTwoCoins;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 6 && correctQuestion < 8) {
            coin = coin + Constant.giveThreeCoins;
            level_coin = Constant.giveThreeCoins;
            StaticUtils.level_coin = level_coin;
        } else if (correctQuestion >= 8) {
            coin = coin + Constant.giveFourCoins;
            level_coin = Constant.giveFourCoins;
            StaticUtils.level_coin = level_coin;
        }
        SettingsPreferences.setPoint(mContext, coin);
        coin_count.setText(String.valueOf(coin));
        editor.apply();

        mContext = getActivity().getBaseContext();
        saveScore();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentComplete).addToBackStack("tag").commitAllowingStateLoss();
        blankAllValue();
        SettingsPreferences.removeSharedPreferencesData(getActivity());

    }


    @Override
    public void onClick(View v) {
        if (questionIndex < questionList.size()) {
            question = questionList.get(questionIndex);
            layout_A.setClickable(false);
            layout_B.setClickable(false);
            layout_C.setClickable(false);
            layout_D.setClickable(false);
            if (progressBarTwo_A.getVisibility() == (View.VISIBLE)) {
                progressBarTwo_A.setVisibility(View.GONE);
                progressBarTwo_B.setVisibility(View.GONE);
                progressBarTwo_C.setVisibility(View.GONE);
                progressBarTwo_D.setVisibility(View.GONE);
                option_a.setVisibility(View.VISIBLE);
                option_b.setVisibility(View.VISIBLE);
                option_c.setVisibility(View.VISIBLE);
                option_d.setVisibility(View.VISIBLE);
            }
            Constant.LeftTime = 0;
            switch (v.getId()) {
                case R.id.a_layout:
                    AddReview(question, btnOpt1);
                    if (btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        layout_A.setBackgroundResource(R.drawable.right_gradient);
                        layout_A.startAnimation(animation);
                        layout_B.setBackgroundResource(R.drawable.answer_bg);
                        layout_C.setBackgroundResource(R.drawable.answer_bg);
                        layout_D.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!btnOpt1.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {

                        layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();
                        String trueAns = question.getTrueAns().trim();
                        if (btnOpt2.getText().toString().trim().equals(trueAns)) {
                            layout_B.startAnimation(animation);
                            layout_B.setBackgroundResource(R.drawable.right_gradient);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);


                        } else if (btnOpt3.getText().toString().trim().equals(trueAns)) {
                            layout_C.setBackgroundResource(R.drawable.right_gradient);
                            layout_C.startAnimation(animation);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt4.getText().toString().trim().equals(trueAns)) {
                            layout_D.setBackgroundResource(R.drawable.right_gradient);
                            layout_D.startAnimation(animation);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);
                        }


                        questionIndex++;
                    }

                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }
                    break;

                case R.id.b_layout:
                    AddReview(question, btnOpt2);
                    if (btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        layout_B.setBackgroundResource(R.drawable.right_gradient);
                        layout_B.startAnimation(animation);
                        layout_A.setBackgroundResource(R.drawable.answer_bg);
                        layout_C.setBackgroundResource(R.drawable.answer_bg);
                        layout_D.setBackgroundResource(R.drawable.answer_bg);

                    } else if (!btnOpt2.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {

                        String trueAns = question.getTrueAns().trim();
                        layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();

                        if (btnOpt1.getText().toString().trim().equals(trueAns)) {
                            layout_A.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.right_gradient);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt3.getText().toString().trim().equals(trueAns)) {
                            layout_C.setBackgroundResource(R.drawable.right_gradient);
                            layout_C.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt4.getText().toString().trim().equals(trueAns)) {
                            layout_D.setBackgroundResource(R.drawable.right_gradient);
                            layout_D.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);
                        }

                        questionIndex++;
                    }

                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }
                    break;
                case R.id.c_layout:
                    AddReview(question, btnOpt3);
                    if (btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        questionIndex++;
                        addScore();
                        layout_C.setBackgroundResource(R.drawable.right_gradient);
                        layout_C.startAnimation(animation);
                        layout_A.setBackgroundResource(R.drawable.answer_bg);
                        layout_B.setBackgroundResource(R.drawable.answer_bg);
                        layout_D.setBackgroundResource(R.drawable.answer_bg);


                    } else if (!btnOpt3.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                        String trueAns = question.getTrueAns().trim();
                        WrongQuestion();

                        if (btnOpt1.getText().toString().trim().equals(trueAns)) {
                            layout_A.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.right_gradient);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt2.getText().toString().trim().equals(trueAns)) {
                            layout_B.startAnimation(animation);
                            layout_B.setBackgroundResource(R.drawable.right_gradient);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_D.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt4.getText().toString().trim().equals(trueAns)) {
                            layout_D.setBackgroundResource(R.drawable.right_gradient);
                            layout_D.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);
                        }
                        questionIndex++;
                    }
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();
                        leftTime = 0;
                    }

                    break;
                case R.id.d_layout:
                    AddReview(question, btnOpt4);
                    // AnswerButtonClickMethod(layout_D, btnOpt4);
                    if (btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        layout_D.setBackgroundResource(R.drawable.right_gradient);
                        layout_D.startAnimation(animation);
                        questionIndex++;
                        layout_A.setBackgroundResource(R.drawable.answer_bg);
                        layout_B.setBackgroundResource(R.drawable.answer_bg);
                        layout_C.setBackgroundResource(R.drawable.answer_bg);

                        addScore();
                    } else if (!btnOpt4.getText().toString().trim().equalsIgnoreCase(question.getTrueAns().trim())) {
                        layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                        WrongQuestion();
                        String trueAns = question.getTrueAns().trim();

                        if (btnOpt1.getText().toString().trim().equals(trueAns)) {
                            layout_A.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.right_gradient);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt2.getText().toString().trim().equals(trueAns)) {
                            layout_B.startAnimation(animation);
                            layout_B.setBackgroundResource(R.drawable.right_gradient);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_C.setBackgroundResource(R.drawable.answer_bg);

                        } else if (btnOpt3.getText().toString().trim().equals(trueAns)) {
                            layout_C.setBackgroundResource(R.drawable.right_gradient);
                            layout_C.startAnimation(animation);
                            layout_A.setBackgroundResource(R.drawable.answer_bg);
                            layout_B.setBackgroundResource(R.drawable.answer_bg);

                        }
                        questionIndex++;
                    }
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    if (myCountDownTimer1 != null) {
                        myCountDownTimer1.cancel();

                    }
                    break;
            }

        }
    }


    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
            if (getActivity() != null) {
                nextQuizQuestion();
            }
        }
    };

    /**/
    public void PlayAreaLeaveDialog(final Activity context) {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();

        }
        Constant.LeftTime = leftTime;

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.exit_msg));
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();
        // Setting OK Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                ((MainActivity) context).getSupportFragmentManager().popBackStack();
                leftTime = 0;
                Constant.LeftTime = 0;
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog1.dismiss();
                if (Constant.LeftTime != 0) {
                    myCountDownTimer = new MyCountDownTimer(Constant.LeftTime, 1000);
                    myCountDownTimer.start();

                }
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    //add attended question in ReviewList
    public void AddReview(Question question, TextView tvBtnOpt) {
        reviews.add(new Review(question.getId(),
                question.getQuestion(),
                question.getImage(),
                question.getTrueAns(),
                tvBtnOpt.getText().toString(),
                question.getOptions(),
                question.getNote()));

        leftTime = 0;
        Constant.LeftTime = 0;
        mHandler.postDelayed(mUpdateUITimerTask, 1000);
        txtScore.setText(String.valueOf(score));
    }


    private void addScore() {

        rightSound();
        correctQuestion++;
        txtTrueQuestion.setText(String.valueOf(correctQuestion));
        rightProgress.setProgress(correctQuestion);
        totalScore = totalScore + 5;
        count_question_completed = count_question_completed + 5;
        score = score + 5;
        txtScore.setText(String.valueOf(score));
        rightAns = SettingsPreferences.getRightAns(mContext);
        rightAns++;
        SettingsPreferences.setRightAns(mContext, rightAns);
        SettingsPreferences.setScore(mContext, totalScore);
        SettingsPreferences.setCountQuestionCompleted(mContext, count_question_completed);
    }

    private void WrongQuestion() {
        setAgain();
        playWrongSound();
        saveScore();
        inCorrectQuestion++;
        totalScore = totalScore - 2;
        count_question_completed = count_question_completed - 2;
        score = score - 2;
        txtFalseQuestion.setText(String.valueOf(inCorrectQuestion));
        wrongProgress.setProgress(inCorrectQuestion);
        txtScore.setText(String.valueOf(score));
    }

    /*
     * Save score in Preferences
     */
    private void saveScore() {
        editor = settings.edit();
        SettingsPreferences.setCountQuestionCompleted(mContext, count_question_completed);
        editor.putInt(SettingsPreferences.TOTAL_SCORE, totalScore);
        editor.putInt(SettingsPreferences.LAST_LEVEL_SCORE, score);
        editor.putInt(SettingsPreferences.COUNT_QUESTION_COMPLETED, count_question_completed);
        editor.apply();

        try {
            mCallback.onEnteredScore(totalScore);
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }

    }

    //play sound when answer is correct
    public void rightSound() {
        if (SettingsPreferences.getSoundEnableDisable(getActivity())) {
            StaticUtils.setrightAnssound(getActivity());
        }
        if (SettingsPreferences.getVibration(getActivity())) {
            StaticUtils.vibrate(getActivity(), StaticUtils.VIBRATION_DURATION);
        }
    }

    //play sound when answer is incorrect
    private void playWrongSound() {
        if (SettingsPreferences.getSoundEnableDisable(getActivity())) {
            StaticUtils.setwronAnssound(getActivity());
        }
        if (SettingsPreferences.getVibration(getActivity())) {
            StaticUtils.vibrate(getActivity(), StaticUtils.VIBRATION_DURATION);
        }
    }

    //set progress again after next question
    private void setAgain() {
        if (progressBarTwo_A.getVisibility() == (View.VISIBLE)) {
            progressBarTwo_A.setVisibility(View.GONE);
            progressBarTwo_B.setVisibility(View.GONE);
            progressBarTwo_C.setVisibility(View.GONE);
            progressBarTwo_D.setVisibility(View.GONE);
        }

    }

    //Skip Question lifeline
    public void SkipQuestionMethod() {
        CheckSound();

        if (!SettingsPreferences.isSkipUsed(getActivity())) {
            if (coin >= 4) {
                if (myCountDownTimer1 != null) {
                    myCountDownTimer1.cancel();
                    leftTime = 0;
                }
                leftTime = 0;
                if (myCountDownTimer != null) {
                    myCountDownTimer.cancel();

                }
                //skip_quation.setImageResource(R.drawable.dec_skip);
                coin = coin - 4;
                coin_count.setText(String.valueOf(coin));
                questionIndex++;
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);

                nextQuizQuestion();
                SettingsPreferences.setSkip(getActivity());
            } else {
                ShowRewarded(getActivity());
            }
        } else {
            AlreadyUsed();
        }
    }

    //Fifty Fifty Lifeline
    public void FiftyFiftyLifeline() {
        CheckSound();
        if (!SettingsPreferences.isFiftyFiftyUsed(getActivity())) {
            if (coin >= 4) {
                btnPosition = 0;
                //fifty_fifty.setImageResource(R.drawable.dec_fifty);
                // TODO Auto-generated method stub
                coin = coin - 4;
                coin_count.setText(String.valueOf(coin));
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 1;
                }

                if (btnOpt2.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {

                    btnPosition = 2;
                }

                if (btnOpt3.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {

                    btnPosition = 3;
                }

                if (btnOpt4.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 4;
                }
                if (btnPosition == 1) {
                    layout_B.startAnimation(fifty_fifty_anim);
                    layout_C.startAnimation(fifty_fifty_anim);


                } else if (btnPosition == 2) {
                    layout_C.startAnimation(fifty_fifty_anim);
                    layout_D.startAnimation(fifty_fifty_anim);

                } else if (btnPosition == 3) {
                    layout_D.startAnimation(fifty_fifty_anim);
                    layout_A.startAnimation(fifty_fifty_anim);

                } else if (btnPosition == 4) {
                    layout_A.startAnimation(fifty_fifty_anim);
                    layout_B.startAnimation(fifty_fifty_anim);

                }
                SettingsPreferences.setFifty_Fifty(getActivity());
            } else {
                ShowRewarded(getActivity());
            }
        } else {
            AlreadyUsed();
        }
    }

    //Reset Timer lifeline method
    public void ResetTimerLifeline() {
        CheckSound();
        if (!SettingsPreferences.isResetUsed(getActivity())) {
            if (coin >= 4) {

                coin = coin - 4;
                coin_count.setText(String.valueOf(coin));

                if (myCountDownTimer1 != null) {
                    myCountDownTimer1.cancel();

                }
                leftTime = 0;
                // TODO Auto-generated method stub
                if (myCountDownTimer != null) {
                    myCountDownTimer.cancel();
                    myCountDownTimer.start();
                } else {
                    myCountDownTimer.start();
                }
                SettingsPreferences.setReset(getActivity());
            } else {
                ShowRewarded(getActivity());

            }
        } else {
            AlreadyUsed();
        }
    }

    //AudiencePoll Lifeline method
    public void AudiencePollLifeline() {
        CheckSound();
        if (!SettingsPreferences.isAudiencePollUsed(getActivity())) {
            if (coin >= 4) {
                btnPosition = 0;
                //audience_poll.setImageResource(R.drawable.dec_audiance);
                coin = coin - 4;
                coin_count.setText(String.valueOf(coin));
                int min = 45;
                int max = 70;
                Random r = new Random();
                int A = r.nextInt(max - min + 1) + min;
                int remain1 = 100 - A;
                int B = r.nextInt(((remain1 - 10)) + 1);
                int remain2 = remain1 - B;
                int C = r.nextInt(((remain2 - 5)) + 1);
                int D = remain2 - C;
                progressBarTwo_A.setVisibility(View.VISIBLE);
                progressBarTwo_B.setVisibility(View.VISIBLE);
                progressBarTwo_C.setVisibility(View.VISIBLE);
                progressBarTwo_D.setVisibility(View.VISIBLE);

                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 1;
                }
                if (btnOpt2.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 2;
                }

                if (btnOpt3.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 3;
                }

                if (btnOpt4.getText().toString().trim().equalsIgnoreCase(questionList.get(questionIndex).getTrueAns().trim())) {
                    btnPosition = 4;
                }

                if (btnPosition == 1) {
                    progressBarTwo_A.setCurrentProgress(A);
                    progressBarTwo_B.setCurrentProgress(B);
                    progressBarTwo_C.setCurrentProgress(C);
                    progressBarTwo_D.setCurrentProgress(D);

                } else if (btnPosition == 2) {
                    progressBarTwo_B.setCurrentProgress(A);
                    progressBarTwo_C.setCurrentProgress(C);
                    progressBarTwo_D.setCurrentProgress(D);
                    progressBarTwo_A.setCurrentProgress(B);

                } else if (btnPosition == 3) {
                    progressBarTwo_C.setCurrentProgress(A);
                    progressBarTwo_B.setCurrentProgress(C);
                    progressBarTwo_D.setCurrentProgress(D);
                    progressBarTwo_A.setCurrentProgress(B);

                } else if (btnPosition == 4) {
                    progressBarTwo_D.setCurrentProgress(A);
                    progressBarTwo_B.setCurrentProgress(C);
                    progressBarTwo_C.setCurrentProgress(D);
                    progressBarTwo_A.setCurrentProgress(B);

                }
                option_a.setVisibility(View.VISIBLE);
                option_b.setVisibility(View.VISIBLE);
                option_c.setVisibility(View.VISIBLE);
                option_d.setVisibility(View.VISIBLE);
                SettingsPreferences.setAudiencePoll(getActivity());
            } else {
                ShowRewarded(getActivity());
            }
        } else {
            AlreadyUsed();
        }
    }


    //Show alert dialog when lifeline already used in current level
    public void AlreadyUsed() {

        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();

        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();

        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.lifeline_dialog, null);
        dialog.setView(dialogView);

        TextView ok = (TextView) dialogView.findViewById(R.id.ok);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        alertDialog.setCancelable(false);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                if (leftTime != 0) {
                    myCountDownTimer1 = new MyCountDownTimer(leftTime, 1000);
                    myCountDownTimer1.start();
                }
            }
        });

    }

    public void BackButtonMethod() {
        CheckSound();
        PlayAreaLeaveDialog(getActivity());
    }

    public void CheckSound() {
        if (SettingsPreferences.getSoundEnableDisable(getActivity())) {
            StaticUtils.backSoundonclick(getActivity());
        }
        if (SettingsPreferences.getVibration(getActivity())) {
            StaticUtils.vibrate(getActivity(), StaticUtils.VIBRATION_DURATION);
        }
    }

    public void SettingButtonMethod() {
        CheckSound();
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();

        }
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.open_next, R.anim.close_next);
    }


    //filter current level question
    public static ArrayList<Question> filter(ArrayList<Question> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Question> filteredModelList = new ArrayList<>();
        for (Question model : models) {
            final String text = "" + model.getLevel().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void blankAllValue() {
        questionIndex = 0;
        totalScore = 0;
        count_question_completed = 0;
        score = 0;
        correctQuestion = 0;
        inCorrectQuestion = 0;
    }


    //Show dialog for rewarded ad
    //if user has not enough coin to use lifeline
    public void ShowRewarded(final Context context) {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        if (!Utils.isNetworkAvailable(getActivity())) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Internet Connection Error!");
            dialog.setMessage("Internet Connection Error! Please connect to working Internet connection");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (leftTime != 0) {
                        myCountDownTimer1 = new MyCountDownTimer(leftTime, 1000);
                        myCountDownTimer1.start();
                    }
                    isDialogOpen = false;
                }
            });
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (leftTime != 0) {
                        myCountDownTimer1 = new MyCountDownTimer(leftTime, 1000);
                        myCountDownTimer1.start();
                    }
                    isDialogOpen = false;
                }
            });
            dialog.show();
            isDialogOpen = true;

        } else {
            isDialogOpen = true;
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
            dialog.setView(dialogView);
            TextView skip = (TextView) dialogView.findViewById(R.id.skip);
            TextView watchNow = (TextView) dialogView.findViewById(R.id.watch_now);
            final AlertDialog alertDialog = dialog.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            alertDialog.setCancelable(false);
            skip.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    isDialogOpen = false;
                    if (leftTime != 0) {
                        myCountDownTimer1 = new MyCountDownTimer(leftTime, 1000);
                        myCountDownTimer1.start();
                    }

                }
            });
            watchNow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRewardedVideo();
                    alertDialog.dismiss();
                    isDialogOpen = false;
                }
            });

        }
    }

    public void showRewardedVideo() {

        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        } else if (!rewardedVideoAd.isLoaded()) {
            loadRewardedVideoAd();
            if (rewardedVideoAd.isLoaded()) {
                rewardedVideoAd.show();
            }
        }
    }

    public static void loadRewardedVideoAd() {

        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(context.getResources().getString(R.string.admob_Rewarded_Video_Ads), new AdRequest.Builder().build());
        }
    }

    RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
        }

        @Override
        public void onRewardedVideoCompleted() {

        }

        @Override
        public void onRewardedVideoAdOpened() {
        }

        @Override
        public void onRewardedVideoStarted() {
        }

        @Override
        public void onRewardedVideoAdClosed() {
            loadRewardedVideoAd();
        }

        @Override
        public void onRewarded(RewardItem reward) {
            // Reward the user.
            coin = coin + 4;
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
            if (Utils.isNetworkAvailable(getActivity())) {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                    coin = coin + 4;
                } else {
                    interstitial = new InterstitialAd(getActivity());
                    interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
                    AdRequest adRequest = new AdRequest.Builder().build();
                    interstitial.loadAd(adRequest);
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                        coin = coin + 4;
                    }
                }
            }
        }


    };

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leftTime = millisUntilFinished;

            int progress = (int) (millisUntilFinished / 1000);

            if (progressTimer == null) {
                progressTimer = (CircularProgressIndicator) v.findViewById(R.id.progressBarTwo);
            } else {
                progressTimer.setCurrentProgress(progress);
            }
            //when left last 5 second we show progress color red
            if (millisUntilFinished <= 6000) {
                progressTimer.SetTimerAttributes(Color.RED, Color.parseColor(Constant.PROGRESS_BG_COLOR), Color.RED, Constant.PROGRESS_TEXT_SIZE);
            } else {
                progressTimer.SetTimerAttributes(Color.parseColor(Constant.PROGRESS_COLOR), Color.parseColor(Constant.PROGRESS_BG_COLOR), Color.WHITE, Constant.PROGRESS_TEXT_SIZE);
            }
        }

        @Override
        public void onFinish() {
            if (questionIndex >= Constant.MAX_QUESTION_PER_LEVEL) {
                levelCompleted();

            } else {

                //WrongQuestion();
                mHandler.postDelayed(mUpdateUITimerTask, 100);
                questionIndex++;
            }

        }
    }

    public static void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.show();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUi();

    }


    @Override
    public void onResume() {

        if (leftTime != 0) {
            myCountDownTimer1 = new MyCountDownTimer(leftTime, Constant.COUNT_DOWN_TIMER);
            myCountDownTimer1.start();

        }
        super.onResume();
        updateUi();


        coin_count.setText(String.valueOf(coin));
    }

    @Override
    public void onPause() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }
        super.onPause();
    }

    void updateUi() {
        if (getActivity() == null) ;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            //leftTime = 0;
        }
        leftTime = 0;
        if (myCountDownTimer1 != null) {
            myCountDownTimer1.cancel();
        }

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        blankAllValue();
        super.onDestroyView();
    }
}