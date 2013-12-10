package com.skplanet.hobbyist;


import android.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {
	
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
		
		// 		        
		Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        System.setProperty("width", String.valueOf(dm.widthPixels));
        System.setProperty("height", String.valueOf(dm.heightPixels));
        
        if (System.getProperty("init") == null) {
        	System.setProperty("init", "1");
        	Intent intent = new Intent (getApplicationContext(), PrototypeActivity.class);
			intent.putExtra(PrototypeActivity.ACTION, "init");
			intent.putExtra(PrototypeActivity.ORDER, 0);
			startActivity(intent);
        }
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
		args.putInt(DummySectionFragment.ARG_MAIN_TAB_NUMBER, 0);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		
		return true;
	}
	

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment implements OnClickListener{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final int DYNAMIC_TOPIC_ID = 0x8000;
		public static final int DYNAMIC_IMAGE_ID = 0x6000;
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String ARG_MAIN_TAB_NUMBER = "main_tab_number";
		public static final String ARG_PROTOTYPE_IMAGE_RID = "prototype_image_rid";
		
		public static final int HOME = 0;
		public static final int DISCOVER = 1;
		public static final int PROFILE = 2;
		public static final int PROTOTYPE = 3;
		

		public DummySectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			int width = Integer.parseInt(System.getProperty("width", "640"));
			View rootView = inflater.inflate(R.layout.fragment_main_postlist, container, false);
			
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
				case HOME:
					LinearLayout topic_tap_layout = (LinearLayout) rootView.findViewById(R.id.topic_tap_list);
					
					String [] topicList = getResources().getStringArray(R.array.topics_list);
					Button[] buttons = new Button [topicList.length];
					for (int i=0; i<topicList.length; i++) {
						buttons[i] = new Button(getActivity());
						buttons[i].setId(DYNAMIC_TOPIC_ID+i);
						buttons[i].setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));
						buttons[i].setBackgroundColor(0xd8edf5);
						buttons[i].setText(topicList[i]);
						
						topic_tap_layout.addView(buttons[i]);
						buttons[i].setOnClickListener(this);
					}
					
					Button comp_btn = (Button) rootView.findViewById(R.id.button_compose);
					comp_btn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent (getActivity().getApplicationContext(), PrototypeActivity.class);
							
							intent.putExtra(PrototypeActivity.ACTION, "compose");
							intent.putExtra(PrototypeActivity.ORDER, 0);
							startActivity(intent);
						}
					});
					
					// add post list
					LinearLayout postlayout = (LinearLayout) rootView.findViewById(R.id.postListLayout);
					int tab_index = getArguments().getInt(ARG_MAIN_TAB_NUMBER);
					String ptList[] = getResources().getStringArray(R.array.topics_name_list);
					int postOrderRid = getResources().getIdentifier(ptList[tab_index], null, getActivity().getPackageName());
					
					// add each post images 
					String postOrderList[] = getResources().getStringArray(postOrderRid);
					for (int i=0; i<postOrderList.length; i++) {
						View postListView = inflater.inflate(R.layout.post_list_view, container, false);
						LinearLayout postImgListView = (LinearLayout)postListView.findViewById(R.id.post_img_lists);
						
						int imgtOrderRid = getResources().getIdentifier(postOrderList[i], null, getActivity().getPackageName());
						String imgOrderList[] = getResources().getStringArray(imgtOrderRid);
						
/*						int nid = Integer.parseInt(imgOrderList[0], 16); */
						for (int j=1;j<imgOrderList.length;j++) {
							int resId = getResources().getIdentifier(imgOrderList[j], null, getActivity().getPackageName());
							
							ImageView postImgView = new ImageView(getActivity());
							postImgView.setLayoutParams(new LayoutParams(width, 440*width/620));
							postImgView.setImageResource(resId);
							postImgView.setClickable(true);
							postImgView.setId(DYNAMIC_IMAGE_ID);
							postImgView.setScaleType(ScaleType.FIT_XY);
							postImgView.setOnClickListener(this);
							
							postImgListView.addView(postImgView);
						}
						
						
						postlayout.addView(postListView);
					}
					break;
				case PROFILE:
					rootView = inflater.inflate(R.layout.fragment_profile, container, false);
					ImageView profile_img = (ImageView) rootView.findViewById(R.id.imageView_profile);
					LayoutParams lp = profile_img.getLayoutParams();
					lp.height = 1189*width/639;
					profile_img.setLayoutParams(lp);
					
					Button profile_btn = (Button) rootView.findViewById(R.id.button_profile_topic);
					profile_btn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent (getActivity().getApplicationContext(), PrototypeActivity.class);
							
							intent.putExtra(PrototypeActivity.ACTION, "profile");
							intent.putExtra(PrototypeActivity.ORDER, 0);
							startActivity(intent);
						}
					});
					
					break;
					
				case DISCOVER:
					rootView = inflater.inflate(R.layout.fragment_search, container, false);
					ImageView dis_img = (ImageView) rootView.findViewById(R.id.imageView_discover);
					LayoutParams dis_lp = dis_img.getLayoutParams();
					dis_lp.height = 1003*width/639;
					dis_img.setLayoutParams(dis_lp);
					break;
					
				case PROTOTYPE:
					rootView = inflater.inflate(R.layout.fragment_prototype, container, false);
					ImageView protImg = (ImageView)rootView.findViewById(R.id.imageView_profile);
					
					protImg.setImageResource(getArguments().getInt(ARG_PROTOTYPE_IMAGE_RID));
					
					break;
			}

			return rootView;
		}


		@Override
		public void onClick(View v) {
			if (v.getId() == DYNAMIC_IMAGE_ID) {
				Intent intent = new Intent (getActivity().getApplicationContext(), PrototypeActivity.class);
				
				intent.putExtra(PrototypeActivity.ACTION, "compose");
				intent.putExtra(PrototypeActivity.ORDER, 8);
				startActivity(intent);
			}
			
			int topic_id = v.getId() - DYNAMIC_TOPIC_ID;
			
			if (topic_id >= 0) {
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, DummySectionFragment.HOME);
				
				args.putInt(DummySectionFragment.ARG_MAIN_TAB_NUMBER, topic_id);
				
				fragment.setArguments(args);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
			} 
		}
	}
}
