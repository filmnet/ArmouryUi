package dev.armoury.android.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.viewmodel.ArmouryViewModel
import timber.log.Timber

abstract class ArmouryActivity<UA: ArmouryUiAction, T: ViewDataBinding, V: ArmouryViewModel<UA>> : AppCompatActivity() {

    /**
     * If an [android.app.Activity] has a getIntent() function and someone
     * start the activity without using getIntent() function and did not provided
     * required parameters, we are going to make this flag a true
     * and base class is going to throw an exception
     */
    private var activityStartedIllegally = false
    protected lateinit var viewDataBinding : T
    protected lateinit var viewModel : V

    override fun onCreate(savedInstanceState: Bundle?) {
        logState("Created")
        if (intent != null) gatherDataFromIntent(intent.extras)
        super.onCreate(savedInstanceState)
        assert (!activityStartedIllegally) {"You need to use getIntent function of ${javaClass.simpleName} in order to start the activity"}
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutResource())
        viewDataBinding.lifecycleOwner = this
        viewModel = generateViewModel()
        setViewNeededData()
        startObserving()
        doOtherTasks()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        logState("onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onStart() {
        logState("Started")
        super.onStart()
    }

    override fun onRestart() {
        logState("onRestart")
        super.onRestart()
    }

    override fun onResume() {
        logState("Resumed")
        super.onResume()
    }

    override fun onPause() {
        logState("Paused")
        super.onPause()
    }

    override fun onStop() {
        logState("Stopped")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logState("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        logState("Destroyed")
        super.onDestroy()
    }

    protected open fun startObserving() {
        viewModel.uiAction.observe(this, this::handleUiAction)
    }

    protected abstract fun gatherDataFromIntent(data : Bundle?)

    @LayoutRes
    protected abstract fun getLayoutResource() : Int

    protected abstract fun generateViewModel() : V

    protected abstract fun setViewNeededData()

    protected abstract fun doOtherTasks()

    protected open fun handleUiAction(action: UA?) {}

    protected fun onArgumentsNotProvided() {
        activityStartedIllegally = true
    }

    private fun logState(state : String) {
        Timber.v("${javaClass.simpleName} $state")
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (currentFragment()?.customizeNavigateUpPressed() != true) super.onSupportNavigateUp() else true
    }

    override fun onBackPressed() {
        if (currentFragment()?.customizeBackButtonPressed() != true) super.onBackPressed()
    }

    open fun currentFragment() : ArmouryFragment<*,*,*>? = null

}