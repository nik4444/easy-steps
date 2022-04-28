package com.easysteps.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.easysteps.R;
import com.easysteps.helper.CustomGauge;
import com.easysteps.helper.PrefKey;
import com.easysteps.helper.Utils;
import com.easysteps.model.AddToAcceptRewardRes;
import com.easysteps.model.BtnAnimDetail;
import com.easysteps.model.ResetSteps;
import com.easysteps.model.addsteps.AddStepsRes;
import com.easysteps.model.getMydailysteps.GetMyDailyStepsRes;
import com.easysteps.model.getMydailysteps.RewardedDatum;
import com.easysteps.retrofit.AsyncHttpRequest;
import com.easysteps.retrofit.AsyncResponseHandler;
import com.easysteps.retrofit.RequestParamsUtils;
import com.easysteps.retrofit.URLs;
import com.easysteps.service.Utils.SharedPreferencesUtils;
import com.easysteps.service.Utils.UIHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;

public class HomeFragment extends Fragment implements View.OnClickListener, OnSuccessListener<DataSet> {

    public static int stepCoins;
    public static int TodaystepCoins;
    public static String PREFS_DATE = "todayDate";
    public double ConditionStartSteps;
    public double ConditionEndSteps;
    LinearLayout ll_daily_reward;
    LinearLayout ll_invite_friends;
    LinearLayout ll_bonus1;
    LinearLayout ll_bonus2;
    LinearLayout ll_bonus3;
    LinearLayout ll_bonus4;
    LinearLayout ll_bonus5;
    ImageView img_daily_reward;
    ImageView img_invite_friends;
    ImageView img_bonus1;
    ImageView img_bonus2;
    ImageView img_bonus3;
    ImageView img_bonus4;
    ImageView img_bonus5;
    TextView txt_1000;
    TextView txt_3000;
    TextView txt_9000;
    TextView txt_14000;
    TextView txt_20000;
    TextView txt_daily_reward;
    TextView txt_invite_fiends;
    TextView txt_bonus1;
    TextView txt_bonus2;
    TextView txt_bonus3;
    TextView txt_bonus4;
    TextView txt_bonus5;
    TextView txt_daily_reward_coins;
    TextView txt_invite_fiends_coins;
    TextView txt_bonus1_coins;
    TextView txt_bonus2_coins;
    TextView txt_bonus3_coins;
    TextView txt_bonus4_coins;
    TextView txt_bonus5_coins;
    TextView txt_coins;
    int earned_coins;
    int UserCoins = 0;
    int RewardedID = 0;
    int is_performed = 0;
    int RewardedCoins = 0;
    boolean account_first_time = false;
    List<RewardedDatum> listRewardData;
    float targetGoal;
    int added_or_not;
    long lastSteps;
    int cntCurr = 0;
    int cntLast = 0;
    int cnt = 0;
    private long steps;
    private Animation slideUp;
    private String currentDate;
    private int currentSteps = 0;
    private int DailySteps = 0;
    private int animationSteps = 0;
    private SharedPreferencesUtils f1327sp;
    private CustomGauge gauge;
    private boolean isBind = false;
    private TextView textViewDistance;
    private TextView textViewStepsCount;
    private Animation zoom_in;
    private ObjectAnimator anim_daily_reward;
    private ObjectAnimator anim_invite_friends;
    private ObjectAnimator anim_bonus_1;
    private ObjectAnimator anim_bonus_2;
    private ObjectAnimator anim_bonus_3;
    private ObjectAnimator anim_bonus_5;
    private ObjectAnimator anim_bonus_4;
    private String todayDate;
    private String getDate;
    private TextView text_total_coins;
    private LinearLayout currentClickedLayout;
    private ImageView currentClickedImgView;
    private TextView currentClickedTextView;
    private int clickedBtnPos;
    private int btnPos;
    private ArrayList<BtnAnimDetail> objectAnimatorList = new ArrayList<>();
    private Animation pulse;
    private TextView text_current;
    private TextView text_coins;
    private NestedScrollView nested_scroll;
    private FitnessOptions fitnessOptions;
    private GoogleSignInAccount account;
    private int GOOGLE_FIT_PERMISSION_REQUEST_CODE = 1;
    private int myLiveSteps;
    private LinearLayout ll_google_sign_in;
    private long steps_today;
    private long newSteps;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static String createSpaceString(String originalString, String stringToBeInserted, int index) {
        String newString = new String();
        for (int i = 0; i < originalString.length(); i++) {
            newString += originalString.charAt(i);
            if (i == index) {
                newString += stringToBeInserted;
            }
        }
        return newString;
    }

    public static boolean setTodayDate(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getTodayDate(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_DATE, Context.MODE_PRIVATE);
        return settings.getString(key, "today");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        this.f1327sp = sharedPreferencesUtils;
        try {
            Integer.parseInt((String) sharedPreferencesUtils.getParam("planWalk_QTY", "20000"));
        } catch (Exception unused) {
            this.f1327sp.setParam("planWalk_QTY", "20000");
        }

        ConditionStartSteps = Prefs.getDouble(PrefKey.condition_start_steps, 0);
        ConditionEndSteps = Prefs.getDouble(PrefKey.condition_end_steps, 1000);

        steps = Prefs.getLong(PrefKey.current_steps, 0);
        account_first_time = Prefs.getBoolean(PrefKey.account_first_time, false);
        pulse = AnimationUtils.loadAnimation(getActivity(), R.anim.pulse);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf.format(new Date());
        getDate = getTodayDate(getActivity(), PREFS_DATE);
        textViewStepsCount = (TextView) view.findViewById(R.id.textViewStepsCount);
        if (steps <= 20000) {
            textViewStepsCount.setText("" + steps);
        } else {
            textViewStepsCount.setText("20000");
        }
        if (!getDate.equals(todayDate)) {
            Prefs.remove(PrefKey.condition_start_steps);
            Prefs.remove(PrefKey.condition_end_steps);
            Prefs.remove(PrefKey.current_steps);
            Prefs.remove(PrefKey.last_steps);
            ResetDailySteps();
            textViewStepsCount.setText("0");
            setTodayDate(getActivity(), PREFS_DATE, todayDate);
        }
        initData();
        init(view);
        setupSharedUtils();
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GetMyDailySteps();
    }

    private void init(View view) {
        this.nested_scroll = (NestedScrollView) view.findViewById(R.id.nested_scroll);
        this.gauge = (CustomGauge) view.findViewById(R.id.gauge);
        this.textViewDistance = (TextView) view.findViewById(R.id.textViewDistance);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void checkAnimation() {
        if (steps >= 1000) {
            if (listRewardData != null) {
                if (listRewardData.get(2).getIsPerformed() == 0) {
                    try {
                        PopUp_Animation(ll_bonus1, 2);
                        ll_bonus1.setBackground(getActivity().getDrawable(R.drawable.app_corner3));
                        img_bonus1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txt_1000.setTextColor(getActivity().getColor(R.color.white));
                            txt_bonus1.setTextColor(getActivity().getColor(R.color.text_steps));
                            txt_bonus1_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "checkAnimation: " + e.getMessage());
                    }

                }
            }
        }
        if (steps >= 3000) {
            if (listRewardData != null) {
                if (listRewardData.get(3).getIsPerformed() == 0) {
                    try {
                        PopUp_Animation(ll_bonus2, 3);
                        ll_bonus2.setBackground(getActivity().getDrawable(R.drawable.app_corner4));
                        img_bonus2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txt_3000.setTextColor(getActivity().getColor(R.color.white));
                            txt_bonus2.setTextColor(getActivity().getColor(R.color.text_steps));
                            txt_bonus2_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "checkAnimation: " + e.getMessage());
                    }
                }
            }
        }
        if (steps >= 9000) {
            if (listRewardData != null) {
                if (listRewardData.get(4).getIsPerformed() == 0) {
                    try {
                        PopUp_Animation(ll_bonus3, 4);
                        ll_bonus3.setBackground(getActivity().getDrawable(R.drawable.app_corner5));
                        img_bonus3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txt_9000.setTextColor(getActivity().getColor(R.color.white));
                            txt_bonus3.setTextColor(getActivity().getColor(R.color.text_steps));
                            txt_bonus3_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "checkAnimation: " + e.getMessage());
                    }
                }
            }
        }
        if (steps >= 14000) {
            if (listRewardData != null) {
                if (listRewardData.get(5).getIsPerformed() == 0) {
                    try {
                        PopUp_Animation(ll_bonus4, 5);
                        ll_bonus4.setBackground(getActivity().getDrawable(R.drawable.app_corner6));
                        img_bonus4.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txt_14000.setTextColor(getActivity().getColor(R.color.white));
                            txt_bonus4.setTextColor(getActivity().getColor(R.color.text_steps));
                            txt_bonus4_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "checkAnimation: " + e.getMessage());
                    }
                }
            }
        }
        if (steps >= 20000) {
            if (listRewardData != null) {
                if (listRewardData.get(6).getIsPerformed() == 0) {
                    try {
                        PopUp_Animation(ll_bonus5, 6);
                        ll_bonus5.setBackground(getActivity().getDrawable(R.drawable.app_corner7));
                        img_bonus5.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            txt_20000.setTextColor(getActivity().getColor(R.color.white));
                            txt_bonus5.setTextColor(getActivity().getColor(R.color.text_steps));
                            txt_bonus5_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "checkAnimation: " + e.getMessage());
                    }
                }
            }

        }
    }

    private void setupSharedUtils() {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        this.f1327sp = sharedPreferencesUtils;
        if (sharedPreferencesUtils.getParam(SharedPreferencesUtils.DISTANCE_MEASUERE_IS_SET, "").equals("")) {
            String country = Locale.getDefault().getCountry();
            if (country.equals("US") || country.equals("UK")) {
                this.f1327sp.setParam(SharedPreferencesUtils.DISTANCE_MEASURE_IM_KM_KEY, false);
            } else {
                this.f1327sp.setParam(SharedPreferencesUtils.DISTANCE_MEASURE_IM_KM_KEY, true);
            }
        }
    }

    private void initData() {
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ).build();
        GoogleSignInAccount account = getGoogleAccount();

        Log.e("TAG", "initDataaccountaccount: " + account);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_google_fit);

            ll_google_sign_in = (LinearLayout) dialog.findViewById(R.id.ll_sign_in);

            ll_google_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleSignIn.requestPermissions(getActivity(),
                            GOOGLE_FIT_PERMISSION_REQUEST_CODE, account, fitnessOptions);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            startDataReading();
        }
    }

    private void startDataReading() {
        getTodayData();
        subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA);
    }

    public void onResume() {
        float f;
        super.onResume();
        String str = (String) this.f1327sp.getParam("planWalk_QTY", "20000");
        try {
            f = Float.parseFloat(str);
        } catch (Exception unused) {
            f = 10000.0f;
        }
        this.gauge.setEndValue(100);
        steps = Prefs.getLong(PrefKey.current_steps, 0);
        this.gauge.setValue(Math.round((((float) steps) / f) * 100.0f));
        this.gauge.invalidate();
    }

    private void subscribeAndGetRealTimeData(DataType dataType) {
        Fitness.getRecordingClient(getActivity(), getGoogleAccount())
                .subscribe(dataType)
                .addOnSuccessListener(aVoid -> {
                    Log.e("TAG", "Successfully subscribed!");
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Failure: " + e.getLocalizedMessage());
                });

//        getDataUSingSensor(dataType);
    }

    private void getTodayData() {
        Fitness.getHistoryClient(getActivity(), getGoogleAccount())
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA).
                addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        steps =
                                dataSet.isEmpty()
                                        ? 0
                                        : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                        Prefs.putLong(PrefKey.current_steps, steps);
                        checkAnimation();
                        float f;
                        String str = (String) f1327sp.getParam("planWalk_QTY", "20000");
                        try {
                            f = Float.parseFloat(str);
                        } catch (Exception unused) {
                            f = 10000.0f;
                        }
                        gauge.setEndValue(100);
                        if (steps <= 20000) {
                            lastSteps = Prefs.getLong(PrefKey.last_steps,0);
                            gauge.setValue(Math.round((((float) steps) / f) * 100.0f));
                            if (steps >= 1000) {
                                Log.e("TAG", "onSuccess: steps"+steps );
                                String string_number = ""+steps;
                                if(string_number.length() == 4){
                                    cntCurr = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                    Log.e("TAGTAG", "onSuccess first: "+cntCurr );
                                }else if(string_number.length() == 5){
                                    cntCurr = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                    Log.e("TAGTAG", "onSuccess cntCurr: "+cntCurr );
                                }
                            }
                            if (lastSteps >= 1000) {
                                String string_number = ""+lastSteps;
                                if(string_number.length() == 4){
                                    cntLast = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                    Log.e("TAG", "onSuccess lastSteps: "+cntLast );
                                }else if(string_number.length() == 5){
                                    cntLast = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                    Log.e("TAGTAG", "onSuccess lastSteps: "+cntLast );
                                }
                            }
                            if (steps > lastSteps) {
                                newSteps = steps - lastSteps;
                                if (newSteps >= 1000) {
                                    String string_number = ""+newSteps;
                                    if(string_number.length() == 4){
                                        cnt = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                        Log.e("TAG", "onSuccess first: "+cnt );
                                    }else if(string_number.length() == 5){
                                        cnt = Integer.parseInt(string_number.substring(0, string_number.length() - 3));
                                        Log.e("TAG", "onSuccess TAGTAG: "+cnt );
                                    }
                                    UpdateMyCoinsAndSetps(steps,cnt);
                                } else if (cntCurr > cntLast) {
                                    UserCoins = cntCurr - cntLast;
                                    UpdateMyCoinsAndSetps(steps,UserCoins);
                                } else {
                                    UpdateMyCoinsAndSetps(steps,0);
                                }
                            }
                            if (String.valueOf(steps).length() == 4) {
                                textViewStepsCount.setText(createSpaceString("" + steps, " ", 0));
                            } else if (String.valueOf(steps).length() == 5) {
                                textViewStepsCount.setText(createSpaceString("" + steps, " ", 1));
                            } else {
                                textViewStepsCount.setText("" + steps);
                            }
                            textViewDistance.setText("" + UIHelper.getDistanceFromSteps(steps, getContext()));
                        } else {
                            gauge.setValue(Math.round((((float) 20000) / f) * 100.0f));
                            textViewStepsCount.setText("20 000");
                            textViewDistance.setText("" + UIHelper.getDistanceFromSteps(20000, getContext()));
                        }
                        gauge.invalidate();
                    }
                });
    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(getActivity(), fitnessOptions);
    }

    private void initView(View view) {
        ll_daily_reward = (LinearLayout) view.findViewById(R.id.ll_daily_reward);
        ll_invite_friends = (LinearLayout) view.findViewById(R.id.ll_invite_friends);
        ll_bonus1 = (LinearLayout) view.findViewById(R.id.ll_bonus1);
        ll_bonus2 = (LinearLayout) view.findViewById(R.id.ll_bonus2);
        ll_bonus3 = (LinearLayout) view.findViewById(R.id.ll_bonus3);
        ll_bonus4 = (LinearLayout) view.findViewById(R.id.ll_bonus4);
        ll_bonus5 = (LinearLayout) view.findViewById(R.id.ll_bonus5);
        img_daily_reward = (ImageView) view.findViewById(R.id.img_daily_reward);
        img_invite_friends = (ImageView) view.findViewById(R.id.img_invite_friends);
        img_bonus1 = (ImageView) view.findViewById(R.id.img_bonus1);
        img_bonus2 = (ImageView) view.findViewById(R.id.img_bonus2);
        img_bonus3 = (ImageView) view.findViewById(R.id.img_bonus3);
        img_bonus4 = (ImageView) view.findViewById(R.id.img_bonus4);
        img_bonus5 = (ImageView) view.findViewById(R.id.img_bonus5);
        txt_1000 = (TextView) view.findViewById(R.id.txt_1000);
        txt_3000 = (TextView) view.findViewById(R.id.txt_3000);
        txt_9000 = (TextView) view.findViewById(R.id.txt_9000);
        txt_14000 = (TextView) view.findViewById(R.id.txt_14000);
        txt_20000 = (TextView) view.findViewById(R.id.txt_20000);
        txt_bonus1 = (TextView) view.findViewById(R.id.txt_bonus1);
        txt_bonus2 = (TextView) view.findViewById(R.id.txt_bonus2);
        txt_bonus3 = (TextView) view.findViewById(R.id.txt_bonus3);
        txt_bonus4 = (TextView) view.findViewById(R.id.txt_bonus4);
        txt_bonus5 = (TextView) view.findViewById(R.id.txt_bonus5);
        txt_daily_reward = (TextView) view.findViewById(R.id.txt_daily_reward);
        txt_invite_fiends = (TextView) view.findViewById(R.id.txt_invite_fiends);
        txt_daily_reward_coins = (TextView) view.findViewById(R.id.txt_daily_reward_coins);
        txt_invite_fiends_coins = (TextView) view.findViewById(R.id.txt_invite_fiends_coins);
        txt_bonus1_coins = (TextView) view.findViewById(R.id.txt_bonus1_coins);
        txt_bonus2_coins = (TextView) view.findViewById(R.id.txt_bonus2_coins);
        txt_bonus3_coins = (TextView) view.findViewById(R.id.txt_bonus3_coins);
        txt_bonus4_coins = (TextView) view.findViewById(R.id.txt_bonus4_coins);
        txt_bonus5_coins = (TextView) view.findViewById(R.id.txt_bonus5_coins);
        txt_coins = (TextView) view.findViewById(R.id.txt_coins);
        text_total_coins = (TextView) view.findViewById(R.id.text_total_coins);

        startCountAnimation();
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        zoom_in = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        steps = Utils.getSteps();

        // Button Click Events
        ll_daily_reward.setOnClickListener(this);
        ll_invite_friends.setOnClickListener(this);
        ll_bonus1.setOnClickListener(this);
        ll_bonus2.setOnClickListener(this);
        ll_bonus3.setOnClickListener(this);
        ll_bonus4.setOnClickListener(this);
        ll_bonus5.setOnClickListener(this);

        // Set Logic when Date Changed and Steps automatically set 0
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = df.format(c);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String date = sharedPref.getString(getString(R.string.priviousdate), "0");
        if (!date.equals(currentDate)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.priviousdate), currentDate);
            editor.apply();
            Prefs.remove(PrefKey.steps);
            steps = 0;
        }

    }

    private void startCountAnimation() {
        if (Utils.getSteps() > 20) {
            ValueAnimator animator = ValueAnimator.ofInt(Utils.getSteps() - 10, Utils.getSteps());
            animator.setDuration(5000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                }
            });
            animator.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void SelectedBackGround(LinearLayout ll_layout, ImageView img_view, TextView txt_view, TextView txt_bonus, TextView txt_view2) {
        ll_layout.setBackground(getActivity().getDrawable(R.drawable.round_corner_selected));
        img_view.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white));
        if (txt_view != null) {
            txt_view.setTextColor(getActivity().getColor(R.color.white));
        }
        txt_bonus.setTextColor(getActivity().getColor(R.color.black));
        txt_view2.setTextColor(getActivity().getColor(R.color.black));
        ll_layout.startAnimation(slideUp);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_daily_reward:
                is_performed = listRewardData.get(0).getIsPerformed();
                if (is_performed == 0) {
                    RewardedID = listRewardData.get(0).getRewardedId();
                    RewardedCoins = listRewardData.get(0).getCoins();
                    AddToAcceptReward(ll_daily_reward, img_daily_reward, txt_daily_reward, txt_daily_reward_coins, null, 0, 0);
                    listRewardData.get(0).setIsPerformed(1);
                } else {
                    Utils.MyShortSnackbar(ll_daily_reward, "This reward has been taken.");
                }
                break;
            case R.id.ll_invite_friends:
                if (added_or_not == 0) {
                    RewardedID = listRewardData.get(1).getRewardedId();
                    RewardedCoins = listRewardData.get(1).getCoins();
                    added_or_not = 1;
                    AddToAcceptReward(ll_invite_friends, img_invite_friends, txt_invite_fiends, txt_invite_fiends_coins, null, 0, 1);
                } else {
                    Utils.MyShortSnackbar(ll_invite_friends, "This reward has been taken.");
                }
                break;
            case R.id.ll_bonus1:
                if (steps >= 1000) {
                    is_performed = listRewardData.get(2).getIsPerformed();
                    if (is_performed == 0) {
                        RewardedID = listRewardData.get(2).getRewardedId();
                        RewardedCoins = listRewardData.get(2).getCoins();
                        AddToAcceptReward(ll_bonus1, img_bonus1, txt_bonus1, txt_bonus1_coins, txt_1000, 0, 2);
                    } else {
                        Utils.MyShortSnackbar(ll_bonus1, "This reward has been taken.");
                    }
                } else {
                    Utils.MyShortSnackbar(ll_bonus1, "You have to complate 1000 steps for this reward.");
                }
                break;
            case R.id.ll_bonus2:
                if (steps >= 3000) {
                    is_performed = listRewardData.get(3).getIsPerformed();
                    if (is_performed == 0) {
                        RewardedID = listRewardData.get(3).getRewardedId();
                        RewardedCoins = listRewardData.get(3).getCoins();
                        AddToAcceptReward(ll_bonus2, img_bonus2, txt_bonus2, txt_bonus2_coins, txt_3000, 0, 3);
                    } else {
                        Utils.MyShortSnackbar(ll_bonus2, "This reward has been taken.");
                    }
                } else {
                    Utils.MyShortSnackbar(ll_bonus2, "You have to complate 3000 steps for this reward.");
                }
                break;
            case R.id.ll_bonus3:
                if (steps >= 9000) {
                    is_performed = listRewardData.get(4).getIsPerformed();
                    if (is_performed == 0) {
                        RewardedID = listRewardData.get(4).getRewardedId();
                        RewardedCoins = listRewardData.get(4).getCoins();
                        AddToAcceptReward(ll_bonus3, img_bonus3, txt_bonus3, txt_bonus3_coins, txt_9000, 0, 4);
                    } else {
                        Utils.MyShortSnackbar(ll_bonus3, "This reward has been taken.");
                    }
                } else {
                    Utils.MyShortSnackbar(ll_bonus3, "You have to complate 9000 steps for this reward.");
                }
                break;
            case R.id.ll_bonus4:
                if (steps >= 14000) {
                    is_performed = listRewardData.get(5).getIsPerformed();
                    if (is_performed == 0) {
                        RewardedID = listRewardData.get(5).getRewardedId();
                        RewardedCoins = listRewardData.get(5).getCoins();
                        AddToAcceptReward(ll_bonus4, img_bonus4, txt_bonus4, txt_bonus4_coins, txt_14000, 0, 5);
                    } else {
                        Utils.MyShortSnackbar(ll_bonus4, "This reward has been taken.");
                    }
                } else {
                    Utils.MyShortSnackbar(ll_bonus1, "You have to complate 14000 steps for this reward.");
                }
                break;
            case R.id.ll_bonus5:
                if (steps >= 20000) {
                    is_performed = listRewardData.get(6).getIsPerformed();
                    if (is_performed == 0) {
                        RewardedID = listRewardData.get(6).getRewardedId();
                        RewardedCoins = listRewardData.get(6).getCoins();
                        AddToAcceptReward(ll_bonus5, img_bonus5, txt_bonus5, txt_bonus5_coins, txt_20000, 0, 6);
                    } else {
                        Utils.MyShortSnackbar(ll_bonus5, "This reward has been taken.");
                    }
                } else {
                    Utils.MyShortSnackbar(ll_bonus5, "You have to complate 20000 steps for this reward.");
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSuccess(DataSet o) {
        if (o instanceof DataSet) {
            DataSet dataset = (DataSet) o;
            if (dataset != null) {
                getDataFromDataSet(dataset);
            }
        }
    }

    private void getDataFromDataSet(DataSet dataset) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<DataPoint> dataPoints = dataset.getDataPoints();
                for (DataPoint dataPoint : dataPoints) {
                    for (Field field : dataPoint.getDataType().getFields()) {
                        float value = Float.parseFloat(dataPoint.getValue(field).toString());
                        if (field.getName().equals(Field.FIELD_STEPS.getName())) {
                            steps_today = Integer.parseInt(new DecimalFormat("#.##").format(value));
                            Prefs.putLong(PrefKey.current_steps, steps_today);
                            steps = steps_today;
                            float f;
                            String str = (String) f1327sp.getParam("planWalk_QTY", "20000");
                            try {
                                f = Float.parseFloat(str);
                            } catch (Exception unused) {
                                f = 10000.0f;
                            }
                            gauge.setEndValue(100);
                            if (steps <= 20000) {
                                gauge.setValue(Math.round((((float) steps) / f) * 100.0f));
                                textViewStepsCount.setText("" + steps_today);
                                textViewDistance.setText("" + UIHelper.getDistanceFromSteps(steps_today, getContext()));
                            } else {
                                gauge.setValue(Math.round((((float) 20000) / f) * 100.0f));
                                textViewStepsCount.setText("20000");
                                textViewDistance.setText("" + UIHelper.getDistanceFromSteps(20000, getContext()));
                            }
                            gauge.invalidate();

                        }
                    }
                }
            }
        });
    }

    private void UpdateMyCoinsAndSetps(long steps_today, int Coins) {
        try {

            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.StepsCount, "" + steps_today);
            body.addEncoded(RequestParamsUtils.StepsKm, UIHelper.getDistanceFromSteps(steps_today, getContext()));
            body.addEncoded(RequestParamsUtils.StepsDate, todayDate);
            body.addEncoded(RequestParamsUtils.UserCoins, "" + Coins);

            TodaystepCoins = TodaystepCoins + Coins;
            txt_coins.setText(Integer.toString(TodaystepCoins));
            stepCoins = stepCoins + Coins;
            text_total_coins.setText(Integer.toString(stepCoins));

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.ADD_MY_DAILY_STEPS());
            call.enqueue(new UpdateMyCoinsHandler(getActivity()));
        } catch (Exception e) {
            Log.e(" Log", "UpdateProfile: " + e.getMessage());
        }

    }

    private void AddToAcceptReward(LinearLayout currentClickedLayout, ImageView currentClickedImgView, TextView text_current, TextView text_coins, TextView currentClickedTextView, int btnPos, int clickedBtnPos) {
        try {
            this.currentClickedLayout = currentClickedLayout;
            this.currentClickedImgView = currentClickedImgView;
            this.currentClickedTextView = currentClickedTextView;
            this.clickedBtnPos = clickedBtnPos;
            this.btnPos = btnPos;
            this.text_current = text_current;
            this.text_coins = text_coins;
            Log.e("TAG", " AddToAcceptReward: "+RewardedCoins );
             FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());
            body.addEncoded(RequestParamsUtils.RewardedId, "" + RewardedID);
            body.addEncoded(RequestParamsUtils.RewardedCoins, "" + RewardedCoins);
            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.ADD_TO_ACCEPT_REWARD());
            call.enqueue(new AddToAcceptRewardHandler(getActivity()));

        } catch (Exception e) {
        }

    }

    private void GetMyDailySteps() {
        try {
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.GET_MY_DAILY_STEPS());
            call.enqueue(new GetMyDailyStepsHandler(getActivity()));
        } catch (Exception e) {
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (steps <= 20000) {
            UpdateMyCoinsAndSetps(steps, 0);
        } else {
            UpdateMyCoinsAndSetps(20000, 0);
        }
    }

    private void ResetDailySteps() {
        try {
            FormBody.Builder body = RequestParamsUtils.newRequestFormBody(getActivity());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), body.build(), URLs.UPDATE_MY_DAILY_STEPS());
            call.enqueue(new ResetDailyStepsHandler(getActivity()));
        } catch (Exception e) {
        }

    }

    public void PopUp_Animation(LinearLayout linearLayout, int i) {
        if (listRewardData != null) {
            is_performed = listRewardData.get(i).getIsPerformed();
            if (is_performed == 0) {
                linearLayout.startAnimation(pulse);
            }
        }

    }

    private class UpdateMyCoinsHandler extends AsyncResponseHandler {
        UpdateMyCoinsHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Utils.HideProgressDialog(getActivity());
                if (responce != null && responce.length() > 0) {
                    AddStepsRes addSteps = new Gson().fromJson(responce, new TypeToken<AddStepsRes>() {
                    }.getType());
                    if (addSteps.getStatus() == 1) {
                        if (steps > lastSteps) {
                            newSteps = steps - lastSteps;
                            Log.e("TAG", "onSuccess newSteps: "+newSteps );
                            if (newSteps >= 1000) {
                                lastSteps = steps;
                                Prefs.putLong(PrefKey.last_steps,lastSteps);
                                GetMyDailySteps();
                            } else if (cntCurr > cntLast) {
                                lastSteps = steps;
                                Prefs.putLong(PrefKey.last_steps,lastSteps);
                                GetMyDailySteps();
                            }
                        }
                    } else {
//                        Utils.MyShortToast(getActivity(), addSteps.getMessage());
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
                Log.e("TAG", "onFinish: ");
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Log.e("TAG", "onFailure: ");
            Utils.HideProgressDialog(getActivity());
        }
    }

    private class GetMyDailyStepsHandler extends AsyncResponseHandler {
        GetMyDailyStepsHandler(Activity activity) {
            super(activity);
            Utils.HideProgressDialog(activity);
        }

        @Override
        public void onStart() {
            Utils.HideProgressDialog(getActivity());
        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Utils.HideProgressDialog(getActivity());
                if (responce != null && responce.length() > 0) {
                    GetMyDailyStepsRes getMyDailyStepsRes = new Gson().fromJson(responce, new TypeToken<GetMyDailyStepsRes>() {
                    }.getType());
                    if (getMyDailyStepsRes.getStatus() == 1) {
                        stepCoins = getMyDailyStepsRes.getData().getUserCoins();
                        TodaystepCoins = getMyDailyStepsRes.getData().getTodayUserCoins();
                        listRewardData = getMyDailyStepsRes.getData().getRewardedData();
                        added_or_not = getMyDailyStepsRes.getAddedornot();
                        checkAnimation();
                        if (listRewardData != null) {
                            try {
                                if (listRewardData.get(0).getIsPerformed() == 0) {
                                    PopUp_Animation(ll_daily_reward, 0);
                                    txt_daily_reward_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                                    txt_daily_reward.setTextColor(getActivity().getColor(R.color.text_steps));
                                } else {
                                    ll_daily_reward.setBackground(getActivity().getDrawable(R.drawable.app_corner_white));
                                    img_daily_reward.setColorFilter(ContextCompat.getColor(getActivity(), R.color.app_corner_1), PorterDuff.Mode.MULTIPLY);
                                    txt_daily_reward_coins.setTextColor(getActivity().getColor(R.color.text_color));
                                    txt_daily_reward.setTextColor(getActivity().getColor(R.color.text_color));
                                }

                                if (getMyDailyStepsRes.getAddedornot() == 0) {
                                    PopUp_Animation(ll_invite_friends, 1);
                                    txt_invite_fiends_coins.setTextColor(getActivity().getColor(R.color.text_steps));
                                    txt_invite_fiends.setTextColor(getActivity().getColor(R.color.text_steps));
                                } else {
                                    ll_invite_friends.setBackground(getActivity().getDrawable(R.drawable.app_corner_white));
                                    img_invite_friends.setColorFilter(ContextCompat.getColor(getActivity(), R.color.app_corner_2), PorterDuff.Mode.MULTIPLY);
                                    txt_invite_fiends_coins.setTextColor(getActivity().getColor(R.color.text_color));
                                    txt_invite_fiends.setTextColor(getActivity().getColor(R.color.text_color));
                                }
                            } catch (Exception e) {
                                Log.e("TAG", "onSuccess: " + e.getMessage());
                            }

                        } else {

                        }


                        txt_coins.setText("" + TodaystepCoins);
                        if (String.valueOf(stepCoins).length() == 4) {
                            text_total_coins.setText(createSpaceString("" + stepCoins, " ", 0));
                        } else if (String.valueOf(stepCoins).length() == 5) {
                            text_total_coins.setText(createSpaceString("" + stepCoins, " ", 1));
                        } else {
                            text_total_coins.setText("" + stepCoins);
                        }
                    } else {

                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Utils.HideProgressDialog(getActivity());
        }
    }

    private class AddToAcceptRewardHandler extends AsyncResponseHandler {
        AddToAcceptRewardHandler(Activity activity) {
            super(activity);
        }

        @Override
        public void onStart() {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Utils.HideProgressDialog(getActivity());
                if (responce != null && responce.length() > 0) {
                    AddToAcceptRewardRes addToAcceptRewardRes = new Gson().fromJson(responce, new TypeToken<AddToAcceptRewardRes>() {
                    }.getType());
                    if (addToAcceptRewardRes.getStatus() == 1) {
                        currentClickedLayout.clearAnimation();
                        currentClickedLayout.setBackground(getActivity().getDrawable(R.drawable.app_corner_white));
                        currentClickedImgView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.text_steps), PorterDuff.Mode.MULTIPLY);

                        if (currentClickedTextView != null) {
                            currentClickedTextView.setTextColor(getActivity().getColor(R.color.text_steps));
                        }

                        TodaystepCoins = TodaystepCoins + RewardedCoins;
                        txt_coins.setText(Integer.toString(TodaystepCoins));
                        stepCoins = stepCoins + RewardedCoins;
                        text_total_coins.setText(Integer.toString(stepCoins));
                        text_current.setTextColor(getActivity().getColor(R.color.text_color));
                        text_coins.setTextColor(getActivity().getColor(R.color.text_color));

                        listRewardData.get(clickedBtnPos).setIsPerformed(1);

                        Toast.makeText(getActivity(), "" + addToAcceptRewardRes.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Utils.HideProgressDialog(getActivity());
        }
    }

    private class ResetDailyStepsHandler extends AsyncResponseHandler {
        ResetDailyStepsHandler(Activity activity) {
            super(activity);
            Utils.HideProgressDialog(activity);
        }

        @Override
        public void onStart() {
            Utils.HideProgressDialog(getActivity());
        }

        @Override
        public void onSuccess(String responce) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Utils.HideProgressDialog(getActivity());
                if (responce != null && responce.length() > 0) {
                    ResetSteps resetSteps = new Gson().fromJson(responce, new TypeToken<ResetSteps>() {
                    }.getType());
                    if (resetSteps.getStatus() == 1) {

                    } else {

                    }
                }
            });
        }

        @Override
        public void onFinish() {
            try {
                Utils.HideProgressDialog(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Utils.HideProgressDialog(getActivity());
        }
    }
}