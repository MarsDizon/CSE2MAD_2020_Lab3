package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final int PICK_CONTACT = 1;
    private String contact_number = null;

    //UX Variables
    Button selectButton = null;
    Button sendButton = null;
    TextView numView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind views
        selectButton = (Button) findViewById(R.id.selectContactBtn);
        sendButton = (Button) findViewById(R.id.sendButton);
        numView = (TextView) findViewById(R.id.SMSTV);

        //make send button inactive
        sendButton.setEnabled(false);

        //Button listener
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SendActivity.class);
                myIntent.putExtra("contact_num", contact_number);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {

            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                contact_number = cursor.getString(numberIndex);
                // Do something with the phone contact_number
                Log.d("TEST", contact_number);
                numView.setText(contact_number);
                if (contact_number != null && contact_number.length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
                cursor.close();
            }
        }
    }
}