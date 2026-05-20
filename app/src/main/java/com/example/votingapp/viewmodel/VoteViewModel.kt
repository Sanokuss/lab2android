package com.example.votingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votingapp.model.VoteOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class VoteViewModel : ViewModel() {
    // Внутрішній стан
    private val _options = MutableStateFlow<List<VoteOption>>(emptyList())

    // Список завжди відсортований за кількістю голосів (спадання)
    val sortedOptions: StateFlow<List<VoteOption>> = _options
        .map { list -> list.sortedByDescending { it.votes } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Лідер (варіант з найбільшою кількістю голосів)
    val leader: StateFlow<VoteOption?> = _options
        .map { list ->
            val topOption = list.maxByOrNull { o -> o.votes }
            // Якщо ніхто ще не голосував (усі мають 0 голосів) — лідера немає
            if (topOption?.votes == 0) null else topOption
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // Додавання нового варіанту
    fun addOption(title: String) {
        if (title.isNotBlank()) {
            val newOption = VoteOption(title = title.trim())
            _options.value = _options.value + newOption
        }
    }

    // Голосування (копіює список, знаходить елемент, збільшує votes)
    fun vote(id: String) {
        _options.value = _options.value.map { option ->
            if (option.id == id) {
                option.copy(votes = option.votes + 1)
            } else {
                option
            }
        }
    }
}
