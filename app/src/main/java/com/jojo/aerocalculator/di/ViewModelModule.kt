package com.jojo.aerocalculator.di

import com.jojo.aerocalculator.data.models.aircraft.AircraftRepository
import com.jojo.aerocalculator.data.models.aircraft.AircraftRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideAircraftRepository(): AircraftRepository {
        return AircraftRepositoryImpl()
    }
}