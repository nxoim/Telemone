package com.number869.telemone.common

import kotlinx.coroutines.flow.Flow

fun interface ThemeFileSource {
    suspend fun openStream(): Flow<String>?
}
