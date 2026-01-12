package networking

import io.ktor.client.HttpClient

// Provides a platform-specific preconfigured HttpClient.
expect fun createPlatformHttpClient(): HttpClient

