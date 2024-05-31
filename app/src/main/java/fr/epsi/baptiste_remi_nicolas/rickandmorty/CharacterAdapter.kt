package fr.epsi.baptiste_remi_nicolas.rickandmorty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CharacterAdapter(private val characterList: MutableList<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    // Interface pour gérer les clics sur les personnages
    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }

    private var characterClickListener: OnCharacterClickListener? = null

    // Méthode pour définir le listener
    fun setOnCharacterClickListener(listener: CharactersListActivity) {
        this.characterClickListener = listener
    }

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val characterName: TextView = itemView.findViewById(R.id.characterName)

        init {
            // Ajout du ClickListener à l'initialisation
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    characterClickListener?.onCharacterClick(characterList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = characterList[position]
        holder.characterName.text = currentItem.name
    }

    override fun getItemCount() = characterList.size

    fun updateCharacters(newCharacters: List<Character>) {
        characterList.clear()
        characterList.addAll(newCharacters)
        notifyDataSetChanged()
    }
}

