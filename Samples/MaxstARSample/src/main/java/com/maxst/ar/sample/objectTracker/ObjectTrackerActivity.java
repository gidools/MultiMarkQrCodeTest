/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.objectTracker;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

public class ObjectTrackerActivity extends ARActivity {

	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private ObjectTrackerRenderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_tracker);

		renderer = new ObjectTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(renderer);

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();

		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_OBJECT);
//		TrackerManager.getInstance().setTrackingOption(TrackerManager.TrackingOption.NORMAL_TRACKING);

		TrackerManager.getInstance().addTrackerData("2019.3dmap", true);
		TrackerManager.getInstance().loadTrackerData();

		ResultCode resultCode = ResultCode.Success;

		switch (preferCameraResolution) {
			case 0:
				resultCode = CameraDevice.getInstance().start(0, 640, 480);
				break;

			case 1:
				resultCode = CameraDevice.getInstance().start(0, 1280, 720);
				break;

			case 2:
				resultCode = CameraDevice.getInstance().start(0, 1920, 1080);
				break;
		}

		if (resultCode != ResultCode.Success) {
			Toast.makeText(this, R.string.camera_open_fail, Toast.LENGTH_SHORT).show();
			finish();
		}

		//CameraDevice.getInstance().setAutoWhiteBalanceLock(true); // For ODG-R7 preventing camera flickering

		MaxstAR.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();

		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();

		MaxstAR.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
