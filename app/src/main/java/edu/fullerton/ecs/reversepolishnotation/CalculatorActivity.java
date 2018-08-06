package edu.fullerton.ecs.reversepolishnotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity  {

    private static final String TAG = "CalculatorActivity";
    private static final String DECIMAL_POINT = ".";

    private CalculatorStack stack;
    private Button[] numberButtons;
    private Button addButton;
    private Button subButton;
    private Button multButton;
    private Button divButton;
    private ImageButton delButton;
    private Button dropButton;
    private Button decimalButton;
    private TextView inputTextView;
    private Button enterButton;
    private TextView[] stackTextView;

    String[] topFour = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        stack = new CalculatorStack();

        //Get references for digit buttons and set event handlers
        numberButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = (Button) findViewById(getResources().getIdentifier("button" + i, "id", this.getPackageName()));
            Log.d(TAG, "onCreate: " + numberButtons[i].toString());
            numberButtons[i].setOnClickListener(new NumberButtonListener());
        }

        //Get references for stack Text View
        inputTextView = (TextView) findViewById(R.id.inputNumberTextView);
        stackTextView = new TextView[4];
        for (int i = 0; i < 4; i++) {
            stackTextView[i] = (TextView) findViewById(getResources().getIdentifier("stack" + i + "TextView", "id", this.getPackageName()));
        }

        //Get reference to the input TextView
        inputTextView = (TextView) findViewById(R.id.inputNumberTextView);

        //Get Operator Buttons reference
        addButton = (Button) findViewById(R.id.buttonAdd);
        multButton = (Button) findViewById(R.id.buttonMult);
        divButton = (Button) findViewById(R.id.buttonDiv);
        subButton = (Button) findViewById(R.id.buttonSub);

        //Set event handlers for Operator Buttons reference
        addButton.setOnClickListener(new OperatorButtonListener());
        multButton.setOnClickListener(new OperatorButtonListener());
        divButton.setOnClickListener(new OperatorButtonListener());
        subButton.setOnClickListener(new OperatorButtonListener());

        //Get reference to the remanining buttons
        enterButton = (Button) findViewById(R.id.buttonEnter);
        delButton = (ImageButton) findViewById(R.id.buttonDelete);
        decimalButton = (Button) findViewById(R.id.buttonDecimal);
        dropButton = (Button) findViewById(R.id.buttonDrop);

        //Set event handlers for them using inner annonymous methods
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputTextView.getText().toString();
                if (!isValidInput(input))
                    return;
                stack.input(input);
                inputTextView.setText("");
                refreshStackDisplay();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputTextView.getText().toString();
                if (input.isEmpty())
                    return;
                inputTextView.setText(input.substring(0, input.length() - 1));
            }
        });

        decimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputTextView.getText().toString();
                if (input.contains(DECIMAL_POINT))
                    return;
                inputTextView.setText(input + DECIMAL_POINT);
            }
        });

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!stack.getStack().isEmpty()) {
                    float junk = stack.pop();
                    refreshStackDisplay();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("stack",stack.getStack());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (stack.getStack().isEmpty()) {
            stack = new CalculatorStack(savedInstanceState.getSerializable("stack"));
            refreshStackDisplay();
        }
    }

    private void refreshStackDisplay() {
        String [] topFour = stack.getTopFour();
        //SetDisplay for stack
        for(int i = 0 ; i < topFour.length; i++) {
            stackTextView[3-i].setText(topFour[i]);
            Log.d(TAG, "displayStack: " + topFour[i]);
        }
    }

    private class OperatorButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String input = inputTextView.getText().toString();
            if (isValidInput(input))
                stack.input(input);
            stack.evaluateOperation(((Button)view).getText().toString());
            refreshStackDisplay();
            inputTextView.setText("");
        }
    }

    private class NumberButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String digit = ((Button)view).getText().toString();
            String currentNum = inputTextView.getText().toString();
            currentNum  = currentNum + digit;
            inputTextView.setText(currentNum);
        }
    }

    private boolean isValidInput(String input) {
        return !input.isEmpty() && !input.equals(DECIMAL_POINT);
    }
}
