/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.healthkit.demo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.huawei.hihealth.error.HiHealthError
import com.huawei.hihealthkit.HiHealthDataQuery
import com.huawei.hihealthkit.HiHealthDataQueryOption
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.HiHealthKitConstant
import com.huawei.hihealthkit.data.HiHealthPointData
import com.huawei.hihealthkit.data.HiHealthSetData
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.store.HiRealTimeListener
import com.huawei.hihealthkit.data.store.HiSportDataCallback
import com.huawei.hihealthkit.data.type.HiHealthPointType
import com.huawei.hihealthkit.data.type.HiHealthSetType
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Third-party Demo program page
 */
class MainActivity : Activity() {
    private var btn11: Button? = null
    private var btn12: Button? = null
    private var btn13: Button? = null
    private var btn21: Button? = null
    private var btn22: Button? = null
    private var btn23: Button? = null
    private var btn31: Button? = null
    private var btn32: Button? = null
    private var btn33: Button? = null
    private var btn41: Button? = null
    private var btn42: Button? = null
    private var btn43: Button? = null
    private var btn44: Button? = null
    private var btn45: Button? = null
    private var btn51: Button? = null
    private var btn52: Button? = null
    private var btn53: Button? = null
    private var tvRestult: TextView? = null
    private val mHandler: MyHandler = MyHandler()
    private var mContext: Context? = null
    private val sportDataCallback: HiSportDataCallback = object : HiSportDataCallback {
        override fun onResult(resultCode: Int) {
            Log.i(TAG, "resultCode:$resultCode")
            combineResult(resultCode, "status changed")
        }

        override fun onDataChanged(state: Int, bundle: Bundle) {
            val sb = StringBuilder()
            Log.i(TAG, "onChange state: $state")
            if (state == 2) {
                for (key in bundle.keySet()) {
                    sb.append(key + " : " + bundle[key].toString()).append(" ")
                }
            }
            combineResult(state, sb.toString())
        }
    }
    var heartCallback: HiRealTimeListener = object : HiRealTimeListener {
        override fun onResult(state: Int) {
            Log.i(TAG, " onResult state:$state")
            combineResult(state, "state changed")
        }

        override fun onChange(resultCode: Int, value: String) {
            val sb = StringBuilder()
            Log.i(
                TAG,
                " onChange resultCode: $resultCode value: $value"
            )
            if (resultCode == HiHealthError.SUCCESS) {
                try {
                    val jsonObject = JSONObject(value)
                    sb.append("hri_info : " + jsonObject.getInt("hri_info"))
                    sb.append("hr_info : " + jsonObject.getInt("hr_info"))
                    sb.append("hrsqi_info : " + jsonObject.getInt("hrsqi_info"))
                    sb.append("time_info : " + jsonObject.getLong("time_info"))
                } catch (e: JSONException) {
                    Log.e(TAG, "JSONException e" + e.message)
                }
            }
            combineResult(resultCode, sb.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindDataView()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        btn11!!.setOnClickListener {
            val userAllowTypesToRead = intArrayOf(
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_INFORMATION,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_USER_PROFILE_FEATURE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_POINT_STEP_SUM,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_RUN_METADATA,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_SET_WEIGHT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_DATA_REAL_TIME_SPORT,
                HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT
            )
            val userAllowTypesToWrite =
                intArrayOf(HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_WRITE_DATA_SET_WEIGHT)
            HiHealthAuth.requestAuthorization(
                mContext, userAllowTypesToWrite, userAllowTypesToRead
            ) { resultCode, `object` ->
                Log.i(
                    TAG,
                    "requestAuthorization onResult:$resultCode"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(
                        TAG,
                        "requestAuthorization success resultContent:$`object`"
                    )
                }
                combineResult(resultCode, `object`)
            }
        }
        btn12!!.setOnClickListener {
            HiHealthDataStore.getGender(
                mContext
            ) { resultCode, gender ->
                combineResult(resultCode, gender)
                if (resultCode == HiHealthError.SUCCESS) {
                    val value = gender as Int
                }
            }
        }
        btn13!!.setOnClickListener {
            HiHealthDataStore.getBirthday(
                mContext
            ) { resultCode, birthday ->
                combineResult(resultCode, birthday)
                if (resultCode == HiHealthError.SUCCESS) {
                    // For example, "1978-05-20" would return 19780520
                    val value = birthday as Int
                }
            }
        }
        btn21!!.setOnClickListener {
            HiHealthDataStore.getHeight(
                mContext
            ) { resultCode, height ->
                combineResult(resultCode, height)
                if (resultCode == HiHealthError.SUCCESS) {
                    val value = height as Int
                }
            }
        }
        btn22!!.setOnClickListener {
            HiHealthDataStore.getWeight(
                mContext
            ) { resultCode, weight ->
                combineResult(resultCode, weight)
                if (resultCode == HiHealthError.SUCCESS) {
                    val value = weight as Int
                }
            }
        }
        btn23!!.setOnClickListener {
            val timeout = 0
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000 * 60 * 60 * 24 * 30L
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_STEP_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.execQuery(
                mContext, hiHealthDataQuery, timeout
            ) { resultCode, data ->
                Log.i(
                    TAG,
                    "query steps resultCode: $resultCode"
                )
                var result = ""
                if (resultCode == HiHealthError.SUCCESS) {
                    val dataList: List<*> = data as ArrayList<*>
                    if (dataList.size >= 1) {
                        val pointData = dataList[dataList.size - 1] as HiHealthPointData
                        result = result + pointData.value.toString()
                    }
                }
                combineResult(resultCode, result)
            }
        }
        btn31!!.setOnClickListener {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000L * 60 * 60 * 24 * 30L // Check Data of the latest 30 days
            val hiHealthDataQuery = HiHealthDataQuery(
                HiHealthPointType.DATA_POINT_STEP_SUM,
                startTime, endTime, HiHealthDataQueryOption()
            )
            HiHealthDataStore.getCount(
                mContext, hiHealthDataQuery
            ) { resultCode, data ->
                combineResult(resultCode, data)
                if (resultCode == HiHealthError.SUCCESS) {
                    val count = data as Int
                    Log.i(
                        TAG,
                        "walk track number: $count"
                    )
                }
            }
        }
        btn32!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                run {
                    val timeout = 0
                    val endTime = System.currentTimeMillis()
                    val startTime = endTime - 1000 * 60 * 60 * 24 * 30L
                    val hiHealthDataQuery =
                        HiHealthDataQuery(
                            HiHealthSetType.DATA_SET_RUN_METADATA,
                            startTime, endTime, HiHealthDataQueryOption()
                        )
                    HiHealthDataStore.execQuery(
                        mContext,
                        hiHealthDataQuery,
                        timeout
                    ) { i, data ->
                        val sb = StringBuilder()
                        val dataList =
                            data as List<*>
                        if (dataList.size >= 1) {
                            val hiHealthData =
                                dataList[dataList.size - 1] as HiHealthSetData
                            val map = hiHealthData.map
                            sb.append("start time : " + hiHealthData.startTime)
                            sb.append("total_time : " + map["total_time"])
                            sb.append("total_distance : " + map["total_distance"])
                            sb.append("total_calories : " + map["total_calories"])
                            sb.append("step : " + map["step"])
                            sb.append("average_pace : " + map["average_pace"])
                            sb.append("average_speed : " + map["average_speed"])
                            sb.append("average_step_rate : " + map["average_step_rate"])
                            sb.append("step_distance : " + map["step_distance"])
                            sb.append("average_heart_rate : " + map["average_heart_rate"])
                            sb.append("total_altitude : " + map["total_altitude"])
                            sb.append("total_descent : " + map["total_descent"])
                        }
                        combineResult(i, sb.toString())
                    }
                }
            }
        })
        btn33!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                run {
                    val timeout = 0
                    val endTime = System.currentTimeMillis()
                    val startTime = endTime - 86400000L * 30
                    val hiHealthDataQuery =
                        HiHealthDataQuery(
                            HiHealthSetType.DATA_SET_WEIGHT_EX,
                            startTime, endTime, HiHealthDataQueryOption()
                        )
                    HiHealthDataStore.execQuery(
                        mContext,
                        hiHealthDataQuery,
                        timeout
                    ) { resultCode, data ->
                        val sb = StringBuilder()
                        if (resultCode == HiHealthError.SUCCESS) {
                            Log.i(
                                TAG,
                                "query not null,enter set data"
                            )
                            val dataList =
                                data as List<*>
                            if (dataList.size >= 1) {
                                val hiHealthData =
                                    dataList[dataList.size - 1] as HiHealthSetData
                                val map =
                                    hiHealthData.map
                                sb.append("data start time : " + hiHealthData.startTime)
                                sb.append("data end time : " + hiHealthData.endTime)
                                sb.append("weight : weight " + map[HiHealthPointType.DATA_POINT_WEIGHT])
                                sb.append("weight : bmi " + map[HiHealthPointType.DATA_POINT_WEIGHT_BMI])
                                sb.append(
                                    "weight : muscle volume "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_MUSCLES]
                                )
                                sb.append(
                                    "weight : basic metabolism "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_BMR]
                                )
                                sb.append(
                                    "weight : moisture " + map[HiHealthPointType.DATA_POINT_WEIGHT_MOISTURE]
                                )
                                sb.append(
                                    "weight : visceral fat "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_FATLEVEL]
                                )
                                sb.append(
                                    "weight : bone mineral content "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_BONE_MINERAL]
                                )
                                sb.append(
                                    "weight : protein " + map[HiHealthPointType.DATA_POINT_WEIGHT_PROTEIN]
                                )
                                sb.append(
                                    "weight : body score "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_BODYSCORE]
                                )
                                sb.append(
                                    "weight : physical age "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_BODYAGE]
                                )
                                sb.append(
                                    "weight : body fat percentage "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_BODYFAT]
                                )
                                sb.append(
                                    "weight : body fat scale "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_IMPEDANCE]
                                )
                                sb.append(
                                    "weight : moisture percentage "
                                            + map[HiHealthPointType.DATA_POINT_WEIGHT_MOISTURERATE]
                                )
                            }
                        }
                        combineResult(resultCode, sb.toString())
                    }
                }
            }
        })
        btn41!!.setOnClickListener {
            HiHealthDataStore.startReadingHeartRate(
                mContext,
                heartCallback
            )
        }
        btn44!!.setOnClickListener {
            HiHealthDataStore.stopReadingHeartRate(
                mContext,
                heartCallback
            )
        }
        btn42!!.setOnClickListener {
            HiHealthDataStore.registerSportData(
                mContext,
                sportDataCallback
            )
        }
        btn45!!.setOnClickListener {
            HiHealthDataStore.unregisterSportData(
                mContext,
                sportDataCallback
            )
        }
        btn43!!.setOnClickListener {
            val map: MutableMap<Int, Double> =
                HashMap<Int, Double>()
            map[HiHealthPointType.DATA_POINT_WEIGHT] = 75.5
            map[HiHealthPointType.DATA_POINT_WEIGHT_BMI] = 18.8
            map[HiHealthPointType.DATA_POINT_WEIGHT_MUSCLES] = 33.5
            val endTime = System.currentTimeMillis()
            val hiHealthSetData =
                HiHealthSetData(HiHealthSetType.DATA_SET_WEIGHT_EX, map, endTime, endTime)
            HiHealthDataStore.saveSample(
                mContext, hiHealthSetData
            ) { resultCode, `object` ->
                combineResult(resultCode, `object`)
                Log.i(
                    TAG,
                    "saveSample resultCode is $resultCode"
                )
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(
                        TAG,
                        "saveSample resultList: $`object`"
                    )
                }
            }
        }
        btn51!!.setOnClickListener {
            val map: Map<*, *> = HashMap<Any?, Any?>()
            val endTime = System.currentTimeMillis()
            val startTime = endTime - 1000L * 60 * 60 * 24
            val hiHealthSetData =
                HiHealthSetData(HiHealthSetType.DATA_SET_WEIGHT_EX, map, startTime, endTime)
            HiHealthDataStore.deleteSample(
                mContext, hiHealthSetData
            ) { resultCode, data ->
                Log.i(
                    TAG,
                    "delete sample of the latest 24h onResult: $resultCode"
                )
            }
        }
        btn52!!.setOnClickListener {
            val sportType = HiHealthKitConstant.SPORT_TYPE_RUN
            HiHealthDataStore.startSport(
                mContext, sportType
            ) { resultCode, message ->
                combineResult(resultCode, message)
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(TAG, "start sport success")
                }
            }
        }
        btn53!!.setOnClickListener {
            HiHealthDataStore.stopSport(
                mContext
            ) { resultCode, message ->
                combineResult(resultCode, message)
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(TAG, "stop sport success")
                }
            }
        }
    }

    private fun bindDataView() {
        mContext = this
        btn11 = findViewById(R.id.btn_click_11)
        btn12 = findViewById(R.id.btn_click_12)
        btn13 = findViewById(R.id.btn_click_13)
        btn21 = findViewById(R.id.btn_click_21)
        btn22 = findViewById(R.id.btn_click_22)
        btn23 = findViewById(R.id.btn_click_23)
        btn31 = findViewById(R.id.btn_click_31)
        btn32 = findViewById(R.id.btn_click_32)
        btn33 = findViewById(R.id.btn_click_33)
        btn41 = findViewById(R.id.btn_click_41)
        btn42 = findViewById(R.id.btn_click_42)
        btn43 = findViewById(R.id.btn_click_43)
        btn44 = findViewById(R.id.btn_click_44)
        btn45 = findViewById(R.id.btn_click_45)
        btn51 = findViewById(R.id.btn_click_51)
        btn52 = findViewById(R.id.btn_click_52)
        btn53 = findViewById(R.id.btn_click_53)
        tvRestult = findViewById(R.id.result_view)
    }

    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg == null) {
                return
            }
            handleMessageInfo(msg)
        }

        private fun handleMessageInfo(msg: Message) {
            val deviceList01 = msg.obj.toString()
            tvRestult!!.text = deviceList01
        }
    }

    fun sendMsg(result: String?) {
        val message = Message.obtain()
        message.obj = result
        mHandler.sendMessage(message)
    }

    fun combineResult(resultCode: Int, `object`: Any?) {
        val sb = StringBuilder()
        sb.append("resultCode = ").append(resultCode)
        sb.append(" result = ").append(`object`)
        sendMsg(sb.toString())
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}