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
class EssentialsViewModel @Inject constructor() : ViewModel() {

    private val _essentialsList = MutableStateFlow<List<Essentials>>(emptyList())
    val essentialsList = _essentialsList.asStateFlow()

//    private val _essentialsOne = MutableStateFlow<Essentials>(Essentials())
//    val essentialsOne = _essentialsOne.asStateFlow()
    private val firebaseDatabase = Firebase.database

    private val _state = MutableStateFlow<EssentialsState>(EssentialsState.Nothing)
    val state = _state.asStateFlow()

    fun addEssentials(userEmail: String, ename: String, eplace: String, eprice: String) {
        val essentials = Essentials(
            id = firebaseDatabase.reference.child("essentials").push().key ?: UUID.randomUUID().toString(),
            userEmail = userEmail,
            ename = ename,
            eplace = eplace,
            eprice = eprice
        )
        firebaseDatabase.reference.child("essentials").child(essentials.id).setValue(essentials)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _state.value = EssentialsState.Success
                } else {
                    _state.value = EssentialsState.Error
                }
            }
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

//    fun listenForSingleEssentials(id: String) {
//        firebaseDatabase.reference.child("essentials")
//            .orderByChild("id")
//            .equalTo(id)
//            .limitToFirst(1)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val essentials: Essentials? = snapshot.children.firstOrNull()?.getValue(Essentials::class.java)
//                    essentials?.let { _essentialsOne.value = it }
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
//
//    }

}

sealed class EssentialsState {
    object Nothing: EssentialsState()
    object Loading: EssentialsState()
    object Success: EssentialsState()
    object Error: EssentialsState()
}