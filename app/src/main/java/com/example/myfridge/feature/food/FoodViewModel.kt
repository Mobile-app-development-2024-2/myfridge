package com.example.myfridge.feature.food

import androidx.lifecycle.ViewModel
import com.example.myfridge.feature.model.Food
import com.example.myfridge.feature.shop.ShopState
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor() : ViewModel() {

    private val _foodList = MutableStateFlow<List<Food>>(emptyList())
    val foodList = _foodList.asStateFlow()

    private val _foodOne = MutableStateFlow<Food>(Food())
    val foodOne = _foodOne.asStateFlow()
    private val firebaseDatabase = Firebase.database

    private val _state = MutableStateFlow<FoodState>(FoodState.Nothing)
    val state = _state.asStateFlow()

    fun addFood(userEmail: String, name: String, expDate: String, place: String, price: String) {
        val food = Food(
            id = firebaseDatabase.reference.child("food").push().key ?: UUID.randomUUID().toString(),
            userEmail = userEmail,
            name = name,
            expDate = expDate,
            place = place,
            price = price
        )

        firebaseDatabase.reference.child("food").child(food.id).setValue(food)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _state.value = FoodState.Success
                } else {
                    _state.value = FoodState.Error
                }
            }
    }

    fun listenForFood(userEmail: String) {
        firebaseDatabase.reference.child("food")
            .orderByChild("userEmail")
            .equalTo(userEmail)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Food>()
                    snapshot.children.forEach { data ->
                        val food = data.getValue(Food::class.java)
                        food?.let {
                            list.add(it)
                        }
                    }
                    _foodList.value = list
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun listenForSingleFood(id: String) {
        firebaseDatabase.reference.child("food")
            .orderByChild("id")
            .equalTo(id)
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val food: Food? = snapshot.children.firstOrNull()?.getValue(
                        Food::class.java)
                    food?.let { _foodOne.value = it }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    fun deleteFood(foodId: String) {
        firebaseDatabase.reference.child("food").child(foodId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = FoodState.Success
                } else {
                    _state.value = FoodState.Error
                }
            }
    }

}

sealed class FoodState {
    object Nothing: FoodState()
    object Loading: FoodState()
    object Success: FoodState()
    object Error: FoodState()
}