
package com.bufferchime.agriculturefact.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bufferchime.agriculturefact.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.activity.MainActivity;
import com.bufferchime.agriculturefact.activity.SettingActivity;
import com.bufferchime.agriculturefact.helper.AppController;
import com.bufferchime.agriculturefact.helper.SettingsPreferences;
import com.bufferchime.agriculturefact.helper.StaticUtils;
import com.bufferchime.agriculturefact.helper.Utils;
import com.bufferchime.agriculturefact.model.Level;
import com.bufferchime.agriculturefact.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentLock extends Fragment {
    LevelListAdapter adapter;

    public static String fromQue;
    private static int levelNo = 1;
    public static Context mContext;
    List<Level> levelList;
    RecyclerView recyclerView;
    ImageView back, setting;
    RecyclerView.LayoutManager layoutManager;
    public TextView emptyMsg, tvLevel;
    public ProgressBar progressBar;
    public static ArrayList<Question> questionList;
    public CoordinatorLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock, container, false);
        mContext = getActivity().getBaseContext();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        back = (ImageView) view.findViewById(R.id.back);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyMsg = (TextView) view.findViewById(R.id.empty_msg);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layout = (CoordinatorLayout) view.findViewById(R.id.layout);
        levelNo = MainActivity.dbHelper.GetLevelById(Constant.CATE_ID, Constant.SUB_CAT_ID);
        Bundle bundle = getArguments();
        assert bundle != null;
        tvLevel.setText(getString(R.string.select_level));
        fromQue = bundle.getString("fromQue");
        getActivity().setTitle(getString(R.string.select_level));
        levelList = new ArrayList<>();

        for (int i = 0; i < Constant.TotalLevel; i++) {
            Level level = new Level();
            level.setLevelNo(levelNo);
            level.setLevel("" + (i + 1));
            level.setQuestion("que : " + Constant.MAX_QUESTION_PER_LEVEL);
            levelList.add(level);
        }
        System.out.println("level  " + levelList.size());
        if (levelList.size() == 0) {
            emptyMsg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            getData();
        }


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


        return view;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(getActivity())) {
            getQuestionsFromJson();
        } else {
            setSnackBar();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setSnackBar() {
        Snackbar snackbar = Snackbar
                .make(layout, getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData();
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /*
     * Get Question From Json
     * here we get all question by category or subcategory
     */
    public void getQuestionsFromJson() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.QUIZ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean(Constant.ERROR);

                            if (!error) {
                                JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                                questionList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Question question = new Question();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    question.setId(Integer.parseInt(object.getString(Constant.ID)));
                                    question.setQuestion(object.getString(Constant.QUESTION));
                                    question.setImage(object.getString(Constant.IMAGE));
                                    question.addOption(object.getString(Constant.OPTION_A));
                                    question.addOption(object.getString(Constant.OPTION_B));
                                    question.addOption(object.getString(Constant.OPTION_C));
                                    question.addOption(object.getString(Constant.OPTION_D));
                                    String rightAns = object.getString("answer");
                                    question.setAnsOption(rightAns);
                                    if (rightAns.equalsIgnoreCase("A")) {
                                        question.setTrueAns(object.getString(Constant.OPTION_A));
                                    } else if (rightAns.equalsIgnoreCase("B")) {
                                        question.setTrueAns(object.getString(Constant.OPTION_B));
                                    } else if (rightAns.equalsIgnoreCase("C")) {
                                        question.setTrueAns(object.getString(Constant.OPTION_C));
                                    } else {
                                        question.setTrueAns(object.getString(Constant.OPTION_D));
                                    }
                                    question.setLevel(object.getString(Constant.LEVEL));
                                    question.setNote(object.getString(Constant.NOTE));

                                    if (question.getOptions().size() == 4) {
                                        questionList.add(question);
                                        adapter = new LevelListAdapter(getActivity(), levelList);
                                        recyclerView.setAdapter(adapter);
                                    }


                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.accessKey, Constant.accessKeyValue);

                if (fromQue.equals("cate")) {
                    params.put(Constant.getQuestionByCategory, "1");
                    params.put(Constant.category, "" + Constant.CATE_ID);
                } else if (fromQue.equals("subCate")) {
                    params.put(Constant.getQuestion, "1");
                    params.put(Constant.subCategoryId, Constant.SelectedSubCategoryID);
                }
                System.out.print("----que params-----   " + params.toString());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private ArrayList<Question> filter(ArrayList<Question> models, String query) {
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

    public class LevelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public Activity activity;
        private List<Level> levelList;

        public LevelListAdapter(Activity activity, List<Level> levelList) {
            this.levelList = levelList;
            this.activity = activity;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_item, parent, false);
            return new LevelViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            LevelViewHolder viewHolder = (LevelViewHolder) holder;

            Level level = levelList.get(position);
            if (level.getLevelNo() >= position + 1) {
                viewHolder.lock.setImageResource(R.drawable.unlock);
            } else {
                viewHolder.lock.setImageResource(R.drawable.lock);
            }
            viewHolder.levelNo.setText("Level : " + level.getLevel());
            viewHolder.tvQuestion.setText(level.getQuestion());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SettingsPreferences.getSoundEnableDisable(activity)) {
                        StaticUtils.backSoundonclick(activity);
                    }
                    if (SettingsPreferences.getVibration(activity)) {
                        StaticUtils.vibrate(activity, StaticUtils.VIBRATION_DURATION);
                    }
                    StaticUtils.RequestlevelNo = position + 1;
                    //filter question by level
                    ArrayList<Question> question = filter(questionList, "" + StaticUtils.RequestlevelNo);


                    if (levelNo >= position + 1) {
                        if (question.size() >= Constant.MAX_QUESTION_PER_LEVEL) {
                            FragmentPlay fragmentPlay = new FragmentPlay();
                            FragmentTransaction ft = ((MainActivity) activity).getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, fragmentPlay, "fragment");
                            ft.addToBackStack("tag");
                            ft.commit();
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_enough_question), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Level is Locked", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return levelList.size();
        }

        public class LevelViewHolder extends RecyclerView.ViewHolder {

            ImageView lock;
            CardView cardView;
            TextView levelNo, tvQuestion;

            public LevelViewHolder(View itemView) {
                super(itemView);
                lock = (ImageView) itemView.findViewById(R.id.lock);
                levelNo = (TextView) itemView.findViewById(R.id.level_no);
                tvQuestion = (TextView) itemView.findViewById(R.id.question_no);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
            }
        }
    }

}

