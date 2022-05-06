package com.easysteps.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.easysteps.R
import com.easysteps.base.BaseFragment
import com.easysteps.databinding.FragmentProfileBinding
import com.easysteps.pref.SharedPref
import com.easysteps.retrofit.RequestParamsUtils
import com.easysteps.viewModel.UpdateProfileViewModel
import com.vikktorn.picker.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by NIKUNJ on 06-05-2022.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile), View.OnClickListener,
    OnCountryPickerListener {

    private var cityObject: ArrayList<City>? = arrayListOf()
    private var stateObject: ArrayList<State>? = arrayListOf()
    private var selectedCountry: String? = null
    private var countryPicker: CountryPicker? = null

    val viewModel: UpdateProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObserver()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.txtSave.setOnClickListener(this)
        binding.icBack.setOnClickListener(this)
        binding.txtCountry.setOnClickListener(this)
    }

    private fun setUpObserver() {
        viewModel.apiErrors.observe(requireActivity()) { handleError(it) }

        viewModel.appLoader.observe(requireActivity()) { updateLoaderUI(it) }

        viewModel.dataError.observe(requireActivity()) { it.printStackTrace() }

        viewModel.updateProfileData.observe(requireActivity()) {
            if (it.status == 1) {
                SharedPref.updateProfile = it.data
                binding.icBack.performClick()
            }
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        setData()
        try {
            getStateJson()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        try {
            getCityJson("")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        countryPicker = CountryPicker.Builder().with(requireContext()).listener(this).build()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.txtSave -> {
                updateProfileData()
            }
            R.id.icBack -> {
                val manager = parentFragmentManager
                manager.popBackStackImmediate()
            }
            R.id.txtCountry -> {
                countryPicker!!.showDialog(requireActivity().supportFragmentManager)
            }
        }
    }

    private fun updateProfileData() {
        val map = HashMap<String, Any>()
        map[RequestParamsUtils.userAddress] = binding.edtAddress.text.toString().trim()
        map[RequestParamsUtils.userCity] = binding.edtCity.text.toString().trim()
        map[RequestParamsUtils.userEmail] = binding.edtEmailAddress.text.toString().trim()
        map[RequestParamsUtils.userCountry] = selectedCountry ?: ""
        map[RequestParamsUtils.userState] = binding.edtState.text.toString().trim()
        map[RequestParamsUtils.userPostCode] = binding.edtPostcode.text.toString().trim()
        viewModel.updateProfile(map)
    }

    private fun getStateJson() {
        var json: String? = null
        try {
            val inputStream = requireActivity().assets.open("states.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val jsonObject = JSONObject(json)
        val events = jsonObject.getJSONArray("states")
        for (j in 0 until events.length()) {
            val cit = events.getJSONObject(j)
            val stateData = State()
            stateData.stateId = cit.getString("id").toInt()
            stateData.stateName = cit.getString("name")
            stateData.countryId = cit.getString("country_id").toInt()
            stateObject?.add(stateData)
        }
    }

    private fun getCityJson(id: String) {
        var json: String? = null
        try {
            val inputStream = requireActivity().assets.open("cities.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val jsonObject = JSONObject(json)
        val events = jsonObject.getJSONArray("cities")
        for (j in 0 until events.length()) {
            val cit = events.getJSONObject(j)
            val cityData = City()
            cityData.cityId = cit.getString("id").toInt()
            cityData.cityName = cit.getString("name")
            cityData.stateId = cit.getString("state_id").toInt()
            if (id != "") cityObject?.add(cityData)
            else {
                if (id == cit.getString("state_id")) {
                    cityObject?.add(cityData)
                }
            }
        }
    }

    override fun onSelectCountry(country: Country?) {
        selectedCountry = country!!.name
        binding.txtCountry.text = country.name
    }

    private fun setData() {
        binding.edtName.setText(SharedPref.updateProfile?.userName.toString())
        binding.edtAddress.setText(SharedPref.updateProfile?.userAddress)
        binding.txtCountry.text = SharedPref.updateProfile?.userCountry
        binding.edtState.setText(SharedPref.updateProfile?.userState)
        binding.edtCity.setText(SharedPref.updateProfile?.userCity)
        binding.edtPostcode.setText(SharedPref.updateProfile?.userPostCode)
    }
}