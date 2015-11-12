package com.nicolausyes.example;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.nicolausyes.circleview.CircleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.main);
        for(int i = 0; i < viewGroup.getChildCount(); i++ )
            viewGroup.getChildAt(i).setOnClickListener(onClickListener);

        ((CircleView)findViewById(R.id.id4)).setColor(Color.BLACK);
        ((CircleView)findViewById(R.id.id4)).setSecondColor(Color.MAGENTA);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewGroup viewGroup = (ViewGroup)findViewById(R.id.main);
            for(int i = 0; i < viewGroup.getChildCount(); i++ ) {
                CircleView circleView = (CircleView) viewGroup.getChildAt(i);
                circleView.setSelected(circleView.getId() == v.getId());
            }
        }
    };
}
