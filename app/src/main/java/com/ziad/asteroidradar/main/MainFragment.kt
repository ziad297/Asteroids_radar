package com.ziad.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ziad.asteroidradar.R
import com.ziad.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = mainViewModel
        val astroAdapter=AsteroidsAdapter(AsteroidsAdapter.OnClickListener{
            mainViewModel.astroDetails(it)
        })
        binding.asteroidRecycler.adapter=astroAdapter
        mainViewModel.filterMutableLiveData.observe(viewLifecycleOwner) {
            if (it == Filter.TODAY) {
                mainViewModel.AstroToday.observe(viewLifecycleOwner) { asteroid ->
                    asteroid.apply {
                        astroAdapter.submitList(this)
                    }
                }
            } else {
                mainViewModel.Astro.observe(viewLifecycleOwner) { asteroid ->
                    asteroid.apply {
                        astroAdapter.submitList(this)
                    }
                }
            }

        }


        mainViewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                mainViewModel.displayAstroDetailsCompleted()
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.filterMutableLiveData.value = when(item.title){
            "View week asteroids"-> Filter.ALL
            "View today asteroids"-> Filter.TODAY
            "View saved asteroids"-> Filter.SAVED
            else ->{
                Filter.ALL
            }
        }

        return true
    }
}






