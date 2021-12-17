package com.bittahlover.lab4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bittahlover.lab4.database.MyDbManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.android.synthetic.main.activity_play.*

var currentPlayer: Player? = null
val players: ArrayList<Player> = ArrayList()

class MainActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(players.size == 0)
        {
            myDbManager.openDb()
            val dataList = myDbManager.readDbData()
            if(dataList.isEmpty())
            {
                myDbManager.insert("Max","Stalker228","10:10:10")
                dataList.add(Player("Max","Stalker228","10:10:10"))
                myDbManager.insert("Ilya","Bloom123","00:10:10")
                dataList.add(Player("Ilya","Bloom123","00:10:10"))
                myDbManager.insert("Oleg","BloodSicker","00:00:10")
                dataList.add(Player("Oleg","BloodSicker","00:00:10"))
            }
            for(item in dataList)
            {
                players.add(item)
            }
            myDbManager.closeDb()
        }

        btnPlay.setOnClickListener {
            if (currentPlayer != null)
            {
                btnLog.setText(R.string.btn_log_in)
                btnLog.setOnClickListener {
                    logInDialog()
                }
                val intent = Intent(this@MainActivity, PlayActivity::class.java)
                startActivity(intent)
            }
        }
        btnRecords.setOnClickListener {
            val intent = Intent(this@MainActivity, RecordsActivity::class.java)
            startActivity(intent)
        }
        btnLog.setOnClickListener {
            logInDialog()
        }
    }
    private fun logInDialog()
    {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.log_in_layot, null)
        builder.setView(view)
        builder.setPositiveButton("OK"
        ) { dialog, id ->
            logInPlayer(view)
            dialog.cancel()
        }
        builder.create()
        builder.show()
    }

    private fun logInPlayer(view: View)
    {
        val realName: String = (view.findViewById(R.id.editRealName) as EditText).text.toString()
        val userName: String = (view.findViewById(R.id.editUserName) as EditText).text.toString()
        if (players.indexOfFirst{it.userName == userName} == -1)
        {
            currentPlayer = Player(realName,userName,"00:00:00")
            btnLog.setText(R.string.btn_log_out)
            btnLog.setOnClickListener {
                logOutPlayer()
            }
        }
        else
        {
            Toast.makeText(applicationContext, R.string.user_name_exist, Toast.LENGTH_SHORT).show()
        }
    }
    private fun logOutPlayer()
    {
        currentPlayer = null
        btnLog.setText(R.string.btn_log_in)
        btnLog.setOnClickListener {
            logInDialog()
        }
    }
}