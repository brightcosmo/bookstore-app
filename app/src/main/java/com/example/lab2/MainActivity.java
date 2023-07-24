package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.provider.BookEntity;
import com.example.lab2.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    EditText id, title, isbn, author, desc, price;
    BookViewModel mBookViewModel;
    DrawerLayout drawerLayout;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("books_database");
    DatabaseReference mTable = mRef.child("books");

    ArrayList<Book> recyclerData = new ArrayList<>();
    View myFrame;
    GestureDetector gestureDetector;
    int startX;
    int startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);


        adapter = new MyRecyclerViewAdapter();
        mBookViewModel = new BookViewModel(this.getApplication());
        mBookViewModel.getAllItems().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id = findViewById(R.id.id);
        title = findViewById(R.id.title);
        isbn = findViewById(R.id.isbn);
        author = findViewById(R.id.author);
        desc = findViewById(R.id.description);
        price = findViewById(R.id.price);

        drawerLayout = findViewById(R.id.drawer);
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new navigationListener());

        // getSupportFragmentManager().beginTransaction().replace(R.id.frame1, new RecycleViewFragment()).commit();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

                StringTokenizer sT = new StringTokenizer(msg, "|");
                String idString = sT.nextToken();
                String titleString = sT.nextToken();
                String isbnString = sT.nextToken();
                String authorString = sT.nextToken();
                String descriptionString = sT.nextToken();
                String priceString = sT.nextToken();
                boolean priceBool = Boolean.parseBoolean(sT.nextToken());

                double priceDouble = Double.parseDouble(priceString);
                if (priceBool){
                    priceDouble += 100;
                } else {
                    priceDouble += 5;
                }

                priceString = String.valueOf(priceDouble);

                id.setText(idString);
                title.setText(titleString);
                isbn.setText(isbnString);
                author.setText(authorString);
                desc.setText(descriptionString);
                price.setText(priceString);
            }
        };
        registerReceiver(myReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addBook();
                arrayAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        myFrame = findViewById(R.id.frame);
        myFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }
    class navigationListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.addbooks){
                addBook();
            }
            else if (id == R.id.removelastbook){
                if (arrayList.size() > 0){
                    arrayList.remove(arrayList.size()-1);
                }
                if (recyclerData.size() > 0){
                    recyclerData.remove(recyclerData.size()-1);
                }
                mBookViewModel.deleteLastBook();
            }
            else if (id == R.id.removeallbooks){
                arrayList.clear();
                recyclerData.clear();
                mBookViewModel.deleteAll();
            }
            else if (id == R.id.close){
                finish();
            }
            else if (id == R.id.ListAll){
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
            }
            else if (id == R.id.deleteexpensive){
                mBookViewModel.deleteExpensive();
            }
            else if (id == R.id.deleteunknown){
                mBookViewModel.deleteUnknown();
            }
            else if (id == R.id.filterPrice){
                filterPrice();
            }
            else if (id == R.id.filterTitle){
                filterTitle();

            }
            arrayAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            drawerLayout.closeDrawers();
            return true;
        }
    }
    public void filterTitle(){
        mBookViewModel = new BookViewModel(this.getApplication());
        mBookViewModel.getTitleFilteredBooks().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });
    }

    public void filterPrice(){
        mBookViewModel = new BookViewModel(this.getApplication());
        mBookViewModel.getPriceFilteredBooks().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });
    }
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        // single tap -> toast and random isbn
        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            Toast.makeText(MainActivity.this, "Single Tap Confirmed", Toast.LENGTH_SHORT).show();
            // Generate new random ISBN using RandomString
            isbn.setText(RandomString.generateNewRandomString(8));
            return super.onSingleTapUp(e);
        }


        // double tap -> clear fields
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            Toast.makeText(MainActivity.this, "Double Tap", Toast.LENGTH_SHORT).show();
            clearField();
            return super.onDoubleTap(e);
        }


        // scroll -> increment/decrement the price based on distance
        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            int priceValue;
            if (price.getText().toString().equals("")){
                priceValue = 0;
            }
            else {
                priceValue = Integer.parseInt(price.getText().toString());
            }

            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                if (e1.getX() < e2.getX()) {
                    price.setText(String.valueOf(Math.max(priceValue - (int) (distanceX/2), 0)));
                }
                if (e1.getX() > e2.getX()) {
                    price.setText(String.valueOf(Math.max(priceValue - (int) (distanceX/2), 0)));
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        // fling -> move the app to the background
        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > 800) {
                moveTaskToBack(true);
            }
            if (Math.abs(velocityY) > 200) {
                title.setText("untitled");
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // long press -> load saved values
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            loadBook();
            super.onLongPress(e);
        }
    }

    public void clearField() {
        id.getText().clear();
        title.getText().clear();
        isbn.getText().clear();
        author.getText().clear();
        desc.getText().clear();
        price.getText().clear();
    }

    public void addBook() {
        String id_data = "0";
        String title_data = title.getText().toString();
        String isbn_data = isbn.getText().toString();
        String author_data = author.getText().toString();
        String desc_data = desc.getText().toString();
        String price_data = price.getText().toString();

        boolean flag = true;
        // validation to check that the fields are not empty
        List<String> stringData = Arrays.asList(title_data, isbn_data, author_data, desc_data, price_data);
        for (int i = 0; i < stringData.size(); i++){
            if (stringData.get(i).equals("")){
                flag = false;
            }
        }

        if (flag) {
            String msg = "Title: " + title_data + "\nPrice: " + price_data;

            Toast titlePriceToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            titlePriceToast.show();

            SharedPreferences data = getSharedPreferences("data", 0);
            SharedPreferences.Editor editor = data.edit();

            editor.putString("id", id_data);
            editor.putString("title", title_data);
            editor.putString("isbn", isbn_data);
            editor.putString("author", author_data);
            editor.putString("desc", desc_data);
            editor.putString("price", price_data);
            editor.apply();

            //Book newBook = new Book(id_data, title_data, isbn_data, author_data, desc_data, price_data);

            //arrayList.add(title_data + "    |    " + price_data);
            //recyclerData.add(newBook);

            BookEntity newBE = new BookEntity(title_data, isbn_data, author_data, desc_data, Double.parseDouble(price_data));
            mBookViewModel.insert(newBE);
        }
    }

    public void doublePrice(View view) {
        EditText price = findViewById(R.id.price);
        double newPrice = Double.parseDouble(String.valueOf(price.getText())) * 2;
        price.setText(String.valueOf(newPrice));
    }

    public void loadBook() {
        SharedPreferences data = getSharedPreferences("data", 0);
        id.setText(data.getString("id", ""));
        title.setText(data.getString("title", ""));
        isbn.setText(data.getString("isbn", ""));
        author.setText(data.getString("author", ""));
        desc.setText(data.getString("desc", ""));
        price.setText(data.getString("price", ""));
    }

    public void setISBN(View view) {
        SharedPreferences data = getSharedPreferences("data", 0);
        SharedPreferences.Editor editor = data.edit();
        editor.putString("isbn", "00112233");
        editor.apply();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
        Log.i("lab3", "onSaveInstanceState");
        outState.putString("title", title.getText().toString());
        outState.putString("isbn", isbn.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.clearField();

        Log.i("lab3", "onRestoreInstanceState");
        title.setText(savedInstanceState.getString("title"));
        isbn.setText(savedInstanceState.getString("isbn"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences data = getSharedPreferences("data", 0);
        id.setText(data.getString("id", ""));
        title.setText(data.getString("title", ""));
        isbn.setText(data.getString("isbn", ""));
        author.setText(data.getString("author", ""));
        desc.setText(data.getString("desc", ""));
        price.setText(data.getString("price", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.clearfields){
            clearField();
        }
        else if (id == R.id.loadbook){
            loadBook();
        }
        else if (id == R.id.totalbooks){
            Toast.makeText(this, "Total books: " + arrayList.size(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}