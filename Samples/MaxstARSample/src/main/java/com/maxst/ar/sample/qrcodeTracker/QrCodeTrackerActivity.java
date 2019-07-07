/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.qrcodeTracker;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.sample.ARActivity;
import com.maxst.ar.sample.R;
import com.maxst.ar.sample.util.SampleUtil;

import java.lang.ref.WeakReference;

public class QrCodeTrackerActivity extends ARActivity implements View.OnClickListener {

	private QrCodeTrackerRenderer QrCodeTargetRenderer;
	private GLSurfaceView glSurfaceView;
	private int preferCameraResolution = 0;
	private TextView recognizedQrCodeView;
	private RecognizedQrCodeHandler recognizedQrCodeHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qrcode_tracker);

		QrCodeTargetRenderer = new QrCodeTrackerRenderer(this);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView.setEGLContextClientVersion(2);
		glSurfaceView.setRenderer(QrCodeTargetRenderer);

		recognizedQrCodeView = (TextView)findViewById(R.id.recognized_QrCode);
		recognizedQrCodeHandler = new RecognizedQrCodeHandler(recognizedQrCodeView);

		preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();

		glSurfaceView.onResume();
		TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_QR_TRACKER);

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

		MaxstAR.onResume();

		recognizedQrCodeHandler.sendEmptyMessage(0);
	}

	@Override
	protected void onPause() {
		super.onPause();

		glSurfaceView.queueEvent(new Runnable() {
			@Override
			public void run() {
			}
		});

		glSurfaceView.onPause();

		TrackerManager.getInstance().stopTracker();
		CameraDevice.getInstance().stop();
		MaxstAR.onPause();

		recognizedQrCodeHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {

	}

	private static class RecognizedQrCodeHandler extends Handler {

		private WeakReference<TextView> targetViewWeakReference;

		private RecognizedQrCodeHandler(TextView targetView) {
			targetViewWeakReference = new WeakReference<TextView>(targetView);
		}

		@Override
		public void handleMessage(Message msg) {
			TextView targetView = targetViewWeakReference.get();
			if (targetView != null) {
				TrackingResult trackingResult = TrackerManager.getInstance().updateTrackingState().getTrackingResult();

				String recognizedQrCodeID = new String();

				if (trackingResult.getCount() < 1)
					targetView.setText("QRCODE : WAITING...");
				else {
					for (int i = 0; i < trackingResult.getCount(); i++) {
						recognizedQrCodeID += trackingResult.getTrackable(i).getName().toString() + ", ";
					}
					targetView.setText("QRCODE : " + recognizedQrCodeID);
				}

				sendEmptyMessageDelayed(0, 33);
			}
		}
	}
}
