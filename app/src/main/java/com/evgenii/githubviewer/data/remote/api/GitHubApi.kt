package com.evgenii.githubviewer.data.remote.api

import com.evgenii.githubviewer.data.local.TokenStorage
import com.evgenii.githubviewer.data.remote.dto.RepoDetailsDto
import com.evgenii.githubviewer.data.remote.dto.RepoDto
import com.evgenii.githubviewer.data.remote.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class GitHubApi(
    private val tokenStorage: TokenStorage
) {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val baseUrl = "https://api.github.com"

    private fun HttpRequestBuilder.addAuthHeader() {
        val token = tokenStorage.authToken
        if (token != null) {
            header(HttpHeaders.Authorization, "token $token")
        }
    }

    // GET /user — получить информацию о владельце токена
    suspend fun getUserInfo(token: String): UserDto {
        return httpClient.get("$baseUrl/user") {
            header(HttpHeaders.Authorization, "token $token")
        }.body()
    }

    // GET /user/repos — получить список репозиториев
    suspend fun getRepositories(): List<RepoDto> {
        return httpClient.get("$baseUrl/user/repos?per_page=10") {
            addAuthHeader()
        }.body()
    }

    // GET /repos/{owner}/{repo} — получить детали одного репозитория
    suspend fun getRepository(owner: String, repo: String): RepoDetailsDto {
        return httpClient.get("$baseUrl/repos/$owner/$repo") {
            addAuthHeader()
        }.body()
    }

    // GET /repos/{owner}/{repo}/readme — получить README (сырой markdown)
/*    suspend fun getRepositoryReadme(owner: String, repo: String, branch: String): String {
        return httpClient.get("$baseUrl/repos/$owner/$repo/readme?ref=$branch") {
            addAuthHeader()
            header(HttpHeaders.Accept, "application/vnd.github.raw+json")
        }.body()
    }

 */

    suspend fun getRepositoryReadme(owner: String, repo: String, branch: String): String {
        val response = httpClient.get("$baseUrl/repos/$owner/$repo/readme?ref=$branch") {
            addAuthHeader()
            header(HttpHeaders.Accept, "application/vnd.github.raw+json")
        }
        return if (response.status.value in 200..299) {
            response.body()
        } else {
            ""
        }
    }
}