package com.gghez.game2048.inapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gghez.game2048.R
import com.gghez.game2048.data.settings.SettingsRepository
import com.gghez.game2048.inapp.BillingManager
import com.gghez.game2048.inapp.PremiumPlan
import com.gghez.game2048.withLocale
import kotlinx.coroutines.runBlocking

class PremiumActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DARK = "extra_dark"
    }

    // Áp dụng ngôn ngữ trong app (giống MainActivity)
    override fun attachBaseContext(newBase: Context) {
        val tag = runBlocking { SettingsRepository(newBase).currentLanguageTag() }
        super.attachBaseContext(if (tag.isEmpty()) newBase else newBase.withLocale(tag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Phải đặt night mode TRƯỚC super.onCreate
        if (intent.hasExtra(EXTRA_DARK)) {
            delegate.localNightMode =
                if (intent.getBooleanExtra(EXTRA_DARK, false)) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val billing = BillingManager(this) {
            finish()
        }
        billing.start()

        if (billing.isPremium()) {
            finish()
            return
        }

        val plans = listOf(
            PremiumPlan("premium_basic", "🌱 " + getString(R.string.plan_basic), "9.000đ"),
            PremiumPlan("premium_standard", "🌸 " + getString(R.string.plan_standard), "29.000đ", true),
            PremiumPlan("premium_plus", "🎀 " + getString(R.string.plan_plus), "49.000đ"),
            PremiumPlan("premium_pro", "💎 " + getString(R.string.plan_pro), "99.000đ"),
            PremiumPlan("premium_lifetime", "👑 " + getString(R.string.plan_lifetime), "199.000đ")
        )

        val adapter = PremiumPlanAdapter(plans) { plan ->
            billing.buy(this, plan.id)
        }

        val rv = findViewById<RecyclerView>(R.id.rvPlans)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }
}

