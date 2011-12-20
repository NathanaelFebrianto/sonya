package com.nhn.socialanalytics.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.nhn.socialanalytics.miner.opinion.FeatureResultSet;
import com.nhn.socialanalytics.miner.opinion.FeatureResultSet.FeatureSummary;

@SuppressWarnings("serial")
public class FeatureSummaryChart extends JPanel {
	
	ChartPanel chartPiePanel;
	ChartPanel chartNumsPanel;
	ChartPanel chartPolarityPanel;
	
	public FeatureSummaryChart() {
		setLayout(new BorderLayout());
		
		DefaultPieDataset emptyPieDataset = new DefaultPieDataset();
		DefaultCategoryDataset emptyDataset = new DefaultCategoryDataset();
		
		JFreeChart chartPie = ChartFactory.createPieChart("Positive & Negative", emptyPieDataset, true, true, false);
		
		JFreeChart chartNums = ChartFactory.createBarChart("Feature Summary", "Series", "# of Clauses", emptyDataset, 
				PlotOrientation.VERTICAL , true, true, false);
		
		JFreeChart chartPolarity = ChartFactory.createBarChart("Feature Polarity", "Series", "Polarity", emptyDataset, 
				PlotOrientation.VERTICAL , true, true, false);

		chartNums.setBackgroundPaint(Color.WHITE);
		chartPolarity.setBackgroundPaint(Color.WHITE);
		
		chartPiePanel = new ChartPanel(chartPie);
		chartNumsPanel = new ChartPanel(chartNums);
		chartPolarityPanel = new ChartPanel(chartPolarity);
		
		add(chartPiePanel, BorderLayout.NORTH);
		add(chartNumsPanel, BorderLayout.CENTER);
		add(chartPolarityPanel, BorderLayout.SOUTH);
	}
	
	public void updateChart(FeatureResultSet resultSet, boolean includeEtc) {	
		DefaultPieDataset datasetPie = new DefaultPieDataset();
		DefaultCategoryDataset datasetNums = new DefaultCategoryDataset();
		DefaultCategoryDataset datasetPolarity = new DefaultCategoryDataset();
		
		Map<String, Double> pieData = new HashMap<String, Double>();
		
		for (FeatureSummary featureSum : resultSet) {
			String feature = featureSum.getFeature();
			double docNums =featureSum.getDocNums();
			double posNums = featureSum.getPositiveNums();
			double negNums = featureSum.getNegativeNums();
			double polarity = featureSum.getPolarity();
			
			double neutralNums = (docNums - posNums - negNums);
			
			if (!includeEtc) {
				if (!feature.equalsIgnoreCase("ETC")) {
					// pie chart data
					Double neutralSum = pieData.get("Neutral");
					if (neutralSum == null)
						pieData.put("Neutral", neutralNums);
					else
						neutralSum = neutralSum + neutralNums;
					
					Double posSum = pieData.get("Positive");
					if (posSum == null)
						pieData.put("Positive", posNums);
					else
						posSum = posSum + posNums;
					
					Double negSum = pieData.get("Negative");
					if (negSum == null)
						pieData.put("Negative", negNums);
					else
						negSum = negSum + negNums;
					
					// bar chart data1
					datasetNums.addValue(negNums, "Negative", feature);
					datasetNums.addValue(posNums, "Positive", feature);
					datasetNums.addValue(neutralNums, "Neutral", feature);
					
					// bar chart data2
					datasetPolarity.addValue(polarity, "Polarity", feature);
				}
			}
			else {
				// pie chart data
				Double neutralSum = pieData.get("Neutral");
				if (neutralSum == null)
					pieData.put("Neutral", neutralNums);
				else
					neutralSum = neutralSum + neutralNums;
				
				Double posSum = pieData.get("Positive");
				if (posSum == null)
					pieData.put("Positive", posNums);
				else
					posSum = posSum + posNums;
				
				Double negSum = pieData.get("Negative");
				if (negSum == null)
					pieData.put("Negative", negNums);
				else
					negSum = negSum + negNums;
				
				
				// bar chart data1
				datasetNums.addValue(negNums, "Negative", feature);
				datasetNums.addValue(posNums, "Positive", feature);
				datasetNums.addValue(neutralNums, "Neutral", feature);
				
				// bar chart data2
				datasetPolarity.addValue(polarity, "Polarity", feature);				
			}
		}
		
		datasetPie.setValue("Negative", pieData.get("Negative"));
		datasetPie.setValue("Positive", pieData.get("Positive"));
		datasetPie.setValue("Neutral", pieData.get("Neutral"));
		
		JFreeChart chartPie = ChartFactory.createPieChart("Positive & Negative", datasetPie, true, true, false);
		
		JFreeChart chartNums = ChartFactory.createBarChart("Feature Summary" + "(" + resultSet.getObject() + ")", 
				"Series", "# of Clauses", datasetNums, PlotOrientation.VERTICAL , true, true, false);
		
		JFreeChart chartPolarity = ChartFactory.createBarChart("Feature Polarity" + "(" + resultSet.getObject() + ")", 
				"Series", "Polarity", datasetPolarity, PlotOrientation.VERTICAL , true, true, false);

		chartPiePanel.setChart(chartPie);
		chartNumsPanel.setChart(chartNums);
		chartPolarityPanel.setChart(chartPolarity);
	}
	
	/**
     * A custom renderer that returns a different color for each item in a single series.
     */
    class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
        	return this.colors[column % this.colors.length];
        }
    }
	
	public static void main(String[] args) {
		FeatureSummaryChart chart = new FeatureSummaryChart();
		JFrame f = new JFrame("Test");
		f.setSize(500, 500);
		f.getContentPane().add(chart);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
