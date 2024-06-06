package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class CharacterAdapter(private val characterList: MutableList<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var listener: OnCharacterClickListener? = null

    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }

    fun setOnCharacterClickListener(listener: OnCharacterClickListener) {
        this.listener = listener
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val characterImage: ImageView = itemView.findViewById(R.id.characterImage)
        val characterName: TextView = itemView.findViewById(R.id.characterName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = characterList[position]
        holder.characterName.text = currentItem.name
        holder.characterImage.load(currentItem.image) {
        }

        holder.itemView.setOnClickListener {
            listener?.onCharacterClick(currentItem)
        }
    }

    override fun getItemCount() = characterList.size

    fun updateCharacters(newCharacters: List<Character>) {
        characterList.clear()
        characterList.addAll(newCharacters)
        notifyDataSetChanged()
    }
}

