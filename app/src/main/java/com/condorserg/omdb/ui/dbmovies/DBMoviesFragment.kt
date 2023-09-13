package com.condorserg.omdb.ui.dbmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.condorserg.omdb.R
import com.condorserg.omdb.databinding.FragmentDbMoviesBinding
import com.condorserg.omdb.adapter.MovieListAdapter
import com.google.android.material.snackbar.Snackbar

class DBMoviesFragment : Fragment() {

    private var _binding: FragmentDbMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DbMoviesViewModel by viewModels()
    private var movieAdapter: MovieListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDbMoviesBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            getString(R.string.db_movies_fragment_title)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        Snackbar.make(
            requireView(),
            getString(R.string.snackbar_text),
            Snackbar.LENGTH_SHORT
        )
            .setAction(getString(R.string.snackbar_action_text)) {
                Snackbar.make(requireView(), "Список обновлен", Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.nav_view)
                    .show()
            }
            .setAnchorView(R.id.nav_view)
            .show()
    }

    private fun initList() {
        movieAdapter = MovieListAdapter()
        with(binding.movieListDB) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {

        viewModel.moviesDBList.observe(viewLifecycleOwner) { movieAdapter?.items = it }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
    }
}