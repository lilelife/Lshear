package com.example.PersonInfoSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lshare.MainActivity.Type;
import com.example.lshare.R;

import java.io.File;

public class MPoPuWindow extends PopupWindow implements OnClickListener {

	public Context mContext;

	private Type type;

	public Activity mActivity;

	private File file;
	private Uri ImgUri;

	private TextView mTakePhoto, mAlbumPhoto, mCancel;

	public MPoPuWindow(Context context, Activity mActivity) {
		initView(context);
		this.mActivity = mActivity;
	}

	private void initView(Context mContext) {
		this.mContext = mContext;
		View v = LayoutInflater.from(mContext).inflate(R.layout.activity_popu,
				null);
		setContentView(v);

		mTakePhoto = (TextView) v.findViewById(R.id.photo_take);
		mAlbumPhoto = (TextView) v.findViewById(R.id.photo_album);
		mCancel = (TextView) v.findViewById(R.id.photo_cancel);

		mTakePhoto.setOnClickListener(this);
		mAlbumPhoto.setOnClickListener(this);
		mCancel.setOnClickListener(this);

		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ScreenUtils.getScreenHeight(mContext));

		// 设置SelectPicPopupWindow弹出窗体可点�?
		this.setTouchable(true);
		this.setFocusable(true);
		this.setOutsideTouchable(true);

		// 刷新状�?
		this.update();
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popuwindow_from_bottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x50000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	public void showPopupWindow(View parent) {

		if (!this.isShowing()) {
			this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		} else {
			this.dismiss();

		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.photo_take:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			file = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".jpg");
			ImgUri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, ImgUri);
			mActivity.startActivityForResult(intent, 1);
			type = Type.CAMERA;
			if (listener != null) {
				listener.getType(type);
				listener.getImgUri(ImgUri, file);
			}

			this.dismiss();
			break;
		case R.id.photo_album:
			Intent intent2 = new Intent("android.intent.action.PICK");
			intent2.setType("image/*");
			mActivity.startActivityForResult(intent2, 2);
			type = Type.PHONE;
			if (listener != null) {
				listener.getType(type);
			}
			this.dismiss();
			break;
		case R.id.photo_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	public void onPhoto(Uri uri, int outputX, int outputY) {
		Intent intent = null;

		intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("circleCrop", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		mActivity.startActivityForResult(intent, 3);
	}

	public interface onGetTypeClckListener {
		void getType(Type type);

		void getImgUri(Uri ImgUri, File file);
	}

	private onGetTypeClckListener listener;

	public void setOnGetTypeClckListener(onGetTypeClckListener listener) {
		this.listener = listener;
	}

}
