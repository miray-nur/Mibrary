package com.example.mibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {
    private ListView listView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        listView = findViewById(R.id.listView);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);
        listView.setAdapter(bookAdapter);

        booksRef = FirebaseDatabase.getInstance().getReference("books");

        checkFirebaseConnection();

        fetchBookData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Book book = (Book) view.getTag();

            Intent intent = new Intent(BookListActivity.this, BookDetailActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("description", book.getDescription());
            intent.putExtra("videoUrl", book.getVideoUrl());
            startActivity(intent);
        });
    }

    private void fetchBookData() {
        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (connected != null && connected) {
                            booksRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        bookList.clear();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Book book = snapshot.getValue(Book.class);
                                            if (book != null) {
                                                bookList.add(book);
                                            }
                                        }
                                        bookAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(BookListActivity.this, "No books found.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(BookListActivity.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(BookListActivity.this, "Firebase not connected.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(BookListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void checkFirebaseConnection() {
        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (connected == null || !connected) {
                            Toast.makeText(BookListActivity.this, "Firebase not connected, retrying...", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(BookListActivity.this::checkFirebaseConnection, 5000);
                        } else {
                            Toast.makeText(BookListActivity.this, "Firebase connected.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(BookListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
