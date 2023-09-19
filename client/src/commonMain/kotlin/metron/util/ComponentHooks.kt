package metron.util

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentConfiguration
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import korlibs.datastructure.iterators.fastForEach

abstract class ComponentHooks<T> : ComponentType<T>() {
    open val onAdded: ComponentHook<T> = { _, _ -> }
    open val onRemoved: ComponentHook<T> = { _, _ -> }
}

inline fun <reified T : Component<*>> ComponentConfiguration.add(vararg componentHooksList: ComponentHooks<T>) {
    componentHooksList.fastForEach { componentHooks ->
        onAdd(componentHooks, componentHooks.onAdded)
        onRemove(componentHooks, componentHooks.onRemoved)
    }
}
