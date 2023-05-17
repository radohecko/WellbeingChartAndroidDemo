package com.example.chartdemoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class WellbeingChart {

    public Boolean whiteBackground = false;
    public Boolean hideAxisAndLabels = false;
    public Boolean isHowdyScoreType = false;
    public Boolean enableLeftAxis = false;
    public Boolean enableDataZones = true;
    public Boolean enableCustomMarker = false;
    public Float lineWidth = 3f;
    public Float circleRadius = 4f;
    public String lineColor = "#000000";

    public LineChart chart;

    public LineChart getChart(
            Context context,
            List<String> labels,
            List<Float> data,
            Boolean whiteBackground,
            Boolean hideAxisAndLabels,
            Boolean isHowdyScoreType,
            Float lineWidth,
            Float circleRadius,
            Boolean enableLeftAxis,
            Boolean enableDataZones,
            String lineColor,
            Boolean enableCustomMarker) {
        chart = new LineChart(context);
        setupChart(chart, context, labels, data, whiteBackground, hideAxisAndLabels, isHowdyScoreType, lineWidth, circleRadius, enableLeftAxis, enableDataZones, lineColor, enableCustomMarker);

        return chart;
    }

    private void setupChart(
            LineChart lineChart,
            Context context,
            List<String> labels,
            List<Float> data,
            Boolean whiteBackground,
            Boolean hideAxisAndLabels,
            Boolean isHowdyScoreType,
            Float lineWidth,
            Float circleRadius,
            Boolean enableLeftAxis,
            Boolean enableDataZones,
            String lineColor,
            Boolean enableCustomMarker) {

        this.whiteBackground = whiteBackground;
        this.hideAxisAndLabels = hideAxisAndLabels;
        this.isHowdyScoreType = isHowdyScoreType;
        this.lineWidth = lineWidth;
        this.circleRadius = circleRadius;
        this.enableLeftAxis = enableLeftAxis;
        this.enableDataZones = enableDataZones;
        this.lineColor = lineColor;
        this.enableCustomMarker = enableCustomMarker;

        int color = Color.parseColor(this.lineColor);

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {
            entries.add(new Entry(i, data.get(i)));
        }

        LineDataSet wellbeingDataSet = new LineDataSet(entries, "");

        wellbeingDataSet.setDrawValues(false);
        wellbeingDataSet.setLineWidth(this.lineWidth);
        wellbeingDataSet.setDrawFilled(false);
        wellbeingDataSet.setColor(color);
        wellbeingDataSet.setCircleColor(color);
        wellbeingDataSet.setCircleHoleColor(color);
        wellbeingDataSet.setCircleSize(this.circleRadius);
        wellbeingDataSet.setHighlightLineWidth(0f);
        wellbeingDataSet.setDrawHighlightIndicators(false);

        LineDataSet greenZoneDataSet =
                getZoneDataSet(context, entries, this.isHowdyScoreType ? 5f : 100f, R.color.green, R.drawable.green_gradient);
        LineDataSet yellowZoneDataSet =
                getZoneDataSet(context, entries, this.isHowdyScoreType ? 2f : 50f, R.color.yellow, this.isHowdyScoreType ? R.drawable.yellow2_gradient : R.drawable.yellow_gradient);
        LineDataSet redZoneDataSet =
                getZoneDataSet(context, entries, this.isHowdyScoreType ? 1f : 35f, R.color.red, R.drawable.red_gradient);

        LineData lineData = this.enableDataZones ?
                new LineData(greenZoneDataSet, yellowZoneDataSet, redZoneDataSet, wellbeingDataSet) :
                new LineData(wellbeingDataSet);

        lineData.setHighlightEnabled(this.enableCustomMarker);

        if (this.enableCustomMarker) {
            String colors[] = context.getResources().getStringArray(R.array.colors);
            ArrayList<Integer> colorList = new ArrayList<Integer>();

            for (int i = 0; i < colors.length; i++) {
                colorList.add(Color.parseColor(colors[i]));
            }

            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins_regular);

            lineChart.setRenderer(new MarkerChartRenderer(lineChart, lineChart.getAnimator(), lineChart.getViewPortHandler(), labels, colorList, typeface));
        }

        lineChart.setData(lineData);

        ValueFormatter formatter = new ValueFormatter() {
            public String getAxisLabel(float value, AxisBase axis) {
                return labels.get((int)value % labels.size());
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(this.hideAxisAndLabels ? false : true);
        xAxis.setDrawGridLines(this.hideAxisAndLabels ? false : true);
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setGridLineWidth(1.5f);
        xAxis.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_regular));
        xAxis.setTextSize(12);
        xAxis.setDrawLabels(this.hideAxisAndLabels ? false : true);

        lineChart.setExtraBottomOffset(xAxis.mLabelHeight);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(this.enableLeftAxis);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(this.isHowdyScoreType ? 5f : 100f);

        if (this.enableLeftAxis) {
            leftAxis.setDrawLabels(false);
            leftAxis.setGridColor(ColorUtil.getWhiteColorWithAlpha(0f));
            leftAxis.setAxisLineColor(ColorUtil.getWhiteColorWithAlpha(0f));
        }

        lineChart.getLegend().setEnabled(false);
        lineChart.setDescription(null);
        CustomXAxisRenderer customRenderer = new CustomXAxisRenderer(lineChart.getViewPortHandler(), xAxis, lineChart.getTransformer(YAxis.AxisDependency.LEFT));
        lineChart.setXAxisRenderer(customRenderer);

        float numberOfItemsIn6Months = 5f;
        float numberOfItemsToShow = Math.min(labels.size() - 1, numberOfItemsIn6Months);

        lineChart.setVisibleXRangeMaximum(numberOfItemsToShow);
        ViewPortHandler handler = lineChart.getViewPortHandler();
        handler.setMaximumScaleX((labels.size() - 1) / numberOfItemsToShow);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.moveViewToX(labels.size());
        lineChart.setDoubleTapToZoomEnabled(false);

        if (this.enableLeftAxis) {
            LimitLine limitLine = new LimitLine(50f);
            limitLine.setLineWidth(3f);
            limitLine.setLineColor(ColorUtil.getWhiteColorWithAlpha(0.4f));
            limitLine.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_regular));
            limitLine.enableDashedLine(20f, 12f, 0f);

            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(limitLine);
            leftAxis.setAxisMaximum(100f);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawLimitLinesBehindData(false);
        }

        lineChart.setExtraLeftOffset(20);
        lineChart.setExtraRightOffset(20);
    }

    private LineDataSet getZoneDataSet(
            Context context,
            List<Entry> entries,
            Float threshold,
            Integer color,
            Integer gradient
    ) {
        List<Entry> zoneData = new ArrayList<Entry>();
        for (Entry it : entries) {
            zoneData.add(new Entry(it.getX(), threshold));
        }

        LineDataSet zoneDataSet = new LineDataSet(zoneData, "");

        zoneDataSet.setDrawValues(false);
        zoneDataSet.setDrawCircles(false);
        zoneDataSet.setDrawCircleHole(false);
        zoneDataSet.setDrawFilled(true);
        zoneDataSet.setLineWidth(0f);
        zoneDataSet.setFormLineWidth(0f);
        zoneDataSet.setColor(ContextCompat.getColor(context, R.color.transparent));
        zoneDataSet.setHighlightLineWidth(0f);
        zoneDataSet.setDrawHighlightIndicators(true);

        int zoneColor = ContextCompat.getColor(context, this.whiteBackground ? R.color.white : color);

        zoneDataSet.setFillColor(zoneColor);
        zoneDataSet.setColor(zoneColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            zoneDataSet.setDrawFilled(true);
            Drawable fillGradient = ContextCompat.getDrawable(context, this.whiteBackground ? R.drawable.white_gradient : gradient);
            zoneDataSet.setFillDrawable(fillGradient);
        }

        return zoneDataSet;
    }
}

class CustomXAxisRenderer extends XAxisRenderer {

    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        String line[] = formattedLabel.split("\n");
        Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);

        if (line.length > 1) {
            Utils.drawXAxisValue(c, line[1], x, y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
        }
    }
}

class MarkerChartRenderer extends LineChartRenderer {

    private final LineChart lineChart;
    private final List<String> labels;
    private final ArrayList<Integer> colors;
    private final Typeface typeface;

    public MarkerChartRenderer(LineChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, List<String> labels, ArrayList<Integer> colors, Typeface typeface) {
        super(chart, animator, viewPortHandler);
        this.lineChart = chart;
        this.labels = labels;
        this.colors = colors;
        this.typeface = typeface;
    }

    @Override
    public void drawExtras(Canvas canvas)
    {
        super.drawExtras(canvas);

        Highlight[] highlighted = lineChart.getHighlighted();

        if (highlighted == null) {
            return;
        }

        float phaseY = mAnimator.getPhaseY();

        float[] position = new float[4];
        position[0] = 0;
        position[1] = 0;
        position[2] = 0;
        position[3] = 0;

        LineData lineData = mChart.getLineData();

        for (Highlight high : highlighted) {
            int dataSetIndex = high.getDataSetIndex();

            ILineDataSet set = lineData.getDataSetByIndex(dataSetIndex);
            Transformer trans = lineChart.getTransformer(set.getAxisDependency());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry entry = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(entry, set)) {
                continue;
            }

            position[0] = entry.getX();
            position[1] = entry.getY() * phaseY;
            position[2] = entry.getY();
            position[3] = 100 * phaseY;

            trans.pointValuesToPixel(position);

            Paint paint = new Paint();
            Paint circlePaint = new Paint();
            Paint holePaint = new Paint();

            circlePaint.setColor(Color.WHITE);

            if (entry.getY() >= 0 && entry.getY() <= 35) {
                holePaint.setColor(colors.get(0));
            } else if (entry.getY() >= 36 && entry.getY() <= 50) {
                holePaint.setColor(colors.get(1));
            } else {
                holePaint.setColor(colors.get(2));
            }

            paint.setTypeface(typeface);
            paint.setTextSize(34);
            paint.setStrokeWidth(3);
            paint.setColor(ColorUtil.getWhiteColorWithAlpha(0.4f));

            Integer index = Math.round(entry.getX());
            String[] label = labels.get(index).split("\n");

            Integer labelOffset = label[0].length() > 5 ? 50 : 40;

            if (entry.getY() < 90) {
                canvas.drawLine(position[0], position[1], position[0], position[3] + 50, paint);
                canvas.drawText("" + label[0], position[0] - labelOffset, position[3], paint);
                canvas.drawText("" + label[1], position[0] - 38, position[3] + 35, paint);

            } else {
                canvas.drawLine(position[0], position[1], position[0], lineChart.getHeight() - 120, paint);
                canvas.drawText("" + label[0], position[0] - labelOffset, lineChart.getHeight() - 85, paint);
                canvas.drawText("" + label[1], position[0] - 38, lineChart.getHeight() - 50, paint);
            }

            canvas.drawCircle(position[0], position[1], 16, circlePaint);
            canvas.drawCircle(position[0], position[1], 13, holePaint);
        }
    }
}

class ColorUtil {

    public static Integer getWhiteColorWithAlpha(Float ratio) {
        Integer color = Color.WHITE;
        return Color.argb(Math.round(Color.alpha(color) * ratio), Color.red(color), Color.green(color), Color.blue(color));
    }
}

