package com.example.myapplication

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fragment.*
import com.example.model.Model
import com.example.model.data.DataConst.ACCESS_TOKEN_ROW
import com.example.model.data.DataConst.ADD_DISCUSSIONS
import com.example.model.data.DataConst.ADD_LIST
import com.example.model.data.DataConst.ADD_LIST_TO_DISCUSSION
import com.example.model.data.DataConst.ADD_VK
import com.example.model.data.DataConst.ADD_WORDS
import com.example.model.data.DataConst.CHANGE_PASSWORD
import com.example.model.data.DataConst.CONFIRM
import com.example.model.data.DataConst.DELETE_DISCUSSIONS
import com.example.model.data.DataConst.DELETE_LIST
import com.example.model.data.DataConst.DELETE_LIST_FROM_DISCUSSION
import com.example.model.data.DataConst.DELETE_WORD
import com.example.model.data.DataConst.EMAIL_ROW
import com.example.model.data.DataConst.GET_ACCEPTED
import com.example.model.data.DataConst.GET_DISCUSSIONS
import com.example.model.data.DataConst.GET_LISTS
import com.example.model.data.DataConst.GET_LIST_FOR_DISCUSSION
import com.example.model.data.DataConst.GET_POSTS
import com.example.model.data.DataConst.GET_WORDS
import com.example.model.data.DataConst.LOGIN
import com.example.model.data.DataConst.NOTIFICATION_ROW
import com.example.model.data.DataConst.OFF
import com.example.model.data.DataConst.ON
import com.example.model.data.DataConst.PASSWORD_ROW
import com.example.model.data.DataConst.REFRESH
import com.example.model.data.DataConst.REFRESH_TOKEN_ROW
import com.example.model.data.DataConst.REGISTER
import com.example.model.data.DataConst.USERNAME_ROW
import com.example.model.response.*
import com.example.myapplication.MainConst.CHANGE_NAME_ADVANCE
import com.example.myapplication.MainConst.CHANGE_PASSWORD_ADVANCE
import com.example.myapplication.MainConst.CONFIRM_ADVANCE
import com.example.myapplication.MainConst.CONFIRM_FAILED
import com.example.myapplication.MainConst.DISCUSSION_ADD_ADVANCE
import com.example.myapplication.MainConst.DISCUSSION_DELETE_ADVANCE
import com.example.myapplication.MainConst.DISCUSSION_LIST_EMPTY
import com.example.myapplication.MainConst.DISCUSSION_NOT_SELECTED
import com.example.myapplication.MainConst.EMPTY_DISCUSSION
import com.example.myapplication.MainConst.EMPTY_DISCUSSION_LISTS
import com.example.myapplication.MainConst.EMPTY_GET_LISTS
import com.example.myapplication.MainConst.EMPTY_GET_WORDS
import com.example.myapplication.MainConst.EMPTY_POSTS
import com.example.myapplication.MainConst.EXIT_MESSAGE
import com.example.myapplication.MainConst.EXIT_TITLE
import com.example.myapplication.MainConst.LISTS_FOR_DISCUSSION_EMPTY
import com.example.myapplication.MainConst.LIST_ADD_ADVANCE
import com.example.myapplication.MainConst.LIST_ADD_TO_DISCUSSION_ADVANCE
import com.example.myapplication.MainConst.LIST_DELETE_ADVANCE
import com.example.myapplication.MainConst.LIST_DELETE_FROM_DISCUSSION_ADVANCE
import com.example.myapplication.MainConst.LIST_EMPTY
import com.example.myapplication.MainConst.NO
import com.example.myapplication.MainConst.NOT_BLACK_LIST
import com.example.myapplication.MainConst.NOT_CONFIRM_EMAIL
import com.example.myapplication.MainConst.NOT_WHITE_LIST
import com.example.myapplication.MainConst.NULL
import com.example.myapplication.MainConst.REFRESH_PROBLEM
import com.example.myapplication.MainConst.USER_NOT_AUTHORIZED
import com.example.myapplication.MainConst.USER_NOT_EXISTS
import com.example.myapplication.MainConst.USER_NOT_EXISTS_RU
import com.example.myapplication.MainConst.VK_ADD_ADVANCE
import com.example.myapplication.MainConst.VK_FAILED
import com.example.myapplication.MainConst.VK_HELP_TITLE
import com.example.myapplication.MainConst.VK_INFO
import com.example.myapplication.MainConst.WORDS_ADD_ADVANCE
import com.example.myapplication.MainConst.WORD_DELETE_ADVANCE
import com.example.myapplication.MainConst.WORD_LIST_EMPTY
import com.example.myapplication.MainConst.YES
import java.util.*


class MainActivity : AppCompatActivity() {


    private var menu: Menu? = null
    private var loginType = false
    private lateinit var model: Model
    private lateinit var liveDataFromServer: MutableLiveData<String>
    private lateinit var liveDateWhite: MutableLiveData<List<String>>
    private lateinit var liveDateBlack: MutableLiveData<List<String>>
    private lateinit var liveDateWords: MutableLiveData<List<String>>
    private lateinit var liveDateDiscussion: MutableLiveData<List<String>>
    private lateinit var liveDateListsForDiscussions: MutableLiveData<List<String>>
    private lateinit var liveDatePosts: MutableLiveData<SocketResponse>
    private lateinit var liveDateOldPosts: MutableLiveData<List<SocketResponse>>
    private lateinit var liveDateCounter: MutableLiveData<Int>
    private var curListId: Int? = null
    private var curWordId: Int? = null



    private var curBlackDiscussionId : Int? = null
    private var curWhiteDiscussionId : Int? = null
    private var curDiscussionId: Int? = null
    private var curListForDiscussionId: Int? = null
    private var curMonitorListDiscussionId:Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        init()

        if (!model.checkPrefData()) {
            loginType = true
            model.loginWithSaved()
        } else {
            openSign()
        }
    }


    override fun onRestart() {
        super.onRestart()
        println("restart")
    }


    private fun init() {
        liveDateWhite = MutableLiveData<List<String>>()
        liveDateBlack = MutableLiveData<List<String>>()
        liveDateWords = MutableLiveData<List<String>>()
        liveDateDiscussion = MutableLiveData<List<String>>()
        liveDateListsForDiscussions = MutableLiveData<List<String>>()
        liveDatePosts = MutableLiveData<SocketResponse>()
        liveDateCounter = MutableLiveData<Int>()
        liveDateOldPosts = MutableLiveData<List<SocketResponse>>()
        liveDataFromServer = MutableLiveData<String>()
        liveDataFromServer.observe(this, Observer {
            dataValidation()
        })

        model = Model()
        model.init(applicationContext, liveDataFromServer,liveDatePosts)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.account_menu -> accountControl()
            R.id.message_menu -> messageControl()
            R.id.notification_menu -> notificationControl()
        }
        return true
    }

    private fun messageControl() {
        closeFrag(R.id.message_frame)
        openMsg()
    }

    private fun accountControl() {
        closeFrag(R.id.message_frame)
        model.getProfileRequest()
        openAccount()
    }

    private fun notificationControl() {
        val item = menu?.findItem(R.id.notification_menu)
        val curNotificationState = model.prefGetNotification()

        if (curNotificationState == ON) {
            item?.setIcon(R.drawable.ic_baseline_notifications_off_24)
            model.savePrefData(NOTIFICATION_ROW, OFF)
        } else if (curNotificationState == OFF) {
            item?.setIcon(R.drawable.ic_baseline_notifications_24)
            model.savePrefData(NOTIFICATION_ROW, ON)
        }
    }

    private fun toastWarning(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    private fun notificationStartState() {
        val item = menu?.findItem(R.id.notification_menu)
        val curNotificationState = model.prefGetNotification()

        if (curNotificationState == OFF) {
            item?.setIcon(R.drawable.ic_baseline_notifications_off_24)
        } else if (curNotificationState == ON) {
            item?.setIcon(R.drawable.ic_baseline_notifications_24)
        }
    }


    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        model.logoutRequest()
        advanceLogout()
    }

    private val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        println(NO)
    }


    private fun createExitDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this, R.style.cust_dialog)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(YES, DialogInterface.OnClickListener(positiveButtonClick))
        builder.setNegativeButton(NO, DialogInterface.OnClickListener(negativeButtonClick))

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun showActionBar() {
        supportActionBar?.show()
    }


    private fun openFrag(f: Fragment, idHolder: Int) {
        supportFragmentManager.beginTransaction().replace(idHolder, f).commit()
    }

    private fun closeFrag(idHolder: Int) {
        val fragment = supportFragmentManager.findFragmentById(idHolder);

        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    private fun openSign() {
        hideActionBar()
        openFrag(signIn.newInstance(), R.id.first_frame)
        openFrag(registration_f.newInstance(), R.id.second_frame)
    }

    private fun openReg() {
        hideActionBar()
        openFrag(from_reg.newInstance(), R.id.first_frame)
        openFrag(BackSign.newInstance(), R.id.second_frame)
    }

    private fun openMsg() {
        showActionBar()
        openMenu()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                model.getDiscussionsRequest()
            }
        }, 50)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                model.getActiveDiscussionsSocket()
            }
        }, 100)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                model.getProfileRequest()
            }
        }, 150)

        val msgFragment = msg_window()
        msgFragment.setArgs(liveDateDiscussion,liveDatePosts,liveDateOldPosts,liveDateCounter)
        openFrag(msgFragment, R.id.message_frame)
    }

    private fun openMenu() {
        val size = menu?.size()

        if (size == 0) {
            menuInflater.inflate(R.menu.main_menu, menu)
        }

        notificationStartState()
    }


    private fun openAccount() {
        openFrag(Account.newInstance(), R.id.message_frame)
    }

    private fun openVK() {
        openFrag(vk.newInstance(), R.id.message_frame)
    }

    private fun createHelpDialog(title:String,message:String)
    {
        val builder = AlertDialog.Builder(this, R.style.cust_dialog2)
        builder.setTitle(title)
        builder.setMessage(message)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    open fun onVkInfo(v: View)
    {
        createHelpDialog(VK_HELP_TITLE, VK_INFO)
    }

    open fun onOpenChange(v: View) {
        openFrag(change_f.newInstance(), R.id.message_frame)
    }

    open fun onRegView(v: View) {
        openReg()
    }

    open fun onSignView(v: View) {
        openSign()
    }

    open fun onSignIn(v: View) {
        val email = findViewById<EditText>(R.id.emailSignIn).text.toString()
        val password = findViewById<EditText>(R.id.passwordSignIn).text.toString()

        loginType = false
        model.loginRequest(email, password)
    }

    open fun onChangePassword(v: View) {
        val oldPassword = findViewById<EditText>(R.id.editOldPassword).text.toString()
        val newPassword = findViewById<EditText>(R.id.editNewPassword).text.toString()
        val repNewPassword = findViewById<EditText>(R.id.editRepNewPassword).text.toString()

        model.changePasswordRequest(oldPassword, newPassword, repNewPassword)
    }

    open fun onLogout(v: View) {
        createExitDialog(EXIT_TITLE, EXIT_MESSAGE)
    }


    open fun onReg(v: View) {
        val email = findViewById<EditText>(R.id.emailReg).text.toString()
        val password = findViewById<EditText>(R.id.passwordReg).text.toString()
        val passwordRep = findViewById<EditText>(R.id.passwordRegRepeat).text.toString()

        model.registrationRequest(email, password, passwordRep)
    }


    open fun onStartMonitoring(v: View)
    {
        if(curMonitorListDiscussionId!=null) {
            model.startSocket(curMonitorListDiscussionId!!)
            model.getActiveDiscussionsSocket()
        }
        else
        {
            toastWarning(DISCUSSION_NOT_SELECTED)
        }
    }

    open fun onStopMonitoring(v:View)
    {
        if(curMonitorListDiscussionId!=null)
        {
            model.stopSocket(curMonitorListDiscussionId!!)
        }
        else
        {
            toastWarning(DISCUSSION_NOT_SELECTED)
        }
    }


    open fun onChangeName(v: View) {
        val name = findViewById<EditText>(R.id.usernameAccount).text.toString()
        model.updateProfileRequest(name)
    }


    open fun onFilterSet(v: View) {
        val filterFragment = filter()
        filterFragment.setArgs(liveDateWhite, liveDateBlack, liveDateWords)
        openFrag(filterFragment, R.id.message_frame)
        model.getListsRequest()
    }


    open fun onAddList(v: View) {
        val name = findViewById<EditText>(R.id.addListEditText).text.toString()
        val white = findViewById<Switch>(R.id.switchList).isChecked.toString()
        println("List name: $name")
        println("White: $white")
        model.addListRequest(name, white)
    }

    open fun onOpenVkAccount(v: View) {
        closeFrag(R.id.message_frame)
        openVK()
    }


    open fun onAddVk(v: View) {
        val login = findViewById<EditText>(R.id.edit_login_vk).text.toString()
        val password = findViewById<EditText>(R.id.edit_password_vk).text.toString()
        model.addVkRequest(login, password)
    }


    open fun onAddWords(v: View) {
        val words = findViewById<EditText>(R.id.addWordsEditText).text.toString()
        if(curListId!=null) {
            model.addWordsRequest(words, curListId!!)
            return
        }
        toastWarning(LIST_EMPTY)
    }

    open fun onDeleteList(v:View)
    {
        if(curListId != null)
        {
            model.deleteListRequest(curListId.toString())
            println("METHOD DELETE LIST")

            return
        }

        toastWarning(LIST_EMPTY)
    }


    open fun onDeleteWord(v:View)
    {
        if(curWordId == null)
        {
            toastWarning(WORD_LIST_EMPTY)
            return
        }

        val word = model.getWordFromId(curWordId!!)

        val listId = curListId.toString()
        if(word!=null) {
            model.deleteWordRequest(word, listId)
            return
        }
    }


    open fun onOpenDiscussions(v: View) {
        val discussionFragment = discusssions_fragment()
        discussionFragment.setDiscussionList(liveDateDiscussion,liveDateWhite,liveDateBlack,liveDateListsForDiscussions)
        openFrag(discussionFragment, R.id.message_frame)
        model.getDiscussionsRequest()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                model.getListsRequest()
            }
        }, 50)
    }



    open fun onAddDiscussion(v: View) {
        val link = findViewById<EditText>(R.id.edit_link).text.toString()
        val description = findViewById<EditText>(R.id.edit_description).text.toString()
        model.addDiscussionsRequest(link, description)
    }

    open fun onDeleteDiscussion(v: View) {

        if(curDiscussionId != null)
        {
            model.deleteDiscussionsRequest(curDiscussionId.toString())
            return
        }
        toastWarning(DISCUSSION_LIST_EMPTY)
    }


    open fun onAddWhiteListToDiscussion(v:View)
    {
        if (curWhiteDiscussionId==null)
        {
            toastWarning(NOT_WHITE_LIST)
            return
        }
        else if (curDiscussionId == null)
        {
            toastWarning(DISCUSSION_LIST_EMPTY)
            return
        }

        println("I SEND WHITE $curWhiteDiscussionId")
        println("I SEND DISCUSSION $curDiscussionId")

        val listId = curWhiteDiscussionId.toString()
        val discussionId = curDiscussionId.toString()
        val white = true.toString()

        model.addListToDiscussionRequest(listId,discussionId,white)

    }


    open fun onAddBlackListToDiscussion(v:View)
    {
        if (curBlackDiscussionId==null)
        {
            toastWarning(NOT_BLACK_LIST)
            return
        }
        else if (curDiscussionId == null)
        {
            toastWarning(DISCUSSION_LIST_EMPTY)
            return
        }
        println("I SEND BLACK $curBlackDiscussionId")
        println("I SEND DISCUSSION $curDiscussionId")

        val listId = curBlackDiscussionId.toString()
        val discussionId = curDiscussionId.toString()
        val white = false.toString()

        model.addListToDiscussionRequest(listId,discussionId,white)
    }


    fun onDeleteFromDiscussion(v:View)
    {
        println("DELETE FROM DISCUSSION PARAMETERS list_id $curListForDiscussionId discussion_id $curDiscussionId")
        if(curDiscussionId == null)
        {
            toastWarning(DISCUSSION_LIST_EMPTY)
            return
        }
        else if (curListForDiscussionId == null)
        {
            toastWarning(LISTS_FOR_DISCUSSION_EMPTY)
            return
        }

        model.deleteListFromDiscussionRequest(curDiscussionId.toString(),curListForDiscussionId.toString())
    }


    fun clearMonitorDiscussion(position:Int?)
    {
        curMonitorListDiscussionId = position
    }

    fun startRefreshPosts()
    {
        model.getAcceptedPath(curMonitorListDiscussionId.toString())
    }

    fun setCurMonitorDiscussion(position:Int)
    {
        val monitorId = model.getDiscussionId(position!!)
        if(curMonitorListDiscussionId != monitorId)
        {

            println("Change discussion id $monitorId")
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    model.getAcceptedPath(monitorId.toString())
                }
            }, 50)
            curMonitorListDiscussionId = monitorId
        }
    }

    fun loadNewPosts(counter:Int)
    {
        model.getPostsRequest(curMonitorListDiscussionId.toString(),counter.toString())
    }


    fun setCurListsForDiscussion(position:Int)
    {
        val listsForDiscussionId = model.getIdListForDiscussion(position)
        println("Cur lists for discussion id $listsForDiscussionId")
        curListForDiscussionId = listsForDiscussionId
    }


    fun setCurBlackDiscussion(position:Int)
    {
        val blackId = model.getListId(false,position)
        println("Cur black id $blackId")
        curBlackDiscussionId = blackId
    }

    fun setCurWhiteDiscussion(position:Int)
    {
        val whiteId = model.getListId(true,position)
        println("Cur white id $whiteId")
        curWhiteDiscussionId = whiteId
    }

    fun setCurDiscussionId(position: Int) {
        val discussionId = model.getDiscussionId(position)
        println("Cur discussion id $discussionId")
        curDiscussionId = discussionId
        if(curDiscussionId != null){
            model.getListForDiscussionRequest(curDiscussionId.toString())
        }
    }

    fun setCurWordId(id: Int)
    {
        curWordId = id
    }

    fun setCurListId(white: Boolean, position: Int) {

        curListId = model.getListId(white, position)

        println("CurListID:$curListId")
        if (curListId != null) {
            curListId = curListId

            model.getWordsRequest(curListId!!)
        }
        else
        {
            val empty = listOf<String>()
            liveDateWords.postValue(empty)
        }
    }

    private fun createCodeDialog() {
        val factory = LayoutInflater.from(this)
        val codeView = factory.inflate(R.layout.code_dialog, null)
        val customDialog = AlertDialog.Builder(this).create()
        customDialog.setView(codeView)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val editCode = codeView.findViewById<EditText>(R.id.editTextCode)
        val confirmButton = codeView.findViewById<Button>(R.id.dialogConfirmButton)

        confirmButton.setOnClickListener {

            val code = editCode.text.toString()
            model.confirmRequest(code)
            customDialog.cancel()
        }

        customDialog.show()
    }


    private fun advanceRegister() {
        openSign()
        createCodeDialog()
    }

    private fun advanceLogin(response: HttpResponse) {
        closeFrag(R.id.first_frame)
        closeFrag(R.id.second_frame)

        model.savePrefData(EMAIL_ROW, model.getEmail())
        model.savePrefData(PASSWORD_ROW, model.getPassword())
        model.savePrefData(ACCESS_TOKEN_ROW, response.accessToken)
        model.savePrefData(REFRESH_TOKEN_ROW, response.refreshToken)
        model.getProfileRequest()
        model.joinSocket()

        println("AccessToken: ${response.accessToken}")
        openMsg()
    }

    private fun advanceRefresh(response: HttpResponse) {
        model.savePrefData(ACCESS_TOKEN_ROW, response.accessToken)
        model.savePrefData(REFRESH_TOKEN_ROW, response.refreshToken)
        model.getProfileRequest()
        model.joinSocket()

        println("AccessToken: ${response.accessToken}")
        openMsg()
    }

    private fun advanceChangePassword() {
        model.savePrefData(PASSWORD_ROW, model.getPassword())
        closeFrag(R.id.message_frame)
        toastWarning(CHANGE_PASSWORD_ADVANCE)
        openAccount()
    }

    private fun advanceGetData(profile: Profile) {
        model.savePrefData(EMAIL_ROW, profile.email)
        model.savePrefData(USERNAME_ROW, profile.name)
    }

    private fun advanceChangeName(profile: Profile) {
        model.savePrefData(EMAIL_ROW, profile.email)
        model.savePrefData(USERNAME_ROW, profile.name)
        toastWarning(CHANGE_NAME_ADVANCE)
    }

    private fun advanceLogout() {
        model.savePrefData(EMAIL_ROW, null)
        model.savePrefData(PASSWORD_ROW, null)
        model.savePrefData(USERNAME_ROW, null)
        model.savePrefData(ACCESS_TOKEN_ROW, null)
        model.savePrefData(REFRESH_TOKEN_ROW, null)
        goToNullLocalConst()
        closeFrag(R.id.message_frame)
        openSign()
    }


    private fun advanceGetLists(list: List<Catalog>) {
        model.splitBlackWhiteLists(list)
        val whiteList = model.getWhiteLists()
        val blackList = model.getBlackLists()
        liveDateWhite.postValue(model.getNamesLists(whiteList))
        liveDateBlack.postValue(model.getNamesLists(blackList))
    }


    private fun advanceGetWords(list: List<Catalog>) {

        model.initWordsKeeper(list)
        liveDateWords.postValue(model.getWords())
    }


    private fun advanceGetDiscussions(list: List<Discussion>)
    {
        model.initDiscussionKeeper(list)
        if(curDiscussionId != null){
            model.getListForDiscussionRequest(curDiscussionId.toString())
        }
        else {
            val list = mutableListOf<ListForDiscussions>()
            model.initListsForDiscussion(list)
        }

        liveDateDiscussion.postValue(model.getDescriptionList())
    }

    private fun advanceGetListForDiscussion(list:List<ListForDiscussions>)
    {
        model.initListsForDiscussion(list)
        val names = model.getNameListsForDiscussion()
        liveDateListsForDiscussions.postValue(names)
    }

    private fun advanceGetPosts(posts:List<SocketResponse>)
    {
        liveDateOldPosts.postValue(posts)
    }


    private fun emptyGetWords() {
        val list = listOf<String>()
        val catalog = listOf<Catalog>()
        model.initWordsKeeper(catalog)
        println("EMPTY word")
        curWordId = null
        liveDateWords.postValue(list)
    }

    private fun emptyGetLists()
    {
        val list = listOf<String>()
        val catalog = listOf<Catalog>()
        curBlackDiscussionId = null
        curWhiteDiscussionId = null
        curListId = null
        curWordId = null
        model.splitBlackWhiteLists(catalog)
        liveDateWhite.postValue(list)
        liveDateBlack.postValue(list)
        liveDateWords.postValue(list)
    }


    private fun emptyGetDiscussions()
    {
        val list = listOf<String>()
        val discussion = listOf<Discussion>()
        model.initDiscussionKeeper(discussion)

        liveDateDiscussion.postValue(list)
    }

    private fun emptyGetDiscussionLists()
    {
        val list = mutableListOf<ListForDiscussions>()
        model.initListsForDiscussion(list)
        val date = model.getNameListsForDiscussion()
        liveDateListsForDiscussions.postValue(date)
    }


    private fun dataValidation()
    {
        val strResponse = liveDataFromServer.value.toString()

        if(strResponse == EMPTY_DISCUSSION_LISTS)
        {
            emptyGetDiscussionLists()
            println("Hello empty lists for discussion")
            return
        }

        if(strResponse == EMPTY_GET_LISTS)
        {
            emptyGetLists()
            return
        }

        if(strResponse == EMPTY_GET_WORDS)
        {
            emptyGetWords()
            return
        }

        if(strResponse == EMPTY_DISCUSSION)
        {
            emptyGetDiscussions()
            return
        }

        if(strResponse == EMPTY_POSTS)
        {
            return
        }

        if (strResponse == NULL)
            return

        if(strResponse.isEmpty())
            return

        println(strResponse)

        val response = model.convertResponseToJson(strResponse)

        println("tO STRING" + response.toString())

        with(response)
        {
            if (result == true)
            {
                when (method) {
                    REGISTER -> {
                        advanceRegister()
                        return
                    }
                    CHANGE_PASSWORD -> {
                        advanceChangePassword()
                        return
                    }
                    CONFIRM -> {
                        toastWarning(CONFIRM_ADVANCE)
                        return
                    }
                    ADD_LIST -> {
                        model.getListsRequest()
                        toastWarning(LIST_ADD_ADVANCE)
                        return
                    }
                    ADD_VK -> {
                        toastWarning(VK_ADD_ADVANCE)
                        openAccount()
                        return
                    }
                    ADD_WORDS -> {
                        model.getWordsRequest(curListId!!)
                        toastWarning(WORDS_ADD_ADVANCE)
                        return
                    }
                    ADD_DISCUSSIONS -> {
                        model.getDiscussionsRequest()
                        toastWarning(DISCUSSION_ADD_ADVANCE)
                        return
                    }
                    DELETE_DISCUSSIONS -> {
                        model.getDiscussionsRequest()
                        curListId = null
                        curWordId = null
                        curBlackDiscussionId  = null
                        curWhiteDiscussionId = null
                        curDiscussionId = null
                        curListForDiscussionId = null
                        curMonitorListDiscussionId  = null
                        toastWarning(DISCUSSION_DELETE_ADVANCE)
                        return
                    }
                    DELETE_LIST -> {
                        model.getListsRequest()
                        curBlackDiscussionId = null
                        curWhiteDiscussionId = null
                        curListId = null
                        curWordId = null
                        toastWarning(LIST_DELETE_ADVANCE)
                        return
                    }
                    DELETE_WORD -> {
                        model.getWordsRequest(curListId!!)
                        curWordId = null
                        toastWarning(WORD_DELETE_ADVANCE)
                        return
                    }
                    ADD_LIST_TO_DISCUSSION -> {
                        toastWarning(LIST_ADD_TO_DISCUSSION_ADVANCE)
                        if(curDiscussionId!=null) {
                            model.getListForDiscussionRequest(curDiscussionId.toString())
                        }
                        return
                    }
                    DELETE_LIST_FROM_DISCUSSION -> {
                        toastWarning(LIST_DELETE_FROM_DISCUSSION_ADVANCE)
                        if(curDiscussionId!=null) {
                        model.getListForDiscussionRequest(curDiscussionId.toString())
                        }
                        return
                    }
                }

            }

            if(response.list?.get(0)?.white != null && method == GET_LISTS)
            {
                if (list != null) {
                    advanceGetLists(list)
                }
                return
            }

            if(method == GET_LIST_FOR_DISCUSSION && response.discussion_lists?.get(0)?.list_id !=null)
            {
                println("Hello not empty lists for discussion")
                if(discussion_lists!=null) {
                    advanceGetListForDiscussion(discussion_lists)
                }
                return
            }

            if(method == GET_ACCEPTED && response.counter!=null)
            {
                val counter = response.counter
                liveDateCounter.postValue(counter)
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        model.getPostsRequest(curMonitorListDiscussionId.toString(),counter.toString())
                    }
                }, 100)
            }

            if(method == GET_POSTS && response.posts?.get(0)?.message_num!=null)
            {
                advanceGetPosts(response.posts)
                println("Hello from get_posts ${response.posts}")
                return
            }

            if(method == GET_DISCUSSIONS && response.discussions?.get(0)?.description !=null)
            {
                if(discussions!=null){
                    advanceGetDiscussions(discussions)
                }
                return
            }

            if(method == GET_WORDS && response.list?.get(0)?.word != null)
            {
                if (list != null) {
                    advanceGetWords(list)
                }
                return
            }

            if (accessToken != null && (method == LOGIN))
            {
                advanceLogin(response)
                return
            }

            if(accessToken!=null && method==REFRESH)
            {
                advanceRefresh(response)
                return
            }

            if(response.profile?.confirmed == true && response.profile.name!=null)
            {
                advanceGetData(response.profile)
                return
            }

            if(errorMessage==REFRESH_PROBLEM || errorMessage== USER_NOT_AUTHORIZED)
            {
                openSign()
                return
            }

            if(errorMessage == USER_NOT_EXISTS)
            {
                toastWarning(USER_NOT_EXISTS_RU)
                return
            }

            if(errorMessage == NOT_CONFIRM_EMAIL)
            {
                createCodeDialog()
                return
            }

            if(result == false && method == CONFIRM)
            {
                toastWarning(CONFIRM_FAILED)
                return
            }

            if(result == false && method == ADD_VK)
            {
                toastWarning(VK_FAILED)
                return
            }

            if(errorMessage?.isNotEmpty() == true) {

                toastWarning(errorMessage.toString())
            }
        }

    }

    private fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.getFragments()
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }



    private fun goToNullLocalConst()
    {
        curListId = null
        curWordId = null
        curBlackDiscussionId  = null
        curWhiteDiscussionId = null
        curDiscussionId = null
        curListForDiscussionId = null
        curMonitorListDiscussionId  = null
        init()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val curFragment = getVisibleFragment()
        if(curFragment!=null)
        {
            println("я сохранил это все блин")
            supportFragmentManager.putFragment(outState, "curSaveFragment", curFragment);
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        model.closeSocket()
    }


}