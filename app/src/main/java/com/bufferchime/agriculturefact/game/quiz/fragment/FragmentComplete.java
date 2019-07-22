package com.bufferchime.agriculturefact.game.quiz.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bufferchime.agriculturefact.game.quiz.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.game.quiz.activity.MainActivity;
import com.bufferchime.agriculturefact.game.quiz.activity.ReviewActivity;
import com.bufferchime.agriculturefact.game.quiz.activity.SettingActivity;
import com.bufferchime.agriculturefact.game.quiz.helper.AppController;
import com.bufferchime.agriculturefact.game.quiz.helper.CircularProgressIndicator2;
import com.bufferchime.agriculturefact.game.quiz.helper.SettingsPreferences;
import com.bufferchime.agriculturefact.game.quiz.helper.StaticUtils;
import com.bufferchime.agriculturefact.game.quiz.helper.Utils;
import com.bufferchime.agriculturefact.game.quiz.model.Question;
import com.bufferchime.agriculturefact.game.quiz.model.Review;
import com.google.android.gms.ads.AdListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static com.bufferchime.agriculturefact.game.quiz.fragment.FragmentPlay.filter;
import static com.bufferchime.agriculturefact.game.quiz.fragment.FragmentPlay.interstitial;

public class FragmentComplete extends Fragment implements View.OnClickListener {
    public Button btnPlayAgain, btnShare, btnRateUs, btnQuite, btnReview, btnPdf;
    public TextView txt_result_title, txt_score, txtLevelTotalScore, txt_right, txt_wrong, point, coin_count, tvLevel;
    ImageView setting, back;
    public CircularProgressIndicator2 result_prog;
    public SharedPreferences settings;
    public static Context mContex;
    int levelNo = 1,
            lastLevelScore = 0,
            coin = 0,
            totalScore = 0;

    public View v;
    public FragmentPlay fragmentPlay;
    public FragmentLock fragmentLevel;
    boolean isLevelCompleted;


    ///for pdf generate
    AlertDialog alertDialog;
    Button btnView, btnShare1, btnCancel;
    ProgressBar progressBar;
    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_complete, container, false);
        final int[] CLICKABLE = new int[]{R.id.btn_playagain, R.id.btn_share, R.id.btn_quite};
        for (int i : CLICKABLE) {
            v.findViewById(i).setOnClickListener(this);
        }
        fragmentPlay = new FragmentPlay();
        fragmentLevel = new FragmentLock();
        mContex = getActivity().getBaseContext();

        Utils.loadAd(getActivity());


     /*   interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);*/

        result_prog = (CircularProgressIndicator2) v.findViewById(R.id.result_progress);
        result_prog.SetAttributes1();
        settings = getActivity().getSharedPreferences(SettingsPreferences.SETTING_Quiz_PREF, 0);
        txt_result_title = (TextView) v.findViewById(R.id.txt_result_title);
        txt_right = (TextView) v.findViewById(R.id.right);
        txt_wrong = (TextView) v.findViewById(R.id.wrong);
        txt_score = (TextView) v.findViewById(R.id.txtScore);
        txtLevelTotalScore = (TextView) v.findViewById(R.id.total_score);
        tvLevel = (TextView) v.findViewById(R.id.tvLevel);
        setting = (ImageView) v.findViewById(R.id.setting);
        back = (ImageView) v.findViewById(R.id.back);
        btnPdf = (Button) v.findViewById(R.id.btnPdf);
        coin = SettingsPreferences.getPoint(mContex);
        totalScore = settings.getInt(SettingsPreferences.TOTAL_SCORE, 0);
        txt_score.setText("" + totalScore);
        lastLevelScore = settings.getInt(SettingsPreferences.LAST_LEVEL_SCORE, 0);
        txtLevelTotalScore.setText("" + lastLevelScore);
        point = (TextView) v.findViewById(R.id.earncoin);
        coin_count = (TextView) v.findViewById(R.id.coin_count);
        point.setText("" + StaticUtils.level_coin);
        coin_count.setText("" + coin);

        btnPlayAgain = (Button) v.findViewById(R.id.btn_playagain);
        btnRateUs = (Button) v.findViewById(R.id.btn_rate);
        btnQuite = (Button) v.findViewById(R.id.btn_quite);
        btnReview = (Button) v.findViewById(R.id.btn_review);
        btnPlayAgain.setOnClickListener(this);
        btnRateUs.setOnClickListener(this);
        btnQuite.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        btnPdf.setOnClickListener(this);
        if (FragmentPlay.reviews.size() == 0) {
            btnReview.setVisibility(View.GONE);
        }
        btnShare = (Button) v.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);
        isLevelCompleted = settings.getBoolean(SettingsPreferences.IS_LAST_LEVEL_COMPLETED, false);

        levelNo = MainActivity.dbHelper.GetLevelById(Constant.CATE_ID, Constant.SUB_CAT_ID);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {

                    try {
                        AppController.StopSound();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsPreferences.getSoundEnableDisable(getActivity())) {
                    StaticUtils.backSoundonclick(getActivity());
                }
                if (SettingsPreferences.getVibration(getActivity())) {
                    StaticUtils.vibrate(getActivity(), StaticUtils.VIBRATION_DURATION);
                }
                Intent playQuiz = new Intent(getActivity(), SettingActivity.class);
                startActivity(playQuiz);
                getActivity().overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });
        if (isLevelCompleted) {
            // levelNo--;
            if (Constant.TotalLevel == StaticUtils.RequestlevelNo) {
                btnPlayAgain.setText("Play Again");

            } else {
                txt_result_title.setText(getActivity().getString(R.string.completed));
                btnPlayAgain.setText(getResources().getString(R.string.next_play));

                tvLevel.setText(getResources().getString(R.string.next_play));
            }
        } else {
            txt_result_title.setText(getActivity().getString(R.string.not_completed));
            btnPlayAgain.setText(getResources().getString(R.string.play_next));
            tvLevel.setText(getResources().getString(R.string.play_next));
        }
        result_prog.setCurrentProgress((double) getPercentageCorrect(StaticUtils.TotalQuestion, StaticUtils.CoreectQuetion));

        txt_right.setText("" + StaticUtils.CoreectQuetion);
        txt_wrong.setText("" + StaticUtils.WrongQuation);
        /*adRequest = new AdRequest.Builder().build();
        FragmentPlay.interstitial.loadAd(adRequest);*/


        Utils.interstitial.setAdListener(new AdListener() {

         /*   @Override
            public void onAdLoaded() {
                Utils.displayInterstitial();
                super.onAdLoaded();
            }*/

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Utils.loadAd(getActivity());
            }
        });
        return v;
    }

    public static float getPercentageCorrect(int questions, int correct) {
        float proportionCorrect = ((float) correct) / ((float) questions);
        return proportionCorrect * 100;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_playagain:
                if (isLevelCompleted) {
                    if (Constant.TotalLevel == StaticUtils.RequestlevelNo) {
                        StaticUtils.RequestlevelNo = 1;
                    } else {
                        StaticUtils.RequestlevelNo = StaticUtils.RequestlevelNo + 1;
                    }

                    ArrayList<Question> question = new ArrayList<>();
                    question.clear();
                    question = filter(FragmentLock.questionList, "" + StaticUtils.RequestlevelNo);
                    if (question.size() < Constant.MAX_QUESTION_PER_LEVEL) {
                        Toast.makeText(mContex, getString(R.string.no_enough_question), Toast.LENGTH_SHORT).show();
                    } else {
                        FragmentPlay.displayInterstitial();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ft.replace(R.id.fragment_container, fragmentPlay, "fragment");
                        ft.addToBackStack("tag");
                        ft.commit();

                    }
                } else {
                    FragmentPlay.displayInterstitial();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.replace(R.id.fragment_container, fragmentPlay, "fragment");
                    ft.addToBackStack("tag");
                    ft.commit();

                }

                break;
            case R.id.btn_share:
                Utils.displayInterstitial();
                shareClicked();


                break;

            case R.id.btnPdf:
                Utils.displayInterstitial();
                PdfClicked();
                break;
            case R.id.btn_rate:

                Utils.displayInterstitial();
                rateClicked();

                break;
            case R.id.btn_quite:

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                break;
            case R.id.btn_review:

                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    public void shareClicked() {
        final String sharetext = "I have finished Level No : " + StaticUtils.RequestlevelNo + " with " + lastLevelScore + " Score in " + getString(R.string.app_name);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, "" + sharetext + "  https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
        startActivity(Intent.createChooser(share, "Share " + getString(R.string.app_name) + "!"));
    }

    private void rateClicked() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + getActivity().getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getActivity().getPackageName())));
        }
    }

    public void PdfClicked() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), WRITE_EXTERNAL_STORAGE_PERMS, 0);
        } else {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater1 = (LayoutInflater) (getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            assert inflater1 != null;
            @SuppressLint("InflateParams") final View dialogView = inflater1.inflate(R.layout.progress_dialog, null);
            dialog.setView(dialogView);
            progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
            btnView = (Button) dialogView.findViewById(R.id.btnView);
            btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
            btnShare1 = (Button) dialogView.findViewById(R.id.btnShare);
            btnShare1.setVisibility(View.INVISIBLE);
            btnView.setVisibility(View.INVISIBLE);

            progressBar.setMax(100);
            alertDialog = dialog.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "quiz/report.pdf");
                    Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file);
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(uri, "application/pdf");
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    try {
                        startActivity(Intent.createChooser(target, "Open File"));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        // Instruct the user to install a PDF reader here, or something
                    }
                    alertDialog.dismiss();
                }
            });
            btnShare1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "quiz/report.pdf");
                    // Uri uri = Uri.fromFile(file);
                    Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file);
                    String sharetext = "I have finished Level No : " + StaticUtils.RequestlevelNo + " with " + lastLevelScore + " Score in " + getString(R.string.app_name);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_TEXT, "" + sharetext + "  https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    getActivity().startActivity(Intent.createChooser(share, "Share"));
                    alertDialog.dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            if (getActivity() != null)
                new GeneratePdf().execute();
        }
    }

    /*
     * Generate pdf for current level question
     */
    public void GeneratePdf(String dest) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /*
             * Creating Document
             */
            final Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("WRTEAM");
            document.addCreator("WRTEAM");

            /*
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 26.0f;
            final float mValueFontSize = 24.0f;

            /*
             * How to USE FONT....
             */
            final BaseFont urName = BaseFont.createFont("assets/fonts/centarell.ttf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            final LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("Online Quiz", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);


            //here use loop for add questionList in page
            for (int i = 0; i < FragmentPlay.reviews.size(); i++) {
                final Review review = FragmentPlay.reviews.get(i);
                Font mOrderAcNameFont1 = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
                Chunk mOrderAcNameChunk1 = new Chunk(("" + (i + 1) + ". " + Html.fromHtml(review.getQuestion())), mOrderAcNameFont1);
                Paragraph mOrderAcNameParagraph1 = new Paragraph(mOrderAcNameChunk1);
                document.add(mOrderAcNameParagraph1);
                try {
                    if (!review.getImgUrl().isEmpty() || review.getImgUrl() != null) {
                        String imageUrl = review.getImgUrl();

                        Image image2 = Image.getInstance(new URL(imageUrl));
                        image2.scaleAbsolute(200f, 200f);
                        document.add(image2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                QuestionOption(document, urName, mValueFontSize, review, lineSeparator);
            }

            document.close();


            //FileUtils.openFile(getActivity(), new File(dest));

        } catch (IOException | DocumentException ie) {
            Log.e("createPdf: Error", "" + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(getActivity(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }



    public void QuestionOption(Document document, BaseFont urName, float mValueFontSize, Review review, LineSeparator lineSeparator) {
        try {
            Font mOrderDateValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk = new Chunk("(a) " + Html.fromHtml(review.getOptionList().get(0)), mOrderDateValueFont);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            Font mOrderDateValueFont1 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk1 = new Chunk("(b) " + Html.fromHtml(review.getOptionList().get(1)), mOrderDateValueFont1);
            Paragraph mOrderDateValueParagraph1 = new Paragraph(mOrderDateValueChunk1);
            document.add(mOrderDateValueParagraph1);

            Font mOrderDateValueFont2 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk2 = new Chunk("(c) " + Html.fromHtml(review.getOptionList().get(2)), mOrderDateValueFont2);
            Paragraph mOrderDateValueParagraph2 = new Paragraph(mOrderDateValueChunk2);
            document.add(mOrderDateValueParagraph2);

            Font mOrderDateValueFont3 = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDateValueChunk3 = new Chunk("(d) " + Html.fromHtml(review.getOptionList().get(3)), mOrderDateValueFont3);
            Paragraph mOrderDateValueParagraph3 = new Paragraph(mOrderDateValueChunk3);
            document.add(mOrderDateValueParagraph3);

            Font mOrderDateValueFont4 = new Font(urName, mValueFontSize, Font.NORMAL, new BaseColor(139, 0, 0));
            Chunk mOrderDateValueChunk4 = new Chunk("True Answer  : " + Html.fromHtml(review.getRightAns()), mOrderDateValueFont4);
            mOrderDateValueChunk4.setUnderline(0.1f, -2f);
            Paragraph mOrderDateValueParagraph4 = new Paragraph(mOrderDateValueChunk4);
            document.add(mOrderDateValueParagraph4);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));
        } catch (DocumentException ie) {
            Log.e("createPdf: Error", "" + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(getActivity(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GeneratePdf extends AsyncTask<String, Void, String> {

        @Override
        public void onPreExecute() {
            progressBar.setIndeterminate(true);
        }

        @Override
        protected String doInBackground(String... params) {

            GeneratePdf(getAppPath(Objects.requireNonNull(getActivity())) + "report.pdf");
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            btnShare1.setVisibility(View.VISIBLE);
            btnView.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(false);
            progressBar.setProgress(100);

        }


        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    /**
     * Get Path of App which contains Files
     *
     * @return path of root dir
     */
    public static String getAppPath(Context context) {

        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator);
        // Uri uri= FileProvider.getUriForFile(context, context.getPackageName()+".provider",dir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.loadAd(getActivity());
    }
}