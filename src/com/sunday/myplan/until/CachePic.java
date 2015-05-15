package com.sunday.myplan.until;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.sunday.myplan.io.DiskLruCache;

@SuppressLint("NewApi")
public class CachePic {
	DiskLruCache mDiskLruCache = null;
	Context context;
	public CachePic(Context context,String planName){
		this.context=context;
		openCache(planName);
	}



	private void openCache(String planName) {

		try {  
			File cacheDir = getDiskCacheDir(context, planName);  
			if (!cacheDir.exists()) {  
				cacheDir.mkdirs();  
			}  
			mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);  
		} catch (IOException e) {  
			e.printStackTrace();  
		}
	}
	public  void wirteCache( Uri u) {
		final ContentResolver cr = context.getContentResolver();
		final Uri uri=u;
		new Thread(new Runnable() {  
			@Override  
			public void run() {  
				try {  
					String key = hashKeyForDisk(uri.toString());  
					DiskLruCache.Editor editor = mDiskLruCache.edit(key);  
					if (editor != null) {  
						OutputStream outputStream = editor.newOutputStream(0);
						//修改inputstream
						//修改outoutstream
						if (downloadUrlToStream(cr.openInputStream(uri), outputStream)) {  
							editor.commit();  
						} else {  
							editor.abort();  
						}  
					}  
					mDiskLruCache.flush();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
		}).start();
	}
	public Bitmap readCache(String key)
	{ 
		Bitmap bitmap=null;
		try {  
		    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);  
		    if (snapShot != null) {  
		        InputStream is = snapShot.getInputStream(0);  
		        bitmap = BitmapFactory.decodeStream(is);  
		    }  
		} catch (IOException e) {  
		    e.printStackTrace();  
		}  
		return bitmap;
		
	}

	private boolean downloadUrlToStream(InputStream inputStream, OutputStream outputStream) {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(inputStream, 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	private String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	private File getDiskCacheDir(Context context, String uniqueName) {  
		String cachePath;  
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())  
				|| !Environment.isExternalStorageRemovable()) {  
			cachePath = context.getExternalCacheDir().getPath();  
		} else {  
			cachePath = context.getCacheDir().getPath();  
		}  
		return new File(cachePath + File.separator + uniqueName);  

	}
}