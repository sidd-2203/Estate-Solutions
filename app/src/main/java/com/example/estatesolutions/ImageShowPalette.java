package com.example.estatesolutions;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

public class ImageShowPalette extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    CardView cardView;
    ImageView arrow,imageToShow;
    Group hiddenGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show_palette);

//        cardView = findViewById(R.id.base_cardView);
//        arrow = findViewById(R.id.show);
//        imageToShow=findViewById(R.id.imageView12);
//        constraintLayout=findViewById(R.id.constraintView1);
//        //hiddenGroup = findViewById(R.id.card_group);
//
//        constraintLayout.setOnClickListener(view -> {
//            if(imageToShow.getVisibility()==View.GONE){
//                TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
//                imageToShow.setVisibility(View.VISIBLE);
//                arrow.setImageResource(android.R.drawable.arrow_up_float);
//            }
//            else if(imageToShow.getVisibility()==View.VISIBLE){
//                //TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
//                imageToShow.setVisibility(View.GONE);
//                arrow.setImageResource(android.R.drawable.arrow_down_float);
//            }
////            TransitionManager.endTransitions(cardView);
////              if (imageToShow.getVisibility() == View.VISIBLE) {
////                imageToShow.setVisibility(View.GONE);
////                arrow.setImageResource(android.R.drawable.arrow_down_float);
////            } else {
////                imageToShow.setVisibility(View.VISIBLE);
////                arrow.setImageResource(android.R.drawable.arrow_up_float);
////            }
//        });


    }


}