package com.androidlab.travelplannerapp

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

object MockWebServerManager {
    private val mockWebServer = MockWebServer()

    fun start() {
        mockWebServer.start()
    }

    fun shutdown() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(response: MockResponse) {
        mockWebServer.enqueue(response)
    }

    fun getBaseUrl(): String {
        return mockWebServer.url("/").toString()
    }
}