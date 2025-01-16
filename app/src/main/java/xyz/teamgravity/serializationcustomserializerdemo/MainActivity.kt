package xyz.teamgravity.serializationcustomserializerdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        val stringEndpoint = "https://openlibrary.org/works/OL1968368W.json"
        val jsonObjectEndpoint = "https://openlibrary.org/works/OL82563W.json"

        lifecycleScope.launch {
            listOf(
                stringEndpoint,
                jsonObjectEndpoint
            ).forEach { url ->
                val response = client.get(url)
                val dto = response.body<BookDto>()
                println(dto)
            }
        }
    }
}
