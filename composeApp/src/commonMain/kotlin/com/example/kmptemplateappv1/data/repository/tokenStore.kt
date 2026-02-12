package com.example.kmptemplateappv1.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Simple token store for commonMain.
 *
 * This provides a thread-safe in-memory implementation that can be used during
 * development and tests. For production you should provide platform-specific
 * secure storage (Keychain/Keystore) via expect/actual or DI.
 */
interface TokenStore {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}

private class InMemoryTokenStore : TokenStore {
    private val mutex = Mutex()
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override suspend fun getAccessToken(): String? = mutex.withLock { accessToken }
    override suspend fun getRefreshToken(): String? = mutex.withLock { refreshToken }

    override suspend fun setTokens(accessToken: String, refreshToken: String) = mutex.withLock {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override suspend fun clearTokens() = mutex.withLock {
        accessToken = null
        refreshToken = null
    }
}

// Default token store instance used by the HTTP client. Replace this with a
// platform-backed implementation if you need persistence/secure storage.
val tokenStore: TokenStore = InMemoryTokenStore()

