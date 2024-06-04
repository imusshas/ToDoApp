package com.nasiat.todo.repository

import android.util.Log
import com.nasiat.todo.api.BdAppsApi
import com.nasiat.todo.model.api_response.RequestOTPResponse
import com.nasiat.todo.model.api_response.VerifyOTPResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class BdAppsApiRepositoryImpl(private val api: BdAppsApi) : BdAppsApiRepository {
    override suspend fun requestOTP(subscriberId: String): Flow<Response<RequestOTPResponse>> =
        flow {
            emit(api.requestOTP(subscriberId))
        }.catch {
            Log.d(TAG, "requestOTP: ${it.message}")
        }

    override suspend fun verifyOTP(
        referenceNo: String,
        otp: String
    ): Flow<Response<VerifyOTPResponse>> = flow {
        emit(api.verifyOTP(referenceNo, otp))
    }.catch {
        Log.d(TAG, "verifyOTP: ${it.message}")
    }


    companion object {
        private const val TAG = "BdAppsApiRepositoryImpl"
    }

}