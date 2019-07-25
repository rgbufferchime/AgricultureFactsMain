package com.bufferchime.agriculturefact.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.bufferchime.agriculturefact.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.fragment.FragmentPlay;
import com.bufferchime.agriculturefact.helper.AppController;
import com.bufferchime.agriculturefact.helper.Utils;
import com.bufferchime.agriculturefact.model.Review;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bufferchime.agriculturefact.activity.MainActivity.bookmarkDBHelper;
import static com.bufferchime.agriculturefact.activity.MainActivity.context;

public class ReviewActivity extends AppCompatActivity {

    public TextView txtQuestion, txtQuestion1, btnOpt1, btnOpt2, btnOpt3, btnOpt4, tvLevel, tvQuestionNo;
    public ImageView prev, next, back, setting, bookmark, report;
    NetworkImageView imgQuestion;
    public LinearLayout layout_A, layout_B, layout_C, layout_D;
    private int questionIndex = 0;

    private int NO_OF_QUESTION;
    public Button btnSolution;
    public TextView tvSolution, tvTitle;
    public CardView cardView;
    public ArrayList<Review> reviews = new ArrayList<>();
    AlertDialog alertDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressBar imgProgress;
    RelativeLayout fabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        btnOpt1 = (TextView) findViewById(R.id.btnOpt1);
        btnOpt2 = (TextView) findViewById(R.id.btnOpt2);
        btnOpt3 = (TextView) findViewById(R.id.btnOpt3);
        btnOpt4 = (TextView) findViewById(R.id.btnOpt4);
        txtQuestion = (TextView) findViewById(R.id.question);
        txtQuestion1 = (TextView) findViewById(R.id.question1);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvSolution = (TextView) findViewById(R.id.tvSolution);
        tvQuestionNo = (TextView) findViewById(R.id.questionNo);

        imgQuestion = (NetworkImageView) findViewById(R.id.imgQuestion);
        imgProgress = (ProgressBar) findViewById(R.id.imgProgress);

        fabLayout = (RelativeLayout) findViewById(R.id.fabLayout);
        layout_A = (LinearLayout) findViewById(R.id.a_layout);
        layout_B = (LinearLayout) findViewById(R.id.b_layout);
        layout_C = (LinearLayout) findViewById(R.id.c_layout);
        layout_D = (LinearLayout) findViewById(R.id.d_layout);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        report = (ImageView) findViewById(R.id.report);
        bookmark = (ImageView) findViewById(R.id.bookmark);
        btnSolution = (Button) findViewById(R.id.btnSolution);

        cardView = (CardView) findViewById(R.id.cardView1);


        bookmark.setVisibility(View.VISIBLE);
        report.setVisibility(View.VISIBLE);
        tvLevel.setText(getString(R.string.review_answer));
        reviews = FragmentPlay.reviews;
        Utils.displayInterstitial();
        ReviewQuestion();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionIndex > 0) {
                    questionIndex--;
                    ReviewQuestion();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionIndex < (reviews.size() - 1)) {
                    questionIndex++;
                    ReviewQuestion();
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportDialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnClick(view, ReviewActivity.this);
                Intent intent = new Intent(ReviewActivity.this, BookmarkList.class);
                startActivity(intent);
            }
        });
    }

    public void ReportDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ReviewActivity.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.report_dialog, null);
        dialog.setView(dialogView);
        TextView tvReport = (TextView) dialogView.findViewById(R.id.tvReport);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        final EditText edtReport = (EditText) dialogView.findViewById(R.id.edtReport);
        final TextInputLayout txtInputReport = (TextInputLayout) dialogView.findViewById(R.id.txtInputReport);
        TextView tvQuestion = (TextView) dialogView.findViewById(R.id.tvQuestion);
        tvQuestion.setText("Que : " + Html.fromHtml(reviews.get(questionIndex).getQuestion()));
        alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(true);
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtReport.getText().toString().isEmpty()) {
                    ReportQuestion(edtReport.getText().toString());
                    txtInputReport.setError(null);
                } else {
                    txtInputReport.setError("Please fill all the data and submit!");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void ReportQuestion(final String message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.QUIZ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean error = obj.getBoolean("error");
                            String message = obj.getString("message");
                            if (!error) {
                                alertDialog.dismiss();
                                Toast.makeText(ReviewActivity.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ReviewActivity.this, message, Toast.LENGTH_LONG).show();
                                System.out.println(" empty msg " + message);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);
                params.put(Constant.reportQuestion, "1");
                params.put(Constant.questionId, "" + reviews.get(questionIndex).getQueId());
                params.put(Constant.messageReport, message);
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void ReviewQuestion() {
        if (questionIndex < reviews.size()) {
            txtQuestion.setText(Html.fromHtml(reviews.get(questionIndex).getQuestion()));
            txtQuestion1.setText(Html.fromHtml(reviews.get(questionIndex).getQuestion()));
            final ArrayList<String> options = new ArrayList<String>();
            options.addAll(reviews.get(questionIndex).getOptionList());
            //  Collections.shuffle(options);
            btnOpt1.setText(Html.fromHtml(options.get(0).trim()));
            btnOpt2.setText(Html.fromHtml(options.get(1).trim()));
            btnOpt3.setText(Html.fromHtml(options.get(2).trim()));
            btnOpt4.setText(Html.fromHtml(options.get(3).trim()));

            tvQuestionNo.setText(" " + (questionIndex + 1) + "/" + reviews.size());

            if (reviews.get(questionIndex).getExtraNote().isEmpty()) {
                btnSolution.setVisibility(View.GONE);
            } else {
                btnSolution.setVisibility(View.VISIBLE);
            }
            bookmark.setTag("unmark");
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Review review = reviews.get(questionIndex);
                    if (bookmark.getTag().equals("unmark")) {
                        String solution = reviews.get(questionIndex).getExtraNote();
                        MainActivity.bookmarkDBHelper.insertIntoDB(review.getQueId(),
                                review.getQuestion(),
                                review.getRightAns(),
                                solution,
                                review.getImgUrl(),
                                options.get(0).trim(),
                                options.get(1).trim(),
                                options.get(2).trim(),
                                options.get(3).trim());
                        bookmark.setImageResource(R.drawable.ic_mark);
                        bookmark.setTag("mark");
                    } else {
                        MainActivity.bookmarkDBHelper.delete_id(reviews.get(questionIndex).getQueId());
                        bookmark.setImageResource(R.drawable.ic_unmark);
                        bookmark.setTag("unmark");
                    }

                }
            });

            int isfav = bookmarkDBHelper.getBookmarks(reviews.get(questionIndex).getQueId());

            if (isfav == reviews.get(questionIndex).getQueId()) {
                bookmark.setImageResource(R.drawable.ic_mark);
                bookmark.setTag("mark");
            } else {
                bookmark.setImageResource(R.drawable.ic_unmark);
                bookmark.setTag("unmark");
            }
            tvSolution.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.INVISIBLE);
            btnSolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String solution = reviews.get(questionIndex).getExtraNote();
                    cardView.setVisibility(View.VISIBLE);
                    tvSolution.setVisibility(View.VISIBLE);
                    tvSolution.setText(solution);

                }
            });


            if (!reviews.get(questionIndex).getImgUrl().isEmpty()) {
                txtQuestion1.setVisibility(View.GONE);
                txtQuestion.setVisibility(View.VISIBLE);
                imgQuestion.setImageUrl(reviews.get(questionIndex).getImgUrl(), imageLoader);
                imgQuestion.setVisibility(View.VISIBLE);
                imgProgress.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(reviews.get(questionIndex).getImgUrl())
                        .into(imgQuestion, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //do something when picture is loaded successfully
                                imgProgress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                //do something when there is picture loading error
                                imgProgress.setVisibility(View.GONE);
                            }
                        });
            } else {
                imgQuestion.setVisibility(View.GONE);
                txtQuestion1.setVisibility(View.VISIBLE);
                txtQuestion.setVisibility(View.GONE);
            }


            if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_A.setBackgroundResource(R.drawable.right_gradient);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_B.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {

                layout_C.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                layout_D.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_D.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_D.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                }

            } else if (btnOpt4.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getRightAns().trim())) {
                layout_D.setBackgroundResource(R.drawable.right_gradient);
                layout_A.setBackgroundResource(R.drawable.answer_bg);
                layout_C.setBackgroundResource(R.drawable.answer_bg);
                layout_B.setBackgroundResource(R.drawable.answer_bg);
                if (btnOpt1.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_A.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt2.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_B.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_C.setBackgroundResource(R.drawable.answer_bg);
                } else if (btnOpt3.getText().toString().trim().equalsIgnoreCase(reviews.get(questionIndex).getWrongAns())) {
                    layout_C.setBackgroundResource(R.drawable.wrong_gradient);
                    layout_A.setBackgroundResource(R.drawable.answer_bg);
                    layout_B.setBackgroundResource(R.drawable.answer_bg);

                }
            }
        }

    }
}
