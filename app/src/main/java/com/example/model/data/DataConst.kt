package com.example.model.data

object DataConst {


    //sql injection control

    val SQL_INJECTION = "['\";.<>`=/\\|&]".toRegex()
    val SQL_LOGIN_INJECTION = "['\";<>`=/\\|&]".toRegex()
    val SQL_LINK_INJECTION ="['\";<>`=\\|&]".toRegex()
    val SQL_WORD_INJECTION = "['\".<>`=/\\|&]".toRegex()

    //Size const
    const val MIN_LEN = 6
    const val MAX_LEN = 60
    const val CODE_LEN = 4
    const val DIS_MAX_LEN = 255

    //Data const

    private const val ERROR_MESSAGE = "{\"errorMessage\":"
    const val NOT_FILLED = "$ERROR_MESSAGE\"Все поля должны быть заполнены!!!\"}"
    const val MUST_MATCH = "$ERROR_MESSAGE\"Пароли должны совпадать!!!\"}"
    const val TOO_SHORT = "$ERROR_MESSAGE\"Одно из введённых полей слишком короткое!!!\"}"
    const val TOO_LONG = "$ERROR_MESSAGE\"Одно из введённых полей слишком длинное!!!\"}"
    const val VALID = "Valid data"
    const val NOT_MUST_MATCH = "$ERROR_MESSAGE\"Новый пароль не должен совпадать со старым!!!\"}"
    const val INVALID_LENGTH_CODE = "$ERROR_MESSAGE\"Неправильная длина кода!!!\"}"
    const val WORD_LONG = "$ERROR_MESSAGE\"Одно из слов слишком длинное!!!\"}"
    const val DESCRIPTION_LONG = "$ERROR_MESSAGE\"Описание слишком длинное!!!\"}"
    const val DESCRIPTION_SHORT = "$ERROR_MESSAGE\"Описание слишком короткое!!!\"}"
    const val LINK_LONG = "$ERROR_MESSAGE\"Ссылка слишком длинная!!!\"}"
    const val NOT_DIS_LINK ="$ERROR_MESSAGE\"Это не ссылка на обсуждение!!!\"}"
    const val FORBIDDEN_SYMBOLS ="$ERROR_MESSAGE\"Запрещённые символы!!!\"}"




    //methods
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val CONFIRM = "confirm"
    const val CHANGE_PASSWORD = "change_password"
    const val UPDATE_PROFILE = "update_profile"
    const val GET_PROFILE = "get_profile"
    const val REFRESH = "refresh"
    const val ADD_LIST = "add_list"
    const val GET_LISTS = "get_lists"
    const val ADD_VK = "add_vk"
    const val ADD_WORDS = "add_to_list"
    const val GET_WORDS = "get_list"
    const val DELETE_LIST = "delete_list"
    const val DELETE_WORD ="delete_word"
    const val ADD_DISCUSSIONS = "add_discussions"
    const val GET_DISCUSSIONS = "get_discussions"
    const val DELETE_DISCUSSIONS = "delete_discussions"
    const val ADD_LIST_TO_DISCUSSION = "add_list_to_discussion"
    const val GET_LIST_FOR_DISCUSSION = "get_list_for_discussion"
    const val DELETE_LIST_FROM_DISCUSSION = "delete_list_from_discussion"
    const val MONITOR_START = "monitor_start"
    const val GET_POSTS = "get_posts"
    const val GET_ACCEPTED = "get_accepted"
    const val GET_NEW_POSTS = "get_new_posts"


    //new body const
    const val EMAIL_BODY = "email"
    const val PASSWORD_BODY = "password"
    const val NEW_PASSWORD_BODY = "new_password"
    const val METHOD_BODY = "method"
    const val REFRESH_BODY = "refreshToken"
    const val CODE_BODY = "code"
    const val NAME_BODY = "name"
    const val TYPE_BODY = "type"
    const val LOGIN_BODY = LOGIN
    const val LIST_ID_BODY = "list_id"
    const val WORDS_BODY = "words"
    const val GROUP_ID_BODY = "groupId"
    const val TOPIC_ID_BODY = "topicId"
    const val DESCRIPTION_BODY = "description"
    const val DISCUSSION_ID_BODY = "discussion_id"
    const val MESSAGE_NUM_BODY = "message_num"






    //SharedPreference const
    const val TABLE_NAME = "Users"
    const val EMAIL_ROW = "email"
    const val USERNAME_ROW = "username"
    const val PASSWORD_ROW = "password"
    const val NOTIFICATION_ROW = "notification"
    const val REFRESH_TOKEN_ROW = "refreshToken"
    const val ACCESS_TOKEN_ROW = "accessToken"
    const val LOGIN_ROW = "login"


    //notification state
    const val OFF = "OFF"
    const val ON = "ON"


}