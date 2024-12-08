package com.example.myfridge.feature.shop

import androidx.lifecycle.ViewModel
import com.example.myfridge.feature.model.Shop
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
class ShopViewModel @Inject constructor() : ViewModel() {

    private val _shopList = MutableStateFlow<List<Shop>>(emptyList())
    val shopList = _shopList.asStateFlow()
    private val firebaseDatabase = Firebase.database

    private val _state = MutableStateFlow<ShopState>(ShopState.Nothing)
    val state = _state.asStateFlow()

    fun addShop(userEmail: String, name: String, place: String, price: String, eorf: Boolean) {
        val shop = Shop(
            id = firebaseDatabase.reference.child("shop").push().key ?: UUID.randomUUID().toString(),
            userEmail = userEmail,
            name = name,
            place = place,
            price = price,

            eorf = eorf
        )

        firebaseDatabase.reference.child("shop").child(shop.id).setValue(shop)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _state.value = ShopState.Success
                } else {
                    _state.value = ShopState.Error
                }
            }
    }

    fun listenForShop(userEmail: String) {
        firebaseDatabase.reference.child("shop")
            .orderByChild("userEmail")
            .equalTo(userEmail)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Shop>()
                    snapshot.children.forEach { data ->
                        val shop = data.getValue(Shop::class.java)
                        shop?.let {
                            list.add(it)
                        }
                    }
                    _shopList.value = list
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun deleteShop(shopId: String) {
        firebaseDatabase.reference.child("shop").child(shopId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = ShopState.Success
                } else {
                    _state.value = ShopState.Error
                }
            }
    }

}

sealed class ShopState {
    object Nothing: ShopState()
    object Loading: ShopState()
    object Success: ShopState()
    object Error: ShopState()
}