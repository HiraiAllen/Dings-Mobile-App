package com.example.dings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dings.ui.theme.DingsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DingsTheme {
                val texto = remember { mutableStateOf("Hola Mundo") }

                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        val result = fetchDataFromApi("https://example.com/api")
                        texto.value = result.toString()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(text = texto.value)
                }

            }
        }
    }

    private suspend fun fetchDataFromApi(url: String) {
        return withContext(Dispatchers.IO) {
            val requestQueue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
                    //val result = response.toString() // Convertir el JSON en un String
                    response
                },
                { error: VolleyError ->
                    error.printStackTrace()
                    // Return an empty JSONObject if there was an error
                    JSONObject()
                })

            val response = requestQueue.add(jsonObjectRequest).await()
            response.get()
        }
    }
}
