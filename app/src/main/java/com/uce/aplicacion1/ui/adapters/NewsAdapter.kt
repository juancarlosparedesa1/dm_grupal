import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.uce.aplicacion1.R
import com.uce.aplicacion1.ui.entites.NewsDataUI
import com.uce.aplicacion1.databinding.ItemTopNewsBinding
import com.uce.aplicacion1.ui.adapters.NewsDataDiffCallback

class NewsAdapter(
    private val onClickItem: (NewsDataUI) -> Unit,
    private val onDeleteItem: (Int) -> Unit,
    private val onInsertItem: () -> Unit
) : ListAdapter<NewsDataUI, NewsAdapter.NewsViewHolder>(NewsDataDiffCallback()) {

    inner class NewsViewHolder(val binding: ItemTopNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsDataUI, onClickItem: (NewsDataUI) -> Unit, onDeleteItem: (Int) -> Unit) {
            binding.txtTitleNews.text = data.name
            binding.txtUrlNews.text = data.url
            binding.txtDescNews.text = data.description
            binding.imgNews.load(data.image) {
                placeholder(R.drawable.clarin)
            }
            binding.root.setOnClickListener {
                onClickItem(data)
            }
            binding.btnDelete.setOnClickListener {
                onDeleteItem(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemTopNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItem, onDeleteItem)
    }
}
