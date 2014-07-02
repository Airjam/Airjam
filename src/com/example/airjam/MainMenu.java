package com.example.airjam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainMenu extends Activity {

	public Animation fadeOutButton(final ImageButton fadingOut) {
		Animation fadeOut = new AlphaAnimation(1, 0);
	    fadeOut.setInterpolator(new AccelerateInterpolator());
	    fadeOut.setDuration(300);

	    fadeOut.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	                  fadingOut.setVisibility(View.GONE);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });
	    
	    return fadeOut;
	}
	
	public Animation jamButtonSlide(final Button jamButton, final Button actualButton, final int height) {
	    Animation jamButtonSlide = new TranslateAnimation(0,0,0, -(height/3));
	    jamButtonSlide.setInterpolator(new AccelerateInterpolator());
	    jamButtonSlide.setDuration(300);
	    jamButtonSlide.setFillAfter(true);
	    
	    jamButtonSlide.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	                  jamButton.setVisibility(View.GONE);
	                  actualButton.setVisibility(View.VISIBLE);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });
	    
	    return jamButtonSlide;
	}

	public Animation menuButtonSlide(final Button configButton, final Button directionsButton,
									 final Button configButtonActual, final Button directionsButtonActual, 
									 final int dist, final boolean needsOffset) {
	    Animation buttonSlide = new TranslateAnimation(0,dist,0,0);
	    buttonSlide.setInterpolator(new AccelerateInterpolator());
	    buttonSlide.setDuration(300);
	    buttonSlide.setFillAfter(true);
	    
	    if(needsOffset) {
	    	buttonSlide.setStartOffset(100);
	    	
		    buttonSlide.setAnimationListener(new AnimationListener()
		    {
		            public void onAnimationEnd(Animation animation) 
		            {
		                  configButton.setVisibility(View.GONE);
		                  directionsButton.setVisibility(View.GONE);
		                  configButtonActual.setVisibility(View.VISIBLE);
		                  directionsButtonActual.setVisibility(View.VISIBLE);
		            }
		            public void onAnimationRepeat(Animation animation) {}
		            public void onAnimationStart(Animation animation) {}
		    });
	    }
	    
	    return buttonSlide;
	}
	
	public void reset(final Button jamButton, final Button configButton, final Button directionsButton,
					  final ImageButton mainButton) {
		Animation fadeOut = new AlphaAnimation(1, 0);
	    fadeOut.setInterpolator(new AccelerateInterpolator());
	    fadeOut.setDuration(300);

	    fadeOut.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	                  jamButton.setVisibility(View.GONE);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });
	    jamButton.startAnimation(fadeOut);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_menu);
		
		final FirstClick firstClick = new FirstClick();
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		final int width = metrics.widthPixels;
		final int height = metrics.heightPixels;
		
		View leftSide = (View) findViewById(R.id.leftSide);
		
		leftSide.setLayoutParams(new RelativeLayout.LayoutParams(width/2, height));
		
		final ImageButton drumsButton = (ImageButton) findViewById(R.id.button);
		final ImageButton guitarButton = (ImageButton) findViewById(R.id.button2);
		final Button jamButton = (Button) findViewById(R.id.jamButtonSprite);
		final Button jamButtonActual = (Button) findViewById(R.id.jamButton);
		final Button configButton = (Button) findViewById(R.id.configButtonSprite);
		final Button directionsButton = (Button) findViewById(R.id.directionsButtonSprite);
		final Button configButtonActual = (Button) findViewById(R.id.configButton);
		final Button directionsButtonActual = (Button) findViewById(R.id.directionsButton);
		
		drumsButton.setLayoutParams(new RelativeLayout.LayoutParams(width/3, width/3));
		guitarButton.setLayoutParams(new RelativeLayout.LayoutParams(width/3, width/3));
		jamButton.setLayoutParams(new RelativeLayout.LayoutParams(width/2, height/3));
		jamButtonActual.setLayoutParams(new RelativeLayout.LayoutParams(width/2, height/3));
		configButton.setLayoutParams(new RelativeLayout.LayoutParams(width/3, height/7));
		directionsButton.setLayoutParams(new RelativeLayout.LayoutParams(width/3, height/7));
		configButtonActual.setLayoutParams(new RelativeLayout.LayoutParams(width/3, height/7));
		directionsButtonActual.setLayoutParams(new RelativeLayout.LayoutParams(width/3, height/7));
		
		drumsButton.setX(width/4-(width/6));
		guitarButton.setX(3*(width/4)-(width/6));
		jamButton.setX(width/2);
		jamButtonActual.setX(width/2);
		configButton.setX(width-(width/4)-(width/6)+width/2);
		directionsButton.setX(width-(width/4)-(width/6)+width/2);
		configButtonActual.setX(width-(width/4)-(width/6));
		directionsButtonActual.setX(width-(width/4)-(width/6));
		
		drumsButton.setY(height/2-(width/6));
		guitarButton.setY(height/2-(width/6));
		jamButton.setY(height);
		jamButtonActual.setY(height-(height/3));
		configButton.setY(height/8);
		directionsButton.setY(configButton.getY()+height/7+70);
		configButtonActual.setY(height/8);
		directionsButtonActual.setY(configButton.getY()+height/7+70);
		
		jamButtonActual.setVisibility(View.GONE);
		configButtonActual.setVisibility(View.GONE);
		directionsButtonActual.setVisibility(View.GONE);
		
		
		drumsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(firstClick.getValue()) {
					firstClick.setValue(false);
				    guitarButton.startAnimation(fadeOutButton(guitarButton));
				    jamButton.startAnimation(jamButtonSlide(jamButton, jamButtonActual, height));
				    configButton.startAnimation(menuButtonSlide(configButton, directionsButton, configButtonActual, directionsButtonActual, -(width/2), false));
				    directionsButton.startAnimation(menuButtonSlide(configButton, directionsButton, configButtonActual, directionsButtonActual, -(width/2), true));
				
					jamButtonActual.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent startDrums = new Intent("com.example.airjam.DRUMS");
							startActivity(startDrums);
						}
					});
					
					configButtonActual.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent startDrumsConfig = new Intent("com.example.airjam.DRUMS_CONFIG");
							startActivity(startDrumsConfig);
						}
					});
					
					directionsButtonActual.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent startDrumsDirections = new Intent("com.example.airjam.DRUMS_DIRECTIONS");
							startActivity(startDrumsDirections);
						}
					});
				} else {
					reset(jamButtonActual, configButtonActual, directionsButtonActual, guitarButton);
				}
			}
			
		});

		guitarButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jamButton.setX(0);
				jamButtonActual.setX(0);
				configButton.setX((width/4)-(width/6)-width/2);
				directionsButton.setX((width/4)-(width/6)-width/2);
				configButtonActual.setX((width/4)-(width/6));
				directionsButtonActual.setX((width/4)-(width/6));			    
			    
			    drumsButton.startAnimation(fadeOutButton(drumsButton));
			    jamButton.startAnimation(jamButtonSlide(jamButton, jamButtonActual, height));
			    configButton.startAnimation(menuButtonSlide(configButton, directionsButton, configButtonActual, directionsButtonActual, (width/2), false));
			    directionsButton.startAnimation(menuButtonSlide(configButton, directionsButton, configButtonActual, directionsButtonActual, (width/2), true));
			  
				jamButtonActual.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent startDrums = new Intent("com.example.airjam.GUITAR");
						startActivity(startDrums);
					}
				});
				
				configButtonActual.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent startDrumsConfig = new Intent("com.example.airjam.GUITAR_CONFIG");
						startActivity(startDrumsConfig);
					}
				});
				
				directionsButtonActual.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent startDrumsDirections = new Intent("com.example.airjam.GUITAR_DIRECTIONS");
						startActivity(startDrumsDirections);
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
