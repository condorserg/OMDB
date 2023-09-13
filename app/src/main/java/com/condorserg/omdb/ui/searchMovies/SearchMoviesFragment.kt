package com.condorserg.omdb.ui.searchMovies

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.condorserg.omdb.checkRadioButtonFlow
import com.condorserg.omdb.databinding.FragmentMoviesBinding
import com.condorserg.omdb.adapter.MovieListAdapter
import com.condorserg.omdb.textChangedFlow

class SearchMoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val viewModel: MoviesViewModel by viewModels()
    var movieAdapter: MovieListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        bindViewModel()
    }

    private fun initList() {
        movieAdapter = MovieListAdapter()
        with(binding.movieList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        val movieTypeFlow = binding.radioGroup.checkRadioButtonFlow()
        val queryFlow = binding.searchMoviesEditText.textChangedFlow()

        viewModel.movieTypeQueryFlow(movieTypeFlow, queryFlow)
        viewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
        viewModel.moviesList.observe(viewLifecycleOwner) { movieAdapter?.items = it}

    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.movieList.isVisible = isLoading.not()
        binding.progressBar.isVisible = isLoading
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
        viewModel.currentJobCanceller()
    }
}