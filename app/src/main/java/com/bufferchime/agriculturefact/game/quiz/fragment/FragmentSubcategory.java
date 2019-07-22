package com.bufferchime.agriculturefact.game.quiz.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bufferchime.agriculturefact.game.quiz.Constant;
import com.bufferchime.agriculturefact.R;
import com.bufferchime.agriculturefact.game.quiz.activity.MainActivity;
import com.bufferchime.agriculturefact.game.quiz.activity.SettingActivity;
import com.bufferchime.agriculturefact.game.quiz.helper.AppController;
import com.bufferchime.agriculturefact.game.quiz.helper.CircleImageView;
import com.bufferchime.agriculturefact.game.quiz.helper.SettingsPreferences;
import com.bufferchime.agriculturefact.game.quiz.helper.StaticUtils;
import com.bufferchime.agriculturefact.game.quiz.helper.Utils;
import com.bufferchime.agriculturefact.game.quiz.model.SubCategory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bufferchime.agriculturefact.game.quiz.fragment.FragmentPlay.loadRewardedVideoAd;

public class FragmentSubcategory extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    ProgressBar progressBar;
    ArrayList<SubCategory> subCateList;
    AdView mAdView;
    TextView txtBlankList, tvTitle;
    CoordinatorLayout layout;
    ImageView back, setting;
    public String cateId, maxLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);


        mAdView = (AdView) view.findViewById(R.id.banner_AdView);
        mAdView.loadAd(new AdRequest.Builder().build());
        txtBlankList = (TextView) view.findViewById(R.id.txtblanklist);
        txtBlankList.setText(getString(R.string.no_category));
        recyclerView = (RecyclerView) view.findViewById(R.id.category_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        layout = (CoordinatorLayout) view.findViewById(R.id.layout);
        back = (ImageView) view.findViewById(R.id.back);
        setting = (ImageView) view.findViewById(R.id.setting);
        tvTitle = (TextView) view.findViewById(R.id.tvLevel);
        tvTitle.setText(getString(R.string.select_subcategory));
        Bundle bundle = getArguments();
        assert bundle != null;

        cateId = bundle.getString("cateId");
        maxLevel = bundle.getString("maxLevel");
        getData();
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

        return view;
    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(getActivity())) {


            getSubCategoryFromJson();


        } else {
            setSnackBar(layout);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void getSubCategoryFromJson() {
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
                                subCateList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SubCategory subCate = new SubCategory();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    subCate.setId(object.getString(Constant.ID));
                                    subCate.setCategoryId(object.getString(Constant.MAIN_CATE_ID));
                                    subCate.setName(object.getString(Constant.SUB_CATE_NAME));
                                    subCate.setImage(object.getString(Constant.IMAGE));
                                    subCate.setStatus(object.getString(Constant.STATUS));
                                    subCate.setMaxLevel(object.getString(Constant.MAX_LEVEL));
                                    subCateList.add(subCate);


                                }
                                if (subCateList.size() == 0) {
                                    txtBlankList.setVisibility(View.VISIBLE);
                                    txtBlankList.setText(getString(R.string.no_sub_category));
                                }
                                SubCategoryAdapter adapter = new SubCategoryAdapter(getActivity(), subCateList);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                //if there was no subcategory available then, we get direct question from category
                                progressBar.setVisibility(View.GONE);
                                txtBlankList.setVisibility(View.VISIBLE);
                                txtBlankList.setText(getString(R.string.no_sub_category));
                                loadRewardedVideoAd();
                                Constant.SelectedSubCategoryID = cateId;

                                if (maxLevel == null) {
                                    Constant.TotalLevel = 0;
                                } else if (maxLevel.equals("null")) {
                                    Constant.TotalLevel = 0;
                                } else {

                                    Constant.TotalLevel = Integer.parseInt(maxLevel);
                                }
                                FragmentLock fragmentlock = new FragmentLock();
                                FragmentTransaction ft = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_container, fragmentlock, "fragmentlock");
                                Bundle bundle = new Bundle();
                                bundle.putString("fromQue", "cate");
                                fragmentlock.setArguments(bundle);
                                ft.commit();

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
                params.put(Constant.getSubCategory, "1");
                params.put(Constant.categoryId, cateId);
                System.out.println("params  " + params.toString());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void setSnackBar(View coordinatorLayout) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
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

    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemRowHolder> {
        private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        private ArrayList<SubCategory> dataList;
        private Context mContext;


        public SubCategoryAdapter(Context context, ArrayList<SubCategory> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public SubCategoryAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull SubCategoryAdapter.ItemRowHolder holder, final int position) {
            txtBlankList.setVisibility(View.GONE);
            final SubCategory subCate = dataList.get(position);
            holder.text.setText(subCate.getName());
            holder.image.setDefaultImageResId(R.drawable.ic_launcher);
            holder.image.setImageUrl(subCate.getImage(), imageLoader);


            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.SelectedSubCategoryID = subCate.getId();
                    if (subCate.getMaxLevel().isEmpty()) {
                        Constant.TotalLevel = 0;
                    } else {
                        Constant.TotalLevel = Integer.parseInt(subCate.getMaxLevel());
                    }
                    FragmentLock fragmentlock = new FragmentLock();
                    FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
                    ft.hide(getFragmentManager().findFragmentByTag("subcategoryfragment"));
                    ft.add(R.id.fragment_container, fragmentlock, "fragmentlock");
                    ft.addToBackStack(null);
                    Bundle bundle = new Bundle();
                    //here we pass fromQue parameter to next fragment
                    // so that we can decide from which api we have to get question
                    bundle.putString("fromQue", "subCate");
                    fragmentlock.setArguments(bundle);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();

                }
            });
        }

        @Override
        public int getItemCount() {
            return (null != dataList ? dataList.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public CircleImageView image;
            public TextView text;
            RelativeLayout relativeLayout;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image = (CircleImageView) itemView.findViewById(R.id.imgcategory);
                text = (TextView) itemView.findViewById(R.id.item_title);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.cat_layout);
            }
        }
    }
}