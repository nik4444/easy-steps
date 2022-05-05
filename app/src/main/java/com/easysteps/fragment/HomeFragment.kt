package com.easysteps.fragment


import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentHomeBinding
import com.easysteps.helper.PrefKey
import com.easysteps.helper.Utils
import com.easysteps.pref.SharedPref
import com.easysteps.pref.SharedPref.condition_end_steps
import com.easysteps.pref.SharedPref.condition_start_steps
import com.easysteps.pref.SharedPref.current_steps
import com.easysteps.pref.SharedPref.distanceMeasure
import com.easysteps.pref.SharedPref.f1327sp
import com.easysteps.pref.SharedPref.last_steps
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.service.Utils.UIHelper
import com.easysteps.viewModel.HomeViewModel
import com.easysteps.viewModel.models.RewardData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.pixplicity.easyprefs.library.Prefs
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by NIKUNJ on 04-05-2022.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), View.OnClickListener {

    val viewModel: HomeViewModel by viewModels()

    var stepCoins = 0
    var todaystepCoins = 0
    var earnedCoins = 0
    var conditionStartSteps = 0.0
    var conditionEndSteps = 0.0
    var UserCoins = 0
    var RewardedID = 0
    var is_performed = 0
    var RewardedCoins = 0
    var account_first_time = false
    var listRewardData: List<RewardData>? = null
    var targetGoal = 0f
    var added_or_not = 0
    var lastSteps: Long = 0
    var cntCurr = 0
    var cntLast = 0
    var cnt = 0

    private var currentClickedLayout: LinearLayout? = null

    private var currentClickedImgView: ImageView? = null
    private var text_coins: TextView? = null
    private var currentClickedTextView: TextView? = null

    private var text_current: TextView? = null
    private val textCoins: TextView? = null

    private var steps: Long = 0
    private val slideUp: Animation? = null
    private val currentDate: String? = null
    private val currentSteps = 0
    private val DailySteps = 0
    private val animationSteps = 0
    private val isBind = false
    private val textViewDistance: TextView? = null
    private val textViewStepsCount: TextView? = null
    private val zoom_in: Animation? = null
    private val anim_daily_reward: ObjectAnimator? = null
    private val anim_invite_friends: ObjectAnimator? = null
    private val anim_bonus_1: ObjectAnimator? = null
    private val anim_bonus_2: ObjectAnimator? = null
    private var ll_google_sign_in: LinearLayout? = null
    private val anim_bonus_3: ObjectAnimator? = null
    private val anim_bonus_5: ObjectAnimator? = null
    private val anim_bonus_4: ObjectAnimator? = null
    private var todayDate: String? = null
    private var getDate: String? = null
    private var clickedBtnPos = 0
    private var btnPos = 0
    private var pulse: Animation? = null

    private var fitnessOptions: FitnessOptions? = null
    private val GOOGLE_FIT_PERMISSION_REQUEST_CODE = 1
    private val myLiveSteps = 0
    private val steps_today: Long = 0
    private var newSteps: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        getMyDailyStep()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupSharedUtils()
        setUpObserver()
        setOnClickListener()
    }

    private fun setupSharedUtils() {
        val country = Locale.getDefault().country
        distanceMeasure = !(country == "US" || country == "UK")
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.updateStepError.observe(requireActivity()) { it.printStackTrace() }
        viewModel.updateStepData.observe(requireActivity()) {}

        viewModel.addDailyStepError.observe(requireActivity()) { it.printStackTrace() }
        viewModel.addDailyStepData.observe(requireActivity()) {
            if (it.status == 1) {
                it.run {
                    if (steps > lastSteps) {
                        newSteps = steps - lastSteps
                        Timber.e("onSuccess newSteps: $newSteps")
                        if (newSteps >= 1000) {
                            lastSteps = steps
                            Prefs.putLong(PrefKey.last_steps, lastSteps)
                            getMyDailyStep()
                        } else if (cntCurr > cntLast) {
                            lastSteps = steps
                            Prefs.putLong(PrefKey.last_steps, lastSteps)
                            getMyDailyStep()
                        }
                    }

                }
            }
        }

        viewModel.addToAcceptRewardError.observe(requireActivity()) { it.printStackTrace() }
        viewModel.addToAcceptRewardData.observe(requireActivity()) {
            if (it.status == 1) {
                it.run {
                    currentClickedLayout!!.clearAnimation()
                    currentClickedLayout!!.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner_white)
                    currentClickedImgView!!.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.text_steps),
                        PorterDuff.Mode.MULTIPLY
                    )

                    if (currentClickedTextView != null) {
                        currentClickedTextView!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_steps))
                    }

                    todaystepCoins += RewardedCoins
                    binding.txtCoins.text = todaystepCoins.toString()
                    stepCoins += RewardedCoins
                    binding.textTotalCoins.text = stepCoins.toString()
                    text_current!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    text_coins!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))

                    listRewardData!![clickedBtnPos].isPerformed = 1

                    Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getStepError.observe(requireActivity()) { it.printStackTrace() }
        viewModel.getStepData.observe(requireActivity()) {
            if (it.status == 1) {
                it.data.run {
                    stepCoins = this?.userCoins!!
                    todaystepCoins = this.todayUserCoins
                    listRewardData = this.RewardedData
                    added_or_not = it.addedornot
                    checkAnimation()
                    if (listRewardData != null) {
                        try {
                            if (listRewardData!![0].isPerformed == 0) {
                                popUpAnimation(binding.llDailyReward, 0)
                                binding.txtDailyRewardCoins.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.text_steps
                                    )
                                )
                                binding.txtDailyReward.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_steps))
                            } else {
                                binding.llDailyReward.background =
                                    ContextCompat.getDrawable(requireContext(), R.drawable.app_corner_white)
                                binding.imgDailyReward.setColorFilter(
                                    ContextCompat.getColor(requireContext(), R.color.app_corner_1),
                                    PorterDuff.Mode.MULTIPLY
                                )
                                binding.txtDailyRewardCoins.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.text_color
                                    )
                                )
                                binding.txtDailyReward.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                            }
                            if (it.addedornot == 0) {
                                popUpAnimation(binding.llInviteFriends, 1)
                                binding.txtInviteFiendsCoins.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.text_steps
                                    )
                                )
                                binding.txtInviteFiends.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_steps))
                            } else {
                                binding.llInviteFriends.background = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.app_corner_white
                                )
                                binding.imgInviteFriends.setColorFilter(
                                    ContextCompat.getColor(requireContext(), R.color.app_corner_2),
                                    PorterDuff.Mode.MULTIPLY
                                )
                                binding.txtInviteFiendsCoins.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.text_color
                                    )
                                )
                                binding.txtInviteFiends.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                            }
                        } catch (e: java.lang.Exception) {
                            Timber.e("onSuccess: " + e.message)
                        }
                    }

                    binding.txtCoins.text = todaystepCoins.toString()
                    when (stepCoins.toString().length) {
                        4 -> {
                            binding.textTotalCoins.text = createSpaceString(stepCoins.toString(), " ", 0)
                        }
                        5 -> {
                            binding.textTotalCoins.text = createSpaceString(stepCoins.toString(), " ", 1)
                        }
                        else -> {
                            binding.textTotalCoins.text = stepCoins.toString()
                        }
                    }
                }
            }
        }

    }

    private fun getMyDailyStep() {
        viewModel.getDailySteps()
    }

    private fun setOnClickListener() {
        binding.llDailyReward.setOnClickListener(this)
        binding.llInviteFriends.setOnClickListener(this)
        binding.llBonus1.setOnClickListener(this)
        binding.llBonus2.setOnClickListener(this)
        binding.llBonus3.setOnClickListener(this)
        binding.llBonus4.setOnClickListener(this)
        binding.llBonus5.setOnClickListener(this)
    }

    private fun initView() {
        conditionStartSteps = condition_start_steps.toDouble()
        conditionEndSteps = condition_end_steps.toDouble()
        steps = current_steps.toLong()
        account_first_time = SharedPref.account_first_time
        pulse = AnimationUtils.loadAnimation(activity, R.anim.pulse)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        todayDate = sdf.format(Date())
        getDate = SharedPref.todayDate

        if (steps <= 20000) {
            binding.textViewStepsCount.text = steps.toString()
        } else {
            binding.textViewStepsCount.text = "20000"
        }

        if (getDate != todayDate) {
            condition_start_steps = 0
            SharedPref.condition_end_steps = 0
            current_steps = 0
            last_steps = 0
            viewModel.updateDailySteps()
            binding.textViewStepsCount.text = "0"
            SharedPref.todayDate = todayDate.toString()
        }

        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ).build()
        val account: GoogleSignInAccount = GoogleSignIn.getAccountForExtension(requireActivity(), fitnessOptions!!)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions!!)) {
            val dialog = Dialog(requireContext())
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.dialog_google_fit)
            ll_google_sign_in = dialog.findViewById<View>(R.id.ll_sign_in) as LinearLayout
            ll_google_sign_in?.setOnClickListener {
                GoogleSignIn.requestPermissions(
                    requireActivity(),
                    GOOGLE_FIT_PERMISSION_REQUEST_CODE, account, fitnessOptions!!
                )
                dialog.dismiss()
            }
            dialog.show()
        } else {
            getTodayData()
            subscribeAndGetRealTimeData(DataType.TYPE_STEP_COUNT_DELTA)

        }

    }

    override fun onResume() {
        val f: Float
        super.onResume()
        val str = f1327sp
        f = try {
            str.toFloat()
        } catch (unused: java.lang.Exception) {
            10000.0f
        }
        binding.gauge.endValue = 100
        steps = Prefs.getLong(PrefKey.current_steps, 0)
        binding.gauge.value = (steps.toFloat() / f * 100.0f).roundToInt()
        binding.gauge.invalidate()
    }

    override fun onDetach() {
        super.onDetach()
        if (steps <= 20000) {
            updateMyCoinsAndSteps(steps, 0)
        } else {
            updateMyCoinsAndSteps(20000, 0)
        }
    }


    private fun getTodayData() {
        Fitness.getHistoryClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireActivity(), fitnessOptions!!))
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA).addOnSuccessListener { dataSet ->
                steps =
                    if (dataSet.isEmpty) 0 else dataSet.dataPoints[0].getValue(Field.FIELD_STEPS)
                        .asInt().toLong()
                Prefs.putLong(PrefKey.current_steps, steps)
                checkAnimation()
                val f: Float
                val str = f1327sp
                f = try {
                    str.toFloat()
                } catch (unused: java.lang.Exception) {
                    10000.0f
                }
                binding.gauge.endValue = 100
                if (steps <= 20000) {
                    lastSteps = Prefs.getLong(PrefKey.last_steps, 0)
                    binding.gauge.value = (steps.toFloat() / f * 100.0f).roundToInt()
                    if (steps >= 1000) {
                        Timber.e("onSuccess: steps$steps")
                        val stringNumber = "" + steps
                        if (stringNumber.length == 4) {
                            cntCurr = stringNumber.substring(0, stringNumber.length - 3).toInt()
                            Timber.e("onSuccess first: $cntCurr")
                        } else if (stringNumber.length == 5) {
                            cntCurr = stringNumber.substring(0, stringNumber.length - 3).toInt()
                            Timber.e("onSuccess cntCurr: $cntCurr")
                        }
                    }
                    if (lastSteps >= 1000) {
                        val stringNumber = "" + lastSteps
                        if (stringNumber.length == 4) {
                            cntLast = stringNumber.substring(0, stringNumber.length - 3).toInt()
                            Timber.e("onSuccess lastSteps: $cntLast")
                        } else if (stringNumber.length == 5) {
                            cntLast = stringNumber.substring(0, stringNumber.length - 3).toInt()
                            Timber.e("onSuccess lastSteps: $cntLast")
                        }
                    }
                    if (steps > lastSteps) {
                        newSteps = steps - lastSteps
                        when {
                            newSteps >= 1000 -> {
                                val stringNumber = "" + newSteps
                                if (stringNumber.length == 4) {
                                    cnt = stringNumber.substring(0, stringNumber.length - 3).toInt()
                                    Timber.e("onSuccess first: $cnt")
                                } else if (stringNumber.length == 5) {
                                    cnt = stringNumber.substring(0, stringNumber.length - 3).toInt()
                                    Timber.e("onSuccess TAGTAG: $cnt")
                                }
                                updateMyCoinsAndSteps(steps, cnt)
                            }
                            cntCurr > cntLast -> {
                                UserCoins = cntCurr - cntLast
                                updateMyCoinsAndSteps(steps, UserCoins)
                            }
                            else -> {
                                updateMyCoinsAndSteps(steps, 0)
                            }
                        }
                    }
                    when (steps.toString().length) {
                        4 -> {
                            binding.textViewStepsCount.text = createSpaceString("" + steps, " ", 0)
                        }
                        5 -> {
                            binding.textViewStepsCount.text = createSpaceString("" + steps, " ", 1)
                        }
                        else -> {
                            binding.textViewStepsCount.text = steps.toString()
                        }
                    }
                    binding.textViewDistance.text = UIHelper.getDistanceFromSteps(steps, context)
                } else {
                    binding.gauge.value = (20000.toFloat() / f * 100.0f).roundToInt()
                    binding.textViewStepsCount.text = "20 000"
                    binding.textViewDistance.text = UIHelper.getDistanceFromSteps(20000, context)
                }
                binding.gauge.invalidate()
            }
    }

    private fun updateMyCoinsAndSteps(steps: Long, cnt: Int) {
        todaystepCoins += steps.toInt()
        binding.txtCoins.text = todaystepCoins.toString()
        stepCoins += cnt
        binding.textTotalCoins.text = stepCoins.toString()

        val map = HashMap<String, Any>()
        map[RequestParamsUtils.StepsCount] = steps.toString()
        map[RequestParamsUtils.StepsKm] = UIHelper.getDistanceFromSteps(steps_today, context)
        map[RequestParamsUtils.StepsDate] = todayDate.toString()
        map[RequestParamsUtils.UserCoins] = cnt.toString()

        viewModel.addDailySteps(map)

    }

    private fun subscribeAndGetRealTimeData(dataType: DataType) {
        Fitness.getRecordingClient(requireActivity(), GoogleSignIn.getAccountForExtension(requireActivity(), fitnessOptions!!))
            .subscribe(dataType)
            .addOnSuccessListener {
                Timber.e("Successfully subscribed!")
            }
            .addOnFailureListener { e: Exception ->
                Timber.e("Failure: " + e.localizedMessage)
            }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.llDailyReward -> {
                is_performed = listRewardData!![0].isPerformed
                if (is_performed == 0) {
                    RewardedID = listRewardData!![0].rewardedId
                    RewardedCoins = listRewardData!![0].coins
                    addToAcceptReward(
                        binding.llDailyReward,
                        binding.imgDailyReward,
                        binding.txtDailyReward,
                        binding.txtDailyRewardCoins,
                        null,
                        0
                    )
                    listRewardData!![0].isPerformed = 1
                } else {
                    Utils.MyShortSnackbar(binding.llDailyReward, "This reward has been taken.")
                }
            }
            R.id.llInviteFriends -> {
                if (added_or_not == 0) {
                    RewardedID = listRewardData!![1].rewardedId
                    RewardedCoins = listRewardData!![1].coins
                    added_or_not = 1
                    addToAcceptReward(
                        binding.llInviteFriends,
                        binding.imgInviteFriends,
                        binding.txtInviteFiends,
                        binding.txtInviteFiendsCoins,
                        null,
                        1
                    )
                } else {
                    Utils.MyShortSnackbar(binding.llInviteFriends, "This reward has been taken.")
                }
            }
            R.id.llBonus1 -> {
                if (steps >= 1000) {
                    is_performed = listRewardData!![2].isPerformed
                    if (is_performed == 0) {
                        RewardedID = listRewardData!![2].rewardedId
                        RewardedCoins = listRewardData!![2].coins
                        addToAcceptReward(
                            binding.llBonus1,
                            binding.imgBonus1,
                            binding.txtBonus1,
                            binding.txtBonus1Coins,
                            binding.txt1000,
                            2
                        )
                    } else {
                        Utils.MyShortSnackbar(binding.llBonus1, "This reward has been taken.")
                    }
                } else {
                    Utils.MyShortSnackbar(binding.llBonus1, "You have to complate 1000 steps for this reward.")
                }
            }
            R.id.llBonus2 -> {
                if (steps >= 3000) {
                    is_performed = listRewardData!![3].isPerformed
                    if (is_performed == 0) {
                        RewardedID = listRewardData!![3].rewardedId
                        RewardedCoins = listRewardData!![3].coins
                        addToAcceptReward(
                            binding.llBonus2,
                            binding.imgBonus2,
                            binding.txtBonus2,
                            binding.txtBonus2Coins,
                            binding.txt3000,
                            3
                        )
                    } else {
                        Utils.MyShortSnackbar(binding.llBonus2, "This reward has been taken.")
                    }
                } else {
                    Utils.MyShortSnackbar(binding.llBonus2, "You have to complate 3000 steps for this reward.")
                }
            }
            R.id.llBonus3 -> {
                if (steps >= 9000) {
                    is_performed = listRewardData!![4].isPerformed
                    if (is_performed == 0) {
                        RewardedID = listRewardData!![4].rewardedId
                        RewardedCoins = listRewardData!![4].coins
                        addToAcceptReward(
                            binding.llBonus3,
                            binding.imgBonus3,
                            binding.txtBonus3,
                            binding.txtBonus3Coins,
                            binding.txt9000,
                            4
                        )
                    } else {
                        Utils.MyShortSnackbar(binding.llBonus3, "This reward has been taken.")
                    }
                } else {
                    Utils.MyShortSnackbar(binding.llBonus3, "You have to complate 9000 steps for this reward.")
                }
            }
            R.id.llBonus4 -> {
                if (steps >= 14000) {
                    is_performed = listRewardData!![5].isPerformed
                    if (is_performed == 0) {
                        RewardedID = listRewardData!![5].rewardedId
                        RewardedCoins = listRewardData!![5].coins
                        addToAcceptReward(
                            binding.llBonus4,
                            binding.imgBonus4,
                            binding.txtBonus4,
                            binding.txtBonus4Coins,
                            binding.txt14000,
                            5
                        )
                    } else {
                        Utils.MyShortSnackbar(binding.llBonus4, "This reward has been taken.")
                    }
                } else {
                    Utils.MyShortSnackbar(binding.llBonus1, "You have to complate 14000 steps for this reward.")
                }
            }
            R.id.llBonus5 -> {
                if (steps >= 20000) {
                    is_performed = listRewardData!![6].isPerformed
                    if (is_performed == 0) {
                        RewardedID = listRewardData!![6].rewardedId
                        RewardedCoins = listRewardData!![6].coins
                        addToAcceptReward(
                            binding.llBonus5,
                            binding.imgBonus5,
                            binding.txtBonus5,
                            binding.txtBonus5Coins,
                            binding.txt20000,
                            6
                        )
                    } else {
                        Utils.MyShortSnackbar(binding.llBonus5, "This reward has been taken.")
                    }
                } else {
                    Utils.MyShortSnackbar(binding.llBonus5, "You have to complate 20000 steps for this reward.")
                }
            }

        }
    }

    private fun addToAcceptReward(
        currentClickedLayout: LinearLayout,
        currentClickedImgView: ImageView,
        text_current: TextView,
        text_coins: TextView,
        currentClickedTextView: TextView?,
        clickedBtnPos: Int
    ) {
        this.currentClickedLayout = currentClickedLayout
        this.currentClickedImgView = currentClickedImgView
        this.currentClickedTextView = currentClickedTextView
        this.clickedBtnPos = clickedBtnPos
        this.btnPos = 0
        this.text_current = text_current
        this.text_coins = text_coins

        val map = HashMap<String, Any>()
        map[RequestParamsUtils.RewardedId] = RewardedID.toString()
        map[RequestParamsUtils.RewardedCoins] = RewardedCoins
        viewModel.addToAcceptReward(map)

    }

    fun createSpaceString(originalString: String, stringToBeInserted: String?, index: Int): String? {
        var newString: String? = String()
        for (i in originalString.indices) {
            newString += originalString[i]
            if (i == index) {
                newString += stringToBeInserted
            }
        }
        return newString
    }

    private fun checkAnimation() {
        if (steps >= 1000) {
            if (listRewardData != null) {
                if (listRewardData!![2].isPerformed == 0) {
                    try {
                        popUpAnimation(binding.llBonus1, 2)
                        binding.llBonus1.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner3)
                        binding.imgBonus1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.txt1000.setTextColor(requireActivity().getColor(R.color.white))
                            binding.txtBonus1.setTextColor(requireActivity().getColor(R.color.text_steps))
                            binding.txtBonus1Coins.setTextColor(requireActivity().getColor(R.color.text_steps))
                        }
                    } catch (e: java.lang.Exception) {
                        Timber.e("checkAnimation: " + e.message)
                    }
                }
            }
        }
        if (steps >= 3000) {
            if (listRewardData != null) {
                if (listRewardData!![3].isPerformed == 0) {
                    try {
                        popUpAnimation(binding.llBonus2, 3)
                        binding.llBonus2.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner4)
                        binding.imgBonus2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.txt3000.setTextColor(requireActivity().getColor(R.color.white))
                            binding.txtBonus2.setTextColor(requireActivity().getColor(R.color.text_steps))
                            binding.txtBonus2Coins.setTextColor(requireActivity().getColor(R.color.text_steps))
                        }
                    } catch (e: java.lang.Exception) {
                        Timber.e("checkAnimation: " + e.message)
                    }
                }
            }
        }
        if (steps >= 9000) {
            if (listRewardData != null) {
                if (listRewardData!![4].isPerformed == 0) {
                    try {
                        popUpAnimation(binding.llBonus3, 4)
                        binding.llBonus3.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner5)
                        binding.imgBonus3.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.txt9000.setTextColor(requireActivity().getColor(R.color.white))
                            binding.txtBonus3.setTextColor(requireActivity().getColor(R.color.text_steps))
                            binding.txtBonus3Coins.setTextColor(requireActivity().getColor(R.color.text_steps))
                        }
                    } catch (e: java.lang.Exception) {
                        Timber.e("checkAnimation: " + e.message)
                    }
                }
            }
        }
        if (steps >= 14000) {
            if (listRewardData != null) {
                if (listRewardData!![5].isPerformed == 0) {
                    try {
                        popUpAnimation(binding.llBonus4, 5)
                        binding.llBonus4.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner6)
                        binding.imgBonus4.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.txt14000.setTextColor(requireActivity().getColor(R.color.white))
                            binding.txtBonus4.setTextColor(requireActivity().getColor(R.color.text_steps))
                            binding.txtBonus4Coins.setTextColor(requireActivity().getColor(R.color.text_steps))
                        }
                    } catch (e: java.lang.Exception) {
                        Timber.e("checkAnimation: " + e.message)
                    }
                }
            }
        }
        if (steps >= 20000) {
            if (listRewardData != null) {
                if (listRewardData!![6].isPerformed == 0) {
                    try {
                        popUpAnimation(binding.llBonus5, 6)
                        binding.llBonus5.background = ContextCompat.getDrawable(requireContext(), R.drawable.app_corner7)
                        binding.imgBonus5.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            binding.txt20000.setTextColor(requireActivity().getColor(R.color.white))
                            binding.txtBonus5.setTextColor(requireActivity().getColor(R.color.text_steps))
                            binding.txtBonus5Coins.setTextColor(requireActivity().getColor(R.color.text_steps))
                        }
                    } catch (e: java.lang.Exception) {
                        Timber.e("checkAnimation: " + e.message)
                    }
                }
            }
        }
    }

    private fun popUpAnimation(linearLayout: LinearLayout, i: Int) {
        if (listRewardData != null) {
            is_performed = listRewardData!![i].isPerformed
            if (is_performed == 0) {
                linearLayout.startAnimation(pulse)
            }
        }
    }
}