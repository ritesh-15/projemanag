package com.example.projemanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.databinding.ItemCardBinding
import com.example.projemanag.models.Board
import com.example.projemanag.models.Card


class CardListItemAdapter(private val context: Context,
                          private val list:ArrayList<Card>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener? = null

    interface OnClickListener{
        fun onClick(position:Int,model: Board)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardListItemAdapter.MyViewHolder(
            ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.binding.tvCardName.text = model.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: CardListItemAdapter.OnClickListener){
        this.onClickListener = onClickListener
    }

    class MyViewHolder(binding:ItemCardBinding)
        :RecyclerView.ViewHolder(binding.root){
        val binding = binding
    }
}