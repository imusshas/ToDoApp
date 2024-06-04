package com.nasiat.todo.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasiat.todo.model.UserModel
import com.nasiat.todo.model.UserValidator
import com.nasiat.todo.repository.BdAppsApiRepository
import com.nasiat.todo.repository.UserPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NavigationViewModel: ViewModel(), KoinComponent {

    private val userPref: UserPref by inject()
    private val bdAppsApiRepository: BdAppsApiRepository by inject()

    val user = userPref.getUserModel().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UserModel()
    )

    private val _phoneNo = MutableStateFlow("")
    val phoneNo = _phoneNo.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp = _otp.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError = _otpError.asStateFlow()

    private val _navigateToOTPScreen = MutableStateFlow(false)
    val navigateToOTPScreen = _navigateToOTPScreen.asStateFlow()

    private val _navigateToPromptScreen = MutableStateFlow(false)
    val navigateToPromptScreen = _navigateToPromptScreen.asStateFlow()


    private val _referenceNo = MutableStateFlow<String?>(null)
    private val referenceNo = _referenceNo.asStateFlow()



    fun onEvent(event: AuthenticationUIEvent) {
        when (event) {
            is AuthenticationUIEvent.PhoneNoChanged -> {
                _phoneNo.update { event.phoneNo }
            }
            AuthenticationUIEvent.SignUpButtonClicked -> {
                if (validatePhoneNoState()) {
                    signIn()
                }
            }
            is AuthenticationUIEvent.OTPChanged -> {
                _otp.update { event.otp }
            }
            AuthenticationUIEvent.VerifyOTPButtonClicked -> {
                verifyOTP()
            }
        }
    }

    private fun saveUser(userModel: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        userPref.saveUserModel(userModel)
        _navigateToPromptScreen.update { true }
        _phoneNo.update { "" }
    }


    private fun signIn() = viewModelScope.launch {

        val phone = phoneNo.value
        val subscriberId = if (phone[0] == '+' && phone[1] == '8' && phone[2] == '8') {
            phone.substring(3)
        } else if (phone[0] == '8' && phone[1] == '8') {
            phone.substring(2)
        } else {
            phone
        }

        bdAppsApiRepository.requestOTP(subscriberId = subscriberId).collectLatest {
            val response = it
            if (response.isSuccessful) {
                val requestOTPResponse = response.body()
                Log.d(TAG, "requestOTP: ${response.body()}")
                if (requestOTPResponse?.statusDetail == "Success") {
                    _referenceNo.update { requestOTPResponse.referenceNo }
                    _navigateToOTPScreen.update { true }
                } else if (requestOTPResponse?.statusDetail == "user already registered") {
                    _error.update { "User is Already Registered" }
                }
            } else {
                Log.d(TAG, "requestOTP: error: ${response.errorBody()}")
            }
        }
    }


    private fun verifyOTP() = viewModelScope.launch {
        referenceNo.value?.let { referenceNo ->
            bdAppsApiRepository.verifyOTP(
                referenceNo = referenceNo,
                otp = otp.value
            ).collectLatest {
                val verifyResponse = it
                if (verifyResponse.isSuccessful) {
                    val verifyOTPResponse = verifyResponse.body()
                    if (verifyOTPResponse?.statusDetail == "Success") {
                        saveUser(UserModel(phoneNo = phoneNo.value))
                    } else {
                        _otpError.update { "Invalid OTP" }
                    }
                } else {
                    Log.d(TAG, "verifyOTP: error: ${verifyResponse.errorBody()}")
                }
            }
        }
    }


    private fun validatePhoneNoState(): Boolean {
        _error.update {
            UserValidator.validatePhoneNo(phoneNo.value)
        }

        return error.value == null
    }

    companion object {
        private const val TAG = "NavigationViewModel"
    }
}