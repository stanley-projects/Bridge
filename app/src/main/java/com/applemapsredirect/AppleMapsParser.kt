package com.applemapsredirect

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

data class ParsedLocation(
    val lat: Double? = null,
    val lng: Double? = null,
    val query: String? = null,
    val isDirections: Boolean = false
) {
    fun toGoogleMapsUri(): String {
        return when {
            isDirections && lat != null && lng != null ->
                "google.navigation:q=$lat,$lng"
            lat != null && lng != null ->
                "geo:$lat,$lng?q=$lat,$lng"
            !query.isNullOrBlank() ->
                "geo:0,0?q=${Uri.encode(query)}"
            else -> throw IllegalStateException("No location data to convert")
        }
    }

    fun toGoogleMapsWebUrl(): String {
        return when {
            isDirections && lat != null && lng != null ->
                "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"
            lat != null && lng != null ->
                "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
            !query.isNullOrBlank() ->
                "https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}"
            else -> throw IllegalStateException("No location data to convert")
        }
    }
}

object AppleMapsParser {

    suspend fun parse(urlString: String): ParsedLocation {
        val resolved = resolveRedirects(urlString.trim())
        return parseUrl(resolved)
    }

    private fun parseUrl(urlString: String): ParsedLocation {
        val uri = Uri.parse(urlString)

        // 1. Check for ll= (lat,lng coordinates)
        uri.getQueryParameter("ll")?.let { ll ->
            val parts = ll.split(",")
            if (parts.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lng = parts[1].trim().toDoubleOrNull()
                if (lat != null && lng != null) {
                    return ParsedLocation(lat = lat, lng = lng)
                }
            }
        }

        // 2. Check for daddr= (directions destination)
        uri.getQueryParameter("daddr")?.let { daddr ->
            val parts = daddr.split(",")
            if (parts.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lng = parts[1].trim().toDoubleOrNull()
                if (lat != null && lng != null) {
                    return ParsedLocation(lat = lat, lng = lng, isDirections = true)
                }
            }
            // daddr might be an address string
            return ParsedLocation(query = daddr, isDirections = true)
        }

        // 3. Check for address=
        uri.getQueryParameter("address")?.let { address ->
            if (address.isNotBlank()) {
                return ParsedLocation(query = address)
            }
        }

        // 4. Check for q= (query/place name)
        uri.getQueryParameter("q")?.let { q ->
            if (q.isNotBlank()) {
                // q might be coordinates
                val parts = q.split(",")
                if (parts.size == 2) {
                    val lat = parts[0].trim().toDoubleOrNull()
                    val lng = parts[1].trim().toDoubleOrNull()
                    if (lat != null && lng != null) {
                        return ParsedLocation(lat = lat, lng = lng)
                    }
                }
                return ParsedLocation(query = q)
            }
        }

        // 5. Check for saddr= (source address, less common)
        uri.getQueryParameter("saddr")?.let { saddr ->
            if (saddr.isNotBlank()) {
                return ParsedLocation(query = saddr)
            }
        }

        // 6. Try to extract from path (e.g., /place/Eiffel+Tower)
        val path = uri.path
        if (!path.isNullOrBlank() && path != "/") {
            val placePath = path.removePrefix("/place/").removePrefix("/")
            if (placePath.isNotBlank() && placePath != path.removePrefix("/")) {
                return ParsedLocation(query = Uri.decode(placePath))
            }
        }

        throw IllegalArgumentException(
            "Could not extract location from URL. Supported parameters: ll, q, address, daddr"
        )
    }

    private suspend fun resolveRedirects(urlString: String): String {
        // If it already has query params we can parse, skip network call
        val uri = Uri.parse(urlString)
        val hasParams = uri.getQueryParameter("ll") != null ||
                uri.getQueryParameter("q") != null ||
                uri.getQueryParameter("address") != null ||
                uri.getQueryParameter("daddr") != null
        if (hasParams) return urlString

        // Follow redirects to get the final URL
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.instanceFollowRedirects = false
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.requestMethod = "GET"

                val responseCode = conn.responseCode
                if (responseCode in 301..303 || responseCode == 307 || responseCode == 308) {
                    val location = conn.getHeaderField("Location")
                    conn.disconnect()
                    if (!location.isNullOrBlank()) {
                        // Recursively resolve in case of multiple redirects
                        return@withContext resolveRedirects(location)
                    }
                }
                conn.disconnect()
                urlString
            } catch (_: Exception) {
                urlString
            }
        }
    }
}
