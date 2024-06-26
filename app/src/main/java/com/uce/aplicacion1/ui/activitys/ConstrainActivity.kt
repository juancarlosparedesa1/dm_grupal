
package com.uce.aplicacion1.ui.activitys
import NewsAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.uce.aplicacion1.R
import com.uce.aplicacion1.databinding.ActivityConstrainBinding
import com.uce.aplicacion1.logic.usercase.GetAllTopsNewUserCase
import com.uce.aplicacion1.logic.usercase.GetOneTopNewUserCase
import com.uce.aplicacion1.ui.entites.NewsDataUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConstrainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConstrainBinding
    private var items: MutableList<NewsDataUI> = mutableListOf()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstrainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnInsert.setOnClickListener {
            addItem()
        }

        initVariables()
        initListeners()
        initData()
        initBottom()

    }

    private fun initVariables() {
        newsAdapter = NewsAdapter(
            { descriptionItem(it) },
            { deleteItem(it) },
            { addItem() })

        binding.rvTopNews.adapter = newsAdapter
        binding.rvTopNews.layoutManager = CarouselLayoutManager()
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                deleteItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTopNews)
    }

    private fun initListeners() {


        binding.refreshRV.setOnRefreshListener {
            initData()
            binding.refreshRV.isRefreshing = false
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.listarItem -> {
                    startActivity(Intent(this, DetailItemActivity::class.java))
                    true
                }
                R.id.favItem -> {
                    Snackbar.make(binding.refreshRV, "Item Favoritos", Snackbar.LENGTH_LONG).show()
                    true
                }
                R.id.noFavItem -> {
                    Snackbar.make(binding.refreshRV, "Item no Fap", Snackbar.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun initData() {
        binding.pgbarLoadData.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val result = GetAllTopsNewUserCase().invoke()
            withContext(Dispatchers.Main) {
                binding.pgbarLoadData.visibility = View.INVISIBLE
                result.onSuccess {
                    items = it.toMutableList()
                    newsAdapter.submitList(items) // Corrección aquí
                }
                result.onFailure {
                    Snackbar.make(binding.refreshRV, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun descriptionItem(news: NewsDataUI) {
        Snackbar.make(binding.refreshRV, news.name, Snackbar.LENGTH_LONG).show()
    }

    private fun deleteItem(position: Int) {
        Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
        items.removeAt(position)
        newsAdapter.submitList(items) // Corrección aquí
    }

    private fun showBottomSheet() {
        val bottomSheet = findViewById<FrameLayout>(R.id.favItem)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initBottom(){

        class ModalBottomSheet : BottomSheetDialogFragment() {

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? = inflater.inflate(R.layout.activity_constrain, container, false)

        }

        val standardBottomSheet = findViewById<FrameLayout>(R.id.standard_bottom_sheet)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)

        binding.standardBottomSheet.setOnClickListener {

            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.txtNombre.text = "Juanito"
            binding.txtClave.text = "123456"
            standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
        }
    }


    private fun addItem() {
        binding.pgbarLoadData.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val addNew = GetOneTopNewUserCase().invoke()
            withContext(Dispatchers.Main) {
                binding.pgbarLoadData.visibility = View.INVISIBLE
                addNew.onSuccess {
                    items.add(it)
                    newsAdapter.submitList(items) // Corrección aquí
                }
                addNew.onFailure {
                    Snackbar.make(binding.refreshRV, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}
