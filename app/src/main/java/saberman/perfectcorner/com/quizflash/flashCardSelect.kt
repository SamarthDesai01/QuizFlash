package saberman.perfectcorner.com.quizflash

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_flash_card_select.*
import kotlinx.android.synthetic.main.app_bar_flash_card_select.*
import saberman.perfectcorner.com.quizflash.R.layout.activity_quiz
import java.io.Serializable

class flashCardSelect : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card_select)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val flashMap: Serializable = intent.getSerializableExtra("flashMap")
        // Convert Serializable to HashMap
        var stringMap = flashMap.toString()
        stringMap = stringMap.substring(1, stringMap.length - 1)
        val terms: List<String> = stringMap.split(", ")
        val map: HashMap<String, String> = HashMap<String, String>()
        for (str in terms) {
            var flash: List<String> = str.split("=")
            map.put(flash[0], flash[1])
        }
        outputText.text = map.toString()


        //Code to take us to the quiz activity
        val toQuizButton = findViewById<Button>(R.id.toQuiz)
        toQuizButton.setOnClickListener{
            val toQuizIntent = Intent(this, quizActivity::class.java)
            toQuizIntent.putExtra("flashMap", intent.getSerializableExtra("flashMap"))
            startActivity(toQuizIntent)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.flash_card_select, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val intent = Intent(this, settingsActivity::class.java)
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
