package com.example.georgia.newsappstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class News   {

//New title
    private String mTitle;

    //New section
    private String mSection;

    //The date of the article
    private String mDate;


    //Author of the article
    private String mAuthor;

    //The Url from article
    private String mUrl;

/** Create constructor  {@link News} object.
 *@param title is the title of the article
 *@param section is
 *@param date is the date of the article
 *@param author is the author of the article
 * @param url is the website URL to find more details about the article
 */

   public News (String title, String section, String date, String author, String url){

       mTitle = title;
       mSection = section;
       mDate = date;
       mAuthor = author;
       mUrl = url;
    }

    /**
     * Returns the title of the article.
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * Returns the category of the article.
     */
    public String getmSection() {
        return mSection;
    }

    /**
     * Returns the date of the article.
     */
    public String getmDate() {
        return mDate;
    }

    /**
     * Returns the website URL to find more information about the earthquake.
     */
    public String getmUrl() {
        return mUrl;
    }

    /**
     * Returns the name of the author of the article.
     */

    public String getmAuthor(){ return mAuthor;}

}
