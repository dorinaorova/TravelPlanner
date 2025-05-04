package com.androidlab.travelplannerapp

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerManager {
    private val mockWebServer = MockWebServer()

    fun start() {
        mockWebServer.start(8080)
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

    fun setDispatcher(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }

    fun takeRequest(): RecordedRequest{
        return mockWebServer.takeRequest()
    }
}