package net.simonvt.messagebar;

import java.util.LinkedList;

import com.mysoft.autosendsms.R;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class MessageBar {

	public interface OnMessageClickListener {

		void onMessageClick(Parcelable token);
	}

	private static final String STATE_MESSAGES = "net.simonvt.messagebar.MessageBar.messages";
	private static final String STATE_CURRENT_MESSAGE = "net.simonvt.messagebar.MessageBar.currentMessage";

	private static final long ANIMATION_DURATION = 600;

	private long hide_delay = 5000;

	private boolean immediately = false;

//	private WindowManager mWindowManager;
//	private WindowManager.LayoutParams wmParams;
	private View barView;
	private View mContainer;

	private TextView mTextView;

	private TextView mButton;

	private LinkedList<Message> mMessages = new LinkedList<Message>();

	private Message mCurrentMessage;

	private boolean mShowing;

	private OnMessageClickListener mClickListener;

	private Handler mHandler;

	private AlphaAnimation mFadeInAnimation;

	private AlphaAnimation mFadeOutAnimation;

	public MessageBar(Activity activity) {
//		mWindowManager = (WindowManager) activity.getApplication().getSystemService(Context.WINDOW_SERVICE);
//		wmParams = new WindowManager.LayoutParams();
//		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//		wmParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
//		wmParams.y = 100;
//		wmParams.format = PixelFormat.TRANSLUCENT;
//		wmParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
//				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		 ViewGroup container = (ViewGroup)
		 activity.findViewById(android.R.id.content);
		barView = activity.getLayoutInflater().inflate(R.layout.mb__messagebar, container);
		init(barView);
	}

	public void setImmediately(boolean immediately) {
		this.immediately = immediately;
	}

	public void setDuration(long duration) {
		this.hide_delay = duration;
	}

	private void init(View v) {
		mContainer = v.findViewById(R.id.mbContainer);
		mContainer.setVisibility(View.GONE);
		mTextView = (TextView) v.findViewById(R.id.mbMessage);
		mButton = (TextView) v.findViewById(R.id.mbButton);
		mButton.setOnClickListener(mButtonListener);

		mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
		mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
		mFadeInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		mFadeOutAnimation.setDuration(ANIMATION_DURATION);
		mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Message nextMessage = mMessages.poll();

				if (nextMessage != null) {
					show(nextMessage);
				} else {
					mCurrentMessage = null;
					mContainer.setVisibility(View.GONE);
					mShowing = false;
//					barView.setVisibility(View.GONE);
//					mWindowManager.removeView(barView);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		mHandler = new Handler();
	}

	public void show(String message) {
		show(message, null);
	}

	public void show(String message, String actionMessage) {
		show(message, actionMessage, 0);
	}

	public void show(String message, String actionMessage, int actionIcon) {
		show(message, actionMessage, actionIcon, null);
	}

	public void show(String message, String actionMessage, int actionIcon, Parcelable token) {
		Message m = new Message(message, actionMessage, actionIcon, token);
		if (mShowing) {
			mMessages.add(m);
		} else {
			show(m);
		}
	}

	private void show(Message message) {
		show(message, immediately);
	}

	private void show(Message message, boolean immediately) {
//		mWindowManager.addView(barView, wmParams);
		mShowing = true;
		barView.setVisibility(View.VISIBLE);
		mContainer.setVisibility(View.VISIBLE);
		mCurrentMessage = message;
		mTextView.setText(message.mMessage);
		if (message.mActionMessage != null) {
			mTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			mButton.setVisibility(View.VISIBLE);
			mButton.setText(message.mActionMessage);

			mButton.setCompoundDrawablesWithIntrinsicBounds(message.mActionIcon, 0, 0, 0);
		} else {
			mTextView.setGravity(Gravity.CENTER);
			mButton.setVisibility(View.GONE);
		}

		if (immediately) {
			mFadeInAnimation.setDuration(0);
		} else {
			mFadeInAnimation.setDuration(ANIMATION_DURATION);
		}
		mContainer.startAnimation(mFadeInAnimation);
		mHandler.postDelayed(mHideRunnable, hide_delay);
	}

	private final View.OnClickListener mButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mClickListener != null && mCurrentMessage != null) {
				mClickListener.onMessageClick(mCurrentMessage.mToken);
				mCurrentMessage = null;
				mHandler.removeCallbacks(mHideRunnable);
				mHideRunnable.run();
			}
		}
	};

	public void setOnClickListener(OnMessageClickListener listener) {
		mClickListener = listener;
	}
	
	public void hide(){
		mHideRunnable.run();
	}

	public void clear() {
		mMessages.clear();
		mHideRunnable.run();
	}

	private final Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mContainer.startAnimation(mFadeOutAnimation);
		}
	};

	public void onRestoreInstanceState(Bundle state) {
		Message currentMessage = state.getParcelable(STATE_CURRENT_MESSAGE);
		if (currentMessage != null) {
			show(currentMessage, true);
			Parcelable[] messages = state.getParcelableArray(STATE_MESSAGES);
			for (Parcelable p : messages) {
				mMessages.add((Message) p);
			}
		}
	}

	public Bundle onSaveInstanceState() {
		Bundle b = new Bundle();

		b.putParcelable(STATE_CURRENT_MESSAGE, mCurrentMessage);

		final int count = mMessages.size();
		final Message[] messages = new Message[count];
		int i = 0;
		for (Message message : mMessages) {
			messages[i++] = message;
		}

		b.putParcelableArray(STATE_MESSAGES, messages);

		return b;
	}

	private static class Message implements Parcelable {

		final String mMessage;

		final String mActionMessage;

		final int mActionIcon;

		final Parcelable mToken;

		public Message(String message, String actionMessage, int actionIcon, Parcelable token) {
			mMessage = message;
			mActionMessage = actionMessage;
			mActionIcon = actionIcon;
			mToken = token;
		}

		public Message(Parcel p) {
			mMessage = p.readString();
			mActionMessage = p.readString();
			mActionIcon = p.readInt();
			mToken = p.readParcelable(getClass().getClassLoader());
		}

		public void writeToParcel(Parcel out, int flags) {
			out.writeString(mMessage);
			out.writeString(mActionMessage);
			out.writeInt(mActionIcon);
			out.writeParcelable(mToken, 0);
		}

		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
			public Message createFromParcel(Parcel in) {
				return new Message(in);
			}

			public Message[] newArray(int size) {
				return new Message[size];
			}
		};
	}
}
