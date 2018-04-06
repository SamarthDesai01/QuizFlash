package saberman.perfectcorner.com.quizflash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class LoginScreen : AppCompatActivity() {

    val MY_PERMISSIONS_REQUEST_INTERNET = 0

    fun getQuizlet() {
        val loginButton:Button = findViewById<Button>(R.id.enterUserButton)
        loginButton.setOnClickListener {
            val userName = findViewById<TextView>(R.id.quizletUserName)

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "https://api.quizlet.com/2.0/users/${userName.text}?client_id=${BuildConfig.QuizletSecAPIKEY}"

            // Request a string response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        val obj: JSONArray = response.getJSONArray("sets")
                        val id = JSONObject(obj[0].toString()).get("id")

                        val setURL = "https://api.quizlet.com/2.0/sets/$id?client_id=${BuildConfig.QuizletSecAPIKEY}"

                        val flashRequest = JsonObjectRequest(Request.Method.GET, setURL, null,
                                Response.Listener { res ->
                                    var flashMap = HashMap<String, String>()
                                    val flashcards: JSONArray = res.getJSONArray("terms")
                                    for (i in 0 until flashcards.length()) {
                                        val flash: JSONObject = flashcards.getJSONObject(i)
                                        flashMap[flash.getString("term")] = flash.getString("definition")
                                    }
                                    Toast.makeText(this@LoginScreen, "Welcome " + userName.text + "!", Toast.LENGTH_SHORT).show()

                                    val intent = Intent(this, flashCardSelect::class.java) //create intent to go to the next activity
                                    intent.putExtra("flashMap", flashMap)

                                    startActivity(intent)
                                },
                                Response.ErrorListener {
                                    Toast.makeText(this@LoginScreen, "That didn't work!", Toast.LENGTH_SHORT).show()

                                })
                        queue.add(flashRequest)
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@LoginScreen, "That didn't work!", Toast.LENGTH_SHORT).show()
                        //TODO: REMOVE ONCE CRASH ERROR IS FIXED, SIMPLY TO TEST NEXT SCREEN
                        val tempIntent = Intent(this, flashCardSelect::class.java)
                        //Hardcoded map set to test next screen
                        var tempMap =  HashMap<String,String>()
                        tempMap.put("Keep", "it up!")
                        tempMap.put("Nice",  "work!")
                        tempMap.put("Samarth", "Desai")
                        tempMap.put("12", "13")
                        tempMap.put("14", "15")

                        tempIntent.putExtra("tempMap", tempMap)

                        startActivity(tempIntent)
                    })

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.INTERNET)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.INTERNET),
                        MY_PERMISSIONS_REQUEST_INTERNET)

            }
        } else {
            // Permission has already been granted
            getQuizlet()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_INTERNET -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getQuizlet()
                } else {
                    Toast.makeText(this@LoginScreen, "Couldn't get permissions!" , Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }


    }

}
