package com.big.corporation;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

   private EditText inputField;
   private TextView outputField;
   private TextView generatedPasswordField;
   private TextView strengthTextView;
   private TextView lengthPasswordTextView;

   private ImageView passwordSensor;

   private  PasswordHelper helper;
   private String[] map_rus_eng;

   private View copyPasswordButton;
   private View copyNewPasswordButton;
   private View generateButton;

   private SeekBar passwordLength;
   private CompoundButton digitsCheck, symbolsCheck, capsCheck;

    public static final int MIN_PSW_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = findViewById(R.id.input_field);
        outputField = findViewById(R.id.output_field);
        map_rus_eng = getResources().getStringArray(R.array.rus_eng_dictionary);
        helper = new PasswordHelper(map_rus_eng);
        copyPasswordButton = findViewById(R.id.btn_copy_psw);

        generatedPasswordField = findViewById(R.id.generated_password);
        copyNewPasswordButton = findViewById(R.id.btn_copy_new_psw);
        passwordLength = findViewById(R.id.password_length);
        digitsCheck = findViewById(R.id.isDigitsEnabled);
        capsCheck = findViewById(R.id.isCapsEnabled);
        symbolsCheck = findViewById(R.id.isSymbolsEnabled);
        generateButton = findViewById(R.id.button_generate);
        strengthTextView = findViewById(R.id.password_strength);
        passwordSensor = findViewById(R.id.image_password_strength);
        lengthPasswordTextView = findViewById(R.id.text_password_length);

        copyPasswordButton.setEnabled(false);
        copyNewPasswordButton.setEnabled(false);

        copyPasswordButton.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(getString(R.string.clip_title), outputField.getText().toString())
            );
        });

        copyNewPasswordButton.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(getString(R.string.clip_title), generatedPasswordField.getText().toString())
            );
        });

        generateButton.setOnClickListener(view -> {
            generatedPasswordField.setText(helper.generatePassword(MIN_PSW_LENGTH + passwordLength.getProgress(),
                    capsCheck.isChecked(), digitsCheck.isChecked(), symbolsCheck.isChecked()));
        });

        passwordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateCount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                outputField.setText(helper.convert(s));
                updatePasswordSensor();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateCount(int progress) {
        int total = MIN_PSW_LENGTH + progress;
        String added = getResources().getQuantityString(R.plurals.symbols_count, progress);
        String result = getResources().getQuantityString(R.plurals.symbols_count, total);
        lengthPasswordTextView.setText(getString(R.string.length_format, progress, added, total, result));
    }

    public void updatePasswordSensor(){
        String password = outputField.getText().toString();
        int strength = helper.getPasswordStrengthLevel(password);
        strengthTextView.setText(getResources().getStringArray(R.array.strengths)[strength]);
        int sensorLevel = 0;

        switch (strength) {
            case (0): sensorLevel = 0; break;
            case (1): sensorLevel = 1; break;
            case (2): sensorLevel = 5; break;
            case (3): sensorLevel = 8; break;
        }

        passwordSensor.getDrawable().setLevel(sensorLevel * 1000);
    }
}
