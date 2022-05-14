package com.draco.ladb.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.draco.ladb.R
import com.draco.ladb.utils.ADB
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HelpPreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var adb: ADB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adb = ADB.getInstance(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.help, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.reset_key) -> {
                context?.let {
                    lifecycleScope.launch(Dispatchers.IO) {
                        /* Unpair server and client */
                        with(PreferenceManager.getDefaultSharedPreferences(it).edit()) {
                            putBoolean(getString(R.string.paired_key), false)
                            commit()
                        }

                        adb.reset()
                    }
                    activity?.finish()
                }
            }
            getString(R.string.licenses_key) -> {
                val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
                startActivity(intent)
            }
            else -> {
                if (preference !is SwitchPreference && preference !is EditTextPreference) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(preference.title)
                        .setMessage(preference.summary)
                        .show()
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

}