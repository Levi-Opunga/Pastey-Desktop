package client

import entities.ClipNote
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import khttp.get
import khttp.responses.Response
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


suspend fun getNotes(): List<ClipNote> {
    val r = get("https://pastey.up.railway.app/api/note")
    r.text;
    var notes = Json.decodeFromString<List<ClipNote>>(r.text)
    return notes;
}


object PasteyClient {
    val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getNotes(): List<ClipNote> {
        val r = client.request { url("https://pastey.up.railway.app/api/note") }.body<List<ClipNote>>()
        return r;
    }

    suspend fun updateNotes(note: ClipNote): ClipNote {
        val r = client.request {
            url("https://pastey.up.railway.app/api/note/${note.id}")
            setBody(note)
            method = HttpMethod.Put
            headers {
                append("Content-Type", "application/json")
            }
        }.body<ClipNote>()
        return r;
    }
    suspend fun createNote(note: ClipNote): ClipNote {
        val r = client.request {
            url("https://pastey.up.railway.app/api/note")
            setBody(note)
            method = HttpMethod.Post
            headers {
                append("Content-Type", "application/json")
            }
        }.body<ClipNote>()
        return r;
    }
    suspend fun deleteNote(id:Long): String {
        val r = client.request {
            url("https://pastey.up.railway.app/api/note/${id}")
            method = HttpMethod.Delete
        }.body<String>()
        return r;
    }
}