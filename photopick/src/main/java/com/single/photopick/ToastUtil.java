package com.single.photopick;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;


public class ToastUtil {

	private static Toast toast;
	private static String showingText;

	/**
	 * 显示toast，时长为Toast.LENGTH_SHORT
	 * 
	 * @param context
	 *            The context to use. Usually your Application or Activity
	 *            object.
	 * @param text
	 *            The text to show. Can be formatted text.
	 */
	public static void showToastShort(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_SHORT
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param resId
	 *            The resource id of the string resource to use. Can be
	 *            formatted text.
	 */
	public static void showToastShort(Context context, int resId) {
		showToast(context, context.getResources().getString(resId),
				Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_LONG
	 * 
	 * @param context
	 *            The context to use. Usually your Application or Activity
	 *            object.
	 * @param text
	 *            The text to show. Can be formatted text.
	 */
	public static void showToastLong(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_LONG
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param resId
	 *            The resource id of the string resource to use. Can be
	 *            formatted text.
	 */
	public static void showToastLong(Context context, int resId) {
		showToast(context, context.getResources().getString(resId),
				Toast.LENGTH_LONG);
	}

	/**
	 * 显示一个toast，在这个toast没有完全消失之前，不会再显示同样的toast
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param text
	 *            text The text to show. Can be formatted text.
	 * @param duration
	 *            {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
	 */
	private static void showToast(Context context, String text, int duration) {
		if (text != null && !text.equals(showingText)) {
			toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
			showingText = text;
			// 启动计时器，当toast消失后，将showingText置为null
			if (duration == Toast.LENGTH_SHORT) {
				newCountDownTimer(2000);
			} else if (duration == Toast.LENGTH_LONG) {
				newCountDownTimer(3500);
			}
		}
	}

	private static void newCountDownTimer(int time) {
		new CountDownTimer(time, time) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				showingText = null;
			}
		}.start();
	}

	/**
	 * cancel the toast </br> you can use in Activity's onDestory method
	 * 
	 */
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
