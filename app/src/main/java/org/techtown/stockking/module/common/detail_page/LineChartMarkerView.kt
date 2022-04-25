package org.techtown.stockking.module.common.detail_page

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import org.techtown.stockking.R
import org.techtown.stockking.databinding.ActivityDetailBinding

class LineChartMarkerView(
    context: Context,
    layout: Int,
    private val labels: ArrayList<String>,
    ): MarkerView(context, layout) {

    private val date: TextView = findViewById(R.id.date_tv)
    private val price: TextView = findViewById(R.id.price_tv)
    private val uiScreenWidth = resources.displayMetrics.widthPixels

//    override fun getOffset(): MPPointF {
//        return MPPointF(-(width / 2f), -height.toFloat()+120)
//    }
    override fun draw(canvas: Canvas, posx: Float, posy: Float) {
        // Check marker position and update offsets.
        var posx = posx
        if (uiScreenWidth - posx - width < width) {
            posx -= width.toFloat()
        }

        // translate to the correct position and draw
        canvas.translate(posx, posy)
        draw(canvas)
        canvas.translate(-posx, -posy)
    }


    // entry content의 텍스트에 지정
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val xIdx = e?.x?.toInt()
        date.text = labels[xIdx!!].toString()
        price.text = e?.y.toString()
        super.refreshContent(e, highlight)
    }
}


