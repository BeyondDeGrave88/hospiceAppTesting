package ru.iteco.fmhandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.adapter.NewsListAdapter
import ru.iteco.fmhandroid.databinding.FragmentMainBinding
import ru.iteco.fmhandroid.utils.Utils
import ru.iteco.fmhandroid.viewmodel.AuthViewModel
import ru.iteco.fmhandroid.viewmodel.NewsViewModel

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenCreated {
            newsViewModel.onRefresh()
        }

        lifecycleScope.launchWhenResumed {
            newsViewModel.loadNewsExceptionEvent.collect {
                showErrorToast(R.string.error)
            }
        }
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)

        val mainMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentMain.mainMenuImageButton
        )
        mainMenu.inflate(R.menu.menu_main)
        val menuItemMain = mainMenu.menu.getItem(0)
        menuItemMain.isEnabled = false
        binding.containerCustomAppBarIncludeOnFragmentMain.mainMenuImageButton.setOnClickListener {
            mainMenu.show()
        }
        mainMenu.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.menu_item_news -> {
                    findNavController().navigate(R.id.action_mainFragment_to_newsListFragment)
                    true
                }

                R.id.menu_item_about -> {
                    findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
                    true
                }

                else -> false
            }
        }

        val authorizationMenu = PopupMenu(
            context,
            binding.containerCustomAppBarIncludeOnFragmentMain.authorizationImageButton
        )
        authorizationMenu.inflate(R.menu.authorization)

        binding.containerCustomAppBarIncludeOnFragmentMain.authorizationImageButton.setOnClickListener {
            authorizationMenu.show()
        }

        binding.containerCustomAppBarIncludeOnFragmentMain.ourMissionImageButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_our_mission_fragment)
        }

        authorizationMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.authorization_logout_menu_item -> {
                    authViewModel.logOut()
                    findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                    true
                }

                else -> false
            }
        }

        binding.containerListNewsIncludeOnFragmentMain.apply {
            sortNewsMaterialButton.visibility = View.GONE
            filterNewsMaterialButton.visibility = View.GONE
            editNewsMaterialButton.visibility = View.GONE

            expandMaterialButton.setOnClickListener {
                when (allNewsTextView.visibility) {
                    View.GONE -> {
                        allNewsTextView.visibility = View.VISIBLE
                        allNewsCardsBlockConstraintLayout.visibility = View.VISIBLE
                        expandMaterialButton.setIconResource(R.drawable.expand_less_24)
                    }

                    else -> {
                        allNewsTextView.visibility = View.GONE
                        allNewsCardsBlockConstraintLayout.visibility = View.GONE
                        expandMaterialButton.setIconResource(R.drawable.expand_more_24)
                    }
                }
            }

            allNewsTextView.setOnClickListener {
                if (Utils.isOnline(requireContext())) {
                    findNavController().navigate(R.id.action_mainFragment_to_newsListFragment)
                } else {
                    showErrorToast(R.string.error)
                }
            }
        }

        val newsListAdapter = NewsListAdapter(newsViewModel)
        binding.containerListNewsIncludeOnFragmentMain.newsListRecyclerView.adapter =
            newsListAdapter
        lifecycleScope.launchWhenCreated {
            newsViewModel.data.collectLatest {
                newsListAdapter.submitList(it.take(3))
            }
        }

    }

    private fun showErrorToast(text: Int) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}
