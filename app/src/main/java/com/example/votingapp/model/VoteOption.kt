package com.example.votingapp.model

import java.util.UUID

data class VoteOption(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val votes: Int = 0
)
