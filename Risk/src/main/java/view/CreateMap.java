package view;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * @author yashpandya
 *
 */
public class CreateMap extends Application {
	
	/**
	 * @param args
	 */
    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Risk Game");

        //GridPane with 10px padding around edge
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Continent Label - constrains use (child, column, row)
        Label continentLabel = new Label("Continents:");
        GridPane.setConstraints(continentLabel, 0, 0);

        //Continent Input
        TextField continentInput = new TextField();
        continentInput.setPromptText("No of Continents");
        GridPane.setConstraints(continentInput, 1, 0);

        //Country Label
        Label countryLabel = new Label("Countries:");
        GridPane.setConstraints(countryLabel, 0, 1);

        //Country Input
        TextField countryInput = new TextField();
        countryInput.setPromptText("No of Countries");
        GridPane.setConstraints(countryInput, 1, 1);

        //Next Page
        Button nextButton1 = new Button("Next");
        GridPane.setConstraints(nextButton1, 1, 2);
        nextButton1.setOnAction( e -> getData(continentInput, continentInput.getText(), countryInput, countryInput.getText()) );
        
        //Add everything to grid
        grid.getChildren().addAll(continentLabel, continentInput, countryLabel, countryInput, nextButton1);

        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }
    
    //Get the data entered by User
    private void getData(TextField continentInput, String continentMessage, TextField countryInput, String countryMessage){
        System.out.println("Number of Continents are : " + continentInput.getText());
        System.out.println("Number of Countries are : " + countryInput.getText());
    }

}