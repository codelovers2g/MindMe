package com.mindmesolo.mindme.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mindmesolo.mindme.R;

/**
 * Created by User1 on 6/1/2016.
 */
public class LaunchScreen extends AppCompatActivity {

    private static final String TAG = "LaunchScreen";

    ViewPager viewPager;

    Button _btn1, _btn2, _btn3, _btn4, _btn5;

    CustomViewPagerLaunch adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchscreen);
        viewPager = (ViewPager) findViewById(R.id.customviewpager);
        Button b1 = (Button) findViewById(R.id.loginbutton);
        Button b2 = (Button) findViewById(R.id.Signupbutton);
        // final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CustomViewPagerLaunch(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(50);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), login.class);
                startActivity(intent);
                //finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ActivitySignup.class);
                startActivity(intent);
                //finish();
            }
        });
        setTab();
        initButton();
    }

    private void initButton() {
        _btn1 = (Button) findViewById(R.id.btn1);
        _btn2 = (Button) findViewById(R.id.btn2);
        _btn3 = (Button) findViewById(R.id.btn3);
        _btn4 = (Button) findViewById(R.id.btn4);
        _btn5 = (Button) findViewById(R.id.btn5);
        setSelectedButton(_btn1);
    }

    private void setSelectedButton(Button btn) {
        btn.setBackgroundResource(R.drawable.dotindicatorselected);
    }

    private void setUnselectedButton(Button btn) {
        btn.setBackgroundResource(R.drawable.dotindicator);
    }

    private void setTab() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int position) {
                Fragment f = adapter.getItem(viewPager.getCurrentItem());
                if (f.getClass().equals(new Launch1().getClass())) {
                    position = 1;
                } else if (f.getClass().equals(new Launch2().getClass())) {
                    position = 2;
                } else if (f.getClass().equals(new Launch3().getClass())) {
                    position = 3;
                } else if (f.getClass().equals(new Launch4().getClass())) {
                    position = 4;
                } else if (f.getClass().equals(new Launch5().getClass())) {
                    position = 5;
                }
                btnAction(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void btnAction(int position) {
        switch (position) {
            case 1:
                setSelectedButton(_btn1);
                setUnselectedButton(_btn2);
                setUnselectedButton(_btn3);
                setUnselectedButton(_btn4);
                setUnselectedButton(_btn5);
                break;
            case 2:
                setSelectedButton(_btn2);
                setUnselectedButton(_btn1);
                setUnselectedButton(_btn3);
                setUnselectedButton(_btn4);
                setUnselectedButton(_btn5);
                break;
            case 3:
                setSelectedButton(_btn3);
                setUnselectedButton(_btn2);
                setUnselectedButton(_btn1);
                setUnselectedButton(_btn4);
                setUnselectedButton(_btn5);
                break;
            case 4:
                setSelectedButton(_btn4);
                setUnselectedButton(_btn2);
                setUnselectedButton(_btn3);
                setUnselectedButton(_btn1);
                setUnselectedButton(_btn5);
                break;
            case 5:
                setSelectedButton(_btn5);
                setUnselectedButton(_btn2);
                setUnselectedButton(_btn3);
                setUnselectedButton(_btn4);
                setUnselectedButton(_btn1);
                break;
        }
    }
}
