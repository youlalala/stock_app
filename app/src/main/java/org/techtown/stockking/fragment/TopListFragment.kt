package org.techtown.stockking.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.techtown.stockking.DetailActivity
import org.techtown.stockking.R
import org.techtown.stockking.adapter.RealtimeTopListAdapter
import org.techtown.stockking.adapter.TopListCapAdapter
import org.techtown.stockking.adapter.TopListChangeAdapter
import org.techtown.stockking.databinding.FragmentToplistBinding

import org.techtown.stockking.network.ApiWrapper
import android.graphics.Color

class TopListFragment : Fragment(){

    lateinit var binding: FragmentToplistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToplistBinding.inflate(inflater, container, false)

        binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
        binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
        binding.realtimeBtn.isSelected = true
        binding.updownBtn.isSelected = false
        binding.transactionBtn.isSelected = false


        ApiWrapper.getTopListRealtime() { it ->
            Log.i("la",it.toString())
            binding.recyclerView.adapter= RealtimeTopListAdapter(it,onClickItem = {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("ticker",it.title)
                intent.putExtra("percent",it.percent)
                intent.putExtra("price",it.price)
                startActivity(intent)
            })
        }

        binding.realtimeBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn1_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn1_detail)
            binding.realtimeBtn.isSelected = true
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = false
            binding.progressBar.visibility=View.VISIBLE
            ApiWrapper.getTopListRealtime() { it ->
                Log.i("la",it.toString())
                binding.recyclerView.adapter= RealtimeTopListAdapter(it,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.title)
                    intent.putExtra("percent",it.percent)
                    intent.putExtra("price",it.price)
                    startActivity(intent)
                })
            }

        }
        //change : 등락율
        binding.updownBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn2_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn2_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = true
            binding.transactionBtn.isSelected = false

            binding.exchangeGroup.visibility = View.GONE
            binding.sortGroup.visibility = View.VISIBLE

            binding.sortUp.isSelected=true
            binding.sortDown.isSelected=false

            binding.progressBar.visibility=View.VISIBLE
            getTopList("change","desc")
            binding.sortUp.setOnClickListener {
                binding.sortUp.isSelected=true
                binding.sortDown.isSelected=false
                binding.progressBar.visibility=View.VISIBLE
                getTopList("change","desc")
            }
            binding.sortDown.setOnClickListener {
                binding.sortUp.isSelected=false
                binding.sortDown.isSelected=true
                binding.progressBar.visibility=View.VISIBLE
                getTopList("change","asc")
            }
        }
        binding.transactionBtn.setOnClickListener {
            binding.buttonTitleTv.text = resources.getString(R.string.topList_btn3_title)
            binding.buttonDetailTv.text = resources.getString(R.string.topList_btn3_detail)
            binding.realtimeBtn.isSelected = false
            binding.updownBtn.isSelected = false
            binding.transactionBtn.isSelected = true

            binding.exchangeGroup.visibility = View.VISIBLE
            binding.sortGroup.visibility = View.GONE

            binding.progressBar.visibility=View.VISIBLE
            getTopList("cap","en")
            binding.exchangeDollar.isSelected=true
            binding.exchangeWon.isSelected=false

            binding.exchangeDollar.setOnClickListener{
                binding.exchangeDollar.isSelected=true
                binding.exchangeWon.isSelected=false
                binding.progressBar.visibility=View.VISIBLE
                getTopList("cap","en")
            }
            binding.exchangeWon.setOnClickListener {
                binding.exchangeDollar.isSelected=false
                binding.exchangeWon.isSelected=true
                binding.progressBar.visibility=View.VISIBLE
                getTopList("cap","kr")
            }
        }

        return binding.root
    }

    fun getTopList(filter1: String, filter2: String){
        if(filter1 == "change"){
            ApiWrapper.getTopListChange(filter2) { it ->
                binding.progressBar.visibility=View.GONE
                binding.recyclerView.adapter= TopListChangeAdapter(it,this,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.symbol)
                    startActivity(intent)
                })
            }
        }else if(filter1 == "cap"){
            ApiWrapper.getTopListCap(filter2){ it ->
                binding.progressBar.visibility=View.GONE
                binding.recyclerView.adapter= TopListCapAdapter(it,filter2,this,onClickItem = {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("ticker",it.symbol)
                    startActivity(intent)
                })

            }
        }
    }

}



