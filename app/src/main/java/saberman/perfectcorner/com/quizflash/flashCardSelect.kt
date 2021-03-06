package saberman.perfectcorner.com.quizflash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.Serializable

class flashCardSelect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card_select)
        setSupportActionBar(findViewById(R.id.flashCardToolBar))


        val toQuizButton = findViewById<Button>(R.id.toQuiz)
        val outputText = findViewById<TextView>(R.id.termText)
        val extras = intent.extras

        //Final formatted map that contains all flashcards
        var map: HashMap<String, String> = HashMap<String, String>()

        //TODO: REMOVE EXTRAS CHECKING CODE, ONLY HERE TO TEST UNTIL CRASH ERROR IS FIXED
        //Checks which flashcards sets were passed in (Quizlet or Hardcoded) and reformats to a properly formatted map
        if(extras.containsKey("flashMap")){ //Check if quizlet data was successfully received
            val flashMap: Serializable = intent.getSerializableExtra("flashMap")
            // Convert Serializable to HashMap
            var stringMap = flashMap.toString()
            //Remove "{" and "}" from the string
            stringMap = stringMap.substring(1, stringMap.length - 1)
            //Isolate the terms from their definitions
            val terms: List<String> = stringMap.split(", ")

            //Reassociate the split terms and definitions back into a unified map
            for (str in terms) {
                var flash: List<String> = str.split("=")
                map.put(flash[0], flash[1])
            }
        } else{
            val tempMap: Serializable = intent.getSerializableExtra("tempMap")
            var tempString = tempMap.toString()

            val terms: List<String> = tempString.split(", ")

            //Reassociate the split terms and definitions back into a unified map
            for (str in terms) {
                var flash: List<String> = str.split("=")
                map.put(flash[0], flash[1])
            }
        }

        outputText.text = map.toString()

        //Code to take us to the quiz activity
        toQuizButton.setOnClickListener{
            val toQuizIntent = Intent(this, quizActivity::class.java)
            //TODO: CAN REMOVE EXTRAS CONTENT CHECKING CODE ONCE INITIAL CRASH IS FIXED
            if(extras.containsKey("flashMap")){
                toQuizIntent.putExtra("flashMap", intent.getSerializableExtra("flashMap"))
            } else{ //Pass in hardcoded set to quiz activity if quizlet set not present, named "flashMap" for next screen compatibility
                toQuizIntent.putExtra("flashMap", intent.getSerializableExtra("tempMap"))
            }
            startActivity(toQuizIntent)
        }



    }
}
