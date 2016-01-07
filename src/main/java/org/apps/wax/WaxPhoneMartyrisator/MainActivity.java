package org.apps.wax.WaxPhoneMartyrisator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Thread thread_work;
    ProgressBar progressBar;
    TextView textArea;
    Button button_send;
    Button button_cancel;

    String number = "0479708049";
    String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus";
    boolean dummyMessageOrUserMessage = true;
    int amount = 5;
    boolean makeBeep = false; // Fait beep a chaque envois.
    boolean testMode = true; // Si oui on envois rien du tout.
    int delayBefore = 2000;
    int delayBetween = 2000;

    private EditText edit_text_numero;
    private EditText edit_text_message;
    private CheckBox chkBox_randomOrUserMsg;
    private EditText edit_text_amount;
    private CheckBox chkBox_testMode;
    private CheckBox chkBox_makeBeep;
    private EditText edit_text_delayBefore;
    private EditText edit_text_delayBetween;

    View parameterPanel;
    ScrollView scroll_view_log;
    LinearLayout paneau_progression;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        button_send = (Button) findViewById(R.id.buttonsend);
        //button_quit.setText("Quit");
        button_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parameterPanel.setVisibility(View.GONE);
                doAction();
            }
        });

        button_cancel = (Button) findViewById(R.id.buttonCancel);
        //button_quit.setText("Quit");
        button_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (thread_work!=null) {
                    thread_work.interrupt();
                    parameterPanel.setVisibility(View.VISIBLE);
                }
            }
        });


        textArea = (TextView) findViewById(R.id.editText);
        textArea.setMovementMethod(new ScrollingMovementMethod());

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        scroll_view_log = (ScrollView)findViewById(R.id.scrollView2);

        paneau_progression = (LinearLayout)findViewById(R.id.linearLayout2);
        paneau_progression.setVisibility(View.GONE);
        

        initDateAndUI();

        button_cancel.requestFocus();


        parameterPanel = findViewById(R.id.parameterpanel);


    }

    private void initDateAndUI() {
        edit_text_numero = (EditText) findViewById(R.id.editText_Numero);
        edit_text_numero.setText(number);
        edit_text_numero.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edit_text_numero.getText().toString();
                Log.d("Change number", txt);
                number = txt;
            }
        });

        edit_text_message = (EditText) findViewById(R.id.editText_Message);
        edit_text_message.setText(message);
        edit_text_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edit_text_message.getText().toString();
                Log.d("Change message", txt);
                message = txt;
            }
        });


        chkBox_randomOrUserMsg = (CheckBox) findViewById(R.id.checkBox_RandomMsgOrUserMsg);
        chkBox_randomOrUserMsg.setChecked(dummyMessageOrUserMessage);
        chkBox_randomOrUserMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean txt = chkBox_randomOrUserMsg.isChecked();
                Log.d("Change message type", "" + txt);
                dummyMessageOrUserMessage = txt;
            }
        });


        edit_text_amount = (EditText) findViewById(R.id.editText_Amount);
        edit_text_amount.setText("" + amount);
        edit_text_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edit_text_amount.getText().toString();
                Log.d("Change message", txt);
                try {
                    amount = Integer.parseInt(txt);
                }
                catch(NumberFormatException e)
                {

                }
            }
        });


        chkBox_makeBeep = (CheckBox) findViewById(R.id.checkBox_MakeBeep);
        chkBox_makeBeep.setChecked(makeBeep);
        chkBox_makeBeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean txt = chkBox_makeBeep.isChecked();
                Log.d("Change makeBeep type", "" + txt);
                makeBeep = txt;
            }
        });

        chkBox_testMode = (CheckBox) findViewById(R.id.checkBox_TestMode);
        chkBox_testMode.setChecked(testMode);
        chkBox_testMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean txt = chkBox_testMode.isChecked();
                Log.d("Change testMode type", "" + txt);
                testMode = txt;
            }
        });



        edit_text_delayBefore = (EditText) findViewById(R.id.editText_DelayBefroe);
        edit_text_delayBefore.setText("" + delayBefore);
        edit_text_delayBefore.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edit_text_delayBefore.getText().toString();
                Log.d("Change delayBefore", txt);
                try {
                    delayBefore = Integer.parseInt(txt);
                }
                catch(NumberFormatException e)
                {

                }
            }
        });

        edit_text_delayBetween = (EditText) findViewById(R.id.editText_DelayBeetween);
        edit_text_delayBetween.setText("" + delayBetween);
        edit_text_delayBetween.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = edit_text_delayBetween.getText().toString();
                Log.d("Change message", txt);
                try {
                    delayBetween = Integer.parseInt(txt);
                }
                catch(NumberFormatException e)
                {

                }
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doAction() {

        if (thread_work!=null)
            thread_work.interrupt();

        thread_work = new ThreadSend(this);
        paneau_progression.setVisibility(View.VISIBLE);
        button_send.setEnabled(false);
        thread_work.start();

    }


}
