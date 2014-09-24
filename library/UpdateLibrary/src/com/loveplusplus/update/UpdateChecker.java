package com.loveplusplus.update;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class UpdateChecker extends Fragment {

	// private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
	private static final String NOTICE_TYPE_KEY = "type";
	// private static final String SUCCESSFUL_CHECKS_REQUIRED_KEY = "nChecks";
	private static final int NOTICE_NOTIFICATION = 2;
	private static final int NOTICE_DIALOG = 1;
	private static final String TAG = "UpdateChecker";
	private static final String NAME_OF_NODE = "NAME_OF_NODE";

	private FragmentActivity mContext;
	private Thread mThread;
	private int mTypeOfNotice;
	private String nameOfNode="jike";//更新文件version的xml节点中的name属性，区分不同的软件

	/**
	 * Show a Dialog if an update is available for download. Callable in a
	 * FragmentActivity. Number of checks after the dialog will be shown:
	 * default, 5
	 * 
	 * @param fragmentActivity
	 *            Required.
	 */
	public static void checkForDialog(FragmentActivity fragmentActivity,String nameOfNode) {
		FragmentTransaction content = fragmentActivity
				.getSupportFragmentManager().beginTransaction();
		UpdateChecker updateChecker = new UpdateChecker();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
		args.putString(NAME_OF_NODE, nameOfNode);
		// args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

	/**
	 * Show a Notification if an update is available for download. Callable in a
	 * FragmentActivity Specify the number of checks after the notification will
	 * be shown.
	 * 
	 * @param fragmentActivity
	 *            Required.
	 * @param notificationIconResId
	 *            R.drawable.* resource to set to Notification Icon.
	 */
	public static void checkForNotification(FragmentActivity fragmentActivity,String nameOfNode) {
		FragmentTransaction content = fragmentActivity
				.getSupportFragmentManager().beginTransaction();
		UpdateChecker updateChecker = new UpdateChecker();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
		args.putString(NAME_OF_NODE, nameOfNode);
		// args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
		// args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

	/**
	 * This class is a Fragment. Check for the method you have chosen.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = (FragmentActivity) activity;
		Bundle args = getArguments();
		mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
		nameOfNode=args.getString(NAME_OF_NODE);
		checkForUpdates();
	}

	/**
	 * Heart of the library. Check if an update is available for download
	 * parsing the desktop Play Store page of the app
	 */
	private void checkForUpdates() {
		mThread = new Thread() {
			@Override
			public void run() {
				// if (isNetworkAvailable(mContext)) {
				String info = sendPost();
				if (info != null) {
//					parseJson(info);
					try {
						List<UpdateNode> list= getNodesFromXml(new ByteArrayInputStream(
								info.getBytes("UTF-8")));
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getName().equals(nameOfNode)) {
								parseXml(list.get(i));
								break;
							}
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					Log.e(TAG, "can't get app update info");
				}
			}

		};
		mThread.start();
	}

	protected String sendPost() {
		HttpURLConnection uRLConnection = null;
		InputStream is = null;
		BufferedReader buffer = null;
		String result = null;
		try {
			URL url = new URL(Constants.APP_UPDATE_SERVER_URL);
			uRLConnection = (HttpURLConnection) url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setConnectTimeout(10 * 1000);
			uRLConnection.setReadTimeout(10 * 1000);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Connection", "Keep-Alive");
			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection
					.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uRLConnection
					.setRequestProperty("Content-Type", "application/json");
			uRLConnection.connect();

			is = uRLConnection.getInputStream();

			String content_encode = uRLConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}
			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			result = strBuilder.toString();
		} catch (Exception e) {
			Log.e(TAG, "http post error", e);
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (uRLConnection != null) {
				uRLConnection.disconnect();
			}
		}
		return result;
	}

	private void parseJson(String json) {
		mThread.interrupt();
		Looper.prepare();
		try {

			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
			String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
			int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

			int versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;

			if (apkCode > versionCode) {
				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					showNotification(updateMessage, apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
					showDialog(updateMessage, apkUrl);
				}
			} else {
				// Toast.makeText(mContext,
				// mContext.getString(R.string.app_no_new_update),
				// Toast.LENGTH_SHORT).show();
			}

		} catch (PackageManager.NameNotFoundException ignored) {
		} catch (JSONException e) {
			Log.e(TAG, "parse json error", e);
		}
	}

	/*
	 * 从节点中获取信息并显示更新对话框，或返回没有更新结果false
	 */
	private Boolean parseXml(UpdateNode myNode) {
		mThread.interrupt();
		Looper.prepare();
		try {
			String updateMessage = myNode.getContent();
			String apkUrl = myNode.getDownload_url();
			int apkCode = myNode.getVersionCode();// 以后的版本按照VersionCode更新，避免手机上显示的版本过高
			String apkVersionName = myNode.getVersion();// 老版本是按Version号更新的，如8.0.0

			int versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
			String versionName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;

			if (apkCode > versionCode
					|| apkVersionName.compareTo(versionName) > 0) {
				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					showNotification(updateMessage, apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
					showDialog(updateMessage, apkUrl);
				}
				return true;
			} else {
				// Toast.makeText(mContext,
				// mContext.getString(R.string.app_no_new_update),
				// Toast.LENGTH_SHORT).show();
				return false;
			}

		} catch (PackageManager.NameNotFoundException ignored) {
		} catch (Exception e) {
			Log.e(TAG, "parse json error", e);
		}
		return false;
	}

	/**
	 * Show dialog
	 * 
	 * @see Dialog#show(android.support.v4.app.FragmentActivity)
	 */
	public void showDialog(String content, String apkUrl) {
		Dialog d = new Dialog();
		Bundle args = new Bundle();
		args.putString(Constants.APK_UPDATE_CONTENT, content);
		args.putString(Constants.APK_DOWNLOAD_URL, apkUrl);
		d.setArguments(args);
		d.setCancelable(false);
		d.show(mContext.getSupportFragmentManager(), null);
	}

	/**
	 * Show Notification
	 * 
	 * @param info
	 * 
	 * @see Notification#show(android.content.Context, int)
	 */
	public void showNotification(String content, String apkUrl) {
		android.app.Notification noti;
		Intent myIntent = new Intent(mContext, DownloadService.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra(Constants.APK_DOWNLOAD_URL, apkUrl);
		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0,
				myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		int smallIcon = mContext.getApplicationInfo().icon;
		noti = new NotificationCompat.Builder(mContext)
				.setTicker(getString(R.string.newUpdateAvailable))
				.setContentTitle(getString(R.string.newUpdateAvailable))
				.setContentText(content).setSmallIcon(smallIcon)
				.setContentIntent(pendingIntent).build();

		noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti);
	}

	/**
	 * Check if a network available
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean connected = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				connected = ni.isConnected();
			}
		}
		return connected;
	}

	/**
	 * 获取所有节点的信息 参数fileName：为xml文档路径
	 */
	public List<UpdateNode> getNodesFromXml(InputStream inputStream) {
		List<UpdateNode> nodeList = new ArrayList<UpdateNode>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// 首先找到xml文件
		factory = DocumentBuilderFactory.newInstance();
		try {
			// 找到xml，并加载文档
			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
			// 找到根Element
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("soft");
			// 遍历根节点所有子节点
			UpdateNode node = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				node = new UpdateNode();
				Element softElement = (Element) (nodes.item(i));
				// 获取name属性值
				node.setName(softElement.getAttribute("name"));

				Element version = (Element) softElement.getElementsByTagName(
						"version").item(0);
				node.setVersion(version.getFirstChild().getNodeValue());
				Element content = (Element) softElement.getElementsByTagName(
						"content").item(0);
				node.setContent(content.getFirstChild().getNodeValue());
				Element download_url = (Element) softElement
						.getElementsByTagName("download_url").item(0);
				node.setDownload_url(download_url.getFirstChild()
						.getNodeValue());
				Element updatetime = (Element) softElement
						.getElementsByTagName("updatetime").item(0);
				node.setUpdatetime(updatetime.getFirstChild().getNodeValue());
				Element hotelcity = (Element) softElement.getElementsByTagName(
						"hotelcity").item(0);
				node.setHotelcity(Integer.valueOf(hotelcity.getFirstChild()
						.getNodeValue()));
				Element flightcity = (Element) softElement
						.getElementsByTagName("flightcity").item(0);
				node.setFlightcity(Integer.valueOf(flightcity.getFirstChild()
						.getNodeValue()));
				Element iflightcity = (Element) softElement
						.getElementsByTagName("iflightcity").item(0);
				node.setIflightcity(Integer.valueOf(iflightcity.getFirstChild()
						.getNodeValue()));
				Element traincity = (Element) softElement.getElementsByTagName(
						"traincity").item(0);
				node.setTraincity(Integer.valueOf(traincity.getFirstChild()
						.getNodeValue()));
				Element versionCode = (Element) softElement
						.getElementsByTagName("versionCode").item(0);
				node.setVersionCode(Integer.valueOf(versionCode.getFirstChild()
						.getNodeValue()));
				nodeList.add(node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return nodeList;
	}
}
