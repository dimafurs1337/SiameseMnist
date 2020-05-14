package com.example.siamesemnist.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.siamesemnist.model.ImageUtils;
import com.example.siamesemnist.model.Model;

import com.example.siamesemnist.views.CustomView;
import com.example.siamesemnist.R;
import com.example.siamesemnist.database.SQLiteHelper;

import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.ByteBuffer;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;
    private ByteBuffer inputImage1;
    private ByteBuffer inputImage2;
    private Interpreter interpreter;

    private int selectedNumber = 0;
    private AlertDialog dialog;
    private CustomView customView;
    private TextView tvSelected;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStart:
                Bitmap scaledBitmap = customView.getBitmap(28, 28);
                float result = Model.predict(interpreter, this, selectedNumber, inputImage1, inputImage2, scaledBitmap);
                tv.setText("Score: " + Math.floor((1 - result) * 10));
                sqLiteHelper.insertData(
                        String.valueOf(selectedNumber),
                        String.valueOf(Math.floor((1 - result) * 10)),
                        ImageUtils.imageToByte(customView.bitmap)
                );
                break;
            case R.id.buttonClear:
                clearView();
                break;
        }

    }

    private void clearView(){
        customView.clear();
        tv.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.itemAbout:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemHistory:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(){
        sqLiteHelper = new SQLiteHelper(this, SQLiteHelper.dbName, null,1);
        tv = findViewById(R.id.text_view);
        customView = findViewById(R.id.customView);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonStart).setOnClickListener(this);
        tvSelected = findViewById(R.id.textViewSelected);
        Button selectNumberButton = findViewById(R.id.buttonSelect);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick number")
                .setSingleChoiceItems(R.array.numbers,0, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(DialogInterface dialog, int which) {
                        selectedNumber = which;
                        tvSelected.setText("Selected digit: "+ selectedNumber);
                        clearView();
                    }
                })
                .setPositiveButton("Ok", null);
        dialog = builder.create();
        View.OnClickListener clickToPick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        };
        selectNumberButton.setOnClickListener(clickToPick);


        try {
            interpreter = new Interpreter( Model.loadModelFile(this) ) ;
        } catch (IOException e) {
            AlertDialog ad = builder.create();
            ad.setMessage("Something went wrong\nFailed to load model");
            ad.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            ad.show();
        }

        inputImage1 = ImageUtils.allocateTensor();
        inputImage2 = ImageUtils.allocateTensor();
    }

}


