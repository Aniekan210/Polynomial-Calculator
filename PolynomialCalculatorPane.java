import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/***************************************************************************
 * Javafx anchore pane class to act as the ui for the polynomial calculator
 *
 * @author Aniekan Ekarika
 * @version v1 max calc is cubic
 ***************************************************************************/
public class PolynomialCalculatorPane extends AnchorPane 
{
    private StringBuilder leftHandSide = new StringBuilder();
    private StringBuilder rightHandSide = new StringBuilder();
    private boolean isLeftSideActive = true;
    
    private Label lhsLabel;
    private Label rhsLabel;
    private Label resultLabel;
    private ToggleGroup sideToggleGroup;
    private Logic calculatorLogic = new Logic();
    private LineChart<Number, Number> lineChart;
    
    public PolynomialCalculatorPane() 
    {
        initializeUI();
    }
    
    private void initializeUI() 
    {
        this.setPrefSize(629, 574);
        this.setStyle("-fx-background-color: #ffd9f0;");
        
        // Title
        Label titleLabel = new Label("Polynomial Calculator");
        titleLabel.setFont(Font.font("Verdana", 36));
        AnchorPane.setTopAnchor(titleLabel, 14.0);
        AnchorPane.setLeftAnchor(titleLabel, 121.0);
        
        // Radio buttons
        sideToggleGroup = new ToggleGroup();
        RadioButton lhsRadio = new RadioButton("LEFT HAND SIDE");
        lhsRadio.setFont(Font.font("Verdana", 12));
        lhsRadio.setToggleGroup(sideToggleGroup);
        lhsRadio.setSelected(true);
        AnchorPane.setTopAnchor(lhsRadio, 67.0);
        AnchorPane.setLeftAnchor(lhsRadio, 8.0);
        
        RadioButton rhsRadio = new RadioButton("RIGHT HAND SIDE");
        rhsRadio.setFont(Font.font("Verdana", 12));
        rhsRadio.setToggleGroup(sideToggleGroup);
        AnchorPane.setTopAnchor(rhsRadio, 88.0);
        AnchorPane.setLeftAnchor(rhsRadio, 8.0);
        
        // Equation labels
        lhsLabel = createEquationLabel("LHS", 8.0);
        Label equalsLabel = new Label("=");
        equalsLabel.setFont(Font.font("Verdana Bold", 24));
        equalsLabel.setStyle("-fx-background-color: transparent;");
        AnchorPane.setTopAnchor(equalsLabel, 109.0);
        AnchorPane.setLeftAnchor(equalsLabel, 305.0);
        
        rhsLabel = createEquationLabel("RHS", 333.0);
        
        // Solve button
        Button solveButton = new Button("Solve!!!");
        solveButton.setStyle("-fx-background-color: #19c402; -fx-border-color: #000000;");
        solveButton.setTextFill(javafx.scene.paint.Color.WHITE);
        solveButton.setFont(Font.font("Verdana", 14));
        AnchorPane.setTopAnchor(solveButton, 167.0);
        AnchorPane.setLeftAnchor(solveButton, 269.0);
        solveButton.setOnAction(e -> solveEquation());
        
        // Result label
        resultLabel = new Label("RESULT LABEL");
        resultLabel.setFont(Font.font("Verdana", 14));
        resultLabel.setStyle("-fx-background-color: #FFFFFF;");
        resultLabel.setAlignment(Pos.TOP_LEFT);
        resultLabel.setPadding(new Insets(5));
        resultLabel.setPrefSize(311, 102);
        AnchorPane.setTopAnchor(resultLabel, 215.0);
        AnchorPane.setLeftAnchor(resultLabel, 306.0);
        
        // Create axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X Values");
        yAxis.setLabel("Y Values");
        
        // Set axis bounds to match our domain and range
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(-30);
        yAxis.setUpperBound(30);
        yAxis.setTickUnit(5);
        
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        xAxis.setTickUnit(1);
        
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Equation");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        
        lineChart.setPrefSize(311, 227);
        AnchorPane.setTopAnchor(lineChart, 332.0);
        AnchorPane.setLeftAnchor(lineChart, 306.0);
        
        // Button grid - EXACT match to FXML
        GridPane buttonGrid = new GridPane();
        buttonGrid.setStyle("-fx-background-color: #ffffff;");
        buttonGrid.setPrefSize(277, 346);
        AnchorPane.setTopAnchor(buttonGrid, 215.0);
        AnchorPane.setLeftAnchor(buttonGrid, 14.0);
        
        // Column constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(101);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(101);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(101);
        buttonGrid.getColumnConstraints().addAll(col1, col2, col3);
        
        // Row constraints
        for (int i = 0; i < 5; i++) 
        {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(96);
            buttonGrid.getRowConstraints().add(row);
        }
        RowConstraints lastRow = new RowConstraints();
        lastRow.setPrefHeight(57);
        buttonGrid.getRowConstraints().add(lastRow);
        
        // Add buttons in EXACT FXML positions
        buttonGrid.add(createGridButton("x"), 0, 0);
        buttonGrid.add(createGridButton("x^2"), 1, 0);
        buttonGrid.add(createGridButton("x^3"), 2, 0);

        buttonGrid.add(createGridButton("7"), 0, 1);
        buttonGrid.add(createGridButton("8"), 1, 1);
        buttonGrid.add(createGridButton("9"), 2, 1);
        
        buttonGrid.add(createGridButton("4"), 0, 2);
        buttonGrid.add(createGridButton("5"), 1, 2);
        buttonGrid.add(createGridButton("6"), 2, 2);
        
        buttonGrid.add(createGridButton("1"), 0, 3);
        buttonGrid.add(createGridButton("2"), 1, 3);
        buttonGrid.add(createGridButton("3"), 2, 3);
        
        buttonGrid.add(createGridButton("-"), 0, 4);
        buttonGrid.add(createGridButton("0"), 1, 4);
        buttonGrid.add(createGridButton("+"), 2, 4);
        
        Button clrButton = createGridButton("CLR");
        clrButton.setPrefHeight(57);
        buttonGrid.add(clrButton, 0, 5);
        
        Button delButton = createGridButton("DEL");
        delButton.setPrefHeight(57);
        buttonGrid.add(delButton, 1, 5);
        
        this.getChildren().addAll(
            titleLabel, lhsRadio, rhsRadio, 
            lhsLabel, equalsLabel, rhsLabel,
            solveButton, resultLabel, lineChart, buttonGrid
        );
        
        // Set radio button listener
        sideToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            isLeftSideActive = newVal == lhsRadio;
        });
    }
    
    private Label createEquationLabel(String text, double left) 
    {
        Label label = new Label(text);
        label.setFont(Font.font("Verdana", 12));
        label.setStyle("-fx-background-color: #cdebfa; -fx-border-style: solid; -fx-border-width: 2; -fx-border-color: #026799;");
        label.setPadding(new Insets(10, 15, 10, 10));
        label.setPrefSize(287, 39);
        AnchorPane.setTopAnchor(label, 112.0);
        AnchorPane.setLeftAnchor(label, left);
        return label;
    }
    
    private Button createGridButton(String text) 
    {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #0087a6; -fx-border-color: #ffffff;");
        button.setTextFill(javafx.scene.paint.Color.WHITE);
        button.setFont(Font.font("Verdana", 21));
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> handleButtonPress(text));
        return button;
    }
    
    private void handleButtonPress(String buttonText) 
    {
        StringBuilder currentSide = isLeftSideActive ? leftHandSide : rightHandSide;
        
        switch (buttonText) 
        {
            case "CLR":
                currentSide.setLength(0);
                break;
            case "DEL":
                if (currentSide.length() > 0) {
                    // Check for operators with spaces first
                    if (currentSide.toString().endsWith(" - ")) {
                        currentSide.setLength(currentSide.length() - 3);
                    } 
                    else if (currentSide.toString().endsWith(" + ")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    // Handle x^3 term deletion
                    else if (currentSide.toString().endsWith("x^3")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    // Handle x^2 term deletion
                    else if (currentSide.toString().endsWith("x^2")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    // Handle x term deletion
                    else if (currentSide.toString().endsWith("x")) {
                        currentSide.setLength(currentSide.length() - 1);
                    }
                    // Default case - delete single character
                    else {
                        currentSide.setLength(currentSide.length() - 1);
                    }
                }
                break;
            default:
                if (canAddToEquation(currentSide.toString(), buttonText)) {
                    // For operators, add with spaces around them
                    if (isOperator(buttonText)) {
                        // Don't add space before if it's the first character
                        if (currentSide.length() == 0) {
                            currentSide.append(buttonText).append(" ");
                        } 
                        // Don't add space before if previous character is space
                        else if (currentSide.toString().endsWith(" ")) {
                            currentSide.append(buttonText).append(" ");
                        }
                        // Normal case - add spaces around operator
                        else {
                            currentSide.append(" ").append(buttonText).append(" ");
                        }
                    } 
                    // For x terms, add directly (no space after)
                    else if (isXTerm(buttonText)) {
                        currentSide.append(buttonText);
                    }
                    // For numbers, add directly (no space after)
                    else {
                        currentSide.append(buttonText);
                    }
                }
        }
        updateLabels();
    }
    
    private boolean canAddToEquation(String current, String toAdd) 
    {
        current = current.trim();
        
        // Can't add number right after x term
        if (isNumber(toAdd) && (current.endsWith("x") || 
                               current.endsWith("x^2") || 
                               current.endsWith("x^3"))) 
        {
            return false;
        }
        
        // Can't add operator after operator
        if (isOperator(toAdd) && (current.endsWith("+") || 
                                 current.endsWith("-"))) 
        {
            return false;
        }
        
        // Can't add x term after x term
        if (isXTerm(toAdd) && (current.endsWith("x") || 
                               current.endsWith("x^2") || 
                               current.endsWith("x^3"))) 
        {
            return false;
        }
        
        return true;
    }
    
    private boolean isNumber(String s) 
    {
        return s.matches("[0-9]");
    }
    
    private boolean isXTerm(String s) 
    {
        return s.equals("x") || s.equals("x^2") || s.equals("x^3");
    }
    
    private boolean isOperator(String s) 
    {
        return s.equals("+") || s.equals("-");
    }
    
    private boolean endsWithOperator(String s) 
    {
        s = s.trim();
        return s.endsWith("+") || s.endsWith("-");
    }
    
    private void updateLabels() 
    {
        lhsLabel.setText(leftHandSide.length() == 0 ? "LHS" : leftHandSide.toString().trim());
        rhsLabel.setText(rightHandSide.length() == 0 ? "RHS" : rightHandSide.toString().trim());
    }
    
    private void solveEquation() 
    {
        String lhs = leftHandSide.toString().trim();
        String rhs = rightHandSide.toString().trim();
        
        if (lhs.isEmpty() || rhs.isEmpty()) 
        {
            resultLabel.setText("Error: Both sides must have values");
            return;
        }
        
        if (endsWithOperator(lhs) || endsWithOperator(rhs)) 
        {
            resultLabel.setText("Error: Cannot end with operator");
            return;
        }
        
        calculatorLogic.set(lhs, rhs);
        
        resultLabel.setText(calculatorLogic.calculate());
        lineChart.setTitle("Graph: " + calculatorLogic.getEquation());
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Function");
        
        double step = 0.01;
        for (double x = -10; x <= 10; x += step) {
            series.getData().add(new XYChart.Data<>(x, calculatorLogic.y(x)));
        }
                
        lineChart.getData().clear();
        lineChart.getData().add(series);
    }
}