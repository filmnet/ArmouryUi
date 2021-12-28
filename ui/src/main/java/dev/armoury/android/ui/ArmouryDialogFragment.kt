package dev.armoury.android.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.viewmodel.ArmouryViewModel
import timber.log.Timber

abstract class ArmouryDialogFragment<UA: ArmouryUiAction, T: ViewDataBinding, V : ArmouryViewModel<UA>> : DialogFragment() {

    protected lateinit var activity: AppCompatActivity
    protected lateinit var viewDataBinding: T
    protected lateinit var viewModel: V
    private var fragmentCalledIllegally = false

    override fun onAttach(context: Context) {
        logState("Attached")
        super.onAttach(context)
        if (context is ArmouryActivity<*, *, *>) {
            activity = context
        } else {
            throw IllegalStateException("You have to use a BaseActivity or one of its children as the container activity")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        logState("container activity created")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logState("Created")
        gatherDataFromArgument(arguments)
        super.onCreate(savedInstanceState)

        assert(!fragmentCalledIllegally) { "You should use newInstance function of ${javaClass.simpleName}" }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logState("View Created")
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        viewDataBinding.lifecycleOwner = this
        viewModel = generateViewModel()
        setViewNeededData()
        doOtherTasks()
        return viewDataBinding.root
    }
    override fun onStart() {
        logState("Started")
        super.onStart()
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

    override fun onDestroy() {
        logState("Destroyed")
        super.onDestroy()
    }

    override fun onDestroyView() {
        logState("View Destroyed")
        super.onDestroyView()
    }

    override fun onDetach() {
        logState("Detached")
        super.onDetach()
    }

    protected fun onArgumentsNotProvided() {
        fragmentCalledIllegally = true
    }

    protected open fun gatherDataFromArgument(arguments: Bundle?) {}

    protected abstract fun generateViewModel(): V

    protected abstract fun getLayoutResource(): Int

    protected abstract fun doOtherTasks()

    protected abstract fun setViewNeededData()

    private fun logState(state: String) {
        Timber.v("${javaClass.simpleName} $state")
    }

}