package com.example.aplicacioninmartiu.ui

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val myData = MutableLiveData<Bundle>()
}