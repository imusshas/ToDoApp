package com.nasiat.todo

import android.app.Application
import androidx.room.Room
import com.nasiat.todo.api.BdAppsApi
import com.nasiat.todo.database.TodoDatabase
import com.nasiat.todo.repository.BdAppsApiRepository
import com.nasiat.todo.repository.BdAppsApiRepositoryImpl
import com.nasiat.todo.repository.ToDoRepo
import com.nasiat.todo.repository.TodoRepoImpl
import com.nasiat.todo.repository.UserPref
import com.nasiat.todo.repository.UserPrefImpl
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(module {
                single {
                    Room.databaseBuilder(this@KoinApp, TodoDatabase::class.java, "db")
                        .build()
                }
                single {
                    TodoRepoImpl( database = get())
                } bind ToDoRepo::class

                single {
                    Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
                        .create(BdAppsApi::class.java)
                } bind BdAppsApi::class

                single {
                    UserPrefImpl(context = this@KoinApp)
                } bind UserPref::class

                single {
                    BdAppsApiRepositoryImpl(api = get())
                } bind BdAppsApiRepository::class
            })
        }
    }


    companion object {
        private const val BASE_URL = "http://20.197.50.116:8913/"
    }
}