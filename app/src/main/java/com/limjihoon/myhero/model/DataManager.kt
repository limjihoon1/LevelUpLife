package com.limjihoon.myhero.model

import com.limjihoon.myhero.data.Inventory
import com.limjihoon.myhero.data.Member2
import com.limjihoon.myhero.data.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataManager {
    private val _memberFlow = MutableStateFlow<Member2?>(null)
    val memberFlow = _memberFlow.asStateFlow()

    private val _inventoryFlow = MutableStateFlow<Inventory?>(null)
    val inventoryFlow = _inventoryFlow.asStateFlow()

    private val _todoFlow = MutableStateFlow<List<Todo>?>(null)
    val todoFlow = _todoFlow.asStateFlow()

    fun updateMember(member: Member2) {
        _memberFlow.value = member
    }

    fun updateInventory(inventory: Inventory) {
        _inventoryFlow.value = inventory
    }

    fun updateTodo(todo: List<Todo>) {
        _todoFlow.value = todo
    }

}