package com.example.posapplication.di

import android.app.Application
import androidx.room.Room
import com.example.posapplication.data.local.POSDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object POSModule {

    @Provides
    @Singleton
    fun providePOSDatabase(app: Application): POSDatabase {
        return Room.databaseBuilder(
            app,
            POSDatabase::class.java,
            "pos_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideInventoryDao(db: POSDatabase) = db.getInventoryDao()

    @Provides
    @Singleton
    fun provideSalesDao(db: POSDatabase) = db.getSalesDao()




}