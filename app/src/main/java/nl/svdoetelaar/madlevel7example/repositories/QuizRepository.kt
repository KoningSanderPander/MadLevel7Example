package nl.svdoetelaar.madlevel7example.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import nl.svdoetelaar.madlevel7example.R
import nl.svdoetelaar.madlevel7example.models.Quiz

class QuizRepository(private val context: Context) {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizDocument =
        firestore.collection("quizzes").document("quiz")

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()
    val quiz: LiveData<Quiz> get() = _quiz

    //the CreateQuizFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createSuccess: LiveData<Boolean> get() = _createSuccess

    suspend fun getQuiz() {
        try {
            withTimeout(5_000) {
                val data = quizDocument
                    .get()
                    .await()

                val question = data.getString("question").toString()
                val answer = data.getString("answer").toString()

                _quiz.value = Quiz(question, answer)
            }
        } catch (e: Exception) {
            throw QuizRetrievalError(context.getString(R.string.get_quiz_exception_message))
        }
    }

    suspend fun createQuiz(quiz: Quiz) {
        try {
            withTimeout(5_000) {
                quizDocument
                    .set(quiz)
                    .await()

                _createSuccess.value = true
            }
        } catch (e: Exception) {
            throw QuizCreateError(e.message.toString(), e)
        }
    }

    class QuizRetrievalError(message: String) : Exception(message)
    class QuizCreateError(message: String, cause: Throwable) : Exception(message, cause)
}