package com.example.imonitor_collect;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_main);
		
		 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	        // init the ListView and Adapter, nothing new
	        initListView();

	        // set a custom shadow that overlays the main content when the drawer
	        // opens
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
	                GravityCompat.START);

//	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//	                R.drawable.ic_drawer, R.string.drawer_open,
//	                R.string.drawer_close)
//	        {
//
//	            /** Called when a drawer has settled in a completely closed state. */
//	            public void onDrawerClosed(View view)
//	            {
//
//	                invalidateOptionsMenu(); // creates call to
//	                                            // onPrepareOptionsMenu()
//	            }
//
//	            /** Called when a drawer has settled in a completely open state. */
//	            public void onDrawerOpened(View drawerView)
//	            {
//
//	                invalidateOptionsMenu(); // creates call to
//	                                            // onPrepareOptionsMenu()
//	            }
//	        };
//
//	        // Set the drawer toggle as the DrawerListener
//	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_content, new PlaceholderFragment()).commit();
		}
		
	}
	private void initListView()
    {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                // Highlight the selected item, update the title, and close the
                // drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mPlanetTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public static class PlaceholderFragment extends Fragment implements Callback{
		private SurfaceHolder sh;
		private SurfaceView sv;
		private Camera mCamera;
		private int cameraId=1;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			sv = (SurfaceView)this.getActivity().findViewById(R.id.monitor_sv1);
			sh = sv.getHolder();
			sh.addCallback(this);
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			cameraId = 0;// default id
			OpenCameraAndSetSurfaceviewSize(cameraId);
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			kill_camera();
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			kill_camera();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			SetAndStartPreview(holder);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
		}
		private Void SetAndStartPreview(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private Void OpenCameraAndSetSurfaceviewSize(int cameraId) {
			mCamera = Camera.open(cameraId);

			sv.getLayoutParams().height = mCamera.getParameters().getPreviewSize().height/2;
			sv.getLayoutParams().width = mCamera.getParameters().getPreviewSize().width/2;
			return null;
		}

		private Void kill_camera() {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
			return null;
		}
	}
}
