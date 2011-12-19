package com.nhn.socialanalytics.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.nhn.socialanalytics.miner.opinion.FeatureResultSet;
import com.nhn.socialanalytics.miner.opinion.FeatureResultSet.FeatureSummary;

@SuppressWarnings("serial")
public class FeatureSummaryChart extends JPanel {
	
	ChartPanel chartNumsPanel;
	ChartPanel chartPolarityPanel;
	
	public FeatureSummaryChart() {
		setLayout(new BorderLayout());
		
		DefaultCategoryDataset emptyDataset = new DefaultCategoryDataset();
		
		JFreeChart chartNums = ChartFactory.createBarChart("Feature Summary", "Series", "# of Clauses", emptyDataset, 
				PlotOrientation.VERTICAL , true, true, false);
		
		JFreeChart chartPolarity = ChartFactory.createBarChart("Feature Polarity", "Series", "Polarity", emptyDataset, 
				PlotOrientation.VERTICAL , true, true, false);

		chartNums.setBackgroundPaint(Color.WHITE);
		chartPolarity.setBackgroundPaint(Color.WHITE);
		
		chartNumsPanel = new ChartPanel(chartNums);
		chartPolarityPanel = new ChartPanel(chartPolarity);
		
		add(chartNumsPanel, BorderLayout.NORTH);
		add(chartPolarityPanel, BorderLayout.SOUTH);
	}
	
	public void updateChart(FeatureResultSet resultSet, boolean includeEtc) {		
		DefaultCategoryDataset datasetNums = new DefaultCategoryDataset();
		DefaultCategoryDataset datasetPolarity = new DefaultCategoryDataset();
		
		for (FeatureSummary featureSum : resultSet) {
			String feature = featureSum.getFeature();
			double docNums =featureSum.getDocNums();
			double posNums = featureSum.getPositiveNums();
			double negNums = featureSum.getNegativeNums();
			double polarity = featureSum.getPolarity();
			
			if (!includeEtc) {
				if (!feature.equalsIgnoreCase("ETC")) {
					datasetNums.addValue(negNums, "Negative", feature);
					datasetNums.addValue(posNums, "Positive", feature);
					datasetNums.addValue(docNums, "Total", feature);
					
					datasetPolarity.addValue(polarity, "Polarity", feature);
				}
			}
			else {
				datasetNums.addValue(negNums, "Negative", feature);
				datasetNums.addValue(posNums, "Positive", feature);
				datasetNums.addValue(docNums, "Total", feature);
				
				datasetPolarity.addValue(polarity, "Polarity", feature);				
			}
		}
		
		JFreeChart chartNums = ChartFactory.createBarChart("Feature Summary" + "(" + resultSet.getObject() + ")", 
				"Series", "# of Clauses", datasetNums, PlotOrientation.VERTICAL , true, true, false);
		
		JFreeChart chartPolarity = ChartFactory.createBarChart("Feature Polarity" + "(" + resultSet.getObject() + ")", 
				"Series", "Polarity", datasetPolarity, PlotOrientation.VERTICAL , true, true, false);

		
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
