package com.example.gamelibrary.data.repository  // 👈 Правильный пакет!

import com.example.gamelibrary.data.local.dao.ProfileDao
import com.example.gamelibrary.data.local.entity.Profile
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val profileDao: ProfileDao) {

    val allProfiles: Flow<List<Profile>> = profileDao.getAllProfiles()

    suspend fun insertProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }

    fun getProfileById(profileId: Int): Flow<Profile?> {
        return profileDao.getProfileById(profileId)
    }
}