package com.shellrider.minipainter.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class CreateEntryViewModel: ViewModel() {
    private val _cachedImagePath = MutableLiveData<String>()
    val cachedImagePath: LiveData<String>
        get() = _cachedImagePath

    private val _miniatureName = MutableLiveData<String>()
    val miniatureName: LiveData<String>
        get() = _miniatureName


    fun setCachedImagePath(cachedImagePath: String){
        _cachedImagePath.value = cachedImagePath
    }

    fun setMiniatureName(miniatureName: String) {
        _miniatureName.value = miniatureName
    }

}