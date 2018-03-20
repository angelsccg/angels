package com.example.angelsdemo.activity.control;

import android.os.Bundle;
import android.view.Window;

import com.angels.widget.RoundProgressBar;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class RoundProgressActivity extends BaseActivity{


	private int mTotalProgress;
	private int mCurrentProgress;
	private RoundProgressBar rpBar01, rpBar02 ,rpBar03, rpBar04;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_circle_progress);
		initVariable();
		viewInit();

		new Thread(new ProgressRunable()).start();
	}

	private void initVariable() {
		mTotalProgress = 100;
		mCurrentProgress = 0;
	}

	private void viewInit() {
		rpBar01 = (RoundProgressBar) findViewById(R.id.roundProgressBar01_id);
		rpBar02 = (RoundProgressBar) findViewById(R.id.roundProgressBar02_id);
		rpBar03 = (RoundProgressBar) findViewById(R.id.roundProgressBar03_id);
		rpBar04 = (RoundProgressBar) findViewById(R.id.roundProgressBar04_id);
	}

	class ProgressRunable implements Runnable {

		@Override
		public void run() {

			while (mCurrentProgress < mTotalProgress) {
				mCurrentProgress += 1;

				rpBar01.setProgress(mCurrentProgress);
				rpBar02.setProgress(mCurrentProgress);
				rpBar03.setProgress(mCurrentProgress);
				rpBar04.setProgress(mCurrentProgress);

				try {
					Thread.sleep(100);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
