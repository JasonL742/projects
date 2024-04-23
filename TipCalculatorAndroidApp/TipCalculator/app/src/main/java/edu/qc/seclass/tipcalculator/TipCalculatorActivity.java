package edu.qc.seclass.tipcalculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TipCalculatorActivity extends AppCompatActivity {

    private EditText checkAmountEditText, partySizeEditText;
    private Button tipButton;
    private TextView tip15TextView, total15TextView,tip20TextView, total20TextView,tip25TextView, total25TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAmountEditText = findViewById(R.id.checkAmountValue);
        partySizeEditText = findViewById(R.id.partySizeValue);
        tipButton = findViewById(R.id.buttonCompute);
        tip15TextView = findViewById(R.id.fifteenPercentTipValue);
        total15TextView = findViewById(R.id.fifteenPercentTotalValue);

        tip20TextView = findViewById(R.id.twentyPercentTipValue);
        total20TextView = findViewById(R.id.twentyPercentTotalValue);

        tip25TextView = findViewById(R.id.twentyfivePercentTipValue);
        total25TextView = findViewById(R.id.twentyfivePercentTotalValue);



        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTip();
            }
        });
    }

    private void calculateTip() {
        String checkAmountStr = checkAmountEditText.getText().toString();
        String partySizeStr = partySizeEditText.getText().toString();

        if (!checkAmountStr.isEmpty() && !partySizeStr.isEmpty() && Integer.parseInt(partySizeStr)!=0) {
            double checkAmount = Double.parseDouble(checkAmountStr);
            int partySize = Integer.parseInt(partySizeStr);

            double total15Amount = checkAmount * (1 + 0.15);
            int individual15Tip = (int) Math.round((checkAmount*0.15) / partySize);
            int individual15Total = (int) Math.round(total15Amount / partySize);

            double total20Amount = checkAmount * (1 + 0.20);
            int individual20Tip = (int) Math.round((checkAmount*0.20) / partySize);
            int individual20Total = (int) Math.round(total20Amount / partySize);

            double total25Amount = checkAmount * (1 + 0.25);
            int individual25Tip = (int) Math.round((checkAmount*0.25) / partySize);
            int individual25Total = (int) Math.round(total25Amount / partySize);

            // Output tip and total amounts to EditText fields
            tip15TextView.setText(Integer.toString( individual15Tip));
            total15TextView.setText( Integer.toString(individual15Total));

            tip20TextView.setText(Integer.toString( individual20Tip));
            total20TextView.setText( Integer.toString(individual20Total));

            tip25TextView.setText(Integer.toString( individual25Tip));
            total25TextView.setText( Integer.toString(individual25Total));

        } else {
            Toast.makeText(TipCalculatorActivity.this, "Empty or incorrect value(s)!", Toast.LENGTH_SHORT).show();
        }
    }

}
