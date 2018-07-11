package com.example.georgia.newsappstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news) {
        super ( context, 0, news );


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from ( getContext () ).inflate ( R.layout.news_list_item, parent, false );

            viewHolder = new ViewHolderItem ();
            viewHolder.newsTitleTextView = (TextView) convertView.findViewById ( R.id.newsTitle );
            viewHolder.newsSectionTextView = (TextView) convertView.findViewById ( R.id.newsCategory );
            viewHolder.newsAuthorTextView = (TextView) convertView.findViewById ( R.id.newsAuthor );
            viewHolder.newsDateTextView = (TextView) convertView.findViewById ( R.id.newsDate );

            convertView.setTag ( viewHolder );

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag ();
        }


        // Find the earthquake at the given position in the list of articles
        News currentNews = getItem ( position );

        String newsTitle = currentNews.getmTitle ();
        String newsSection = currentNews.getmSection ();
        String newsAuthor = currentNews.getmAuthor ();

        // Define a SimpleDateFormat object to deconstruct original date.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd'T'kk:mm:ss'Z'" );
        Date date = null;

        try {
            // Convert the date String into a Date object using the SimpleDateFormat.
            date = simpleDateFormat.parse ( currentNews.getmDate () );
        } catch (ParseException e) {
            e.printStackTrace ();
        }

        // Define a new SimpleDateFormat object to reconstruct the date into the desired format.
        SimpleDateFormat newDateFormat = new SimpleDateFormat ( "LLL dd, yyyy" );
        // Convert the Date object into a String.
        String formattedDate = newDateFormat.format ( date );

        viewHolder.newsTitleTextView.setText ( newsTitle );
        viewHolder.newsSectionTextView.setText ( newsSection );
        viewHolder.newsAuthorTextView.setText ( newsAuthor );
        viewHolder.newsDateTextView.setText ( formattedDate );


        return convertView;
    }

    // our ViewHolder.
    // caches our TextView
    static class ViewHolderItem {
        TextView newsTitleTextView;
        TextView newsSectionTextView;
        TextView newsAuthorTextView;
        TextView newsDateTextView;

    }

}