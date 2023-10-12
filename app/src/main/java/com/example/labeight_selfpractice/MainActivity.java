package com.example.labeight_selfpractice;

import android.content.ContentValues;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    EditText editTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextContent = findViewById(R.id.editTextContent);
    }

    public void writeToInternalStorage(View view) {
        String content = editTextContent.getText().toString();
        String fileName = "YourNote.txt";

        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            showMessage("File written to Internal Storage.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToExternalStorage(View view) {

        // Kiểm tra external storage có sẵn để ghi không
        if (!isExternalStorageWritable()) {
            showMessage("External storage unavailable");
            return;
        }

        // Tên file và nội dung cần ghi
        String fileName = "YourNote.txt";
        String content = editTextContent.getText().toString();

        // Lấy đường dẫn đến thư mục gốc external storage
        File root = Environment.getExternalStorageDirectory();

        // Tạo đường dẫn tuyệt đối đến file cần ghi
        File file = new File(root, fileName);

        // Hiển thị đường dẫn để kiểm tra
        Log.d("MainActivity", "File path: " + file.getAbsolutePath());

        // Ghi nội dung vào file
        writeToFile(file, content);

        // Cập nhật lại media store
        MediaScannerConnection.scanFile(this, new String[] {file.getPath()}, null, null);

        // Thông báo kết quả
        showMessage("File saved");
    }

    // Hàm ghi nội dung vào file
    private void writeToFile(File file, String content) {

        try {
            // Mở luồng để ghi
            FileOutputStream stream = new FileOutputStream(file);

            // Ghi dữ liệu vào luồng
            stream.write(content.getBytes());

            // Đóng luồng
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm hiển thị thông báo
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}

