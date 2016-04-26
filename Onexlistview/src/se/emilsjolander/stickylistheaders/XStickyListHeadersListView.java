package se.emilsjolander.stickylistheaders;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Date;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;

public class XStickyListHeadersListView extends StickyListHeadersListView {

    private XWrapperViewList mList;

    public XStickyListHeadersListView(Context context) {
        this(context, null);
    }

    public XStickyListHeadersListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XStickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Initialize the wrapped list
        this.mList = new XWrapperViewList(context);
        super.mList = this.mList;

        super.init(context, attrs);
    }

    public void setTimeFormat(String timeFormat) {
        mList.setTimeFormat(timeFormat);
    }

    /**
     * enable or disable pull down refresh feature.
     */
    public void setPullRefreshEnable(IXListViewRefreshListener refreshListener) {
        mList.setPullRefreshEnable(refreshListener);
    }

    public void disablePullRefresh() {
        mList.disablePullRefresh();
    }

    public void startRefresh() {
        mList.startRefresh();
    }

    /**
     * enable or disable pull up load more feature.
     */
    public void setPullLoadEnable(IXListViewLoadMore loadMoreListener) {
        mList.setPullLoadEnable(loadMoreListener);
    }


    public void enablePullRefresh() {
        mList.enablePullRefresh();
    }

    public void disablePullLoad() {
        mList.disablePullLoad();
    }

    public void hidePullLoad() {
        mList.hidePullLoad();
    }

    public void showPullLoad() {
        mList.showPullLoad();
    }

    /**
     * set last refresh time
     */
    public void setRefreshTime(Date time) {
        mList.setRefreshTime(time);
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh(Date time) {
        mList.stopRefresh(time);
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        mList.stopLoadMore();
    }
}
