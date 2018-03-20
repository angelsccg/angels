package com.angels.model;

import java.io.Serializable;
/**
 * 
* @ClassName: ACImage 
* @Description: 图片信息
* @author angelsC
* @date 2015-10-15 下午3:09:59 
*
 */
public class ACImage implements Serializable{
	/**标题*/
	private String title;
	/**图片的url*/
	private String imgUrl;
	/**点击图片的链接地址*/
	private String linkUrl;
	/**点击图片的跳转的Activity名称*/
	private String activityName;
	
	public ACImage() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
}
