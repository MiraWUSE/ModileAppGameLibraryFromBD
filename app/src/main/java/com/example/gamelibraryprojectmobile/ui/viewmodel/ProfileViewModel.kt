package com.example.gamelibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelibrary.data.local.entity.Profile
import com.example.gamelibrary.data.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    val allProfiles: Flow<List<Profile>> = repository.allProfiles

    fun insertProfile(profile: Profile) {
        viewModelScope.launch { repository.insertProfile(profile) }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch { repository.updateProfile(profile) }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch { repository.deleteProfile(profile) }
    }

    fun getProfileById(profileId: Int): Flow<Profile?> {
        return repository.getProfileById(profileId)
    }
}