package com.skplanet.hobbyist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

public class PrototypeActivity extends Activity implements OnClickListener{

	public static final String ACTION = "action";
	public static final String ORDER = "order"; 
	String action;
	private int order;
	private int total_orders;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView (R.layout.activity_prototype);
		int rid = -1;
		
		ImageView iv = (ImageView) findViewById (R.id.imageView_act_prototype);
		Intent intent = getIntent();
		
		action = intent.getStringExtra(ACTION);
		order = intent.getIntExtra(ORDER, -1);

		if (action.equals("compose")) {
			String ptImgList[] = getResources().getStringArray(R.array.prototype_compose_order);
			total_orders = ptImgList.length;
			rid = getResources().getIdentifier(ptImgList[order], null, getPackageName());
		} else if (action.equals("profile")) {
			rid = R.drawable.hobbyist_015;
			total_orders = 2;
		} else if (action.equals("init")) {
			total_orders = 2;
			switch (order) {
				case 0 :
					rid = R.drawable.hobbyist_000a;
					break;
				case 1 :
					rid = R.drawable.hobbyist_000b;
					break;
					
			}
		}
		
		if (rid != -1) {
			try {
				// get width and height of this display
		        Display display = getWindowManager().getDefaultDisplay();
		        DisplayMetrics dm = new DisplayMetrics();
		        display.getMetrics(dm);
				
		        Bitmap img = BitmapFactory.decodeResource(getResources(), rid);
		        
		        float ratio = (float) img.getHeight() / (float) img.getWidth();
		        int resizedH = (int) (ratio * dm.widthPixels);
		        	
		        LayoutParams lp = iv.getLayoutParams();
		        lp.height = resizedH;
		        iv.setLayoutParams(lp);
		        iv.setImageResource(rid);
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		final Button btnLeft = (Button) findViewById(R.id.buttonLeft);
		btnLeft.setOnClickListener(this);
		final Button btnRight = (Button) findViewById(R.id.buttonRight);
		btnRight.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonLeft :
				finish();
				break;
			case R.id.buttonRight :
				if (order < total_orders-1) {
					finish();
					Intent intent = new Intent (getApplicationContext(), PrototypeActivity.class);
					intent.putExtra(PrototypeActivity.ACTION, action);
					intent.putExtra(PrototypeActivity.ORDER, order+1);
					startActivity(intent);
				} else {
					finish();
					Intent intents = new Intent (getApplicationContext(),  MainActivity.class);
					startActivity(intents);
				}
					
				break;
		}
	}
}
