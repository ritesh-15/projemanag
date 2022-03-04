package com.example.projemanag.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.R
import com.example.projemanag.activities.TaskListActivity
import com.example.projemanag.databinding.ItemBoardBinding
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task

open class TaskListItemAdapter(private val context: Context,
                               private val list:ArrayList<Task>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = ItemTaskBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)

        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins((15.toDp()).toPx(),
            0,
            (42.toDp()).toPx(),
            0
            )

        view.root.layoutParams = layoutParams

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            if(position == list.size - 1){

                holder.tvAddTaskList.visibility = View.VISIBLE
                holder.llTaskItem.visibility = View.GONE

            }else{

                holder.tvAddTaskList.visibility = View.GONE
                holder.llTaskItem.visibility = View.VISIBLE

            }

            holder.tvTaskListTitle.text = model.title

            holder.tvAddTaskList.setOnClickListener {
                holder.tvAddTaskList.visibility = View.GONE
                holder.cvAddTaskListName.visibility = View.VISIBLE
            }

            holder.ibCloseListName.setOnClickListener {
                holder.tvAddTaskList.visibility = View.VISIBLE
                holder.cvAddTaskListName.visibility = View.GONE
            }

            holder.ibCheckListName.setOnClickListener {

                val listName = holder.etTaskListName.text.toString()

                if(listName.isNotEmpty()){

                    if(context is TaskListActivity){
                        context.createTaskList(listName)
                    }

                }else{
                    Toast.makeText(context,"Please enter list name",Toast.LENGTH_SHORT).show()
                }

            }


            holder.ibEditListName.setOnClickListener {
                holder.binding.etTaskEditListName.setText(model.title)

                holder.binding.cvEditTaskListName.visibility = View.VISIBLE
                holder.binding.llTaskView.visibility = View.GONE


            }

            holder.binding.ibCloseEditableView.setOnClickListener {
                holder.binding.llTaskView.visibility = View.VISIBLE
                holder.binding.cvEditTaskListName.visibility = View.GONE
            }


            holder.binding.ibSaveTaskListName.setOnClickListener{
                 val listName = holder.binding.etTaskEditListName.text.toString()

                if(listName.isNotEmpty()){

                    if(context is TaskListActivity){
                        context.updateTaskList(position,listName,model)
                    }

                }else{
                    Toast.makeText(context,"Please enter list name",Toast.LENGTH_SHORT).show()
                }
            }

            holder.binding.ibTaskListDelete.setOnClickListener {
                if(context is TaskListActivity){
                    context.deleteTaskList(position)
                }
            }


            holder.binding.tvAddCard.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.GONE
                holder.binding.cvAddCard.visibility = View.VISIBLE
            }


            holder.binding.ibCloseCardName.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.VISIBLE
                holder.binding.cvAddCard.visibility = View.GONE
            }

            holder.binding.ibDoneCardName.setOnClickListener {
                val cardName = holder.binding.etCardName.text.toString()

                if(cardName.isNotEmpty()){

                    if(context is TaskListActivity){
                       context.addCardToTaskList(position,cardName)
                    }

                }else{
                    Toast.makeText(context,"Please enter card name",Toast.LENGTH_SHORT).show()
                }
            }



            holder.binding.rvCardList.layoutManager = LinearLayoutManager(context)
            holder.binding.rvCardList.setHasFixedSize(true)

            holder.binding.rvCardList.adapter = CardListItemAdapter(context,model.cardList)



        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp():Int = (this/Resources.getSystem()
        .displayMetrics.density).toInt()

    private fun Int.toPx():Int = (this * Resources.getSystem()
        .displayMetrics.density).toInt()

  inner class MyViewHolder(binding:ItemTaskBinding):RecyclerView.ViewHolder(binding.root){
        val tvAddTaskList = binding.tvAddTaskList
        val llTaskItem = binding.llTaskItem
        val tvTaskListTitle = binding.tvTaskListTitle
        val cvAddTaskListName = binding.cvAddTaskListName
        val ibCloseListName = binding.ibCloseListName
        val ibCheckListName = binding.ibCheckListName
        val etTaskListName = binding.etTaskListName
        val ibEditListName = binding.ibTaskListEdit

        val binding = binding
    }
}