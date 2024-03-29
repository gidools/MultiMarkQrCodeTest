/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */
package com.maxst.ar.sample.markerTracker;

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
import com.maxst.ar.sample.arobject.BaseRenderer;
import com.maxst.ar.sample.arobject.TexturedQuadRenderer;
import com.maxst.ar.sample.arobject.Yuv420spRenderer;

import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


class MarkerTrackerRenderer implements Renderer {

	public static final String TAG = MarkerTrackerRenderer.class.getSimpleName();

	private TexturedQuadRenderer texturedQuadRenderer;
	private TexturedQuadRenderer texturedQuadRenderer1;
	private TexturedQuadRenderer texturedQuadRenderer2;
	private HashMap<String, TexturedQuadRenderer> rendererHashMap = new HashMap<>();

	private int surfaceWidth;
	private int surfaceHeight;
	private Yuv420spRenderer backgroundCameraQuad;

	private final Activity activity;
	private BackgroundRenderHelper backgroundRenderHelper;

	MarkerTrackerRenderer(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		backgroundCameraQuad = new Yuv420spRenderer();

		Bitmap bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_201_flammable_substance.jpg", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("0", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_207_high_voltage_warning.jpg", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("1", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/sign_214_danger_place_warning.jpg", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("2", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠1_관계자외출입금지.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.0f, -0.27f, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("3", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠2_고온주의.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.0f, -0.27f, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("4", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠3_감김끼임주의.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(0.0f, -0.27f, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("5", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠4_배관파손주의.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(-0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("6", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠5_발걸림주의.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(-0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("7", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠6_머리조심.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(-0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("8", texturedQuadRenderer);

		bitmap = MaxstARUtil.getBitmapFromAsset("warning_icon/추가콘텐츠7_추락주의.JPG", activity.getAssets());
		texturedQuadRenderer = new TexturedQuadRenderer();
		texturedQuadRenderer.setTextureBitmap(bitmap);
		texturedQuadRenderer.setTranslate(-0.27f, -0, -0);
		texturedQuadRenderer.setScale(0.4f, 0.4f, 0.1f);
		rendererHashMap.put("9", texturedQuadRenderer);

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

		float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		for (int i = 0; i < trackingResult.getCount(); i++) {
			Trackable trackable = trackingResult.getTrackable(i);

			Log.d(TAG, "Marker name : " + trackable.getName() + ", id : " + trackable.getId());

			if (rendererHashMap.containsKey(trackable.getId())) {
				BaseRenderer renderer = rendererHashMap.get(trackable.getId());
				renderer.setProjectionMatrix(projectionMatrix);
				renderer.setTransform(trackable.getPoseMatrix());
				renderer.draw();
			}
		}
	}
}
