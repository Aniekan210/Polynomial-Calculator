import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***************************************************************************
 * Class to run polynomial calculator
 *
 * @author Aniekan Ekarika
 * @version v1 max calc is cubic
 ***************************************************************************/
public class Start extends Application
{
    @Override
    public void start(Stage stage)
    {
        PolynomialCalculatorPane pane = new PolynomialCalculatorPane();

        Scene scene = new Scene(pane);
        stage.setTitle("Polynomial calculator");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
}
