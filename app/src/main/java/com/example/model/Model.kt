package com.example.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.model.Model.MethodConst.GET
import com.example.model.Model.MethodConst.POST
import com.example.model.data.*
import com.example.model.data.DataConst.ACCESS_TOKEN_ROW
import com.example.model.data.DataConst.NOTIFICATION_ROW
import com.example.model.data.DataConst.ON
import com.example.model.data.DataConst.REFRESH_TOKEN_ROW
import com.example.model.data.DataConst.VALID
import com.example.model.сlient.Client
import com.example.model.requests.*
import com.example.model.push.AlarmHelper
import com.example.model.requests.auth.*
import com.example.model.requests.discussions.*
import com.example.model.requests.lists.*
import com.example.model.requests.discussions.GetAccepted
import com.example.model.requests.monitor.GetPosts
import com.example.model.requests.monitor.Start
import com.example.model.requests.profile.AddVk
import com.example.model.requests.profile.GetProfile
import com.example.model.requests.profile.UpdateProfileData
import com.example.model.response.*
import com.example.model.сlient.SocketClient
import io.ktor.client.request.forms.*
import io.socket.client.Ack
import io.socket.client.Socket
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class Model {

    companion object PathConst {
        //auth
        const val REGISTER_PATH = "/auth/register/"
        const val LOGIN_PATH = "/auth/login/"
        const val LOGOUT_PATH = "/auth/logout/"
        const val CONFIRM_PATH = "/auth/confirm/"
        const val CHANGE_PATH = "/auth/change_password/"
        const val REFRESH_PATH = "/auth/refresh/"

        //profile
        const val UPDATE_PATH = "/profile/update_profile/"
        const val GET_PROFILE_PATH = "/profile/get_profile/"
        const val ADD_VK_PROFILE_PATH = "/profile/add_vk/"

        //lists
        const val ADD_LIST_PATH = "/lists/add_list/"
        const val GET_LISTS_PATH = "/lists/get_lists/"
        const val GET_WORDS_PATH = "/lists/get_list/"
        const val ADD_WORDS_PATH = "/lists/add_to_list/"
        const val DELETE_LISTS_PATH = "/lists/del_list/"
        const val DELETE_WORDS_PATH = "/lists/del_from_list/"

        //discussions
        const val DISCUSSIONS_ADD_PATH = "/discussions/add/"
        const val DISCUSSIONS_GET_PATH = "/discussions/get/"
        const val DISCUSSIONS_DELETE_PATH = "/discussions/del/"
        const val DISCUSSIONS_ADD_LIST_PATH = "/discussions/add_list/"
        const val DISCUSSIONS_GET_LIST_PATH = "/discussions/get_lists/"
        const val DISCUSSION_DELETE_LIST_PATH = "/discussions/del_list/"
        const val GET_ACCEPTED_PATH = "/discussions/get_accepted/"

        //monitor
        const val MONITOR_PATH_START = "/monitor/start/"
        const val GET_POSTS_PATH = "/monitor/get_posts/"
    }

    object MethodConst {
        const val POST = "POST"
        const val GET = "GET"
    }


    private lateinit var context: Context
    private lateinit var client: Client
    private lateinit var alarmHelper: AlarmHelper
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var whiteBlackListSplitter: WhiteBlackSplitter
    private lateinit var discussionKeeper: DiscussionKeeper
    private lateinit var wordsKeeper: WordsKeeper
    private lateinit var discussionsListsKeeper: DiscussionListsKeeper
    private lateinit var liveData: MutableLiveData<String>
    private lateinit var liveDataPosts: MutableLiveData<SocketResponse>
    private lateinit var curUser: User
    private lateinit var socketClient: SocketClient
    private lateinit var mSocket: Socket


    fun init(
        _context: Context,
        _liveData: MutableLiveData<String>,
        _liveDataPosts: MutableLiveData<SocketResponse>
    ) {
        liveData = _liveData
        liveDataPosts = _liveDataPosts
        context = _context
        client = Client()
        alarmHelper = AlarmHelper(context)
        alarmHelper.create()
        preferenceHelper = PreferenceHelper(context)
        whiteBlackListSplitter = WhiteBlackSplitter()
        discussionKeeper = DiscussionKeeper()
        wordsKeeper = WordsKeeper()
        discussionsListsKeeper = DiscussionListsKeeper()
        socketClient = SocketClient()
        socketClient.setSocket()
        prefCheckNullNotification()
    }


    fun joinSocket() {
        mSocket = socketClient.getSocket()

        socketClient.establishConnection()

        val username = prefGetEmail()

        val jsonArg = JSONObject()

        jsonArg.put("username", username)

        println("I want sockets")

        mSocket.emit("join", jsonArg, Ack {
            println("socket join success")
        })

        mSocket.on("event") { args ->

            if (args[0] != null) {
                val json = args[0] as JSONObject
                val response = json.toString()
                println("Data from on $response")
                val convert = convertSocketResponseToJson(response)
                liveDataPosts.postValue(convert)
            }

            println("get data success")
        }

        mSocket.on("usersInfo") { args ->

            if (args[0] != null) {
                val json = args[0] as JSONObject
                val response = json.toString()
                println("Users info: $response")
            }

            println("Users info")

        }


    }

    fun startSocket(discussionId: Int) {
        val email = prefGetEmail()

        val jsonArg = JSONObject()

        jsonArg.put("email", email)
        jsonArg.put("discussion_id", discussionId)

        mSocket.emit("start", jsonArg, Ack {
            println("monitor start success")
        })
    }

    fun getActiveDiscussionsSocket() {
        val email = prefGetEmail()

        val jsonArg = JSONObject()

        jsonArg.put("email", email)

        mSocket.emit("getActiveDiscussions", jsonArg, Ack {
            println("getActiveDiscussions success")
        })
    }

    fun stopSocket(discussion_id: Int) {
        val email = prefGetEmail()

        val jsonArg = JSONObject()

        jsonArg.put("email", email)
        jsonArg.put("discussion_id", discussion_id)

        mSocket.emit("stop", jsonArg, Ack {

            println("socket stop success")
        })

    }

    fun closeSocket() {
        socketClient.closeConnection()
    }


    private fun convertSocketResponseToJson(answer: String): SocketResponse {
        return Json { ignoreUnknownKeys = true }.decodeFromString(answer)
    }


    fun getEmail(): String {
        return curUser.email
    }

    fun getPassword(): String {
        return curUser.password
    }

    fun savePrefData(row: String, value: String?) {
        preferenceHelper.saveDate(row, value)
    }

    fun checkPrefData(): Boolean {
        return preferenceHelper.checkData()
    }

    fun convertResponseToJson(answer: String): HttpResponse {
        return Json { ignoreUnknownKeys = true }.decodeFromString(answer)
    }

    fun loginWithSaved(): Boolean {
        val refreshToken = prefGetRefreshToken()
        if (refreshToken != null) {
            refreshRequest(refreshToken)
            return true
        }
        return false
    }

    fun getAcceptedPath(discussionId: String)
    {
        val getAccepted = GetAccepted(discussionId)
        makeRequest(action = getAccepted, path = GET_ACCEPTED_PATH,
            accessToken = prefGetAccessToken(), method = GET)
    }

    fun getPostsRequest(discussionId: String, messageNum:String)
    {
        val getPosts = GetPosts(discussionId,messageNum)
        makeRequest(action = getPosts, path = GET_POSTS_PATH,
            accessToken = prefGetAccessToken(), method = GET)
    }


    fun monitorStartRequest(discussionId: String)
    {
        val monitorStart = Start(discussionId)
        makeRequest(action = monitorStart, path = MONITOR_PATH_START,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun deleteListFromDiscussionRequest(discussionId:String,listId:String)
    {
        val deleteListFromDiscussions = DeleteListFromDiscussions(discussionId,listId)
        makeRequest(action = deleteListFromDiscussions, path = DISCUSSION_DELETE_LIST_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun getListForDiscussionRequest(discussionId:String)
    {
        val getDiscussionList = GetDiscussionsLists(discussionId)
        makeRequest(action = getDiscussionList, path = DISCUSSIONS_GET_LIST_PATH,
            accessToken = prefGetAccessToken(), method = GET)
    }


    fun addListToDiscussionRequest(listId: String, discussionId:String,white: String)
    {
        val addListToDiscussion = AddListToDiscussion(listId,discussionId,white)
        makeRequest(action = addListToDiscussion, path = DISCUSSIONS_ADD_LIST_PATH,
         accessToken = prefGetAccessToken(), method = POST)
    }


    fun deleteDiscussionsRequest(discussionId: String)
    {
        val deleteDiscussions = DeleteDiscussions(discussionId)
        makeRequest(action = deleteDiscussions, path = DISCUSSIONS_DELETE_PATH,
          accessToken = prefGetAccessToken(), method = POST)
    }


    fun getDiscussionsRequest()
    {
        val getDiscussions = GetDiscussions()
        makeRequest(action = getDiscussions, path = DISCUSSIONS_GET_PATH,
            accessToken = prefGetAccessToken(), method = GET)

    }

    fun addDiscussionsRequest(link:String,description:String)
    {
        val addDiscussions = AddDiscussions(link,description)
        makeRequest(action = addDiscussions, path = DISCUSSIONS_ADD_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun deleteListRequest(listId:String)
    {
        val deleteList = DeleteList(listId)
        makeRequest(action = deleteList, path = DELETE_LISTS_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun deleteWordRequest(word:String,listId:String)
    {
        val deleteWord = DeleteWord(word,listId)
        makeRequest(action = deleteWord,path = DELETE_WORDS_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }


    fun addWordsRequest(words:String,listId: Int)
    {
        val addWords = AddWords(words, listId)
        makeRequest(action = addWords, path = ADD_WORDS_PATH,
           accessToken = prefGetAccessToken(), method = POST)
    }

    fun getWordsRequest(listId:Int)
    {
        val getWords = GetWords(listId.toString())
        makeRequest(action = getWords, path = GET_WORDS_PATH,
            accessToken = prefGetAccessToken(), method = GET)

    }

    fun addVkRequest(login:String,password: String)
    {
        val addVk = AddVk(login,password)
        makeRequest(action = addVk, path = ADD_VK_PROFILE_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun getListsRequest()
    {
        val getLists = GetLists()
        makeRequest(action = getLists, path = GET_LISTS_PATH,
        accessToken = prefGetAccessToken(), method = GET)
    }


    fun addListRequest(name:String,white:String)
    {
        val addList = AddList(name, white)
        makeRequest(action = addList, path = ADD_LIST_PATH,
        accessToken = prefGetAccessToken(), method = POST)
    }


    fun getProfileRequest()
    {
        val getProfile = GetProfile()
        makeRequest(action = getProfile, path = GET_PROFILE_PATH,
            accessToken = prefGetAccessToken(), method = GET)
    }


    fun updateProfileRequest(name:String)
    {
        val updateProfileData = UpdateProfileData(name)
        makeRequest(action = updateProfileData, path = UPDATE_PATH,
            accessToken = prefGetAccessToken(), method = POST)
    }

    fun changePasswordRequest(oldPassword:String,newPassword:String,repNewPassword:String)
    {
        val changePassword = ChangePassword(prefGetEmail().toString(),oldPassword,newPassword,repNewPassword)
        curUser = User(prefGetEmail().toString(),newPassword)
        makeRequest(action = changePassword, path = CHANGE_PATH, method = POST)
    }


    fun registrationRequest(email:String, password: String, repPassword:String)
    {
        val registration = Registration(email,password,repPassword)
        curUser = User(email, password)
        makeRequest(action = registration, path = REGISTER_PATH, method = POST)
    }

    fun loginRequest(email:String,password:String)
    {
        val login = Login(email,password)
        curUser = User(email, password)
        makeRequest(action = login, path = LOGIN_PATH, method = POST)
    }

    fun logoutRequest()
    {
        val logout = Logout(prefGetRefreshToken())
        makeRequest(action = logout, path = LOGOUT_PATH, method = POST)
    }

    fun confirmRequest(code:String)
    {
        val confirm = Confirm(getEmail(),code)
        makeRequest(action = confirm, path = CONFIRM_PATH,method = POST)
    }

    private fun refreshRequest(refreshToken:String)
    {
        val refresh = Refresh(refreshToken)
        println("RefreshToken: $refreshToken")
        makeRequest(action = refresh, path = REFRESH_PATH, method = POST)
    }


    private fun makeRequest(action: Request, path:String,accessToken:String? = null,method:String)
    {
        val checkMessage = action.checkData()
        if (checkMessage!=VALID)
        {
            liveData.postValue(checkMessage)
            return
        }
        val request = action.makeRequest()

        println(request.toString())
        println("Path: $path")

        val params = action.makeParams()
        launchClient(request,params,path,accessToken,method)
    }


    private fun launchClient(
        request: MultiPartFormDataContent,
        params: List<Params>,
        path: String,
        accessToken: String?,
        method: String
    )
    {
        println(request)

        CoroutineScope(Dispatchers.IO).launch{
            val message: Deferred<String> = async {
                client.sendHttp(request,params,path,accessToken,method)
            }

            val answer = message.await()

            liveData.postValue(answer)

            Log.d("Launch: ", answer)
        }
    }


    private fun prefGetEmail():String?
    {
        return preferenceHelper.getData(DataConst.EMAIL_ROW)
    }


    private fun prefCheckNullNotification()
    {
        if(prefGetNotification().isNullOrEmpty())
        {
            savePrefData(NOTIFICATION_ROW, ON)
        }
    }

    private fun prefGetRefreshToken():String?
    {
        return preferenceHelper.getData(REFRESH_TOKEN_ROW)
    }

    private fun prefGetAccessToken():String?
    {
        return preferenceHelper.getData(ACCESS_TOKEN_ROW)
    }

    fun prefGetNotification():String?
    {
        return preferenceHelper.getData(NOTIFICATION_ROW)
    }


    fun splitBlackWhiteLists(list:List<Catalog>)
    {
        whiteBlackListSplitter.init(list)
        whiteBlackListSplitter.splitAll()
    }

    fun getWhiteLists():List<Catalog>
    {
        return whiteBlackListSplitter.getWhiteLists()
    }

    fun getBlackLists():List<Catalog>
    {
        return whiteBlackListSplitter.getBlackLists()
    }

    fun getNamesLists(catalogList:List<Catalog>):List<String>
    {
        return whiteBlackListSplitter.getNamesLists(catalogList)
    }

    fun getListId(white:Boolean,position:Int):Int?
    {
        val list = if(white) {
            getWhiteLists()
        } else {
            getBlackLists()
        }

        if (list.isNotEmpty()) {
            println("Item" + list[position].name)
            return list[position].id!!
        }

        return null
    }


    private fun getWordsFromCatalog(catalogList:List<Catalog>):List<String>
    {
        val words = mutableListOf<String>()

        catalogList.forEach {

            words.add(it.word.toString())
        }

        return words
    }

    fun initDiscussionKeeper(discussion: List<Discussion>)
    {
        discussionKeeper.init(discussion)
    }

    fun getWords():List<String>
    {
        return wordsKeeper.getWords()
    }


    fun initWordsKeeper(list:List<Catalog>)
    {
        val words = getWordsFromCatalog(list)
        wordsKeeper.init(words)
    }

    fun getWordFromId(index:Int):String?
    {
       return wordsKeeper.getWord(index)
    }

    private fun getDiscussion():List<Discussion>
    {
        return discussionKeeper.getDiscussion()
    }

    fun getDiscussionId(index:Int):Int?
    {
        return discussionKeeper.getDiscussionId(index)
    }


    fun getDescriptionList():List<String>
    {
        val discussion = getDiscussion()
        val descriptions = mutableListOf<String>()

        discussion.forEach {

            descriptions.add(it.description.toString())
        }

        return descriptions
    }



    private fun getListsForDiscussion():List<ListForDiscussions>
    {
        return discussionsListsKeeper.getListsForDiscussions()
    }


    fun initListsForDiscussion(listsForDiscussion:List<ListForDiscussions>)
    {
        discussionsListsKeeper.init(listsForDiscussion)
    }


    fun getNameListsForDiscussion():List<String>
    {
        val listsForDiscussions = getListsForDiscussion()
        val name = mutableListOf<String>()
        listsForDiscussions.forEach {

            name.add(it.name.toString())
        }

        return name
    }

    fun getIdListForDiscussion(index:Int):Int?
    {
        return discussionsListsKeeper.getIdLists(index)
    }



}











