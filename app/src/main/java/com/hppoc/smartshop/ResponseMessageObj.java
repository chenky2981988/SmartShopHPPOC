package com.hppoc.smartshop;

/**
 * Created by Chirag Sidhiwala on 6/3/2019.
 * chirag.sidhiwala@hotmail.com
 */
public class ResponseMessageObj {
     /*"type": 1,
             "title": "Aisle 2",
             "subtitle": " Ok, books can be found in Aisle 2, say navigate if you need direction or do you want to buy anything else?",
             "imageUrl": "https://storage.googleapis.com/bluescanner-209101.appspot.com/camfeeds/supermarket2.jpg",
             "buttons": []*/

   // String type;
    String title;
    String subtitle;
    String imageUrl;
   // String[] buttons;


//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public String[] getButtons() {
//        return buttons;
//    }
//
//    public void setButtons(String[] buttons) {
//        this.buttons = buttons;
//    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
