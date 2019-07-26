package com.pubgplayerzofficial.utilities;

import android.os.Environment;

/**
 * Created by lalit on 7/25/2017.
 */
public class Contants {
    public static final Boolean IS_DEBUG_LOG = true;

    public static final String LOG_TAG = "pubgplayerz";
    public static final String APP_NAME = "appName"; // Do NOT change this value; meant for user preference

    public static final String DEFAULT_APPLICATION_NAME = "pubgplayerz";

    public static final String APP_DIRECTORY = "/E" + DEFAULT_APPLICATION_NAME + "Directory/";
    public static final String DATABASE_NAME = "pubgplayerz.db";// Environment.getExternalStorageDirectory() +  APP_DIRECTORY + "evergreen.db";

    public static String SERVICE_BASE_URL = "http://pubgplayerz.com/apis_pubgplayerzofficial/";
//    public static String SERVICE_BASE_URL = "http://pubgbattel.loopfusion.in/apis/";

    public static String outputBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();


    public static final int LIST_PAGE_SIZE = 50;
    public static String InternetMessage = " You are Online.";
    public static final String BAD_NETWORK_MESSAGE = "We are trying hard to get latest content but the network seems to be slow. " +
            "You may continue to use app and get latest with in the app itself. ";


    public static final String Login = "login.php";
    public static final String Signup = "register.php";
    public static final String otpverifiy = "otpverifiy.php";
    public static final String withdrawotpverifiy = "withdrawotpverifiy.php";
    public static final String reset = "reset.php";
    public static final String resetu = "resetu.php";
    public static final String getAllPlayLIst = "getAllPlayLIst.php";
    public static final String getParticipated = "getParticipated.php";
    public static final String getAllOngoingList = "getAllOngoingList.php";
    public static final String getAllResultMatchList = "getAllResultMatchList.php";
    public static final String getWinChicken = "getWinChicken.php";
    public static final String getAllSpecialMatch = "getAllSpecialMatch.php";
    public static final String getFullMatch = "getFullMatch.php";
    public static final String getParticipatedMatch = "getParticipatedMatch.php";
    public static final String getUserProfile = "getUserProfile.php";
    public static final String getTopPlayer = "getTopPlayer.php";
    public static final String updateProfile = "updateProfile.php";
    public static final String updatePassword = "updatePassword.php";
    public static final String checkPubgname = "checkPubgname.php";
    public static final String updateProfileImage = "updateProfileImage.php";
    public static final String getAllTransaction = "getAllTransaction.php";
    public static final String getAllStatics = "getAllStatics.php";
    public static final String sendMoney = "sendMoney.php";
    public static final String addWithdraw = "addWithdraw.php";
    public static final String checkongoingpop = "checkongoingpop.php";
    public static final String getMyReferrals = "getMyReferrals.php";
    public static final String getReferralAmount = "getReferralAmount.php";
    public static final String update = "update.php";
    public static final String checkSlot = "checkSlot.php";
    public static final String checkSession = "checkSession.php";
    public static final String getNews = "getNews.php";

    public static final String patyurl = "http://pubgplayerz.com/gateway.php?";
}
