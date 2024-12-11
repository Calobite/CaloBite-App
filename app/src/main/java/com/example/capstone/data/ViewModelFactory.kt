package com.example.capstone.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.data.di.Injection
import com.example.capstone.ui.analyze.AnalyzeViewModel
import com.example.capstone.ui.detailFood.DetailFoodViewModel
import com.example.capstone.ui.food.FoodViewModel
import com.example.capstone.ui.home.HomeViewModel
import com.example.capstone.ui.login.LoginViewModel
import com.example.capstone.ui.profile.ProfileViewModel
import com.example.capstone.ui.signUp.SignUpViewModel

class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when
        {modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AnalyzeViewModel::class.java) -> {
                AnalyzeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(DetailFoodViewModel::class.java) -> {
                DetailFoodViewModel(userRepository ) as T
            }
            modelClass.isAssignableFrom(FoodViewModel::class.java) -> {
                FoodViewModel(userRepository ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            val userRepository = Injection.provideUserRepository(context)
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(userRepository).also { INSTANCE = it }
            }
        }
    }
}