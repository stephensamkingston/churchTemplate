//package com.hakkum.sync;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import com.hakkum.eby.customclasses.SoundCloudDownload;
//
//import android.content.Context;
//
//public class SynchronizedArrayList<T> extends ArrayList<T> {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	Context context;
//	@SuppressWarnings("rawtypes")
//	static SynchronizedArrayList _instance;
//
//	private boolean _skipWriteFile = false;
//
//	private SynchronizedArrayList() {
//		super();
//
//	}
//
//	@SuppressWarnings("unchecked")
//	public static synchronized SynchronizedArrayList<SoundCloudDownload> getInstance() {
//		if (_instance == null)
//			_instance = new SynchronizedArrayList<SoundCloudDownload>();
//
//		return _instance;
//	}
//
//	public void initialize(Context context) {
//		if (context == null)
//			return;
//
//		this.context = context;
//		readFromFile();
//	}
//
//	@SuppressWarnings("unchecked")
//	private synchronized void readFromFile() {
//
//		if (context == null)
//			return;
//
//		_skipWriteFile = true;
//
//		ArrayList<SoundCloudDownload> items = Util.ReadSettingsUpload(context, Constants.FNAME_QUEUE);
//		this.clear();
//		for (SoundCloudDownload queueItem : items) {
//			this.add((T) queueItem);
//		}
//
//		_skipWriteFile = false;
//	}
//
//	@Override
//	public void add(int index, T object) {
//
//	}
//
//	@Override
//	public synchronized boolean add(T queueItem) {
//
//		boolean ret = super.add(queueItem);
//
//		if (false == _skipWriteFile)
//			writeToFile();
//
//		return ret;
//	}
//
//	@Override
//	public boolean addAll(Collection<? extends T> collection) {
//		return false;
//	}
//
//	@Override
//	public boolean addAll(int index, Collection<? extends T> collection) {
//		return false;
//	}
//
//	@Override
//	public synchronized T remove(int index) {
//		T ret = super.remove(index);
//
//		writeToFile();
//
//		return ret;
//	}
//
//	@Override
//	public synchronized boolean remove(Object object) {
//		boolean ret = super.remove(object);
//
//		writeToFile();
//		return ret;
//	}
//
//	@Override
//	protected void removeRange(int fromIndex, int toIndex) {
//
//	}
//
//	public synchronized void writeToFile() {
//
//		if (context == null)
//			return;
//
//		ArrayList<SoundCloudDownload> items = new ArrayList<SoundCloudDownload>();
//
//		for (int i = 0; i < this.size(); i++) {
//			items.add((SoundCloudDownload) this.get(i));
//		}
//
//		Util.WriteSettingsUpload(context, SoundCloudDownload.FNAME_QUEUE, items);
//	}
//
//}
