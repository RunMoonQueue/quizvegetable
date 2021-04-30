package com.moon.quizvegetable

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.moon.quizvegetable.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val otherAdapter = OtherAdapter(this, arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        FirebaseDatabase.getInstance().reference.child("Icon").addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.i("MQ!","onCancelled:$error")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = arrayListOf<Other>()
                        for (snap in snapshot.children) {
                            val info = snap.getValue(Other::class.java)
                            Log.i("MQ!", "info:$info")
                            list.add(info!!)
                        }
                        Log.i("MQ!", "list:$list")
                        otherAdapter.setItems(list)
                    }
                }
        )
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



class OtherAdapter(private val context: Context, private val items: List<Other>?) :
        RecyclerView.Adapter<OtherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.other_item_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items == null) {
            return
        }
        items[position].run {
            holder.title.text = title
            Glide.with(context).load(this.imageUrl)
                    .into(holder.thumbnail)
            holder.title.setOnClickListener {
                ContextCompat.startActivity(
                        context,
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                        ), null
                )
            }
            holder.thumbnail.setOnClickListener {
                ContextCompat.startActivity(
                        context,
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                        ), null
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    fun setItems(list: List<Other>) {
        (items as ArrayList).run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var title: TextView = view.findViewById(R.id.title)
    }
}

data class Other(
        val imageUrl: String = "",
        val title: String = "",
        val packageName: String = ""
)