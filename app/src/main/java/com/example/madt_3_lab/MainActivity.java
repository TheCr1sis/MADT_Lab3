package com.example.madt_3_lab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Stack;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView calculatorScreen;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private Stack<String> historyStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculatorScreen = findViewById(R.id.calculatorScreen);
    }

    public void onDigitClick(View view) {
        Button button = (Button) view;
        currentInput += button.getText().toString();
        updateScreen();
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String newOperator = button.getText().toString();

        if (!currentInput.isEmpty()) {
            if (currentInput.matches(".*[+\\-*/]$")) {
                currentInput = currentInput.replaceAll("[+\\-*/]$", newOperator);
            } else {
                currentInput += " " + newOperator + " ";
            }
            operator = newOperator;
            historyStack.push(currentInput);
            updateScreen();
        }
    }

    public void onEqualClick(View view) {
        double result = 0;
        String errorMessage = "Error: Invalid input";

        if (!currentInput.isEmpty() && currentInput.contains(" ")) {
            String[] parts = currentInput.split(" ");
            if (parts.length == 3) {
                String firstOperandString = parts[0];
                String operator = parts[1];
                String secondOperandString = parts[2];

                try {
                    double firstOperand = Double.parseDouble(firstOperandString);
                    double secondOperand = Double.parseDouble(secondOperandString);

                    switch (operator) {
                        case "+":
                            result = firstOperand + secondOperand;
                            errorMessage = "";
                            break;
                        case "-":
                            result = firstOperand - secondOperand;
                            errorMessage = "";
                            break;
                        case "*":
                            result = firstOperand * secondOperand;
                            errorMessage = "";
                            break;
                        case "/":
                            if (secondOperand != 0) {
                                result = firstOperand / secondOperand;
                                errorMessage = "";
                            } else {
                                errorMessage = "Error: Division by zero";
                            }
                            break;
                        default:
                            errorMessage = "Error: Invalid operator";
                    }
                } catch (NumberFormatException e) {
                    errorMessage = "Error: Invalid operand";
                }
            }
        }

        if (errorMessage.isEmpty()) {
            currentInput = String.valueOf(result);
        } else {
            currentInput = errorMessage;
        }
        updateScreen();
    }

    public void onClearClick(View view) {
        currentInput = "";
        operator = "";
        firstOperand = 0;
        updateScreen();
    }

    public void onBackClick(View view) {
        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            updateScreen();
        }
    }

    public void onUndoClick(View view) {
        if (!historyStack.isEmpty()) {
            String lastExpression = historyStack.pop();

            if (lastExpression.endsWith(" sqrt")) {
                currentInput = lastExpression.substring(0, lastExpression.length() - 5);
            } else {
                currentInput = lastExpression;

                String[] parts = lastExpression.split(" ");
                if (parts.length == 3) {
                    firstOperand = Double.parseDouble(parts[0]);
                    operator = parts[1];
                } else {
                    operator = "";
                }
            }
            updateScreen();
        }
    }

    public void onSignChangeClick(View view) {
        if (!currentInput.isEmpty()) {
            double number = Double.parseDouble(currentInput);
            number = -number;
            currentInput = String.valueOf(number);
            updateScreen();
        }
    }

    public void onSquareRootClick(View view) {
        if (!currentInput.isEmpty()) {
            double number = Double.parseDouble(currentInput);
            if (number >= 0) {
                number = Math.sqrt(number);
                historyStack.push(currentInput);
                currentInput = number + " sqrt";
                updateScreen();
            } else {
                currentInput = "Error: Invalid input [negative input for square root]";
                updateScreen();
            }
        }
    }

    public void onDecimalClick(View view) {
        String[] parts = currentInput.split(" ");
        String lastPart = parts[parts.length - 1];

        if (!lastPart.contains(".")) {
            currentInput += ".";
            updateScreen();
        }
    }

    private void updateScreen() {
        calculatorScreen.setText(currentInput);
    }
}