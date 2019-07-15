package com.abc.snapit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class snapActivity extends AppCompatActivity {
    private static int REQUEST_IMAGE_CAPTURE=101;
    Button btn;
    String TAG ="we are here";
    private ImageView imageView;
    private String objectDetected=null;
//-------------------------------------------------------------------------------------------------------------------------------------
    //the oncreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);
        imageView=findViewById(R.id.mImageView);
        imageView.setScaleType(ScaleType.FIT_XY);
        btn =(Button)findViewById(R.id.snapButton);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {

            //If there is no permission, disable the onClickListener on the FAB
            btn.setOnClickListener(null);
        } else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   takePicture(v);
                }
            });
        }
    }
    //the end of oncreate
    // ------------------------------------------------------take photo---------------------------------------------------------------------
    public void takePicture(View view)
    {
        Intent imageTakeIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    //---------------------------------------------------------------------------------------------------------------------------------------
//receive the result as bitmap image format
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
       {
           Bundle extras=data.getExtras();
           Bitmap imageBitmap=(Bitmap)extras.get("data");
    //make an api call to the cloud
           runImageLabeling(imageBitmap);
           imageView.setImageBitmap(imageBitmap);
       }
    }
    //---------------------------------------------------------------------------------------------------------------------------
    //the image labeling task
    private void runImageLabeling(Bitmap bitmap) {
        //Create a FirebaseVisionImage
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);


        //create a cloud based image labeller

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();

        //pass the image to the processImage method
        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        //get the  data from the label
                        float maxConfidence=0;
                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            String entityId = label.getEntityId();
                            float confidence = label.getConfidence();
                            if(confidence>maxConfidence)
                            {
                                maxConfidence=confidence;
                                objectDetected=text;
                            }
                        }
                        CharSequence options[]=new CharSequence[]{"yes","no"};
                        final AlertDialog.Builder builder=new AlertDialog.Builder(snapActivity.this);
                        builder.setTitle(objectDetected);
                        builder.setItems(options, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent intent=new Intent(snapActivity.this,shoppingActivity.class);
                                    intent.putExtra("msg",objectDetected);
                                    startActivity(intent);
                                }
                                if(which==1)
                                {
                                    AlertDialog test= builder.create();
                                    test.dismiss();
                                }
                            }
                        });
                        builder.show();
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.e("error caught","exception handled");
                    }
                });
        }
//--------------------------------------------------------------------------------------------------------------------------------------------
}
