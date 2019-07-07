/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.qrcodeTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;
import com.maxst.ar.sample.arobject.BackgroundRenderHelper;
import com.maxst.ar.sample.arobject.TexturedQuadRenderer;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;
import com.maxst.ar.sample.arobject.TexturedCubeRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class QrCodeTrackerRenderer implements Renderer {

	public static final String TAG = QrCodeTrackerRenderer.class.getSimpleName();

	private TexturedQuadRenderer texturedQuadRenderer0;
	private TexturedQuadRenderer texturedQuadRenderer1;
	private TexturedQuadRenderer texturedQuadRenderer2;

	private int surfaceWidth;
	private int surfaceHeight;
	private Yuv420spRenderer backgroundCameraQuad;

	private final Activity activity;
	private BackgroundRenderHelper backgroundRenderHelper;

	QrCodeTrackerRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		backgroundCameraQuad = new Yuv420spRenderer();

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_201_flammable_substance.jpg", activity.getAssets());
		texturedQuadRenderer0 = new TexturedQuadRenderer();
		texturedQuadRenderer0.setTextureBitmap(bitmap);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_207_high_voltage_warning.jpg", activity.getAssets());
		texturedQuadRenderer1 = new TexturedQuadRenderer();
		texturedQuadRenderer1.setTextureBitmap(bitmap);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_214_danger_place_warning.jpg", activity.getAssets());
		texturedQuadRenderer2 = new TexturedQuadRenderer();
		texturedQuadRenderer2.setTextureBitmap(bitmap);

		backgroundRenderHelper = new BackgroundRenderHelper();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;

		MaxstAR.onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

		TrackingState state = TrackerManager.getInstance().updateTrackingState();
		TrackingResult trackingResult = state.getTrackingResult();

		TrackedImage image = state.getImage();
		float[] backgroundPlaneProjectionMatrix = CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix();
		backgroundRenderHelper.drawBackground(image, backgroundPlaneProjectionMatrix);

		Log.e(TAG, "timestamp : " + image.getTimestamp());

		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		for (int i = 0; i < trackingResult.getCount(); i++) {
			Trackable trackable = trackingResult.getTrackable(i);

			Log.d(TAG, "Code name : " + trackable.getName() + ", id : " + trackable.getId());

			switch (trackable.getId()) {
				case "0":
					texturedQuadRenderer0.setProjectionMatrix(projectionMatrix);
					texturedQuadRenderer0.setTransform(trackable.getPoseMatrix());
					texturedQuadRenderer0.setTranslate(0, 0, -0.05f);
					texturedQuadRenderer0.setScale(1.0f, 1.0f, 0.1f);
					texturedQuadRenderer0.draw();
					break;

				case "1":
					texturedQuadRenderer1.setProjectionMatrix(projectionMatrix);
					texturedQuadRenderer1.setTransform(trackable.getPoseMatrix());
					texturedQuadRenderer1.setTranslate(0, 0, -0.05f);
					texturedQuadRenderer1.setScale(1.0f, 1.0f, 0.1f);
					texturedQuadRenderer1.draw();
					break;

				case "2":
					texturedQuadRenderer2.setProjectionMatrix(projectionMatrix);
					texturedQuadRenderer2.setTransform(trackable.getPoseMatrix());
					texturedQuadRenderer2.setTranslate(0, 0, -0.05f);
					texturedQuadRenderer2.setScale(1.0f, 1.0f, 0.1f);
					texturedQuadRenderer2.draw();
					break;
			}
		}
	}
}
