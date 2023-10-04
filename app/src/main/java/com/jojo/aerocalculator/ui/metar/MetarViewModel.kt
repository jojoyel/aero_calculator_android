package com.jojo.aerocalculator.ui.metar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.aerocalculator.BuildConfig
import com.jojo.aerocalculator.data.distant.MetarApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class MetarViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var result = mutableStateOf("")

    private val okHttp by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("X-API-Key", BuildConfig.CHECKWXAPI_TOKEN)
                chain.proceed(requestBuilder.build())
            }
    }

    private val retrofit: MetarApi by lazy {
        Retrofit.Builder()
            .client(okHttp.build())
            .baseUrl("https://api.checkwx.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MetarApi::class.java)
    }

    init {
        val icao = savedStateHandle.get<String>("airport")

        icao?.let {
            viewModelScope.launch {
                val metar = retrofit.metar(it)
                result.value = metar.data.first()
            }
        }
    }

    fun onFetch(icao: String) {
        if (icao.length != 4) return

        viewModelScope.launch {
            val metar = retrofit.metar(icao)
            if (metar.data.isNotEmpty())
                result.value = metar.data.first()
        }
    }
}