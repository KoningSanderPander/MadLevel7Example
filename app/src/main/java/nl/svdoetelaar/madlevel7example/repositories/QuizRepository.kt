package nl.svdoetelaar.madlevel7example.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import nl.svdoetelaar.madlevel7example.models.Quiz

class QuizRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizDocument = firestore.collection("quizzes")
        .document("quiz")

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()
    val quiz: LiveData<Quiz> get() = _quiz

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createSuccess: LiveData<Boolean> get() = _createSuccess

    suspend fun getQuiz() {
        try {
            withTimeout(5_000) {
                val data = quizDocument.get().await()

                _quiz.value = Quiz(
                    data.getString("question").toString(),
                    data.getString("answer").toString()
                )
            }
        } catch (e: Exception) {
            throw QuizRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun createQuiz() {
        try {
            withTimeout(5_000) {
                quizDocument.set(quiz).await()

                _createSuccess.value = true
            }
        } catch (e: Exception) {
            throw QuizCreateError(e.message.toString(), e)
        }
    }

    class QuizRetrievalError(message: String) : Exception(message)
    class QuizCreateError(message: String, cause: Throwable) : Exception(message, cause)
}