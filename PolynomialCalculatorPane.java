import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;

public class PolynomialCalculatorPane extends AnchorPane {
    private StringBuilder leftHandSide = new StringBuilder();
    private StringBuilder rightHandSide = new StringBuilder();
    private boolean isLeftSideActive = true;
    
    private Label lhsLabel;
    private Label rhsLabel;
    private Label resultLabel;
    private ToggleGroup sideToggleGroup;
    private Logic calculatorLogic = new Logic();
    
    // Dark color palette
    private final Color DARK_BASE = Color.web("#1a1a1a");
    private final Color DARK_SURFACE = Color.web("#2d2d2d");
    private final Color ACCENT_GREEN = Color.web("#4CAF50");
    private final Color LIGHT_TEXT = Color.web("#e0e0e0");
    private final Color DARK_TEXT = Color.web("#212121");
    private final Color HIGHLIGHT = Color.web("#3e3e3e");
    private final Color ERROR_RED = Color.web("#ef5350");
    
    public PolynomialCalculatorPane() {
        initializeUI();
    }
    
    private void initializeUI() {
        this.setPrefSize(700, 650);
        this.setStyle("-fx-background-color: " + toHex(DARK_BASE) + ";");
        
        // Title (centered at top)
        Label titleLabel = new Label("POLYNOMIAL CALCULATOR");
        titleLabel.setFont(Font.font("Roboto", 24));
        titleLabel.setTextFill(LIGHT_TEXT);
        AnchorPane.setTopAnchor(titleLabel, 20.0);
        AnchorPane.setLeftAnchor(titleLabel, 0.0);
        AnchorPane.setRightAnchor(titleLabel, 0.0);
        titleLabel.setAlignment(Pos.CENTER);
        
        // Side selector (left side)
        VBox sideSelector = new VBox(5);
        sideSelector.setAlignment(Pos.CENTER_LEFT);
        
        sideToggleGroup = new ToggleGroup();
        ToggleButton lhsRadio = createToggleButton("LEFT SIDE");
        lhsRadio.setSelected(true);
        ToggleButton rhsRadio = createToggleButton("RIGHT SIDE");
        
        sideSelector.getChildren().addAll(lhsRadio, rhsRadio);
        AnchorPane.setTopAnchor(sideSelector, 70.0);
        AnchorPane.setLeftAnchor(sideSelector, 20.0);
        
        // Equation display (center)
        HBox equationDisplay = new HBox(10);
        equationDisplay.setAlignment(Pos.CENTER);
        
        lhsLabel = createEquationDisplayLabel("LHS");
        Label equalsLabel = new Label("=");
        equalsLabel.setFont(Font.font("Roboto", 24));
        equalsLabel.setTextFill(LIGHT_TEXT);
        rhsLabel = createEquationDisplayLabel("RHS");
        
        equationDisplay.getChildren().addAll(lhsLabel, equalsLabel, rhsLabel);
        AnchorPane.setTopAnchor(equationDisplay, 110.0);
        AnchorPane.setLeftAnchor(equationDisplay, 150.0);
        
        // Solve button (below equation)
        Button solveButton = new Button("SOLVE");
        solveButton.setStyle("-fx-background-color: " + toHex(ACCENT_GREEN) + ";");
        solveButton.setTextFill(DARK_TEXT);
        solveButton.setFont(Font.font("Roboto", 14));
        solveButton.setPadding(new Insets(8, 20, 8, 20));
        solveButton.setOnAction(e -> solveEquation());
        setupButtonHoverEffect(solveButton);
        
        AnchorPane.setTopAnchor(solveButton, 160.0);
        AnchorPane.setLeftAnchor(solveButton, 300.0);
        
        // Button grid (left side)
        GridPane buttonGrid = createButtonGrid();
        AnchorPane.setTopAnchor(buttonGrid, 220.0);
        AnchorPane.setLeftAnchor(buttonGrid, 30.0);
        
        // Right side container (holds result and graph button)
        VBox rightSideContainer = new VBox();
        rightSideContainer.setSpacing(20); // Space between result and graph button
        rightSideContainer.setPrefWidth(300);
        AnchorPane.setTopAnchor(rightSideContainer, 220.0);
        AnchorPane.setLeftAnchor(rightSideContainer, 350.0);
        
        // Result display
        VBox resultSection = createResultSection();
        
        // Graph button (centered horizontally in right side container)
        HBox graphButtonContainer = new HBox();
        graphButtonContainer.setAlignment(Pos.CENTER);
        
        Button graphButton = new Button("VIEW GRAPH");
        graphButton.setStyle("-fx-background-color: " + toHex(ACCENT_GREEN) + ";");
        graphButton.setTextFill(DARK_TEXT);
        graphButton.setFont(Font.font("Roboto", 14));
        graphButton.setPadding(new Insets(8, 20, 8, 20));
        graphButton.setOnAction(e -> showGraphWindow());
        setupButtonHoverEffect(graphButton);
        
        graphButtonContainer.getChildren().add(graphButton);
        
        rightSideContainer.getChildren().addAll(resultSection, graphButtonContainer);
        this.getChildren().addAll(
            titleLabel, sideSelector, equationDisplay, 
            solveButton, buttonGrid, rightSideContainer
        );
        
        // Set radio button listener
        sideToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            isLeftSideActive = newVal == lhsRadio;
        });
    }
    
    private VBox createResultSection() {
        VBox container = new VBox(5);
        container.setAlignment(Pos.TOP_LEFT);
        
        resultLabel = new Label("Enter an equation and click SOLVE");
        resultLabel.setFont(Font.font("Roboto Mono", 14));
        resultLabel.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
        resultLabel.setPadding(new Insets(10));
        resultLabel.setPrefSize(300, 150);
        resultLabel.setWrapText(true);
        resultLabel.setAlignment(Pos.TOP_LEFT);
        
        container.getChildren().addAll(resultLabel);
        return container;
    }
    
    private ToggleButton createToggleButton(String text) {
        ToggleButton button = new ToggleButton(text);
        button.setToggleGroup(sideToggleGroup);
        button.setFont(Font.font("Roboto", 12));
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
        
        button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: " + toHex(HIGHLIGHT) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
            } else {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
            }
        });
        
        return button;
    }
    
    private Label createEquationDisplayLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Roboto Mono", 14));
        label.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + ";");
        label.setTextFill(LIGHT_TEXT);
        label.setPadding(new Insets(10, 15, 10, 15));
        label.setPrefSize(200, 40);
        return label;
    }
    
    private GridPane createButtonGrid() {
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(8);
        buttonGrid.setVgap(8);
        buttonGrid.setPadding(new Insets(10));
        buttonGrid.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + ";");
        
        // Column constraints
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(80);
            buttonGrid.getColumnConstraints().add(col);
        }
        
        // Row constraints
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(60);
            buttonGrid.getRowConstraints().add(row);
        }
        
        // Add buttons
        buttonGrid.add(createMathButton("x"), 0, 0);
        buttonGrid.add(createMathButton("x²"), 1, 0);
        buttonGrid.add(createMathButton("x³"), 2, 0);

        buttonGrid.add(createNumberButton("7"), 0, 1);
        buttonGrid.add(createNumberButton("8"), 1, 1);
        buttonGrid.add(createNumberButton("9"), 2, 1);
        
        buttonGrid.add(createNumberButton("4"), 0, 2);
        buttonGrid.add(createNumberButton("5"), 1, 2);
        buttonGrid.add(createNumberButton("6"), 2, 2);
        
        buttonGrid.add(createNumberButton("1"), 0, 3);
        buttonGrid.add(createNumberButton("2"), 1, 3);
        buttonGrid.add(createNumberButton("3"), 2, 3);
        
        buttonGrid.add(createOperatorButton("-"), 0, 4);
        buttonGrid.add(createNumberButton("0"), 1, 4);
        buttonGrid.add(createOperatorButton("+"), 2, 4);
        
        Button clrButton = createControlButton("CLR");
        GridPane.setColumnSpan(clrButton, 1);
        buttonGrid.add(clrButton, 0, 5);
        
        Button delButton = createControlButton("DEL");
        GridPane.setColumnSpan(delButton, 1);
        buttonGrid.add(delButton, 1, 5);
        
        return buttonGrid;
    }
    
    private Button createNumberButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Roboto", 18));
        button.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> handleButtonPress(text));
        setupButtonHoverEffect(button);
        return button;
    }
    
    private Button createMathButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Roboto", 18));
        button.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(ACCENT_GREEN) + ";");
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> handleButtonPress(text.equals("x²") ? "x^2" : text.equals("x³") ? "x^3" : text));
        setupButtonHoverEffect(button);
        return button;
    }
    
    private Button createOperatorButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Roboto", 18));
        button.setStyle("-fx-background-color: " + toHex(HIGHLIGHT) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> handleButtonPress(text));
        setupButtonHoverEffect(button);
        return button;
    }
    
    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Roboto", 14));
        button.setStyle("-fx-background-color: " + toHex(HIGHLIGHT) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button.setOnAction(e -> handleButtonPress(text.equals("CLR") ? "CLR" : "DEL"));
        setupButtonHoverEffect(button);
        return button;
    }
    
    private void setupButtonHoverEffect(Button button) {
        final DropShadow glow = new DropShadow();
        glow.setColor(ACCENT_GREEN.brighter());
        glow.setWidth(15);
        glow.setHeight(15);
        glow.setRadius(5);
        
        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });
        
        button.setOnMouseExited(e -> {
            button.setEffect(null);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }
    
    private void showGraphWindow() {
        if (leftHandSide.length() == 0 || rightHandSide.length() == 0) {
            resultLabel.setText("Error: Enter an equation first");
            resultLabel.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(ERROR_RED) + ";");
            return;
        }
        
        solveEquation();
        
        Stage graphStage = new Stage();
        graphStage.setTitle("Graph: " + calculatorLogic.getEquation());
        
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X Values");
        yAxis.setLabel("Y Values");
        
        xAxis.setStyle("-fx-tick-label-fill: " + toHex(LIGHT_TEXT) + ";");
        yAxis.setStyle("-fx-tick-label-fill: " + toHex(LIGHT_TEXT) + ";");
        
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(-30);
        yAxis.setUpperBound(30);
        yAxis.setTickUnit(5);
        
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        xAxis.setTickUnit(1);
        
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        lineChart.setStyle("-fx-background-color: " + toHex(DARK_BASE) + ";");
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Function");
        
        double step = 0.1;
        for (double x = -10; x <= 10; x += step) {
            double y = calculatorLogic.y(x);
            series.getData().add(new XYChart.Data<>(x, y));
        }
        
        lineChart.getData().add(series);
        lineChart.lookup(".chart-series-line").setStyle("-fx-stroke: " + toHex(ACCENT_GREEN) + ";");
        
        StackPane root = new StackPane(lineChart);
        root.setStyle("-fx-background-color: " + toHex(DARK_BASE) + ";");
        
        Scene scene = new Scene(root, 600, 400);
        graphStage.setScene(scene);
        graphStage.show();
    }
    
    private String toHex(Color color) {
        return String.format("#%02x%02x%02x",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }
    
    private void handleButtonPress(String buttonText) {
        StringBuilder currentSide = isLeftSideActive ? leftHandSide : rightHandSide;
        
        switch (buttonText) {
            case "CLR":
                currentSide.setLength(0);
                break;
            case "DEL":
                if (currentSide.length() > 0) {
                    if (currentSide.toString().endsWith(" - ")) {
                        currentSide.setLength(currentSide.length() - 3);
                    } 
                    else if (currentSide.toString().endsWith(" + ")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    else if (currentSide.toString().endsWith("x^3")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    else if (currentSide.toString().endsWith("x^2")) {
                        currentSide.setLength(currentSide.length() - 3);
                    }
                    else if (currentSide.toString().endsWith("x")) {
                        currentSide.setLength(currentSide.length() - 1);
                    }
                    else {
                        currentSide.setLength(currentSide.length() - 1);
                    }
                }
                break;
            default:
                if (canAddToEquation(currentSide.toString(), buttonText)) {
                    if (isOperator(buttonText)) {
                        if (currentSide.length() == 0) {
                            currentSide.append(buttonText).append(" ");
                        } 
                        else if (currentSide.toString().endsWith(" ")) {
                            currentSide.append(buttonText).append(" ");
                        }
                        else {
                            currentSide.append(" ").append(buttonText).append(" ");
                        }
                    } 
                    else if (isXTerm(buttonText)) {
                        currentSide.append(buttonText);
                    }
                    else {
                        currentSide.append(buttonText);
                    }
                }
        }
        updateLabels();
    }
    
    private boolean canAddToEquation(String current, String toAdd) {
        current = current.trim();
        
        if (isNumber(toAdd) && (current.endsWith("x") || 
                               current.endsWith("x^2") || 
                               current.endsWith("x^3"))) {
            return false;
        }
        
        if (isOperator(toAdd) && (current.endsWith("+") || 
                                 current.endsWith("-"))) {
            return false;
        }
        
        if (isXTerm(toAdd) && (current.endsWith("x") || 
                               current.endsWith("x^2") || 
                               current.endsWith("x^3"))) {
            return false;
        }
        
        return true;
    }
    
    private boolean isNumber(String s) {
        return s.matches("[0-9]");
    }
    
    private boolean isXTerm(String s) {
        return s.equals("x") || s.equals("x^2") || s.equals("x^3");
    }
    
    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-");
    }
    
    private boolean endsWithOperator(String s) {
        s = s.trim();
        return s.endsWith("+") || s.endsWith("-");
    }
    
    private void updateLabels() {
        lhsLabel.setText(leftHandSide.length() == 0 ? "LHS" : leftHandSide.toString().trim());
        rhsLabel.setText(rightHandSide.length() == 0 ? "RHS" : rightHandSide.toString().trim());
    }
    
    private void solveEquation() {
        String lhs = leftHandSide.toString().trim();
        String rhs = rightHandSide.toString().trim();
        
        if (lhs.isEmpty() || rhs.isEmpty()) {
            resultLabel.setText("Error: Both sides must have values");
            resultLabel.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(ERROR_RED) + ";");
            return;
        }
        
        if (endsWithOperator(lhs) || endsWithOperator(rhs)) {
            resultLabel.setText("Error: Cannot end with operator");
            resultLabel.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(ERROR_RED) + ";");
            return;
        }
        
        calculatorLogic.set(lhs, rhs);
        
        resultLabel.setText(calculatorLogic.calculate());
        resultLabel.setStyle("-fx-background-color: " + toHex(DARK_SURFACE) + "; -fx-text-fill: " + toHex(LIGHT_TEXT) + ";");
    }
}