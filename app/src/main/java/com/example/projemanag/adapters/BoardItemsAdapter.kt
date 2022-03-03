package com.example.projemanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.databinding.ItemBoardBinding
import com.example.projemanag.models.Board

open class BoardItemsAdapter(private val context: Context,
                        private val list:ArrayList<Board>
                        ):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.boardImageView)

            holder.tvBoardCreatedBy.text = "Created by ${model.createdBy}"
            holder.tvBoardNameView.text = model.name

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener?.onClick(position,model)
                }
            }
        }

    }

    interface OnClickListener{
        fun onClick(position:Int,model:Board)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener = onClickListener
    }

    private class MyViewHolder(binding:ItemBoardBinding):RecyclerView.ViewHolder(binding.root){
        val tvBoardNameView = binding.tvBoardItemName
        val tvBoardCreatedBy = binding.tvBoardItemCreatedBy
        val boardImageView = binding.boardImage
    }

}