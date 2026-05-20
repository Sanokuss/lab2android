package com.example.votingapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votingapp.viewmodel.VoteViewModel

@Composable
fun VoteScreen(
    viewModel: VoteViewModel,
    modifier: Modifier = Modifier
) {
    // Підписка на стан з ViewModel
    val options by viewModel.sortedOptions.collectAsStateWithLifecycle()
    val leader by viewModel.leader.collectAsStateWithLifecycle()

    // Локальний стан для поля вводу
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "\uD83C\uDFC6 Голосування",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Відображення лідера
        if (leader == null) {
            Text(
                text = "Голосування ще не розпочато",
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            leader?.let { currentLeader ->
                Text(
                    text = "Лідер: ${currentLeader.title} (${currentLeader.votes} голосів)",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Рядок додавання нового варіанту
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Додати варіант...") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.addOption(inputText)
                    inputText = ""
                },
                enabled = inputText.isNotBlank() // Кнопка неактивна, якщо поле порожнє
            ) {
                Text("+ Додати")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Динамічний список карток
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = options,
                key = { it.id } // Обов'язкове використання id для правильної анімації/роботи списку
            ) { option ->
                VoteCard(
                    option = option,
                    onVoteClick = { viewModel.vote(option.id) }
                )
            }
        }
    }
}
