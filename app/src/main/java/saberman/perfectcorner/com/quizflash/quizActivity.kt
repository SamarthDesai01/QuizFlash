package saberman.perfectcorner.com.quizflash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.app_bar_flash_card_select.*
import java.io.Serializable
import java.util.*

class quizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)


        //copy of intent code to get a map of flashcards
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

        var usedKeys: ArrayList<String> = ArrayList<String>() //List containing the keys for all correctly answered sets

        createQuestion(map, usedKeys)



    }

    fun createQuestion(flashCards: HashMap<String, String>, usedKeys: ArrayList<String>){
        var questionDisplay = findViewById<TextView>(R.id.questionText)

        var optionOne = findViewById<RadioButton>(R.id.answerChoice1)
        var optionTwo = findViewById<RadioButton>(R.id.answerChoice2)
        var optionThree = findViewById<RadioButton>(R.id.answerChoice3)
        var optionFour = findViewById<RadioButton>(R.id.answerChoice4)
        var optionGroup = findViewById<RadioGroup>(R.id.answerChoiceGroup)


        var subButton = findViewById<Button>(R.id.submitButton)


        var questionSet:MutableSet<String> = flashCards.keys
        var questionList: ArrayList<String> = ArrayList<String>(questionSet)

        //pick a random question
        var randIndex = (Math.random() * questionList.size).toInt()

        var questionKey = questionList.get(randIndex)

        while(usedKeys.contains(questionKey)){
            randIndex = (Math.random() * questionList.size).toInt()
            questionKey = questionList.get(randIndex)
        }

        //Add this as a used keys
        //usedKeys.add(questionKey)



        //populate the radio buttons
        var tempAnswers:HashSet<String?> = HashSet<String?>()


        tempAnswers.add(flashCards.get(questionKey))


        //continue to pick random keys and add them to the possible answers list
        while(tempAnswers.size < 4 ){
            randIndex = (Math.random() * questionList.size).toInt()
            tempAnswers.add(flashCards.get(questionList.get(randIndex)))
        }

        //Convert the set to a list so that random indexes can be selected for populating the radio buttons
        var tempAnswersList:ArrayList<String> = ArrayList<String>(tempAnswers)
        randIndex =  (Math.random() * tempAnswersList.size).toInt()

        //Code to set each radio button to a random answer
        optionOne.text = tempAnswersList.get(randIndex)
        tempAnswersList.remove(tempAnswersList.get(randIndex))

        randIndex = (Math.random() * tempAnswersList.size).toInt()
        optionTwo.text = tempAnswersList.get(randIndex)
        tempAnswersList.remove(tempAnswersList.get(randIndex))

        randIndex = (Math.random() * tempAnswersList.size).toInt()
        optionThree.text = tempAnswersList.get(randIndex)
        tempAnswersList.remove(tempAnswersList.get(randIndex))

        optionFour.text = tempAnswersList.get(0)

        //Display the current key as the question text
        questionDisplay.text = questionKey;

        //Create a handler to call create question every 10 seconds
        val mHandler = Handler()
        mHandler.postDelayed({
            createQuestion(flashCards, usedKeys)
        }, 10000L)

        //Code to display a countdown timer
        val timeDisplay = findViewById<TextView>(R.id.timerDisplay)
        timeDisplay.text = 10.toString()
        val handler = Handler()
        var count = 10
        val runnable = object : Runnable {
            override fun run() {

                timeDisplay.text = count.toString()

                if (count-- > 0)
                    handler.postDelayed(this, 1000)

            }
        }
        // trigger first time
        handler.post(runnable)
        //Code for Submit Button
        subButton.setOnClickListener(){
            val selectedButton = findViewById<RadioButton>(optionGroup.checkedRadioButtonId)
            if(selectedButton.text.equals(flashCards[questionKey])){
                //Toast.makeText(this@quizActivity, "Correct!", Toast.LENGTH_SHORT).show()
                usedKeys.add(questionKey) //Answered question correctly, add the current key to usedKeys
                Toast.makeText(this@quizActivity, "C", Toast.LENGTH_LONG).show()
            }else{
                //Toast.makeText(this@quizActivity, "Wrong! Correct Answer: " + correctAnswer + "", Toast.LENGTH_SHORT ).show()
                Toast.makeText(this@quizActivity, "W " + questionKey + " " + flashCards[questionKey], Toast.LENGTH_LONG).show()
            }
            if (usedKeys.size != flashCards.size) { //Check to see if all keys have been exhausted
                createQuestion(flashCards, usedKeys)
            } else { //All questions answered correctly, go back to the flash card select screen
                usedKeys.clear()
                Toast.makeText(this@quizActivity, "Twue", Toast.LENGTH_SHORT).show()
                val intentBack = Intent(this, flashCardSelect::class.java) //create intent to go to flashcard info
                intentBack.putExtra("flashMap", flashCards)
                startActivity(intentBack)
            }


        }




    }


}
