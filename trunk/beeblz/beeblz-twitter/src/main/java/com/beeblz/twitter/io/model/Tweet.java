/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.twitter.io.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Data model for a tweet.
 * 
 * @author Young-Gue Bae
 */
public class Tweet implements Serializable {

	private long id;
	private String user;
	private long userNo;
	private String text;
	private String url;
	private String tweetType;
	private String targetUser;
	private String replyUser;
	private long replyUserNo;
	private String retweetedUser;
	private String mentionedUser;
	private String mentionedUsers;
	private boolean positiveAttitude;
	private boolean negativeAttitude;
	private Date createDate;
	private Date colCreateDate;
	private Date colUpdateDate;
	
	private double liwcWc;
	private double liwcWps;
	private double liwcDic;
	private double liwcSixltr;
	private double liwcPronoun;
	private double liwcI;
	private double liwcWe;
	private double liwcSelf;
	private double liwcYou;
	private double liwcOther;
	private double liwcNegate;
	private double liwcAssent;
	private double liwcArticle;
	private double liwcPreps;
	private double liwcNumber;
	private double liwcAffect;
	private double liwcPosemo;
	private double liwcPosfeel;
	private double liwcOptim;
	private double liwcNegemo;
	private double liwcAnx;
	private double liwcAnger;
	private double liwcSad;
	private double liwcCogmech;
	private double liwcCause;
	private double liwcInsight;
	private double liwcDiscrep;
	private double liwcInhib;
	private double liwcTentat;
	private double liwcCertain;
	private double liwcSenses;
	private double liwcSee;
	private double liwcHear;
	private double liwcFeel;
	private double liwcSocial;
	private double liwcComm;
	private double liwcOthref;
	private double liwcFriends;
	private double liwcFamily;
	private double liwcHumans;
	private double liwcTime;
	private double liwcPast;
	private double liwcPresent;
	private double liwcFuture;
	private double liwcSpace;
	private double liwcUp;
	private double liwcDown;
	private double liwcIncl;
	private double liwcExcl;
	private double liwcMotion;
	private double liwcOccup;
	private double liwcSchool;
	private double liwcJob;
	private double liwcAchieve;
	private double liwcLeisure;
	private double liwcHome;
	private double liwcSports;
	private double liwcTv;
	private double liwcMusic;
	private double liwcMoney;
	private double liwcMetaph;
	private double liwcRelig;
	private double liwcDeath;
	private double liwcPhyscal;
	private double liwcBody;
	private double liwcSexual;
	private double liwcEating;
	private double liwcSleep;
	private double liwcGroom;
	private double liwcSwear;
	private double liwcNonfl;
	private double liwcFillers;
	
	public void setLIWCFeatures(Map<String,Double> counts) {
		setLiwcWc(counts.get("WC"));
		setLiwcWps(counts.get("WPS"));
		setLiwcDic(counts.get("DIC"));
		setLiwcSixltr(counts.get("SIXLTR"));

		setLiwcPronoun(counts.get("PRONOUN"));
		setLiwcI(counts.get("I"));
		setLiwcWe(counts.get("WE"));
		setLiwcSelf(counts.get("SELF"));
		setLiwcYou(counts.get("YOU"));
		setLiwcOther(counts.get("OTHER"));
		setLiwcNegate(counts.get("NEGATE"));
		setLiwcAssent(counts.get("ASSENT"));
		setLiwcArticle(counts.get("ARTICLE"));
		setLiwcPreps(counts.get("PREPS"));
		setLiwcNumber(counts.get("NUMBER"));

		setLiwcAffect(counts.get("AFFECT"));
		setLiwcPosemo(counts.get("POSEMO"));
		setLiwcPosfeel(counts.get("POSFEEL"));
		setLiwcOptim(counts.get("OPTIM"));
		setLiwcNegemo(counts.get("NEGEMO"));
		setLiwcAnx(counts.get("ANX"));
		setLiwcAnger(counts.get("ANGER"));
		setLiwcSad(counts.get("SAD"));
		setLiwcCogmech(counts.get("COGMECH"));
		setLiwcCause(counts.get("CAUSE"));
		setLiwcInsight(counts.get("INSIGHT"));
		setLiwcDiscrep(counts.get("DISCREP"));
		setLiwcInhib(counts.get("INHIB"));
		setLiwcTentat(counts.get("TENTAT"));
		setLiwcCertain(counts.get("CERTAIN"));
		setLiwcSenses(counts.get("SENSES"));
		setLiwcSee(counts.get("SEE"));
		setLiwcHear(counts.get("HEAR"));
		setLiwcFeel(counts.get("FEEL"));
		setLiwcSocial(counts.get("SOCIAL"));
		setLiwcComm(counts.get("COMM"));
		setLiwcOthref(counts.get("OTHREF"));
		setLiwcFriends(counts.get("FRIENDS"));
		setLiwcFamily(counts.get("FAMILY"));
		setLiwcHumans(counts.get("HUMANS"));

		setLiwcTime(counts.get("TIME"));
		setLiwcPast(counts.get("PAST"));
		setLiwcPresent(counts.get("PRESENT"));
		setLiwcFuture(counts.get("FUTURE"));
		setLiwcSpace(counts.get("SPACE"));
		setLiwcUp(counts.get("UP"));
		setLiwcDown(counts.get("DOWN"));
		setLiwcIncl(counts.get("INCL"));
		setLiwcExcl(counts.get("EXCL"));
		setLiwcMotion(counts.get("MOTION"));

		setLiwcOccup(counts.get("OCCUP"));
		setLiwcSchool(counts.get("SCHOOL"));
		setLiwcJob(counts.get("JOB"));
		setLiwcAchieve(counts.get("ACHIEVE"));
		setLiwcLeisure(counts.get("LEISURE"));
		setLiwcHome(counts.get("HOME"));
		setLiwcSports(counts.get("SPORTS"));
		setLiwcTv(counts.get("TV"));
		setLiwcMusic(counts.get("MUSIC"));
		setLiwcMoney(counts.get("MONEY"));
		setLiwcMetaph(counts.get("METAPH"));
		setLiwcRelig(counts.get("RELIG"));
		setLiwcDeath(counts.get("DEATH"));
		setLiwcPhyscal(counts.get("PHYSCAL"));
		setLiwcBody(counts.get("BODY"));
		setLiwcSexual(counts.get("SEXUAL"));
		setLiwcEating(counts.get("EATING"));
		setLiwcSleep(counts.get("SLEEP"));
		setLiwcGroom(counts.get("GROOM"));

		setLiwcSwear(counts.get("SWEAR"));
		setLiwcNonfl(counts.get("NONFL"));
		setLiwcFillers(counts.get("FILLERS"));
	}
	
	public long getId() {
		return id;
	}
	public String getUser() {
		return user;
	}
	public long getUserNo() {
		return userNo;
	}
	public String getText() {
		return text;
	}
	public String getUrl() {
		return url;
	}
	public String getTweetType() {
		return tweetType;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public String getReplyUser() {
		return replyUser;
	}
	public long getReplyUserNo() {
		return replyUserNo;
	}
	public String getRetweetedUser() {
		return retweetedUser;
	}
	public String getMentionedUser() {
		return mentionedUser;
	}
	public String getMentionedUsers() {
		return mentionedUsers;
	}
	public boolean getPositiveAttitude() {
		return positiveAttitude;
	}
	public boolean getNegativeAttitude() {
		return negativeAttitude;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getColCreateDate() {
		return colCreateDate;
	}
	public Date getColUpdateDate() {
		return colUpdateDate;
	}
	public double getLiwcWc() {
		return liwcWc;
	}
	public double getLiwcWps() {
		return liwcWps;
	}
	public double getLiwcDic() {
		return liwcDic;
	}
	public double getLiwcSixltr() {
		return liwcSixltr;
	}
	public double getLiwcPronoun() {
		return liwcPronoun;
	}
	public double getLiwcI() {
		return liwcI;
	}
	public double getLiwcWe() {
		return liwcWe;
	}
	public double getLiwcSelf() {
		return liwcSelf;
	}
	public double getLiwcYou() {
		return liwcYou;
	}
	public double getLiwcOther() {
		return liwcOther;
	}
	public double getLiwcNegate() {
		return liwcNegate;
	}
	public double getLiwcAssent() {
		return liwcAssent;
	}
	public double getLiwcArticle() {
		return liwcArticle;
	}
	public double getLiwcPreps() {
		return liwcPreps;
	}
	public double getLiwcNumber() {
		return liwcNumber;
	}
	public double getLiwcAffect() {
		return liwcAffect;
	}
	public double getLiwcPosemo() {
		return liwcPosemo;
	}
	public double getLiwcPosfeel() {
		return liwcPosfeel;
	}
	public double getLiwcOptim() {
		return liwcOptim;
	}
	public double getLiwcNegemo() {
		return liwcNegemo;
	}
	public double getLiwcAnx() {
		return liwcAnx;
	}
	public double getLiwcAnger() {
		return liwcAnger;
	}
	public double getLiwcSad() {
		return liwcSad;
	}
	public double getLiwcCogmech() {
		return liwcCogmech;
	}
	public double getLiwcCause() {
		return liwcCause;
	}
	public double getLiwcInsight() {
		return liwcInsight;
	}
	public double getLiwcDiscrep() {
		return liwcDiscrep;
	}
	public double getLiwcInhib() {
		return liwcInhib;
	}
	public double getLiwcTentat() {
		return liwcTentat;
	}
	public double getLiwcCertain() {
		return liwcCertain;
	}
	public double getLiwcSenses() {
		return liwcSenses;
	}
	public double getLiwcSee() {
		return liwcSee;
	}
	public double getLiwcHear() {
		return liwcHear;
	}
	public double getLiwcFeel() {
		return liwcFeel;
	}
	public double getLiwcSocial() {
		return liwcSocial;
	}
	public double getLiwcComm() {
		return liwcComm;
	}
	public double getLiwcOthref() {
		return liwcOthref;
	}
	public double getLiwcFriends() {
		return liwcFriends;
	}
	public double getLiwcFamily() {
		return liwcFamily;
	}
	public double getLiwcHumans() {
		return liwcHumans;
	}
	public double getLiwcTime() {
		return liwcTime;
	}
	public double getLiwcPast() {
		return liwcPast;
	}
	public double getLiwcPresent() {
		return liwcPresent;
	}
	public double getLiwcFuture() {
		return liwcFuture;
	}
	public double getLiwcSpace() {
		return liwcSpace;
	}
	public double getLiwcUp() {
		return liwcUp;
	}
	public double getLiwcDown() {
		return liwcDown;
	}
	public double getLiwcIncl() {
		return liwcIncl;
	}
	public double getLiwcExcl() {
		return liwcExcl;
	}
	public double getLiwcMotion() {
		return liwcMotion;
	}
	public double getLiwcOccup() {
		return liwcOccup;
	}
	public double getLiwcSchool() {
		return liwcSchool;
	}
	public double getLiwcJob() {
		return liwcJob;
	}
	public double getLiwcAchieve() {
		return liwcAchieve;
	}
	public double getLiwcLeisure() {
		return liwcLeisure;
	}
	public double getLiwcHome() {
		return liwcHome;
	}
	public double getLiwcSports() {
		return liwcSports;
	}
	public double getLiwcTv() {
		return liwcTv;
	}
	public double getLiwcMusic() {
		return liwcMusic;
	}
	public double getLiwcMoney() {
		return liwcMoney;
	}
	public double getLiwcMetaph() {
		return liwcMetaph;
	}
	public double getLiwcRelig() {
		return liwcRelig;
	}
	public double getLiwcDeath() {
		return liwcDeath;
	}
	public double getLiwcPhyscal() {
		return liwcPhyscal;
	}
	public double getLiwcBody() {
		return liwcBody;
	}
	public double getLiwcSexual() {
		return liwcSexual;
	}
	public double getLiwcEating() {
		return liwcEating;
	}
	public double getLiwcSleep() {
		return liwcSleep;
	}
	public double getLiwcGroom() {
		return liwcGroom;
	}
	public double getLiwcSwear() {
		return liwcSwear;
	}
	public double getLiwcNonfl() {
		return liwcNonfl;
	}
	public double getLiwcFillers() {
		return liwcFillers;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setUserNo(long userNo) {
		this.userNo = userNo;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setTweetType(String tweetType) {
		this.tweetType = tweetType;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}
	public void setReplyUserNo(long replyUserNo) {
		this.replyUserNo = replyUserNo;
	}
	public void setRetweetedUser(String retweetedUser) {
		this.retweetedUser = retweetedUser;
	}
	public void setMentionedUser(String mentionedUser) {
		this.mentionedUser = mentionedUser;
	}
	public void setMentionedUsers(String mentionedUsers) {
		this.mentionedUsers = mentionedUsers;
	}
	public void setPositiveAttitude(boolean positiveAttitude) {
		this.positiveAttitude = positiveAttitude;
	}
	public void setNegativeAttitude(boolean negativeAttitude) {
		this.negativeAttitude = negativeAttitude;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setColCreateDate(Date colCreateDate) {
		this.colCreateDate = colCreateDate;
	}
	public void setColUpdateDate(Date colUpdateDate) {
		this.colUpdateDate = colUpdateDate;
	}
	public void setLiwcWc(double liwcWc) {
		this.liwcWc = liwcWc;
	}
	public void setLiwcWps(double liwcWps) {
		this.liwcWps = liwcWps;
	}
	public void setLiwcDic(double liwcDic) {
		this.liwcDic = liwcDic;
	}
	public void setLiwcSixltr(double liwcSixltr) {
		this.liwcSixltr = liwcSixltr;
	}
	public void setLiwcPronoun(double liwcPronoun) {
		this.liwcPronoun = liwcPronoun;
	}
	public void setLiwcI(double liwcI) {
		this.liwcI = liwcI;
	}
	public void setLiwcWe(double liwcWe) {
		this.liwcWe = liwcWe;
	}
	public void setLiwcSelf(double liwcSelf) {
		this.liwcSelf = liwcSelf;
	}
	public void setLiwcYou(double liwcYou) {
		this.liwcYou = liwcYou;
	}
	public void setLiwcOther(double liwcOther) {
		this.liwcOther = liwcOther;
	}
	public void setLiwcNegate(double liwcNegate) {
		this.liwcNegate = liwcNegate;
	}
	public void setLiwcAssent(double liwcAssent) {
		this.liwcAssent = liwcAssent;
	}
	public void setLiwcArticle(double liwcArticle) {
		this.liwcArticle = liwcArticle;
	}
	public void setLiwcPreps(double liwcPreps) {
		this.liwcPreps = liwcPreps;
	}
	public void setLiwcNumber(double liwcNumber) {
		this.liwcNumber = liwcNumber;
	}
	public void setLiwcAffect(double liwcAffect) {
		this.liwcAffect = liwcAffect;
	}
	public void setLiwcPosemo(double liwcPosemo) {
		this.liwcPosemo = liwcPosemo;
	}
	public void setLiwcPosfeel(double liwcPosfeel) {
		this.liwcPosfeel = liwcPosfeel;
	}
	public void setLiwcOptim(double liwcOptim) {
		this.liwcOptim = liwcOptim;
	}
	public void setLiwcNegemo(double liwcNegemo) {
		this.liwcNegemo = liwcNegemo;
	}
	public void setLiwcAnx(double liwcAnx) {
		this.liwcAnx = liwcAnx;
	}
	public void setLiwcAnger(double liwcAnger) {
		this.liwcAnger = liwcAnger;
	}
	public void setLiwcSad(double liwcSad) {
		this.liwcSad = liwcSad;
	}
	public void setLiwcCogmech(double liwcCogmech) {
		this.liwcCogmech = liwcCogmech;
	}
	public void setLiwcCause(double liwcCause) {
		this.liwcCause = liwcCause;
	}
	public void setLiwcInsight(double liwcInsight) {
		this.liwcInsight = liwcInsight;
	}
	public void setLiwcDiscrep(double liwcDiscrep) {
		this.liwcDiscrep = liwcDiscrep;
	}
	public void setLiwcInhib(double liwcInhib) {
		this.liwcInhib = liwcInhib;
	}
	public void setLiwcTentat(double liwcTentat) {
		this.liwcTentat = liwcTentat;
	}
	public void setLiwcCertain(double liwcCertain) {
		this.liwcCertain = liwcCertain;
	}
	public void setLiwcSenses(double liwcSenses) {
		this.liwcSenses = liwcSenses;
	}
	public void setLiwcSee(double liwcSee) {
		this.liwcSee = liwcSee;
	}
	public void setLiwcHear(double liwcHear) {
		this.liwcHear = liwcHear;
	}
	public void setLiwcFeel(double liwcFeel) {
		this.liwcFeel = liwcFeel;
	}
	public void setLiwcSocial(double liwcSocial) {
		this.liwcSocial = liwcSocial;
	}
	public void setLiwcComm(double liwcComm) {
		this.liwcComm = liwcComm;
	}
	public void setLiwcOthref(double liwcOthref) {
		this.liwcOthref = liwcOthref;
	}
	public void setLiwcFriends(double liwcFriends) {
		this.liwcFriends = liwcFriends;
	}
	public void setLiwcFamily(double liwcFamily) {
		this.liwcFamily = liwcFamily;
	}
	public void setLiwcHumans(double liwcHumans) {
		this.liwcHumans = liwcHumans;
	}
	public void setLiwcTime(double liwcTime) {
		this.liwcTime = liwcTime;
	}
	public void setLiwcPast(double liwcPast) {
		this.liwcPast = liwcPast;
	}
	public void setLiwcPresent(double liwcPresent) {
		this.liwcPresent = liwcPresent;
	}
	public void setLiwcFuture(double liwcFuture) {
		this.liwcFuture = liwcFuture;
	}
	public void setLiwcSpace(double liwcSpace) {
		this.liwcSpace = liwcSpace;
	}
	public void setLiwcUp(double liwcUp) {
		this.liwcUp = liwcUp;
	}
	public void setLiwcDown(double liwcDown) {
		this.liwcDown = liwcDown;
	}
	public void setLiwcIncl(double liwcIncl) {
		this.liwcIncl = liwcIncl;
	}
	public void setLiwcExcl(double liwcExcl) {
		this.liwcExcl = liwcExcl;
	}
	public void setLiwcMotion(double liwcMotion) {
		this.liwcMotion = liwcMotion;
	}
	public void setLiwcOccup(double liwcOccup) {
		this.liwcOccup = liwcOccup;
	}
	public void setLiwcSchool(double liwcSchool) {
		this.liwcSchool = liwcSchool;
	}
	public void setLiwcJob(double liwcJob) {
		this.liwcJob = liwcJob;
	}
	public void setLiwcAchieve(double liwcAchieve) {
		this.liwcAchieve = liwcAchieve;
	}
	public void setLiwcLeisure(double liwcLeisure) {
		this.liwcLeisure = liwcLeisure;
	}
	public void setLiwcHome(double liwcHome) {
		this.liwcHome = liwcHome;
	}
	public void setLiwcSports(double liwcSports) {
		this.liwcSports = liwcSports;
	}
	public void setLiwcTv(double liwcTv) {
		this.liwcTv = liwcTv;
	}
	public void setLiwcMusic(double liwcMusic) {
		this.liwcMusic = liwcMusic;
	}
	public void setLiwcMoney(double liwcMoney) {
		this.liwcMoney = liwcMoney;
	}
	public void setLiwcMetaph(double liwcMetaph) {
		this.liwcMetaph = liwcMetaph;
	}
	public void setLiwcRelig(double liwcRelig) {
		this.liwcRelig = liwcRelig;
	}
	public void setLiwcDeath(double liwcDeath) {
		this.liwcDeath = liwcDeath;
	}
	public void setLiwcPhyscal(double liwcPhyscal) {
		this.liwcPhyscal = liwcPhyscal;
	}
	public void setLiwcBody(double liwcBody) {
		this.liwcBody = liwcBody;
	}
	public void setLiwcSexual(double liwcSexual) {
		this.liwcSexual = liwcSexual;
	}
	public void setLiwcEating(double liwcEating) {
		this.liwcEating = liwcEating;
	}
	public void setLiwcSleep(double liwcSleep) {
		this.liwcSleep = liwcSleep;
	}
	public void setLiwcGroom(double liwcGroom) {
		this.liwcGroom = liwcGroom;
	}
	public void setLiwcSwear(double liwcSwear) {
		this.liwcSwear = liwcSwear;
	}
	public void setLiwcNonfl(double liwcNonfl) {
		this.liwcNonfl = liwcNonfl;
	}
	public void setLiwcFillers(double liwcFillers) {
		this.liwcFillers = liwcFillers;
	}
	
}
