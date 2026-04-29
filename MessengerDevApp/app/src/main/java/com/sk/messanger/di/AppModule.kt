package com.sk.messanger.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sk.messanger.data.repository.AuthRepositoryImpl
import com.sk.messanger.data.repository.ChatRepositoryImpl
import com.sk.messanger.data.repository.UserRepositoryImpl
import com.sk.messanger.domain.repository.AuthRepository
import com.sk.messanger.domain.repository.ChatRepository
import com.sk.messanger.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Sudhakar
 * @date 21-04-2026
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firebaseDatabase: FirebaseDatabase) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseDatabase)
    }
    @Provides
    @Singleton
    fun provideUserRepository(firebaseDatabase : FirebaseDatabase) : UserRepository {
        return UserRepositoryImpl(firebaseDatabase = firebaseDatabase)
    }
    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseDatabase: FirebaseDatabase
    ): ChatRepository = ChatRepositoryImpl(firebaseDatabase)

}