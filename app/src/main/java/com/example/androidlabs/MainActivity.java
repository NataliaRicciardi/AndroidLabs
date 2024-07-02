package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private JSONArray characters;
    private CharAdapter charAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new SWInfo().execute("https://swapi.dev/api/people/?format=json");

        ListView listview = findViewById(R.id.listview);

        listview.setOnItemClickListener(
                (parent, view, position, id) -> {
                    try {
                        JSONObject character = characters.getJSONObject(position);
                        String name = character.getString("name");
                        String height = character.getString("height");
                        String mass = character.getString("mass");

                        FrameLayout frame = findViewById(R.id.frame);

                        if (frame == null) {
                            Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("height", height);
                            intent.putExtra("mass", mass);
                            startActivity(intent);
                        }
                        else {
                            DetailsFragment fragment = DetailsFragment.newInstance(name, height, mass);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .commit();
                        }
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }


    private class SWInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                StringBuilder sb = new StringBuilder();
                int byteRead;

                while ((byteRead = inputStream.read()) != -1) {
                    sb.append((char) byteRead);
                }
                connection.disconnect();

                response = sb.toString();

                characters = new JSONObject(response).getJSONArray("results");

            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (characters != null) {
                charAdapter = new CharAdapter(MainActivity.this, characters);
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter(charAdapter);
            }
        }
    }

    private class CharAdapter extends BaseAdapter {

        private final Context context;
        private final JSONArray characters;

        public CharAdapter(Context context, JSONArray characters) {
            this.context = context;
            this.characters = characters;
        }

        @Override
        public int getCount() {
            return characters.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return characters.getJSONObject(position);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
            }

            TextView box = convertView.findViewById(R.id.items);

            try {
                JSONObject character = characters.getJSONObject(position);
                box.setText(character.getString("name"));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            return convertView;
        }
    }
}

