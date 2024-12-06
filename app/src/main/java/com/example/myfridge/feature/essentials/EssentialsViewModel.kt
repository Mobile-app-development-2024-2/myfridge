package com.example.myfridge.feature.essentials

import androidx.lifecycle.ViewModel
import com.example.myfridge.feature.model.Essentials
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
class EssentialsViewModel @Inject constructor() : ViewModel()  {

    private val _essentialsList = MutableStateFlow<List<Essentials>>(emptyList())
    val essentialsList = _essentialsList.asStateFlow()
    private val firebaseDatabase = Firebase.database

    fun addEssentials(userEmail: String, what: String, where: String, price: String) {
        val essentials = Essentials (
            id = firebaseDatabase.reference.child("essentials").push().key ?: UUID.randomUUID().toString(),
            userEmail = userEmail,
            what = what,
            where = where,
            price = price
        )
        firebaseDatabase.reference.child("essentials").child(essentials.id).setValue(essentials)
    }

    fun listenForEssentials(userEmail: String) {
        firebaseDatabase.reference.child("essentials")
            .orderByChild("userEmail")
            .equalTo(userEmail)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Essentials>()
                    snapshot.children.forEach { data ->
                        val essentials = data.getValue(Essentials::class.java)
                        essentials?.let {
                            list.add(it)
                        }
                    }
                    _essentialsList.value = list
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

}