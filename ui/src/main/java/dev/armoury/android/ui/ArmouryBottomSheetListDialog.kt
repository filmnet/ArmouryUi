package dev.armoury.android.ui

import androidx.databinding.ViewDataBinding
import dev.armoury.android.data.ArmouryUiAction
import dev.armoury.android.viewmodel.ArmouryListViewModel

abstract class ArmouryBottomSheetListDialog<UA: ArmouryUiAction, T : ViewDataBinding, V : ArmouryListViewModel<UA, *>> :
    ArmouryBottomSheetDialogFragment<UA, T, V>()