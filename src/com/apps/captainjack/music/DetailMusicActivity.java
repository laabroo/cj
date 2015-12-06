package com.apps.captainjack.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.captainjack.R;
import com.apps.captainjack.adapter.ListViewAdapter;
import com.apps.captainjack.domain.Music;
import com.apps.captainjack.domain.Song;
import com.apps.captainjack.news.NewsActivity;
import com.apps.captainjack.util.OdeNetwork;
import com.apps.captainjack.util.Shortcut;
import com.loopj.android.image.SmartImageView;

public class DetailMusicActivity extends FragmentActivity {

	private ActionBar actionBar;
	private ListView listView;
	private ProgressDialog dialog;
	private static final String TAG = "DetailMusicActivity";
	private MediaPlayer mp;
	public static int currentSongIndex = -1;
	private ArrayList<Song> songlist = new ArrayList<Song>();
	private SmartImageView coverAlbum;
	private TextView labelTitle;
	private int posisiAwal = -1;
	private boolean play = true;
	private boolean pause = false;
	private TextView labelNP;
	private List<View> views;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_music);
		mp = new MediaPlayer();
		mp.reset();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Intent intent = getIntent();
		Music music = (Music) intent.getSerializableExtra("music");
		songlist = music.getSongs();

		actionBar = getActionBar();
		actionBar.hide();
		initComponent();
		coverAlbum.setImageUrl(music.getThumbnail());
		labelTitle.setText(music.getTitle());
		fillData(songlist);

	}

	private void initComponent() {
		dialog = new ProgressDialog(DetailMusicActivity.this);
		dialog.setMessage(getString(R.string.wait));
		dialog.setCancelable(true);
		listView = (ListView) findViewById(R.id.listView);
		labelNP = (TextView) findViewById(R.id.labelNP);
		labelTitle = (TextView) findViewById(R.id.textTitle);
		coverAlbum = (SmartImageView) findViewById(R.id.coverAlbum);

		LinearLayout layBack = (LinearLayout) findViewById(R.id.layBack);
		layBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DetailMusicActivity.this.finish();
			}
		});
	}

	private void fillData(final ArrayList<Song> musicslist) {
		if (musicslist.size() > 0) {
			TextView labelTitle;
			ImageButton btnPlay;
			LayoutInflater inflater = LayoutInflater
					.from(DetailMusicActivity.this);
			View view;
			views = new ArrayList<View>();
			for (int i = 0; i < musicslist.size(); i++) {
				view = inflater.inflate(R.layout.item_song, null, false);
				if (i % 2 == 1) {
					view.setBackgroundResource(R.drawable.bg_list_coklat);
				} else {
					view.setBackgroundResource(R.drawable.bg_list_putih);
				}
				labelTitle = (TextView) view.findViewById(R.id.textTitle);
				btnPlay = (ImageButton) view.findViewById(R.id.buttonPlay);
				String songTitle = musicslist.get(i).getTitle();

				labelTitle.setText(songTitle);

				views.add(view);
			}
			listView.setAdapter(new ListViewAdapter(views));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int pos, long id) {
					Log.i(TAG, "Select index : " + pos);

					// make button play to stop or otherwise
					if (OdeNetwork.isNetAvailable(DetailMusicActivity.this) == true) {
						setNormalButton();
						posisiAwal = pos;
						Log.i(TAG, "Status media : " + mp.isPlaying());
						playSong(pos);
					} else {
						Shortcut.ToastShort(DetailMusicActivity.this,
								"Please check your network connection");
					}
				}
			});

		}
	}

	private void setNormalButton() {
		if (posisiAwal != -1) {
			View viewObject = views.get(posisiAwal);
			final ImageButton btn = (ImageButton) viewObject
					.findViewById(R.id.buttonPlay);
			btn.setImageResource(R.drawable.play);

		}
	}

	private void setButtonAsPause(int index) {
		Log.e(TAG, "Posisi : " + index);
		View viewObject = views.get(index);
		// View viewObject = listView.getChildAt(index);
		final ImageButton btn = (ImageButton) viewObject
				.findViewById(R.id.buttonPlay);
		btn.setImageResource(R.drawable.stop);
	}

	private void setButtonAsPlay(int index) {
		// View viewObject = listView.getChildAt(index);
		View viewObject = views.get(index);
		final ImageButton btn = (ImageButton) viewObject
				.findViewById(R.id.buttonPlay);
		btn.setImageResource(R.drawable.play);
	}

	private void playSong(final int index) {

		if (Shortcut.isNetworkOnline(getApplicationContext()) == true) {

			try {
				labelNP.setText("Please wait for a moment");
				Log.i(TAG, "Posisi Index : " + index);
				Log.i(TAG, "Posisi Current Index : " + currentSongIndex);
				if (index == currentSongIndex) {
					Log.i(TAG, "Posisi Index : " + index);
					Log.i(TAG, "Posisi Current Index : " + currentSongIndex);
					if (mp.isPlaying()) {
						mp.pause();
						setButtonAsPlay(index);
						labelNP.setText("NP : "
								+ songlist.get(index).getTitle());
					} else {
						mp.start();
						setButtonAsPause(index);
						labelNP.setText("NP : "
								+ songlist.get(index).getTitle());
					}
				} else {
					if (mp.isPlaying()) {
						mp.reset();
					}
					setButtonAsPause(index);

					mp.setDataSource(songlist.get(index).getUrlStream());
					mp.prepareAsync();

					mp.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(final MediaPlayer mp) {
							Log.e(TAG, "On Prepared Listener");

							mp.start();

							currentSongIndex = index;
							mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

								@Override
								public void onBufferingUpdate(MediaPlayer mp,
										int percent) {
									Log.i(TAG, "Buffering -- " + percent);
								}
							});

							String songTitle = songlist.get(index).getTitle();
							if (songTitle == null) {
								songTitle = "";
							}

							labelNP.setText("NP : " + songTitle);

							mp.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									Log.i(TAG, "Song finished.");
									View viewObject = listView
											.getChildAt(index);
									ImageButton btn = (ImageButton) viewObject
											.findViewById(R.id.buttonPlay);
									btn.setImageResource(R.drawable.play);

									labelNP.setText("CaptainJack Media Player");
									mp.reset();
								}
							});

						}

					});

					mp.setOnInfoListener(new OnInfoListener() {

						@Override
						public boolean onInfo(MediaPlayer mp, int what,
								int extra) {
							Log.e(TAG, "setOnInfoListener ---- What : " + what
									+ ", Extra : " + extra);
							return false;
						}
					});

					mp.setOnErrorListener(new OnErrorListener() {

						@Override
						public boolean onError(MediaPlayer mp, int what,
								int extra) {
							Log.e(TAG, "setOnErrorListener --------- What : "
									+ what + ", Extra : " + extra);
							return true;
						}
					});
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, "NO NETWORK CONNECTION.");
			mp.stop();
			mp.reset();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mp.isPlaying()) {
			mp.stop();
		}
		mp.release();

	}
}
