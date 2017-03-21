package application;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class GraficoController {


	private Main mainApp;

	@FXML
	private LineChart grafico;

	public GraficoController(){

	}

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;


    }
    public void setGraficoData(XYChart.Series pontos_grafico){
    	grafico.setCreateSymbols(false);
        grafico.getXAxis().setAutoRanging(true);
    	grafico.getYAxis().setAutoRanging(true);

    	grafico.getData().add(pontos_grafico);
    }

}
