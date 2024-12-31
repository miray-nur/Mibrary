package com.example.mibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private List<Book> bookList;
    private Context context;
    private LayoutInflater inflater;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book, parent, false);
        }

        Book book = bookList.get(position);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView authorTextView = convertView.findViewById(R.id.authorTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);

        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());
        descriptionTextView.setText(book.getDescription());

        convertView.setTag(book);
        return convertView;
    }
}
