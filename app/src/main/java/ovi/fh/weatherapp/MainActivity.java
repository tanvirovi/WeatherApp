package ovi.fh.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void getWeather(View view){
        try {
            DownloadTask downloadTask = new DownloadTask();

            String encodeCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            downloadTask.execute("https://openweathermap.org/data/2.5/weather?q=" + encodeCityName + "&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            HttpURLConnection httpURLConnection = null;
            URL url;
            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"oops! something went wrong",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather",jsonObject.getString("weather"));
                JSONArray jsonArray = new JSONArray(weatherInfo);

                String message = "";

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String main =jsonObject1.getString("main");
                    String desc =jsonObject1.getString("description");
                    if (!main.equals("")&&!desc.equals("")){
                        message += main + ": " +desc + "\r\n";
                    }
                }
                if (!message.equals("")){
                    textView.setText(message);
                }else {
                    Toast.makeText(MainActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"something sds went wrong",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView2);
    }
}
