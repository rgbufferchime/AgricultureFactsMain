package com.bufferchime.agriculturefact.game.quiz;

public class Constant {

    //////////////////////// POST API & PARAMETER  //////////////////////////

    public static String QUIZ_URL = "http://quizdemo.wrteam.in/api-v2.php"; // admin page url

    public static String DEVICE_REGISTRATION_API = "http://quizdemo.wrteam.in/RegisterDevice.php";

    /////// PARAMETERS  ///////
    public static String accessKey = "access_key";
    public static String accessKeyValue = "6808";
    public static String getCategories = "get_categories";
    public static String getSubCategory = "get_subcategory_by_maincategory";
    public static String categoryId = "main_id";
    public static String getQuestion = "get_questions_by_subcategory";
    public static String subCategoryId = "subcategory";
    public static String reportQuestion = "report_question";
    public static String questionId = "question_id";
    public static String messageReport = "message";
    public static String getQuestionByCategory = "get_questions_by_category";
    public static String category = "category";
    public static String getPrivacy="privacy_policy_settings";

    public static String ERROR = "error";
    public static String DATA = "data";
    public static String ID = "id";
    public static String CATEGORY_NAME = "category_name";
    public static String IMAGE = "image";
    public static String MAX_LEVEL = "maxlevel";
    public static String MAIN_CATE_ID = "maincat_id";
    public static String SUB_CATE_NAME = "subcategory_name";
    public static String STATUS = "status";
    public static String QUESTION = "question";
    public static String OPTION_A = "optiona";
    public static String OPTION_B = "optionb";
    public static String OPTION_C = "optionc";
    public static String OPTION_D = "optiond";
    public static String LEVEL = "level";
    public static String NOTE = "note";


    public static int MAX_QUESTION_PER_LEVEL = 10; // max question per level

    public static long LeftTime;
    public static String SelectedSubCategoryID;
    public static int TotalLevel;
    public static int CATE_ID;
    public static int SUB_CAT_ID;


    public static String PROGRESS_COLOR = "#606899"; // change progress color in play area while choose audience pole
    public static String PROGRESS_BG_COLOR = "#d8d8d8";
    public static int PROGRESS_TEXT_SIZE=13;
    public static int PROGRESS_STROKE_WIDTH=6;
    public static int RESULT_PROGRESS_STROKE_WIDTH=10;
    public static int RESULT_PROGRESS_TEXT_SIZE=20;
    public static int AUD_PROGRESS_STROKE_WIDTH=5;
    public static int AUD_PROGRESS_TEXT_SIZE=10;
    public static String AUD_PROGRESS_COLOR = "#393d5a";
    public static String AUD_PROGRESS_BG_COLOR = "#d8d8d8";

    public static final String PREF_TEXTSIZE = "fontSizePref";
    //max text size
    public static final String TEXTSIZE_MAX = "30";

    //minimum text size
    public static final String TEXTSIZE_MIN = "16";
    /// you can increase or decrease time
    public static int CIRCULAR_MAX_PROGRESS = 25; // max progress of circular progress
    public static int TIME_PER_QUESTION = 25000;  //here we set 25 second foe each question
    public static int COUNT_DOWN_TIMER = 1000; //here we set 1 second


    //////------------give coin to user , when level completed----------//////

    public static int correctAnswer = 3;  //count level complete when user give >30 percent correct answer
    public static int giveOneCoin = 1;  //give  coin when user give 30 to 40 percent correct answer
    public static int giveTwoCoins = 2; //give  coins when user give 40 to 50 percent correct answer
    public static int giveThreeCoins = 3; //give  coin when user give 50 to 60 percent correct answer
    public static int giveFourCoins = 4;  //give  coin when user give > 60  percent correct answer
}
