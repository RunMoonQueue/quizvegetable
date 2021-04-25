package com.moon.quizvegetable

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.moon.quizvegetable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.word.setOnClickListener {
            startActivity(Intent(this, WordActivity::class.java))
        }

        binding.otherGameImage.setOnClickListener {
            Toast.makeText(this, "준비중입니다!", Toast.LENGTH_SHORT).show()
        }
        binding.otherGame.setOnClickListener {
            Toast.makeText(this, "준비중입니다!", Toast.LENGTH_SHORT).show()
        }

        binding.review.setOnClickListener {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                    )
            )
        }
        binding.reviewImage.setOnClickListener {
            startActivity(
                    Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                    )
            )
        }

//        binding.rank.setOnClickListener {
//            Toast.makeText(this, "이달의 순위!", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, RankActivity::class.java))
//        }
//        binding.rankImage.setOnClickListener {
//            Toast.makeText(this, "이달의 순위!", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, RankActivity::class.java))
//        }

        MobileAds.initialize(this, "ca-app-pub-3578188838033823~7759370018")
        binding.adView.run {
            loadAd(AdRequest.Builder().build())
        }
    }

    override fun onResume() {
        super.onResume()
        updateGold()
        updateStage()
    }

    fun updateGold() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        binding.gold.text = pref.getInt("gold", 200).toString()
    }

    private fun updateStage() {
        var pref = getSharedPreferences("quiz", MODE_PRIVATE)
        val stage = pref.getInt("stage", 1)
        binding.stage.text = "최고단계: $stage"
    }
}