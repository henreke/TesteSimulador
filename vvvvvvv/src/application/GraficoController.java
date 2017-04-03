package application;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class GraficoController {


	private Main mainApp;

	@FXML
	private LineChart grafico;
	
	@FXML
	private TableView<XYChart.Data<Float, Float>> tabela;
	
	@FXML
	private TableColumn<XYChart.Data<Float, Float>,Float> tempoColuna;
	
	@FXML
	private TableColumn<XYChart.Data<Float, Float>,Float> variavelColuna;
	
	private ObservableList<XYChart.Data<Float, Float>> listatabela = FXCollections.observableArrayList();

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
    	
    	//Tabela
    	listatabela = pontos_grafico.getData();
    	tempoColuna.setCellValueFactory(cellData -> cellData.getValue().XValueProperty());
    	variavelColuna.setCellValueFactory(cellData -> cellData.getValue().YValueProperty());
    	tabela.setItems(listatabela);
    	int i = 0;
    	
    }

}

class Dados{
	
	public float tempo;
	public float variavel;
}
