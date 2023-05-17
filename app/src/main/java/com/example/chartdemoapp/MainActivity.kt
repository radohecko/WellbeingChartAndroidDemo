package com.example.chartdemoapp

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<ConstraintLayout>(R.id.fragment_one)

        val labels: List<String> = listOf<String>(
            "1 Jan\n2020",
            "12 Feb\n2020",
            "10 Mar\n2020",
            "2 Apr\n2020",
            "18 May\n2020",
            "21 Jun\n2020",
            "30 Jul\n2020",
            "22 Aug\n2020",
            "7 Sep\n2020",
            "9 Oct\n2020",
            "24 Nov\n2020",
            "5 Dec\n2020",
            "19 Jan\n2021",
            "8 Feb\n2021"
        )
        val data: List<Float> = listOf(0f, 100f, 20f, 80f, 50f, 90f, 40f, 55f, 20f, 90f, 60f, 80f, 40f, 50f)

        val data2: List<Float> = listOf(
            4.0f,
            5.0f,
            3.8f,
            2.5f,
            3.2f,
            4.0f,
            1.8f,
            3.4f,
            4.0f,
            5.0f,
            2.8f,
            2.5f,
            3.2f,
            2.4f
        )

        val isInterventionType = true;
        val isHowdyScore = false;

        var dataset = data;
        val chart = WellbeingChart()

        if (isHowdyScore) {
            dataset = data2;
        }

        if (isInterventionType) {
            val chartView = chart.getChart(this, labels.take(6), data.take(6), false, true, false, 3f, 4f, true, false, "#FFFFFF", true);

            val linearPrams = LinearLayout.LayoutParams(1040, 550)

            val layout1 = CardView(this)
            layout1.layoutParams = linearPrams
            layout1.setBackgroundColor(Color.parseColor("#45C1EB"))
            layout1.addView(chartView)


            root.addView(layout1)
        } else {
            val chartView = chart.getChart(this, labels, dataset, false, false, isHowdyScore, 3f, 4f, false, true, "#000000", false);
            val linearPrams = LinearLayout.LayoutParams(1040, 550)

            val layout1 = CardView(this)
            layout1.layoutParams = linearPrams
            layout1.setBackgroundColor(Color.parseColor("#FFFFFF"))
            layout1.addView(chartView)


            root.addView(layout1)
        }


    }
}