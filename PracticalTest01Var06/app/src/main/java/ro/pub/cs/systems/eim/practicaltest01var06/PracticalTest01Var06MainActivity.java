package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01Var06MainActivity extends AppCompatActivity {
    private EditText editTextName = null;
    private EditText editTextSite = null;
    private Button lessButton = null;
    private Button passButton = null;
    private Button navigateButton = null;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private TextWatcherListener textWatcherListener = new TextWatcherListener();
    private boolean serviceStarted = false;

    private class TextWatcherListener implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passButton = (Button)findViewById(R.id.button_pass);
            editTextSite = (EditText)findViewById(R.id.edit_text_site);
            if (editTextSite.getText() != null && !editTextSite.getText().toString().isEmpty()) {
                passButton.setBackgroundColor(getResources().getColor(R.color.green));
                passButton.setText("Pass");

                //if (!serviceStarted) {
                if (true) {
                    serviceStarted = true;
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06Service.class);
                    stopService(intent);
                    intent.putExtra("site", editTextSite.getText().toString());
                    getApplicationContext().startService(intent);
                }
            }
            else {
                passButton.setBackgroundColor(getResources().getColor(R.color.red));
                passButton.setText("Fail");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            editTextSite = (EditText)findViewById(R.id.edit_text_site);
            passButton = (Button)findViewById(R.id.button_pass);
            switch(view.getId()) {
                case R.id.button_less:
                    if (passButton.getVisibility() == View.VISIBLE) {
                        editTextSite.setVisibility(View.INVISIBLE);
                        passButton.setVisibility(View.INVISIBLE);
                    } else {
                        editTextSite.setVisibility(View.VISIBLE);
                        passButton.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.navigate:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var06SecondaryActivity.class);

                    intent.putExtra("site", editTextSite.getText().toString());
                    intent.putExtra("result", passButton.getText().toString());
                    startActivityForResult(intent, 300);
                    break;
            }
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Message", intent.getStringExtra("message"));
        }
    }

    private IntentFilter intentFilter = new IntentFilter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var06_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextSite = (EditText)findViewById(R.id.edit_text_site);
        editTextName = (EditText)findViewById(R.id.edit_text_name);
        passButton = (Button)findViewById(R.id.button_pass);
        lessButton = (Button)findViewById(R.id.button_less);
        navigateButton = (Button)findViewById(R.id.navigate);
        passButton.setBackgroundColor(getResources().getColor(R.color.red));

        editTextSite.addTextChangedListener(textWatcherListener);
        navigateButton.setOnClickListener(buttonClickListener);
        lessButton.setOnClickListener(buttonClickListener);

        intentFilter = new IntentFilter();
        intentFilter.addAction("action1");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case 300:
                Toast.makeText(getApplication(), "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText edit_text1 = (EditText)findViewById(R.id.edit_text_site);
        EditText edit_text2 = (EditText)findViewById(R.id.edit_text_name);
        savedInstanceState.putString("edit_text1", edit_text1.getText().toString());
        savedInstanceState.putString("edit_text2", edit_text2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("edit_text1")) {
            EditText edit_text1 = (EditText)findViewById(R.id.edit_text_site);
            edit_text1.setText(savedInstanceState.getString("edit_text1"));
        }
        if (savedInstanceState.containsKey("edit_text2")) {
            EditText edit_text1 = (EditText) findViewById(R.id.edit_text_name);
            edit_text1.setText(savedInstanceState.getString("edit_text2"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var06Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test01_var06_main, menu);
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
}
