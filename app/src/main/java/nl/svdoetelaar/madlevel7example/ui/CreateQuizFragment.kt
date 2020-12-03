package nl.svdoetelaar.madlevel7example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import nl.svdoetelaar.madlevel7example.R
import nl.svdoetelaar.madlevel7example.viewmodels.QuizViewModel


class CreateQuizFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeQuizCreation()

        btCreateQuizConfirmAnswer.setOnClickListener {
            viewModel.createQuiz(
                etCreateQuizQuestion.text.toString(),
                etCreateQuizAnswer.text.toString()
            )
        }

    }

    private fun observeQuizCreation() {
        viewModel.createSuccess.observe(viewLifecycleOwner, {
            Toast.makeText(activity, R.string.successfully_created_quiz, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }


}
