package com.group5.diceroller;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class SetCreatorActivity extends Activity {
    Button save_button;
    Button cancel_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creator);
        save_button = (Button)findViewById(R.id.save_button);
        cancel_button = (Button)findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new CancelClickListener());
    }

    class CancelClickListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

}
