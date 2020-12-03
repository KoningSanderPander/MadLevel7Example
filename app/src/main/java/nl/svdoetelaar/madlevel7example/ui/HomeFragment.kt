package nl.svdoetelaar.madlevel7example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import nl.svdoetelaar.madlevel7example.R
import nl.svdoetelaar.madlevel7example.viewmodels.QuizViewModel

class HomeFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getQuiz()

        btCreateQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createQuizFragment)
        }

        viewModel.quiz.observe(viewLifecycleOwner, {
            if (it.answer.isNotBlank()) {
                btStartQuiz.alpha = 1.0f
                btStartQuiz.isClickable = true

                btStartQuiz.setOnClickListener {
                    findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
                }
            }
        })

    }
}
