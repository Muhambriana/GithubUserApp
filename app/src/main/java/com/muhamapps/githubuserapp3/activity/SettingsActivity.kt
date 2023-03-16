package com.muhamapps.githubuserapp3.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.muhamapps.githubuserapp3.AlarmReceiver
import com.muhamapps.githubuserapp3.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsActivity())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    class SettingsActivity : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var alarmReceiver: AlarmReceiver
        private lateinit var alarm: String

        private lateinit var alarmState: SwitchPreferenceCompat

        override fun onCreatePreferences(bundle: Bundle?, s: String?) {
            setPreferencesFromResource(R.xml.root_preferences, s)
            init()
            setSummaries()
            alarmReceiver = AlarmReceiver()
        }

        private fun init() {
            alarm = resources.getString(R.string.alarm_key)
            alarmState = findPreference<SwitchPreferenceCompat>(alarm) as SwitchPreferenceCompat
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            if (key == alarm) {
                alarmState.isChecked = sharedPreferences.getBoolean(alarm, false)
            }
            setAlarmState(alarmState.isChecked)
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            alarmState.isChecked = sh.getBoolean(alarm, false)
        }

        private fun setAlarmState(state: Boolean) {
            if (!state){
                context?.let { alarmReceiver.setAlarmOff(it) }
            }
            else{
                context?.let { alarmReceiver.setAlarmOn(it) }
            }
        }
    }
}