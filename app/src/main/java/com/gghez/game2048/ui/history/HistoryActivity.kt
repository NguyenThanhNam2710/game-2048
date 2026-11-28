package com.gghez.game2048.ui.history

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gghez.game2048.Game2048App
import com.gghez.game2048.data.settings.SettingsRepository
import com.gghez.game2048.ui.theme.Game2048Theme
import com.gghez.game2048.withLocale
import kotlinx.coroutines.runBlocking

class HistoryActivity : ComponentActivity() {

    companion object {
        const val EXTRA_DARK = "extra_dark"
    }

    // Same in-app language handling as MainActivity.
    override fun attachBaseContext(newBase: Context) {
        val tag = runBlocking { SettingsRepository(newBase).currentLanguageTag() }
        super.attachBaseContext(if (tag.isEmpty()) newBase else newBase.withLocale(tag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = (application as Game2048App).container.historyRepo
        val dark = intent.getBooleanExtra(EXTRA_DARK, false)
        setContent {
            val records by repo.records.collectAsStateWithLifecycle(initialValue = emptyList())
            Game2048Theme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    HistoryScreen(records = records, dark = dark, onBack = { finish() })
                }
            }
        }
    }
}