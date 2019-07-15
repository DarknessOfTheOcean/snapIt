package com.abc.snapit;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class shoppingActivity extends AppCompatActivity {
    Button btn,btnAmazon,btnFlipkart,btnSnapdeal;
    String TAG="test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        final String data=getIntent().getStringExtra("msg");

        //first set the back button
        btn=(Button)findViewById(R.id.backBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(shoppingActivity.this,snapActivity.class);
                startActivity(intent1);
            }
        });
        //set the button for amazon website
        btnAmazon=(Button)findViewById(R.id.btnAmazon);

        btnAmazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weblink=new Intent();
                weblink.setAction(Intent.ACTION_VIEW);
                weblink.addCategory(Intent.CATEGORY_BROWSABLE);

                weblink.setData(Uri.parse("https://www.amazon.in/s?k="+data+"&ref=nb_sb_noss_2"));
                startActivity(weblink);
            }
        });

        //set the button for flipkart
        btnFlipkart=(Button)findViewById(R.id.btnFlipkart);

        btnFlipkart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weblink1=new Intent();
                weblink1.setAction(Intent.ACTION_VIEW);
                weblink1.addCategory(Intent.CATEGORY_BROWSABLE);
                weblink1.setData(Uri.parse("https://www.flipkart.com/search?q="+data+"&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off"));
                startActivity(weblink1);
            }
        });
        //set the button for snapdeal
        btnSnapdeal=(Button)findViewById(R.id.btnSnapdeal);

        btnSnapdeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weblink2=new Intent();
                weblink2.setAction(Intent.ACTION_VIEW);
                weblink2.addCategory(Intent.CATEGORY_BROWSABLE);
                weblink2.setData(Uri.parse("https://www.snapdeal.com/search?keyword="+data+"&santizedKeyword=&catId=&categoryId=0&suggested=false&vertical=&noOfResults=20&searchState=&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&categoryUrl=&url=&utmContent=&dealDetail=&sort=rlvncy"));

                startActivity(weblink2);
            }
        });

    }
}
