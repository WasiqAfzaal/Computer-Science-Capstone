package com.WasiqAfzaal.Mobile2App;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * UPDATED as of 03-21-2026 for CS499 Milestone - Enhancement Narrative
 * MainActivity handles user input and displays a greeting message.
 * Enhancements include improved readability, validation, and user feedback.
 */
public class MainActivity extends AppCompatActivity {

    // UI components
    private EditText nameInput;
    private TextView greetingText;
    private Button sayHelloButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        nameInput = findViewById(R.id.nameText);
        greetingText = findViewById(R.id.textGreeting);
        sayHelloButton = findViewById(R.id.buttonSayHello);

        // Disable button until valid input is entered
        sayHelloButton.setEnabled(false);

        // Listen for text input changes
        nameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Required override
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable button only if input is not empty or just spaces
                sayHelloButton.setEnabled(s != null && s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Required override
            }
        });
    }

    /**
     * Displays a greeting message when the button is clicked.
     */
    public void sayHello(View view) {
        String name = nameInput.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            greetingText.setText("Please enter your name.");
            return;
        }

        // Display greeting
        greetingText.setText("Hello, " + name + "!");
    }
}