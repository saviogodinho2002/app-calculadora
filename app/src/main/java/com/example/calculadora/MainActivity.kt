package com.example.calculadora

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView;
    lateinit var editExpression:EditText;
    lateinit var editResult:EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        val itemList = mutableListOf<ButtonItem>();



        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.clean_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                 R.string.op_parentheses_symbol,
                 null,
                 Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.cl_parentheses_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.equal_symbol,
                null,
                Color.BLUE

            )
        )

        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.seven_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.eight_symbol,
                null,
                Color.BLUE

            )
        )

        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.nine_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.plus_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.four_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.five_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.six_symbol,
                null,
                Color.BLUE

            )
        )

        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.sub_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.one_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.two_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.three_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.mul_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.point_symbol,
                null,
                Color.BLUE

            )
        )

        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.zero_symbol,
                null,
                Color.BLUE

            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.backspace_symbol,
                R.drawable.ic_baseline_backspace_24,
                Color.BLUE
            )
        )
        itemList.add(
            ButtonItem(
                itemList.size,
                R.string.div_symbol,
                null,
                Color.BLUE
            )
        )
        val adapter = ButtonsAdapter(itemList){id,stringRes->
            ///agora vai
            when(id){
                0 -> clean();
                3 -> equal();
                18 -> backSpace();
                else -> write(getString(stringRes))

            }
        };

        recyclerView = findViewById(R.id.recycler_buttons);
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = GridLayoutManager(this,4);


        editExpression = findViewById(R.id.edit_expression);
        editResult = findViewById(R.id.edit_result);

        editExpression.doAfterTextChanged {
            val expression = it.toString()
            var regex = "[\\d]*\\.[\\d.]*\\.".toRegex();
            if(regex.containsMatchIn(expression)){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "(([-+][0]{2,})|(^[0]{2,}))".toRegex();
            if(regex.containsMatchIn(expression)){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "([/*]{2,})".toRegex();
            if(regex.containsMatchIn(expression)){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "[+-]{1}[*\\/]".toRegex();
            if(regex.containsMatchIn(it.toString())){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "(^|[\\(]+)[*\\/]".toRegex();
            if(regex.containsMatchIn(it.toString())){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "[\\d.]\\(".toRegex();
            if(regex.containsMatchIn(it.toString())){
                backSpace();
                return@doAfterTextChanged;
            }
            regex = "\\(".toRegex();
            val tempRegex = "\\)".toRegex();
            if( regex.findAll(it.toString()).count() < tempRegex.findAll(it.toString()).count() ){
                backSpace();
                return@doAfterTextChanged;
            }
            solveExpression();
        }

    }
    private fun equal(){
        solveExpression();
        editExpression.text = editResult.text;
    }
    private fun backSpace(){
        if(editExpression.text.toString() != "")
            editExpression.text = (editExpression.text.delete(editExpression.text.length-1,editExpression.text.length))
    }
    private fun write(text: String){
        editExpression.setText("${editExpression.text}${text}");

    }
    private fun solveExpression(){ //botão = ou cada digitada
        try{
            var expression = editExpression.text.toString();
            editResult.setText(MathExpression.getResult(expression).toString())
        }catch (e:Exception){

        }
    }
    private fun clean(){
        editExpression.setText("");
        editResult.setText("");
    }

    private inner class ButtonsAdapter(
        private val buttonsList:List<ButtonItem>,
        private val onClick:(Int,Int)->Unit,
        ):RecyclerView.Adapter<ButtonsAdapter.ButtonViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
            val view = layoutInflater.inflate(R.layout.item_btn_keyboard,parent,false)
            return ButtonViewHolder(view);

        }

        override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
            val currentItem = buttonsList[position];
            holder.bind(currentItem)

        }

        override fun getItemCount(): Int {
           return  buttonsList.size;

        }
        private inner class ButtonViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            fun bind(item:ButtonItem){
                val container:LinearLayout = itemView.findViewById(R.id.layout_item);
                val text:TextView = itemView.findViewById(R.id.txt_item);
                val img:ImageView = itemView.findViewById(R.id.img_item);

                container.setBackgroundColor(item.colorId);

                text.setText(item.symbolText)
                text.visibility = View.VISIBLE

                item.iconDrawable?.let {
                    img.setImageResource(item.iconDrawable!!)
                    img.visibility = View.VISIBLE;
                    text.visibility = View.GONE
                }


                container.setOnClickListener{

                    onClick.invoke(item.itemID,item.symbolText)

                }

            }
        }

    }


}